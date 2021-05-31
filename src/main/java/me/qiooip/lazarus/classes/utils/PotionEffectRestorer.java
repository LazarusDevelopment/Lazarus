package me.qiooip.lazarus.classes.utils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.classes.manager.PvpClass;
import me.qiooip.lazarus.classes.manager.PvpClassManager;
import me.qiooip.lazarus.utils.ServerUtils;
import me.qiooip.lazarus.utils.Tasks;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionEffectAddEvent;
import org.bukkit.event.entity.PotionEffectExpireEvent;
import org.bukkit.event.entity.PotionEffectRemoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class PotionEffectRestorer implements Listener {

    private static final long INFINITE_DURATION = 12_000L;
    private final PvpClassManager pvpClassManager;

    private final Table<UUID, PotionEffectType, EffectRestore> restorers;
    private final Map<UUID, PotionEffect[]> playerEffectCache;

    public PotionEffectRestorer(PvpClassManager pvpClassManager) {
        this.pvpClassManager = pvpClassManager;

        this.restorers = HashBasedTable.create();
        this.playerEffectCache = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
        Bukkit.getOnlinePlayers().forEach(this::cachePlayerEffects);
    }

    public void disable() {
        this.restorers.clear();
        this.playerEffectCache.clear();
    }

    public void removeEffectCache(Player player) {
        this.playerEffectCache.remove(player.getUniqueId());
    }

    public PotionEffect getPotionEffectToRestore(Player player, PotionEffectType effectType) {
        EffectRestore effectRestore = this.restorers.remove(player.getUniqueId(), effectType);
        return effectRestore != null && effectRestore.getCondition().test(player) ? effectRestore.getEffect() : null;
    }

    private void queueEffectRestore(Player player, PotionEffect toAdd, PotionEffect current) {
        int durationDiff = toAdd.getDuration() - current.getDuration();

        if(toAdd.getAmplifier() == current.getAmplifier() && durationDiff < 5) return;
        if(current.getDuration() < INFINITE_DURATION && toAdd.getAmplifier() <= current.getAmplifier()) return;

        EffectRestore effectRestore = this.createEffectRestore(player, current);
        this.restorers.put(player.getUniqueId(), current.getType(), effectRestore);
    }

    private EffectRestore createEffectRestore(Player player, PotionEffect currentEffect) {
        PvpClass pvpClass = this.pvpClassManager.getActivePvpClass(player);
        Predicate<Player> condition = futurePlayer -> true;

        if(currentEffect.getDuration() > INFINITE_DURATION && pvpClass != null) {
            condition = futurePlayer -> pvpClass.isActive(futurePlayer);
        }

        return new EffectRestore(currentEffect, condition);
    }

    private void handleEffectRestore(Player player, PotionEffectType effectType) {
        PotionEffect effect = this.getPotionEffectToRestore(player, effectType);
        if(effect == null) return;

        Tasks.sync(() -> NmsUtils.getInstance().addPotionEffect(player, effect));
    }

    private PotionEffect getPlayerPreviousEffect(Player player, PotionEffectType effectType) {
        PotionEffect[] playerEffects = this.playerEffectCache.get(player.getUniqueId());
        if(playerEffects == null) return null;

        return playerEffects[effectType.getId() - 1];
    }

    public void cachePlayerEffects(Player player) {
        Collection<PotionEffect> currentEffects = player.getActivePotionEffects();
        PotionEffect[] effectCache = new PotionEffect[23];

        for(PotionEffect potionEffect : currentEffects) {
            effectCache[potionEffect.getType().getId() - 1] = potionEffect;
        }

        this.playerEffectCache.put(player.getUniqueId(), effectCache);
    }

    private void addPotionEffectToCache(Player player, PotionEffect potionEffect) {
        if(potionEffect != null) {
            PotionEffect[] effectCache = this.playerEffectCache.get(player.getUniqueId());
            effectCache[potionEffect.getType().getId() - 1] = potionEffect;
        }
    }

    private void removePotionEffectFromCache(Player player, PotionEffectType effectType) {
        PotionEffect[] effectCache = this.playerEffectCache.get(player.getUniqueId());
        if(effectCache == null) return;

        effectCache[effectType.getId() - 1] = null;
    }

    public void removePlayerEffect(Player player, PotionEffectType effectType) {
        PotionEffect effectToRestore = this.getPotionEffectToRestore(player, effectType);

        if(effectToRestore == null) {
            player.removePotionEffect(effectType);
        } else {
            NmsUtils.getInstance().addPotionEffect(player, effectToRestore);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPotionEffectAdd(PotionEffectAddEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        PotionEffect toAdd = ServerUtils.getEffect(event);
        PotionEffect currentEffect = this.getPlayerPreviousEffect(player, toAdd.getType());

        this.addPotionEffectToCache(player, toAdd);

        if(currentEffect != null) {
            this.queueEffectRestore(player, toAdd, currentEffect);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPotionEffectExpire(PotionEffectExpireEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        PotionEffectType effectType = ServerUtils.getEffect(event).getType();

        this.handleEffectRestore(player, effectType);
        this.removePotionEffectFromCache(player, effectType);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPotionEffectRemove(PotionEffectRemoveEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        UUID uuid = event.getEntity().getUniqueId();
        PotionEffectType effectType = ServerUtils.getEffect(event).getType();

        Tasks.sync(() -> {
            Player player = Bukkit.getPlayer(uuid);
            if(player == null) return;

            if(!player.hasPotionEffect(effectType)) {
                this.removePotionEffectFromCache(player, effectType);
            } else {
                this.addPotionEffectToCache(player, NmsUtils.getInstance().getPotionEffect(player, effectType));
            }
        });
    }

    @Getter
    @AllArgsConstructor
    static class EffectRestore {

        private final PotionEffect effect;
        private final Predicate<Player> condition;
    }
}
