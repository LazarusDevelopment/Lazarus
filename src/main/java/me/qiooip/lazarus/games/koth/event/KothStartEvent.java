package me.qiooip.lazarus.games.koth.event;

import lombok.Getter;
import me.qiooip.lazarus.games.koth.RunningKoth;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class KothStartEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private final RunningKoth koth;

	public KothStartEvent(RunningKoth koth) {
		this.koth = koth;

		Bukkit.getPluginManager().callEvent(this);
	}

	@Override
	public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
