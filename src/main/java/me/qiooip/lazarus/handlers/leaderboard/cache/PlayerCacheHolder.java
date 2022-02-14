package me.qiooip.lazarus.handlers.leaderboard.cache;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.qiooip.lazarus.handlers.leaderboard.entry.UuidCacheEntry;

import java.util.NavigableSet;
import java.util.TreeSet;

@Getter
@AllArgsConstructor
public class PlayerCacheHolder {

    @SerializedName("topKills")
    private NavigableSet<UuidCacheEntry<Integer>> topKills;

    @SerializedName("topDeaths")
    private NavigableSet<UuidCacheEntry<Integer>> topDeaths;

    @SerializedName("topBalance")
    private NavigableSet<UuidCacheEntry<Integer>> topBalance;

    @SerializedName("topHighestKillstreak")
    private NavigableSet<UuidCacheEntry<Integer>> topHighestKillstreak;

    public PlayerCacheHolder() {
        this.topKills = new TreeSet<>();
        this.topDeaths = new TreeSet<>();
        this.topBalance = new TreeSet<>();
        this.topHighestKillstreak = new TreeSet<>();
    }

    public void clear() {
        this.topKills.clear();
        this.topDeaths.clear();
        this.topBalance.clear();
        this.topHighestKillstreak.clear();
    }
}
