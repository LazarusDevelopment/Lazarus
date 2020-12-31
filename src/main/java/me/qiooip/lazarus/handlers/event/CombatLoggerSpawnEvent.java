package me.qiooip.lazarus.handlers.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class CombatLoggerSpawnEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    @Setter private boolean cancelled;

    public CombatLoggerSpawnEvent(Player player) {
        super(player);

        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
