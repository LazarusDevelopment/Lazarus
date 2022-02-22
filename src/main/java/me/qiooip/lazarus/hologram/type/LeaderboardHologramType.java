package me.qiooip.lazarus.hologram.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.leaderboard.entry.UuidCacheEntry;

import java.util.List;
import java.util.NavigableSet;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum LeaderboardHologramType {

    PLAYER_KILLS(
        "playerKills",
        Language.HOLOGRAM_TOP_KILLS_LINE_FORMAT,
        Language.HOLOGRAM_TOP_KILLS_HEADER,
        Language.HOLOGRAM_TOP_KILLS_FOOTER,
        Lazarus.getInstance().getLeaderboardHandler().getPlayerCacheHolder().getTopKills()
    ),
    PLAYER_DEATHS(
        "playerDeaths",
        Language.HOLOGRAM_TOP_DEATHS_LINE_FORMAT,
        Language.HOLOGRAM_TOP_DEATHS_HEADER,
        Language.HOLOGRAM_TOP_DEATHS_FOOTER,
        Lazarus.getInstance().getLeaderboardHandler().getPlayerCacheHolder().getTopDeaths()
    ),
    PLAYER_BALANCE(
        "playerBalance",
        Language.HOLOGRAM_TOP_BALANCE_LINE_FORMAT,
        Language.HOLOGRAM_TOP_BALANCE_HEADER,
        Language.HOLOGRAM_TOP_BALANCE_FOOTER,
        Lazarus.getInstance().getLeaderboardHandler().getPlayerCacheHolder().getTopBalance()
    ),
    PLAYER_HIGHEST_KILLSTREAK(
        "playerKillstreak",
        Language.HOLOGRAM_TOP_KILLSTREAK_LINE_FORMAT,
        Language.HOLOGRAM_TOP_KILLSTREAK_HEADER,
        Language.HOLOGRAM_TOP_KILLSTREAK_FOOTER,
        Lazarus.getInstance().getLeaderboardHandler().getPlayerCacheHolder().getTopHighestKillstreak()
    ),
    FACTION_KILLS(
        "factionKills",
        Language.HOLOGRAM_FACTION_TOP_KILLS_LINE_FORMAT,
        Language.HOLOGRAM_FACTION_TOP_KILLS_HEADER,
        Language.HOLOGRAM_FACTION_TOP_KILLS_FOOTER,
        Lazarus.getInstance().getLeaderboardHandler().getFactionCacheHolder().getTopKills()
    ),
    FACTION_POINTS(
        "factionPoints",
        Language.HOLOGRAM_FACTION_TOP_POINTS_LINE_FORMAT,
        Language.HOLOGRAM_FACTION_TOP_POINTS_HEADER,
        Language.HOLOGRAM_FACTION_TOP_POINTS_FOOTER,
        Lazarus.getInstance().getLeaderboardHandler().getFactionCacheHolder().getTopPoints()
    ),
    FACTION_BALANCE(
        "factionBalance",
        Language.HOLOGRAM_FACTION_TOP_BALANCE_LINE_FORMAT,
        Language.HOLOGRAM_FACTION_TOP_BALANCE_HEADER,
        Language.HOLOGRAM_FACTION_TOP_BALANCE_FOOTER,
        Lazarus.getInstance().getLeaderboardHandler().getFactionCacheHolder().getTopBalance()
    ),
    FACTION_KOTHS_CAPPED(
        "factionKoths",
        Language.HOLOGRAM_FACTION_TOP_KOTHS_CAPPED_LINE_FORMAT,
        Language.HOLOGRAM_FACTION_TOP_KOTHS_CAPPED_HEADER,
        Language.HOLOGRAM_FACTION_TOP_KOTHS_CAPPED_FOOTER,
        Lazarus.getInstance().getLeaderboardHandler().getFactionCacheHolder().getTopKothsCapped()
    );

    private final String paramName;
    private final String lineFormat;
    private final List<String> header;
    private final List<String> footer;
    private final NavigableSet<UuidCacheEntry<Integer>> leaderboard;

    public static LeaderboardHologramType getByName(String name) {
        return Stream.of(values()).filter(type -> type.getParamName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
