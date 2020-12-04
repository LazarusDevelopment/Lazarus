package me.qiooip.lazarus.staffmode.event;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class StaffModeToggleEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final boolean enable;

    public StaffModeToggleEvent(Player player, boolean enable) {
        this.player = player;
        this.enable = enable;

        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
