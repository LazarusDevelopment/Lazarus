package me.qiooip.lazarus.handlers.leaderboard.cache;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.handlers.leaderboard.entry.UuidCacheEntry;

import java.util.concurrent.ConcurrentSkipListSet;

@Getter @Setter
@AllArgsConstructor
public class FactionCacheHolder {

    @SerializedName("topKills")
    private ConcurrentSkipListSet<UuidCacheEntry<Integer>> topKills;

    @SerializedName("topPoints")
    private ConcurrentSkipListSet<UuidCacheEntry<Integer>> topPoints;

    @SerializedName("topBalance")
    private ConcurrentSkipListSet<UuidCacheEntry<Integer>> topBalance;

    @SerializedName("topKothsCapped")
    private ConcurrentSkipListSet<UuidCacheEntry<Integer>> topKothsCapped;

    public FactionCacheHolder() {
        this.topKills = new ConcurrentSkipListSet<>();
        this.topPoints = new ConcurrentSkipListSet<>();
        this.topBalance = new ConcurrentSkipListSet<>();
        this.topKothsCapped = new ConcurrentSkipListSet<>();
    }

    public void clear() {
        this.topKills.clear();
        this.topPoints.clear();
        this.topBalance.clear();
        this.topKothsCapped.clear();
    }
}
