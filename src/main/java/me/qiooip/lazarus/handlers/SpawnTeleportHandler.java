package me.qiooip.lazarus.handlers;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.scoreboard.TeleportTimer;
import me.qiooip.lazarus.userdata.Userdata;
import me.qiooip.lazarus.utils.Tasks.Callable;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class SpawnTeleportHandler extends Handler implements Listener {

    public void startSpawnCreditsTeleport(Player player) {
        Userdata userdata = Lazarus.getInstance().getUserdataManager().getUserdata(player);

        if(userdata.getSpawnCredits() <= 0) {
            player.sendMessage(Language.PREFIX + Language.SPAWN_CREDITS_NOT_ENOUGH_CREDITS);
            return;
        }

        this.startTeleportTimer(player, Config.SPAWN_CREDITS_SPAWN_TELEPORT_DELAY, () -> {
            userdata.decrementSpawnCredits();

            player.sendMessage(Language.PREFIX + Language.SPAWN_CREDITS_TELEPORTED
                .replace("<amount>", String.valueOf(userdata.getSpawnCredits())));
        });
    }

    public void startKitmapSpawnTeleport(Player player) {
        this.startTeleportTimer(player, Config.KITMAP_SPAWN_TELEPORT_DELAY, null);
    }

    private void startTeleportTimer(Player player, int delay, Callable callable) {
        Location spawn = Config.WORLD_SPAWNS.get(World.Environment.NORMAL);

        if(spawn == null) {
            player.sendMessage(Language.PREFIX + Language.SPAWN_DOESNT_EXIST
                .replace("<world>", player.getWorld().getName()));
            return;
        }

        TeleportTimer timer = TimerManager.getInstance().getTeleportTimer();

        if(timer.isActive(player)) {
            player.sendMessage(Language.PREFIX + Language.SPAWN_ALREADY_TELEPORTING);
            return;
        }

        if(callable == null) {
            timer.activate(player, delay, spawn);
        } else {
            timer.activate(player, delay, spawn, callable);
        }

        player.sendMessage(Language.PREFIX + Language.SPAWN_TELEPORT_STARTED
            .replace("<time>", String.valueOf(delay)));
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
}
