package me.qiooip.lazarus.games.schedule.event;

import lombok.Getter;
import me.qiooip.lazarus.games.schedule.ScheduleData;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class ScheduleCreateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final ScheduleData schedule;

    public ScheduleCreateEvent(ScheduleData schedule) {
        this.schedule = schedule;
        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
