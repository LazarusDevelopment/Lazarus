package me.qiooip.lazarus.handlers.leaderboard.cache;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.qiooip.lazarus.handlers.leaderboard.entry.UuidCacheEntry;

import java.util.concurrent.ConcurrentSkipListSet;

@Getter
@AllArgsConstructor
public class PlayerCacheHolder {

    @SerializedName("topKills")
    private ConcurrentSkipListSet<UuidCacheEntry<Integer>> topKills;

    @SerializedName("topDeaths")
    private ConcurrentSkipListSet<UuidCacheEntry<Integer>> topDeaths;

    @SerializedName("topBalance")
    private ConcurrentSkipListSet<UuidCacheEntry<Integer>> topBalance;

    @SerializedName("topHighestKillstreak")
    private ConcurrentSkipListSet<UuidCacheEntry<Integer>> topHighestKillstreak;

    public PlayerCacheHolder() {
        this.topKills = new ConcurrentSkipListSet<>();
        this.topDeaths = new ConcurrentSkipListSet<>();
        this.topBalance = new ConcurrentSkipListSet<>();
        this.topHighestKillstreak = new ConcurrentSkipListSet<>();
    }

    public void clear() {
        this.topKills.clear();
        this.topDeaths.clear();
        this.topBalance.clear();
        this.topHighestKillstreak.clear();
    }
}
