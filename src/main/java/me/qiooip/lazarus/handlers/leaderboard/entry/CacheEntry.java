package me.qiooip.lazarus.handlers.leaderboard.entry;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class CacheEntry<K, V extends Comparable<V>> implements Comparable<CacheEntry<K, V>> {

    protected final K key;
    protected final String name;
    protected final V value;
    protected final long timestamp;

    @Override
    public int compareTo(CacheEntry<K, V> other) {
        int valueCompare = -this.value.compareTo(other.getValue());
        return valueCompare == 0 ? Long.compare(this.timestamp, other.getTimestamp()) : valueCompare;
    }

    @Override
    public int hashCode() {
        return this.key.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof CacheEntry<?,?> && ((CacheEntry<?,?>) other).getKey().equals(this.key);
    }
}
