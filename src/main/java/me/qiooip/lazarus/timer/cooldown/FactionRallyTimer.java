package me.qiooip.lazarus.timer.cooldown;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.timer.type.SystemTimer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FactionRallyTimer extends SystemTimer {

    private final Map<UUID, ScheduledFuture<?>> factions;

    public FactionRallyTimer(ScheduledExecutorService executor) {
        super(executor, "FactionRallyTimer", Config.FACTION_RALLY_EXPIRE_AFTER, false);

        this.setExpiryMessage(Language.FACTION_PREFIX + Language.FACTIONS_RALLY_EXPIRED);
        this.factions = new HashMap<>();
    }

    @Override
    public void disable() {
        this.factions.values().forEach(future -> future.cancel(true));
        this.factions.clear();
    }

    public void activate(PlayerFaction playerFaction) {
        this.cancel(playerFaction.getId());
        this.activate(playerFaction.getId());
    }

    public void activate(UUID uuid) {
        if(this.delay <= 0 || this.isActive(uuid)) return;
        this.factions.put(uuid, this.scheduleExpiry(uuid, this.delay));
    }

    public void cancel(PlayerFaction playerFaction) {
        this.cancel(playerFaction.getId());
    }

    public void cancel(UUID uuid) {
        if(!this.isActive(uuid)) return;
        this.factions.remove(uuid).cancel(true);
    }

    public boolean isActive(PlayerFaction playerFaction) {
        return this.isActive(playerFaction.getId());
    }

    public boolean isActive(UUID uuid) {
        return this.factions.containsKey(uuid);
    }

    private void handleRallyRemove(UUID uuid) {
        PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFactionByUuid(uuid);
        if(playerFaction == null) return;

        playerFaction.setRallyLocation(null);

        if(this.expiryMessage != null) {
            playerFaction.sendMessage(this.expiryMessage);
        }
    }

    private ScheduledFuture<?> scheduleExpiry(UUID uuid, int delay) {
        return this.executor.schedule(() -> {
            try {
                this.factions.remove(uuid);
                this.handleRallyRemove(uuid);
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }, delay, TimeUnit.SECONDS);
    }
}
