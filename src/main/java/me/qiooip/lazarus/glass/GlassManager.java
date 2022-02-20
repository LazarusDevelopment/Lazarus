package me.qiooip.lazarus.glass;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.Claim;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.factions.type.RoadFaction;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.utils.Tasks;
import me.qiooip.lazarus.utils.ManagerEnabler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class GlassManager implements Listener, ManagerEnabler {

    private static final int WALL_BORDER_HEIGHT = 4;
    private static final int WALL_BORDER_WIDTH = 5;

    private ScheduledThreadPoolExecutor executor;
    private ScheduledFuture<?> updater;

    private final Table<UUID, Location, GlassInfo> glassCache;
    private final ReentrantLock lock;

    private final Set<Material> overriddenBlocks;

    public GlassManager() {
        this.glassCache = HashBasedTable.create();
        this.lock = new ReentrantLock();

        this.overriddenBlocks = EnumSet.of(Material.AIR, Material.LONG_GRASS, Material.DOUBLE_PLANT,
            Material.YELLOW_FLOWER, Material.RED_ROSE, Material.VINE);

        Tasks.syncLater(this::setupTasks, 10L);
        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    private void setupTasks() {
        this.executor = new ScheduledThreadPoolExecutor(2, Tasks.newThreadFactory("Glass Thread - %d"));
        this.executor.setRemoveOnCancelPolicy(true);

        this.updater = this.executor.scheduleAtFixedRate(new GlassUpdater(), 0L, 100L, TimeUnit.MILLISECONDS);
    }

    public void disable() {
        this.glassCache.clear();

        if(this.updater != null) this.updater.cancel(true);
        if(this.executor != null) this.executor.shutdownNow();
    }

    public GlassInfo getGlassAt(Player player, Location location) {
        return this.glassCache.get(player.getUniqueId(), location);
    }

    public void generateGlassVisual(Player player, GlassInfo info) {
        if(this.glassCache.contains(player.getUniqueId(), info.getLocation())) return;

        int x = info.getLocation().getBlockX() >> 4;
        int z = info.getLocation().getBlockZ() >> 4;

        if(!info.getLocation().getWorld().isChunkLoaded(x, z)) return;

        info.getLocation().getWorld().getChunkAtAsync(x, z, (chunk) -> {
            Material material = info.getLocation().getBlock().getType();
            if(!this.overriddenBlocks.contains(material)) return;

            player.sendBlockChange(info.getLocation(), info.getMaterial(), info.getData());

            this.lock.lock();

            try {
                this.glassCache.put(player.getUniqueId(), info.getLocation(), info);
            } finally {
                this.lock.unlock();
            }
        });
    }

    public void clearGlassVisuals(Player player, GlassType type) {
        this.clearGlassVisuals(player, glassInfo -> glassInfo.getType() == type);
    }

    public void clearGlassVisuals(Player player, GlassType type, Predicate<GlassInfo> predicate) {
        this.clearGlassVisuals(player, glassInfo -> glassInfo.getType() == type && predicate.test(glassInfo));
    }

    private void clearGlassVisuals(Player player, Predicate<GlassInfo> predicate) {
        this.lock.lock();

        try {
            Iterator<Entry<Location, GlassInfo>> iterator = this.glassCache.row(player.getUniqueId()).entrySet().iterator();

            while(iterator.hasNext()) {
                Entry<Location, GlassInfo> entry = iterator.next();
                if(!predicate.test(entry.getValue())) continue;

                Location location = entry.getKey();

                if(!location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) {
                    iterator.remove();
                    continue;
                }

                player.sendBlockChange(entry.getKey(), location.getBlock().getType(), location.getBlock().getData());
                iterator.remove();
            }
        } finally {
            this.lock.unlock();
        }
    }

    private GlassType getGlassType(Player player, GlassType forced) {
        if(forced != null) {
            return forced;
        } else if(TimerManager.getInstance().getCombatTagTimer().isActive(player)) {
            return GlassType.SPAWN_WALL;
        } else if(TimerManager.getInstance().getPvpProtTimer().isActive(player)) {
            return GlassType.CLAIM_WALL;
        } else {
            return null;
        }
    }

    private void handlePlayerMove(Player player, Location from, Location to) {
        this.handlePlayerMove(player, from, to, null);
    }

    private void handlePlayerMove(Player player, Location from, Location to, GlassType forced) {
        GlassType type = this.getGlassType(player, forced);
        if(type == null) return;

        this.clearGlassVisuals(player, type, glassInfo -> {
            Location loc = glassInfo.getLocation();

            return loc.getWorld().getName().equals(to.getWorld().getName())
                && (Math.abs(loc.getBlockX() - to.getBlockX()) > WALL_BORDER_WIDTH
                || Math.abs(loc.getBlockY() - to.getBlockY()) > WALL_BORDER_HEIGHT
                || Math.abs(loc.getBlockZ() - to.getBlockZ()) > WALL_BORDER_WIDTH);
        });

        Set<Claim> claims = ClaimManager.getInstance().getClaimsInSelection(to.getWorld(),
            to.getBlockX() - WALL_BORDER_WIDTH, to.getBlockX() + WALL_BORDER_WIDTH,
            to.getBlockZ() - WALL_BORDER_WIDTH, to.getBlockZ() + WALL_BORDER_WIDTH);

        if(claims.isEmpty()) return;

        if(type == GlassType.SPAWN_WALL) {
            claims.removeIf(claim -> !claim.getOwner().isSafezone());
        } else {
            PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFaction(player);

            claims.removeIf(claim -> claim.getOwner() instanceof RoadFaction || claim.getOwner().isSafezone()
                || (Config.PVP_PROTECTION_CAN_ENTER_OWN_CLAIM && claim.getOwner() == playerFaction));
        }

        claims.forEach(claim -> claim.getClosestSides(to).forEach(side -> {
            for(int y = -WALL_BORDER_HEIGHT + 1; y <= WALL_BORDER_HEIGHT; y++) {
                Location location = side.clone();
                location.setY(to.getBlockY() + y);

                this.generateGlassVisual(player, new GlassInfo(type, location, Material.STAINED_GLASS, (byte) 14));
            }
        }));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        this.lock.lock();

        try {
            this.glassCache.row(player.getUniqueId()).clear();
        } finally {
            this.lock.unlock();
        }
    }

    public enum GlassType {
        SPAWN_WALL, CLAIM_WALL, CLAIM_MAP, CLAIM_SELECTION
    }

    private class GlassUpdater implements Runnable, Listener {

        private final Map<UUID, Location> lastPlayerLocations;

        public GlassUpdater() {
            this.lastPlayerLocations = new HashMap<>();
            Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
        }

        public boolean isEqual(Location loc1, Location loc2) {
            return loc1.getWorld() == loc2.getWorld()
                && loc1.getBlockX() == loc2.getBlockX()
                && loc1.getBlockY() == loc2.getBlockY()
                && loc1.getBlockZ() == loc2.getBlockZ();
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            this.lastPlayerLocations.remove(event.getPlayer().getUniqueId());
        }

        @Override
        public void run() {
            try {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    Location lastLocation = this.lastPlayerLocations.get(player.getUniqueId());

                    if(lastLocation == null || !this.isEqual(lastLocation, player.getLocation())) {
                        handlePlayerMove(player, lastLocation, player.getLocation());
                        this.lastPlayerLocations.put(player.getUniqueId(), player.getLocation());
                    }
                }
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
