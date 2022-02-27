package me.qiooip.lazarus.handlers.leaderboard.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.leaderboard.entry.UuidCacheEntry;
import me.qiooip.lazarus.hologram.type.LeaderboardHologramType;
import me.qiooip.lazarus.userdata.event.UserdataValueType;

import java.util.NavigableSet;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum PlayerLeaderboardType implements LeaderboardType {

    KILLS(
        "kills",
        Language.LEADERBOARDS_KILLS_TITLE,
        Language.LEADERBOARDS_KILLS_LINE_FORMAT,
        LeaderboardHologramType.PLAYER_KILLS,
        Lazarus.getInstance().getLeaderboardHandler().getPlayerCacheHolder().getTopKills()
    ),
    DEATHS(
        "deaths",
        Language.LEADERBOARDS_DEATHS_TITLE,
        Language.LEADERBOARDS_DEATHS_LINE_FORMAT,
        LeaderboardHologramType.PLAYER_DEATHS,
        Lazarus.getInstance().getLeaderboardHandler().getPlayerCacheHolder().getTopDeaths()
    ),
    BALANCE(
        "balance",
        Language.LEADERBOARDS_BALANCE_TITLE,
        Language.LEADERBOARDS_BALANCE_LINE_FORMAT,
        LeaderboardHologramType.PLAYER_BALANCE,
        Lazarus.getInstance().getLeaderboardHandler().getPlayerCacheHolder().getTopBalance()
    ),
    HIGHEST_KILLSTREAK(
        "killstreak",
        Language.LEADERBOARDS_KILLSTREAK_TITLE,
        Language.LEADERBOARDS_KILLSTREAK_LINE_FORMAT,
        LeaderboardHologramType.PLAYER_HIGHEST_KILLSTREAK,
        Lazarus.getInstance().getLeaderboardHandler().getPlayerCacheHolder().getTopHighestKillstreak()
    );

    private final String paramName;
    private final String title;
    private final String lineFormat;
    private final LeaderboardHologramType hologramType;
    private final NavigableSet<UuidCacheEntry<Integer>> leaderboard;

    public static PlayerLeaderboardType getByName(String name) {
        return Stream.of(values()).filter(type -> type.getParamName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static PlayerLeaderboardType getFromUserdataValueType(UserdataValueType type) {
        switch(type) {
            case KILLS: return PlayerLeaderboardType.KILLS;
            case DEATHS: return PlayerLeaderboardType.DEATHS;
            case BALANCE: return PlayerLeaderboardType.BALANCE;
            case HIGHEST_KILLSTREAK: return PlayerLeaderboardType.HIGHEST_KILLSTREAK;
        }

        throw new IllegalArgumentException("PlayerLeaderboardType for '" + type.name() + "' couldn't be found");
    }

    public static UserdataValueType getUserdataValueTypeFrom(PlayerLeaderboardType type) {
        switch(type) {
            case KILLS: return UserdataValueType.KILLS;
            case DEATHS: return UserdataValueType.DEATHS;
            case BALANCE: return UserdataValueType.BALANCE;
            case HIGHEST_KILLSTREAK: return UserdataValueType.HIGHEST_KILLSTREAK;
        }

        throw new IllegalArgumentException("UserdataValueType for '" + type.name() + "' couldn't be found");
    }
}
