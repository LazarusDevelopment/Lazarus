package me.qiooip.lazarus.games.koth.event;

import lombok.Getter;
import me.qiooip.lazarus.games.koth.KothData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class KothCappedEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	private final KothData koth;
	private final Player capper;
	
	public KothCappedEvent(KothData koth, Player capper) {
		this.koth = koth;
		this.capper = capper;

		Bukkit.getPluginManager().callEvent(this);
	}

	@Override
	public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
