package me.qiooip.lazarus.games.conquest.event;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class ConquestStopEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public ConquestStopEvent() {
        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
