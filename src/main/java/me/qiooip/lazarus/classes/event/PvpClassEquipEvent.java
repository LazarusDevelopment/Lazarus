package me.qiooip.lazarus.classes.event;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.classes.manager.PvpClass;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class PvpClassEquipEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    @Getter private final UUID player;
    @Getter private final PvpClass pvpClass;
    @Getter @Setter private boolean cancelled;

    public PvpClassEquipEvent(UUID player, PvpClass pvpClass) {
        this.player = player;
        this.pvpClass = pvpClass;

        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
