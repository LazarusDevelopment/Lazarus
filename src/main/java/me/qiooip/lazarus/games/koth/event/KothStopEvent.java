package me.qiooip.lazarus.games.koth.event;

import lombok.Getter;
import me.qiooip.lazarus.games.koth.KothData;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class KothStopEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private final KothData koth;

	public KothStopEvent(KothData koth) {
		this.koth = koth;

		Bukkit.getPluginManager().callEvent(this);
	}

	@Override
	public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
