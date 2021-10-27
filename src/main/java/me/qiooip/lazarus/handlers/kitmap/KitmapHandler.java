package me.qiooip.lazarus.handlers.kitmap;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.scoreboard.TeleportTimer;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class KitmapHandler extends Handler implements Listener {

    private ItemClearTask itemClearTask;

    public KitmapHandler() {
        if(Config.KITMAP_CLEAR_ITEMS_ENABLED) {
            this.itemClearTask = new ItemClearTask();
        }

        TimerManager.getInstance().getPvpProtTimer().clearPvpProtections();
    }

    @Override
    public void disable() {
        if(this.itemClearTask != null) this.itemClearTask.cancel();
    }

    private void checkPlayerMove(Player player, Location from, Location to) {
        if(from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) return;

        TeleportTimer timer = TimerManager.getInstance().getTeleportTimer();

        if(timer.isActive(player)) {
            timer.cancel(player);
            player.sendMessage(Language.PREFIX + Language.SPAWN_TELEPORT_CANCELLED_MOVED);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if(!Config.KITMAP_DISABLE_ITEM_DROP_IN_SAFEZONE) return;

        if(ClaimManager.getInstance().getFactionAt(event.getPlayer()).isSafezone()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Language.FACTION_PREFIX + Language.KITMAP_DENY_ITEM_DROP);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        this.checkPlayerMove(event.getPlayer(), event.getFrom(), event.getTo());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        this.checkPlayerMove(event.getPlayer(), event.getFrom(), event.getTo());
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        TeleportTimer timer = TimerManager.getInstance().getTeleportTimer();

        if(timer.isActive(player)) {
            timer.cancel(player);
            player.sendMessage(Language.PREFIX + Language.SPAWN_TELEPORT_CANCELLED_DAMAGE);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(event.getEntity().getKiller() == null || !Config.KITMAP_KILL_REWARD_ENABLED) return;

        Config.KITMAP_KILL_REWARD.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
            command.replace("<player>", event.getEntity().getKiller().getName())));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(player.hasPlayedBefore()) return;

        Location spawn = Config.WORLD_SPAWNS.get(Environment.NORMAL);
        player.teleport(spawn == null ? player.getWorld().getSpawnLocation() : spawn);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        TimerManager.getInstance().getTeleportTimer().cancel(event.getPlayer());
    }

    private static class ItemClearTask extends BukkitRunnable {

        ItemClearTask() {
            this.runTaskTimer(Lazarus.getInstance(), 0L, Config.KITMAP_CLEAR_ITEMS_INTERVAL * 20L);
        }

        @Override
        public void run() {
            for(World world : Bukkit.getWorlds()) {
                List<Entity> entities = world.getEntities();

                Tasks.async(() -> entities.forEach(entity -> {
                    if(entity instanceof Item) {
                        entity.remove();
                    }
                }));
            }
        }
    }
}
