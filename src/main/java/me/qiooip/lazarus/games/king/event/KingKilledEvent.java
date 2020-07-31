package me.qiooip.lazarus.games.king.event;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class KingKilledEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private final Player king;
	private final Player killer;
	
	public KingKilledEvent(Player king, Player killer) {
		this.king = king;
		this.killer = killer;

		Bukkit.getPluginManager().callEvent(this);
	}

	@Override
	public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
