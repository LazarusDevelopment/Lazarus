package me.qiooip.lazarus.games.dtc.event;

import lombok.Getter;
import me.qiooip.lazarus.games.conquest.RunningConquest;
import me.qiooip.lazarus.games.dtc.DtcData;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class DtcStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final DtcData data;

    public DtcStartEvent(DtcData data) {
        this.data = data;

        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
