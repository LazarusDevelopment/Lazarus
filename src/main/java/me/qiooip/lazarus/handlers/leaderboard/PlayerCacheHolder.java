package me.qiooip.lazarus.handlers.leaderboard;

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

    @SerializedName("highestKillstreaks")
    private NavigableSet<UuidCacheEntry<Integer>> highestKillstreaks;

    public PlayerCacheHolder() {
        this.topKills = new TreeSet<>();
        this.topDeaths = new TreeSet<>();
        this.topBalance = new TreeSet<>();
        this.highestKillstreaks = new TreeSet<>();
    }
}
