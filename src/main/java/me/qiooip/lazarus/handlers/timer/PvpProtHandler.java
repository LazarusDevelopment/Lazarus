package me.qiooip.lazarus.handlers.timer;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.claim.Claim;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.event.FactionClaimChangeEvent;
import me.qiooip.lazarus.factions.event.FactionClaimChangeEvent.ClaimChangeReason;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.scoreboard.PvpProtTimer;
import me.qiooip.lazarus.utils.PlayerUtils;
import me.qiooip.lazarus.utils.Tasks;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PvpProtHandler extends Handler implements Listener {

    private final List<ItemStack> disabledItems;
    private final List<ItemStack> pickupDenyItems;
    private final Map<UUID, Long> messageDelays;

    private final Set<DamageCause> disabledDamageCauses;

    public PvpProtHandler() {
        this.disabledItems = new ArrayList<>();
        this.pickupDenyItems = new ArrayList<>();
        this.messageDelays = new HashMap<>();

        this.disabledDamageCauses = EnumSet.of(DamageCause.DROWNING, DamageCause.SUFFOCATION,
            DamageCause.POISON, DamageCause.FIRE, DamageCause.FIRE_TICK);
    }

    @Override
    public void disable() {
        this.disabledItems.clear();
        this.pickupDenyItems.clear();
        this.messageDelays.clear();
    }

    private void sendDelayedMessage(Player player, String message) {
        if(this.messageDelays.containsKey(player.getUniqueId()) && (this.messageDelays
            .get(player.getUniqueId()) - System.currentTimeMillis() > 0)) return;

        player.sendMessage(message);
        this.messageDelays.put(player.getUniqueId(), System.currentTimeMillis() + 3000L);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player attacker = PlayerUtils.getAttacker(event);
        if(attacker == null) return;

        Player victim = (Player) event.getEntity();
        PvpProtTimer timer = TimerManager.getInstance().getPvpProtTimer();

        if(timer.isActive(attacker) && attacker == victim) {
            event.setCancelled(true);
            return;
        }

        if(timer.isActive(attacker)) {
            event.setCancelled(true);
            attacker.sendMessage(Language.PREFIX + Language.PVP_PROT_PVP_DENY_VICTIM);
            return;
        }

        if(timer.isActive(victim)) {
            event.setCancelled(true);
            attacker.sendMessage(Language.PREFIX + Language.PVP_PROT_PVP_DENY_ATTACKER.replace("<name>", victim.getName()));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if(!TimerManager.getInstance().getPvpProtTimer().isActive(player)) return;

        if(this.disabledDamageCauses.contains(event.getCause())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if(!TimerManager.getInstance().getPvpProtTimer().isActive(player)) return;

        event.setFoodLevel(20);
        player.setSaturation(20.0F);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.useInteractedBlock() == Result.DENY && event.useItemInHand() == Result.DENY) return;

        if(!event.hasItem() || event.getPlayer().isOp()) return;
        if(!TimerManager.getInstance().getPvpProtTimer().isActive(event.getPlayer())) return;

        Action action = event.getAction();
        if(action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        boolean shouldCancel = false;

        for(ItemStack disabledItem : this.disabledItems) {
            if(disabledItem.getType() != event.getItem().getType()) continue;
            if(disabledItem.getDurability() != event.getItem().getDurability()) continue;

            shouldCancel = true;
            break;
        }

        if(shouldCancel) {
            event.setCancelled(true);

            event.getPlayer().sendMessage(Language.PREFIX + Language.PVP_PROT_ITEM_DENY_MESSAGE
                .replace("<item>", ItemUtils.getItemName(event.getItem())));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if(event.getPlayer().isOp() || !TimerManager.getInstance().getPvpProtTimer().isActive(event.getPlayer())) return;

        boolean shouldCancel = false;

        for(ItemStack disabledItem : this.pickupDenyItems) {
            if(disabledItem.getType() != event.getItem().getItemStack().getType()) continue;
            if(disabledItem.getDurability() != event.getItem().getItemStack().getDurability()) continue;

            shouldCancel = true;
            break;
        }

        if(shouldCancel) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionClaimChange(FactionClaimChangeEvent event) {
        PvpProtTimer timer = TimerManager.getInstance().getPvpProtTimer();

        if(event.getClaimChangeReason() != ClaimChangeReason.CLAIM && event.getFaction().isSafezone()) {
            Tasks.sync(() -> event.getClaims().forEach(claim ->
                claim.getPlayers().forEach(player -> timer.pause(player, false))));
            return;
        }

        event.getClaims().forEach(claim -> {
            Faction owner = claim.getOwner();

            claim.getPlayers().filter(timer::isActive).forEach(player -> {
                if(owner.isSafezone()) {
                    timer.pause(player, true);
                    return;
                }

                if(!owner.shouldCancelPvpTimerEntrance(player)) return;

                Location safeLocation = ClaimManager.getInstance().getSafeLocation(claim);
                safeLocation.setPitch(player.getLocation().getPitch());
                safeLocation.setYaw(player.getLocation().getYaw());

                player.teleport(safeLocation);
                player.sendMessage(Language.FACTION_PREFIX + Language.PVP_PROT_TELEPORT_DUE_TO_CLAIM);
            });
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        PvpProtTimer timer = TimerManager.getInstance().getPvpProtTimer();
        if(!timer.isActive(event.getPlayer())) return;

        timer.pause(event.getPlayer(), ClaimManager.getInstance().getFactionAt(event.getPlayer()).isSafezone());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPortal(PlayerPortalEvent event) {
        if(event.getCause() != TeleportCause.END_PORTAL || !Config.PVP_PROTECTION_DISABLE_END_ENTRY) return;
        if(event.getFrom().getWorld().getEnvironment() != Environment.NORMAL) return;
        if(!TimerManager.getInstance().getPvpProtTimer().isActive(event.getPlayer())) return;

        event.setCancelled(true);
        this.sendDelayedMessage(event.getPlayer(), Language.PREFIX + Language.PVP_PROT_END_PORTAL_TELEPORT_DENY);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if(!TimerManager.getInstance().getPvpProtTimer().isActive(event.getPlayer())) return;

        Faction factionAt = ClaimManager.getInstance().getFactionAt(event.getTo());
        if(!factionAt.shouldCancelPvpTimerEntrance(event.getPlayer())) return;

        if(event.getCause() == TeleportCause.ENDER_PEARL) {
            TimerManager.getInstance().getEnderPearlTimer().cancel(event.getPlayer());
            event.getPlayer().setMetadata("enderpearlRefund", PlayerUtils.TRUE_METADATA_VALUE);
        }

        event.getPlayer().sendMessage(Language.PREFIX + Language.PVP_PROT_DENY_TELEPORT);
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        if(!TimerManager.getInstance().getPvpProtTimer().isActive(event.getPlayer())) return;

        Faction factionAt = ClaimManager.getInstance().getFactionAt(event.getTo());
        if(!factionAt.shouldCancelPvpTimerEntrance(event.getPlayer())) return;

        event.setTo(event.getFrom());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if(Config.KITMAP_MODE_ENABLED && !Config.KITMAP_PVP_TIMER_ENABLED) return;

        Player player = event.getPlayer();
        TimerManager.getInstance().getPvpProtTimer().activate(player, event.getRespawnLocation());

        Location oldLocation = player.getLocation();

        Tasks.sync(() -> {
            Claim claim = ClaimManager.getInstance().getClaimAt(event.getRespawnLocation());
            if(claim == null || !claim.getOwner().shouldCancelPvpTimerEntrance(player)) return;

            Location safeLocation = ClaimManager.getInstance().getSafeLocation(claim);
            safeLocation.setPitch(oldLocation.getPitch());
            safeLocation.setYaw(oldLocation.getYaw());

            player.teleport(safeLocation);
            player.sendMessage(Language.PREFIX + Language.PVP_PROT_DENY_TELEPORT);
        });
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(Config.KITMAP_MODE_ENABLED && !Config.KITMAP_PVP_TIMER_ENABLED) return;

        TimerManager.getInstance().getPvpProtTimer().cancel(event.getEntity());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(Config.KITMAP_MODE_ENABLED && !Config.KITMAP_PVP_TIMER_ENABLED) return;

        Player player = event.getPlayer();
        PvpProtTimer timer = TimerManager.getInstance().getPvpProtTimer();

        if(Lazarus.getInstance().getEotwHandler().isActive() && timer.isActive(player)) {
            timer.cancel(player);
            player.sendMessage(Language.PREFIX + Language.EOTW_PVP_TIMER_DISABLED);
            return;
        }

        if(!player.hasPlayedBefore()) {
            Location spawn = Config.WORLD_SPAWNS.get(Environment.NORMAL);
            player.teleport(spawn == null ? player.getWorld().getSpawnLocation() : spawn);

            timer.activate(player, player.getLocation());
            return;
        }

        if(!ClaimManager.getInstance().getFactionAt(player).isSafezone()) {
            timer.pause(player, false);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        TimerManager.getInstance().getPvpProtTimer().pause(event.getPlayer(), true);
        this.messageDelays.remove(event.getPlayer().getUniqueId());
    }
}
