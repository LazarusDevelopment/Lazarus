package me.qiooip.lazarus.games.conquest.event;

import lombok.Getter;
import me.qiooip.lazarus.games.conquest.RunningConquest;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class ConquestStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final RunningConquest conquest;

    public ConquestStartEvent(RunningConquest conquest) {
        this.conquest = conquest;

        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
