package me.qiooip.lazarus.games.koth.event;

import lombok.Getter;
import me.qiooip.lazarus.games.koth.RunningKoth;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class KothStartedCappingEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final RunningKoth koth;
    private final Player newCapper;

    public KothStartedCappingEvent(RunningKoth koth, Player newCapper) {
        this.koth = koth;
        this.newCapper = newCapper;

        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
