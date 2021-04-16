package me.qiooip.lazarus.handlers.event;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class ExitSetEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final World.Environment environment;
    private final Location location;

    public ExitSetEvent(Player player, World.Environment environment, Location location) {
        this.player = player;
        this.environment = environment;
        this.location = location;

        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}