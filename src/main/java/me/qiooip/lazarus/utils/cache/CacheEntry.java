package me.qiooip.lazarus.utils.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CacheEntry<K> {

    private final K key;
    private final long expiresAt;

    public boolean isExpired() {
        return System.currentTimeMillis() > this.expiresAt;
    }
}
