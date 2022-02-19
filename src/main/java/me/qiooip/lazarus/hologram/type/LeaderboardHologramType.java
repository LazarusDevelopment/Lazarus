package me.qiooip.lazarus.hologram.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.leaderboard.entry.UuidCacheEntry;

import java.util.List;
import java.util.NavigableSet;

@Getter
@AllArgsConstructor
public enum LeaderboardHologramType {

    PLAYER_KILLS(
        Language.HOLOGRAM_TOP_KILLS_LINE_FORMAT,
        Language.HOLOGRAM_TOP_KILLS_HEADER,
        Language.HOLOGRAM_TOP_KILLS_FOOTER,
        Lazarus.getInstance().getLeaderboardHandler().getPlayerCacheHolder().getTopKills()
    ),
    PLAYER_DEATHS(
        Language.HOLOGRAM_TOP_DEATHS_LINE_FORMAT,
        Language.HOLOGRAM_TOP_DEATHS_HEADER,
        Language.HOLOGRAM_TOP_DEATHS_FOOTER,
        Lazarus.getInstance().getLeaderboardHandler().getPlayerCacheHolder().getTopDeaths()
    ),
    PLAYER_BALANCE(
        Language.HOLOGRAM_TOP_BALANCE_LINE_FORMAT,
        Language.HOLOGRAM_TOP_BALANCE_HEADER,
        Language.HOLOGRAM_TOP_BALANCE_FOOTER,
        Lazarus.getInstance().getLeaderboardHandler().getPlayerCacheHolder().getTopBalance()
    ),
    PLAYER_HIGHEST_KILLSTREAK(
        Language.HOLOGRAM_TOP_KILLSTREAK_LINE_FORMAT,
        Language.HOLOGRAM_TOP_KILLSTREAK_HEADER,
        Language.HOLOGRAM_TOP_KILLSTREAK_FOOTER,
        Lazarus.getInstance().getLeaderboardHandler().getPlayerCacheHolder().getTopHighestKillstreak()
    ),
    FACTION_KILLS(
        Language.HOLOGRAM_FACTION_TOP_KILLS_LINE_FORMAT,
        Language.HOLOGRAM_FACTION_TOP_KILLS_HEADER,
        Language.HOLOGRAM_FACTION_TOP_KILLS_FOOTER,
        Lazarus.getInstance().getLeaderboardHandler().getFactionCacheHolder().getTopKills()
    ),
    FACTION_POINTS(
        Language.HOLOGRAM_FACTION_TOP_POINTS_LINE_FORMAT,
        Language.HOLOGRAM_FACTION_TOP_POINTS_HEADER,
        Language.HOLOGRAM_FACTION_TOP_POINTS_FOOTER,
        Lazarus.getInstance().getLeaderboardHandler().getFactionCacheHolder().getTopPoints()
    ),
    FACTION_BALANCE(
        Language.HOLOGRAM_FACTION_TOP_BALANCE_LINE_FORMAT,
        Language.HOLOGRAM_FACTION_TOP_BALANCE_HEADER,
        Language.HOLOGRAM_FACTION_TOP_BALANCE_FOOTER,
        Lazarus.getInstance().getLeaderboardHandler().getFactionCacheHolder().getTopBalance()
    ),
    FACTION_KOTHS_CAPPED(
        Language.HOLOGRAM_FACTION_TOP_KOTHS_CAPPED_LINE_FORMAT,
        Language.HOLOGRAM_FACTION_TOP_KOTHS_CAPPED_HEADER,
        Language.HOLOGRAM_FACTION_TOP_KOTHS_CAPPED_FOOTER,
        Lazarus.getInstance().getLeaderboardHandler().getFactionCacheHolder().getTopKothsCapped()
    );

    private final String lineFormat;
    private final List<String> header;
    private final List<String> footer;
    private final NavigableSet<UuidCacheEntry<Integer>> leaderboard;
}
