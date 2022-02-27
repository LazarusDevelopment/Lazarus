package me.qiooip.lazarus.handlers.leaderboard.event;

import lombok.Getter;
import me.qiooip.lazarus.handlers.leaderboard.type.LeaderboardType;
import me.qiooip.lazarus.hologram.type.LeaderboardHologramType;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class LeaderboardUpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final LeaderboardHologramType type;

    public LeaderboardUpdateEvent(LeaderboardType type) {
        this.type = type.getHologramType();
        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}