package me.qiooip.lazarus.games.dtc.event;

import lombok.Getter;
import me.qiooip.lazarus.games.dtc.DtcData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class DtcDestroyedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final DtcData dtc;
    private final Player winner;

    public DtcDestroyedEvent(DtcData dtc, Player winner) {
        this.dtc = dtc;
        this.winner = winner;
        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
