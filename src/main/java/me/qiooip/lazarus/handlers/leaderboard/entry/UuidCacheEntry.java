package me.qiooip.lazarus.handlers.leaderboard.entry;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class UuidCacheEntry<V extends Comparable<V>> extends CacheEntry<UUID, V> {

    public UuidCacheEntry(Player player, V value) {
        this(player.getUniqueId(), player.getName(), value, System.currentTimeMillis());
    }

    public UuidCacheEntry(UUID key, String name, V value, long timestamp) {
        super(key, name, value, timestamp);
    }
}
