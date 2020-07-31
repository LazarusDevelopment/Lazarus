package me.qiooip.lazarus.handlers.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LazarusKickEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    @Getter private final Player player;
    @Getter private final KickType kickReason;
    @Getter private final String kickMessage;
    @Getter @Setter private boolean cancelled;

    public LazarusKickEvent(Player player, KickType kickReason, String kickMessage) {
        this.player = player;
        this.kickReason = kickReason;
        this.kickMessage = kickMessage;

        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }

    public enum KickType {
        DEATHBAN, LOGOUT, KICKALL, REBOOT, USERDATA_FAILED_TO_LOAD
    }
}
