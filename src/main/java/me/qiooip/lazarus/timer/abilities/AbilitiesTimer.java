package me.qiooip.lazarus.timer.abilities;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.timer.type.PlayerTimer;
import me.qiooip.lazarus.utils.ServerUtils;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.StringUtils.FormatType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class AbilitiesTimer extends PlayerTimer {

    private final Table<UUID, AbilityType, ScheduledFuture<?>> cooldowns;
    private final Function<String, String> placeholderFunction;

    public AbilitiesTimer(ScheduledExecutorService executor) {
        super(executor, "AbilitiesTimer", 0);

        this.cooldowns = HashBasedTable.create();
        this.setFormat(FormatType.MILLIS_TO_SECONDS);

        this.placeholderFunction = ServerUtils.parsePlaceholderFunction(
            Config.ABILITIES_ABILITY_COOLDOWN_PLACEHOLDER, "<abilityName>");
    }

    @Override
    public void disable() {
        this.cooldowns.values().forEach(future -> future.cancel(true));
        this.cooldowns.clear();
    }

    public void activate(Player player, AbilityType abilityType, int delay, String message) {
        this.activate(player.getUniqueId(), abilityType, delay, message);
    }

    private void activate(UUID uuid, AbilityType abilityType, int delay, String message) {
        if(delay <= 0 || this.isActive(uuid, abilityType)) return;
        this.cooldowns.put(uuid, abilityType, this.scheduleExpiry(uuid, abilityType, delay, message));
    }

    public void cancel(Player player, AbilityType abilityType) {
        this.cancel(player.getUniqueId(), abilityType);
    }

    private void cancel(UUID uuid, AbilityType abilityType) {
        if(!this.isActive(uuid, abilityType)) return;

        this.cooldowns.remove(uuid, abilityType).cancel(true);
    }

    public boolean isActive(Player player, AbilityType abilityType) {
        return this.isActive(player.getUniqueId(), abilityType);
    }

    private boolean isActive(UUID uuid, AbilityType abilityType) {
        return this.cooldowns.contains(uuid, abilityType);
    }

    public long getCooldown(Player player, AbilityType abilityType) {
        return this.getCooldown(player.getUniqueId(), abilityType);
    }

    private long getCooldown(UUID uuid, AbilityType abilityType) {
        return this.cooldowns.get(uuid, abilityType).getDelay(TimeUnit.MILLISECONDS);
    }

    public Map<String, String> getActiveAbilities(Player player) {
        if(this.placeholderFunction == null) {
            return null;
        }

        Set<Entry<AbilityType, ScheduledFuture<?>>> cooldowns = this.cooldowns.row(player.getUniqueId()).entrySet();

        if(cooldowns.isEmpty()) {
            return null;
        }

        Map<String, String> abilityPlaceholders = new HashMap<>();

        for(Entry<AbilityType, ScheduledFuture<?>> entries : cooldowns) {
            long remaining = entries.getValue().getDelay(TimeUnit.MILLISECONDS);

            if(remaining < 60_000L) {
                abilityPlaceholders.put(this.placeholderFunction.apply(entries.getKey().getDisplayName()),
                    StringUtils.formatTime(remaining, FormatType.MILLIS_TO_SECONDS) + 's');
            } else {
                abilityPlaceholders.put(this.placeholderFunction.apply(entries.getKey().getDisplayName()),
                    StringUtils.formatTime(remaining, FormatType.MILLIS_TO_MINUTES));
            }
        }

        return abilityPlaceholders;
    }

    public String getDynamicTimeLeft(Player player, AbilityType abilityType) {
        long remaining = this.getCooldown(player, abilityType);

        if(remaining < 60_000L) {
            return StringUtils.formatTime(remaining, FormatType.MILLIS_TO_SECONDS) + 's';
        } else {
            return StringUtils.formatTime(remaining, FormatType.MILLIS_TO_MINUTES);
        }
    }

    private ScheduledFuture<?> scheduleExpiry(UUID uuid, AbilityType abilityType, int delay, String message) {
        return this.executor.schedule(() -> {
            try {
                this.cooldowns.remove(uuid, abilityType);
                if(message == null) return;

                Player player = Bukkit.getPlayer(uuid);
                if(player != null) player.sendMessage(message);
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }, delay, TimeUnit.SECONDS);
    }
}
