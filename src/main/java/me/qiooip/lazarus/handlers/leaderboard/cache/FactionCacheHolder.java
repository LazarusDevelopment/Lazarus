package me.qiooip.lazarus.handlers.leaderboard.cache;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.qiooip.lazarus.handlers.leaderboard.entry.UuidCacheEntry;

import java.util.NavigableSet;
import java.util.TreeSet;

@Getter
@AllArgsConstructor
public class FactionCacheHolder {

    @SerializedName("topKills")
    private NavigableSet<UuidCacheEntry<Integer>> topKills;

    @SerializedName("topPoints")
    private NavigableSet<UuidCacheEntry<Integer>> topPoints;

    @SerializedName("topBalance")
    private NavigableSet<UuidCacheEntry<Integer>> topBalance;

    @SerializedName("topKothsCapped")
    private NavigableSet<UuidCacheEntry<Integer>> topKothsCapped;

    public FactionCacheHolder() {
        this.topKills = new TreeSet<>();
        this.topPoints = new TreeSet<>();
        this.topBalance = new TreeSet<>();
        this.topKothsCapped = new TreeSet<>();
    }

    public void clear() {
        this.topKills.clear();
        this.topPoints.clear();
        this.topBalance.clear();
        this.topKothsCapped.clear();
    }
}
