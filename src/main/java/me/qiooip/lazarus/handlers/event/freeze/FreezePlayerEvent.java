package me.qiooip.lazarus.handlers.event.freeze;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class FreezePlayerEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final CommandSender sender;
    private final Player target;
    private final FreezeType type;
    @Setter private boolean cancelled;

    public FreezePlayerEvent(CommandSender sender, Player target, FreezeType type) {
        this.sender = sender;
        this.target = target;
        this.type = type;

        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}