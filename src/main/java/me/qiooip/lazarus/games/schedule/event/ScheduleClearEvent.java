package me.qiooip.lazarus.games.schedule.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ScheduleClearEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public ScheduleClearEvent() {
        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
