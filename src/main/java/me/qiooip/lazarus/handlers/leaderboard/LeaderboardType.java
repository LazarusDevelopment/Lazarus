package me.qiooip.lazarus.handlers.leaderboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.leaderboard.entry.UuidCacheEntry;

import java.util.NavigableSet;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum LeaderboardType {

    KILLS(
        Language.LEADERBOARDS_KILLS_TITLE,
        Language.LEADERBOARDS_KILLS_LINE_FORMAT,
        Lazarus.getInstance().getLeaderboardHandler().getTopKills()
    ),
    DEATHS(
        Language.LEADERBOARDS_DEATHS_TITLE,
        Language.LEADERBOARDS_DEATHS_LINE_FORMAT,
        Lazarus.getInstance().getLeaderboardHandler().getTopDeaths()
    ),
    BALANCE(
        Language.LEADERBOARDS_BALANCE_TITLE,
        Language.LEADERBOARDS_BALANCE_LINE_FORMAT,
        Lazarus.getInstance().getLeaderboardHandler().getTopBalance()
    ),
    HIGHEST_KILLSTREAK(
        Language.LEADERBOARDS_KILLSTREAK_TITLE,
        Language.LEADERBOARDS_KILLSTREAK_LINE_FORMAT,
        Lazarus.getInstance().getLeaderboardHandler().getHighestKillstreaks()
    );

    private final String title;
    private final String lineFormat;
    private final NavigableSet<UuidCacheEntry<Integer>> leaderboard;

    public static LeaderboardType getByName(String name) {
        return Stream.of(values()).filter(type -> type.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}