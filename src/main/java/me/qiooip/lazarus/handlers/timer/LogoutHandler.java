package me.qiooip.lazarus.handlers.timer;

import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.timer.TimerManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class LogoutHandler extends Handler implements Listener {

	private boolean isActive(Player player) {
		return TimerManager.getInstance().getLogoutTimer().isActive(player);
	}

	private void checkPlayerMove(Player player, Location from, Location to) {
		if(!this.isActive(player)) return;
		if(from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) return;

		TimerManager.getInstance().getLogoutTimer().cancel(player);
		player.sendMessage(Language.PREFIX + Language.LOGOUT_TELEPORT_CANCELLED_MOVED);
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
	public void onPlayerDamage(EntityDamageEvent event) {
		if(!(event.getEntity() instanceof Player)) return;

		Player player = (Player) event.getEntity();
		if(!this.isActive(player)) return;

		TimerManager.getInstance().getLogoutTimer().cancel(player);
		player.sendMessage(Language.PREFIX + Language.LOGOUT_TELEPORT_CANCELLED_DAMAGED);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if(!this.isActive(event.getPlayer())) return;

		TimerManager.getInstance().getLogoutTimer().cancel(event.getPlayer());
	}
}
