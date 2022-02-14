package me.qiooip.lazarus.handlers.leaderboard.type;

import me.qiooip.lazarus.handlers.leaderboard.entry.UuidCacheEntry;

import java.util.NavigableSet;

public interface LeaderboardType {

    String getTitle();
    String getLineFormat();
    NavigableSet<UuidCacheEntry<Integer>> getLeaderboard();
}
