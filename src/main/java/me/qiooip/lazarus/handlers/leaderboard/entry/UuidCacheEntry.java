package me.qiooip.lazarus.handlers.leaderboard.entry;

import lombok.Getter;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class UuidCacheEntry<V extends Comparable<V>> extends CacheEntry<UUID, V> {

    public UuidCacheEntry(Player player, V value) {
        this(player.getUniqueId(), player.getName(), value);
    }

    public UuidCacheEntry(PlayerFaction faction, V value) {
        this(faction.getId(), faction.getName(), value);
    }

    public UuidCacheEntry(UUID key, String name, V value) {
        this(key, name, value, System.currentTimeMillis());
    }

    public UuidCacheEntry(UUID key, String name, V value, long timestamp) {
        super(key, name, value, timestamp);
    }
}
