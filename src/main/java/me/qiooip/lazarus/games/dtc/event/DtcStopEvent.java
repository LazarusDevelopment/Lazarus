package me.qiooip.lazarus.games.dtc.event;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class DtcStopEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public DtcStopEvent() {
        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
