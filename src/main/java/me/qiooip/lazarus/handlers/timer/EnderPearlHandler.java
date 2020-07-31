package me.qiooip.lazarus.handlers.timer;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.scoreboard.EnderPearlTimer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnderPearlHandler extends Handler implements Listener {

    private final Map<UUID, Entity> killCheck;

    public EnderPearlHandler() {
        this.killCheck = new HashMap<>();
    }

    @Override
    public void disable() {
        this.killCheck.clear();
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if(!Config.ENDER_PEARL_COOLDOWN_ENABLED || event.getEntity().getType() != EntityType.ENDER_PEARL) return;
        if(!(event.getEntity().getShooter() instanceof Player)) return;

        Player player = (Player) event.getEntity().getShooter();
        TimerManager.getInstance().getEnderPearlTimer().activate(player.getUniqueId());

        this.killCheck.put(player.getUniqueId(), event.getEntity());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.useInteractedBlock() == Result.DENY && event.useItemInHand() == Result.DENY) return;
        if(!Config.ENDER_PEARL_COOLDOWN_ENABLED || !event.hasItem()) return;

        Player player = event.getPlayer();
        if(player.getGameMode() == GameMode.CREATIVE || event.getItem().getType() != Material.ENDER_PEARL) return;
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        EnderPearlTimer timer = TimerManager.getInstance().getEnderPearlTimer();

        if(timer.isActive(player)) {
            event.setUseItemInHand(Result.DENY);
            player.updateInventory();

            player.sendMessage(Language.PREFIX + Language.ENDERPEARL_DENY_MESSAGE.replace("<seconds>", timer.getTimeLeft(player)));
        }
    }

    @EventHandler
    public void onEnderpearlCancel(PlayerTeleportEvent event) {
        if(!Config.ENDER_PEARL_COOLDOWN_ENABLED) return;
        if(event.getCause() != TeleportCause.ENDER_PEARL || !event.isCancelled()) return;

        TimerManager.getInstance().getEnderPearlTimer().cancel(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        TimerManager.getInstance().getEnderPearlTimer().cancel(event.getEntity());

        Entity entity = this.killCheck.remove(event.getEntity().getUniqueId());
        if(entity != null) entity.remove();
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if(event.getEntity().getType() != EntityType.ENDER_PEARL) return;
        if(!(event.getEntity().getShooter() instanceof Player)) return;

        Player player = (Player) event.getEntity().getShooter();
        this.killCheck.remove(player.getUniqueId());
    }
}
