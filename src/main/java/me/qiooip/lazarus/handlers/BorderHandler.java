package me.qiooip.lazarus.handlers;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class BorderHandler extends Handler implements Listener {

    private boolean isInsideBorder(Location location) {
        int border = Config.BORDER_SIZE.get(location.getWorld().getEnvironment());
        return Math.abs(location.getBlockX()) <= border && Math.abs(location.getBlockZ()) <= border;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if(this.isInsideBorder(event.getLocation())) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if(this.isInsideBorder(event.getBlock().getLocation())) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(Language.PREFIX + Language.BORDER_BLOCK_PLACE_DENY);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if(this.isInsideBorder(event.getBlock().getLocation())) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(Language.PREFIX + Language.BORDER_BLOCK_BREAK_DENY);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBucketEmpty(PlayerBucketFillEvent event) {
        if(this.isInsideBorder(event.getBlockClicked().getLocation())) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(Language.PREFIX + Language.BORDER_BUCKET_FILL_DENY);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if(this.isInsideBorder(event.getBlockClicked().getLocation())) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(Language.PREFIX + Language.BORDER_BUCKET_EMPTY_DENY);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerPortal(PlayerPortalEvent event) {
        if(this.isInsideBorder(event.getTo())) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(Language.PREFIX + Language.BORDER_TELEPORT_DENY);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if(this.isInsideBorder(event.getTo())) return;

        if(!this.isInsideBorder(event.getFrom())) {
            int border = Config.BORDER_SIZE.get(event.getFrom().getWorld().getEnvironment());

            Location newTo = event.getFrom().clone();

            if(Math.abs(newTo.getX()) > Math.abs(newTo.getZ())) {
                newTo.setX(newTo.getX() > 0 ? Math.min(border, newTo.getX()) - 1 : Math.max(border, newTo.getX()) + 1);
            } else {
                newTo.setZ(newTo.getZ() > 0 ? Math.min(border, newTo.getZ()) - 1 : Math.max(border, newTo.getZ()) + 1);
            }

            event.setTo(newTo);
            return;
        }

        event.setCancelled(true);
        event.getPlayer().sendMessage(Language.PREFIX + Language.BORDER_TELEPORT_DENY);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();

        if(from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) return;

        Player player = event.getPlayer();
        if(this.isInsideBorder(event.getTo())) return;

        if(player.getVehicle() != null) player.eject();

        event.setTo(from);
        player.sendMessage(Language.PREFIX + Language.BORDER_REACHED_BORDER);
    }
}
