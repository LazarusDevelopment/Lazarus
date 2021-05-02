package me.qiooip.lazarus.timer.cooldown;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.type.SystemTimer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FactionFreezeTimer extends SystemTimer {

    private Map<UUID, ScheduledFuture<?>> factions;

    public FactionFreezeTimer(ScheduledExecutorService executor) {
        super(executor, "FactionFreezeTimer", Config.FACTION_DTR_FREEZE_DURATION * 60, false);

        this.setExpiryMessage(Language.FACTIONS_NO_LONGER_FROZEN);
    }

    @Override
    public void disable() {
        this.saveTimer();

        this.factions.values().forEach(future -> future.cancel(true));
        this.factions.clear();
    }

    @Override
    protected void loadTimer() {
        this.factions = new HashMap<>();

        ConfigurationSection section = TimerManager.getInstance().getTimersFile().getSection("FactionFreezeTimer");
        if(section == null) return;

        section.getKeys(false).forEach(key -> {
            UUID uuid = UUID.fromString(key);
            PlayerFaction faction = FactionsManager.getInstance().getPlayerFactionByUuid(uuid);

            if(faction != null) {
                this.activate(faction, (int) section.getLong(key) / 1000);
            }
        });
    }

    @Override
    public void saveTimer() {
        ConfigurationSection section = TimerManager.getInstance().getTimersFile().createSection("FactionFreezeTimer");
        this.factions.forEach((uuid, future) -> section.set(uuid.toString(), future.getDelay(TimeUnit.MILLISECONDS)));
    }

    public void activate(PlayerFaction playerFaction) {
        this.cancel(playerFaction.getId());
        this.activate(playerFaction, this.delay);
    }

    public void activate(PlayerFaction playerFaction, int delay) {
        UUID factionId = playerFaction.getId();
        if(delay <= 0 || this.isActive(factionId)) return;

        this.factions.put(factionId, this.scheduleExpiry(factionId, delay));
        TimerManager.getInstance().getDtrRegenTimer().removeFaction(playerFaction);
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

    public long getCooldown(PlayerFaction playerFaction) {
        ScheduledFuture<?> future = this.factions.get(playerFaction.getId());
        return future != null ? future.getDelay(TimeUnit.MILLISECONDS) : 0L;
    }

    private void handleUnfreeze(UUID uuid) {
        PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFactionByUuid(uuid);
        if(playerFaction == null) return;

        TimerManager.getInstance().getDtrRegenTimer().addFaction(playerFaction, playerFaction.getDtr());

        if(this.expiryMessage != null) {
            playerFaction.sendMessage(this.expiryMessage);
        }
    }

    private ScheduledFuture<?> scheduleExpiry(UUID uuid, int delay) {
        return this.executor.schedule(() -> {
            try {
                this.factions.remove(uuid);
                this.handleUnfreeze(uuid);
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }, delay, TimeUnit.SECONDS);
    }
}
