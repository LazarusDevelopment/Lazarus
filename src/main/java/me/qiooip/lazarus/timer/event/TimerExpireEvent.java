package me.qiooip.lazarus.timer.event;

import lombok.Getter;
import me.qiooip.lazarus.timer.Timer;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

@Getter
public class TimerExpireEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final UUID uuid;
    private final Timer timer;

    public TimerExpireEvent(UUID uuid, Timer timer) {
        this.uuid = uuid;
        this.timer = timer;

        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}