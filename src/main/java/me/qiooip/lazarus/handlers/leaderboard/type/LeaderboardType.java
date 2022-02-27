package me.qiooip.lazarus.handlers.leaderboard.type;

import me.qiooip.lazarus.handlers.leaderboard.entry.UuidCacheEntry;
import me.qiooip.lazarus.hologram.type.LeaderboardHologramType;

import java.util.NavigableSet;

public interface LeaderboardType {

    String getTitle();
    String getLineFormat();
    LeaderboardHologramType getHologramType();
    NavigableSet<UuidCacheEntry<Integer>> getLeaderboard();
}
