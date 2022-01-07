package me.qiooip.lazarus.games.koth.event;

import lombok.Getter;
import me.qiooip.lazarus.games.koth.RunningKoth;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class KothKnockedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final RunningKoth koth;
    private final Player knocked;
    private final long capTimeMillis;

    public KothKnockedEvent(RunningKoth koth, Player knocked, long capTimeMillis) {
        this.koth = koth;
        this.knocked = knocked;
        this.capTimeMillis = capTimeMillis;

        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
