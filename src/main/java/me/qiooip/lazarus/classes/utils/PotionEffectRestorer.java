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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class PotionEffectRestorer implements Listener {

    private final PvpClassManager pvpClassManager;

    private final Table<UUID, PotionEffectType, EffectRestore> restorers;
    private final Map<UUID, Collection<PotionEffect>> playerEffectCache;

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

    public PotionEffect getPotionEffectToRestore(Player player, PotionEffectType effectType) {
        EffectRestore effectRestore = this.restorers.remove(player.getUniqueId(), effectType);
        return effectRestore != null && effectRestore.getCondition().test(player) ? effectRestore.getEffect() : null;
    }

    private void queueEffectRestore(Player player, PotionEffect toAdd, PotionEffect current) {
        int durationDiff = toAdd.getDuration() - current.getDuration();
        if(toAdd.getAmplifier() == current.getAmplifier() && durationDiff < 5) return;

        EffectRestore effectRestore = this.createEffectRestore(player, current);
        this.restorers.put(player.getUniqueId(), current.getType(), effectRestore);
    }

    private EffectRestore createEffectRestore(Player player, PotionEffect currentEffect) {
        PvpClass pvpClass = this.pvpClassManager.getActivePvpClass(player);
        Predicate<Player> condition = futurePlayer -> true;

        if(currentEffect.getDuration() > 12_000 && pvpClass != null) {
            condition = futurePlayer -> pvpClass.isActive(futurePlayer);
        }

        return new EffectRestore(currentEffect, condition);
    }

    private void handleEffectRestore(Player player, PotionEffectType effectType) {
        PotionEffect effect = this.getPotionEffectToRestore(player, effectType);
        if(effect == null) return;

        NmsUtils.getInstance().addPotionEffect(player, effect);
    }

    private PotionEffect getPlayerPreviousEffect(Player player, PotionEffectType effectType) {
        Collection<PotionEffect> playerEffects = this.playerEffectCache.get(player.getUniqueId());
        if(playerEffects == null || playerEffects.isEmpty()) return null;

        PotionEffect currentEffect = null;

        for(PotionEffect effect : playerEffects) {
            if(effect.getType().equals(effectType)) {
                currentEffect = effect;
                break;
            }
        }

        return currentEffect;
    }

    private void cachePlayerEffects(Player player) {
        Collection<PotionEffect> currentEffects = player.getActivePotionEffects();

        if(!currentEffects.isEmpty()) {
            this.playerEffectCache.put(player.getUniqueId(), currentEffects);
        }
    }

    public void removePlayerEffect(Player player, PotionEffectType effectType) {
        PotionEffect effectToRestore = this.getPotionEffectToRestore(player, effectType);

        if(effectToRestore == null) {
            player.removePotionEffect(effectType);
        } else {
            NmsUtils.getInstance().addPotionEffect(player, effectToRestore);
        }
    }

    @EventHandler
    public void onPotionEffectAdd(PotionEffectAddEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        PotionEffect toAdd = ServerUtils.getEffect(event);
        PotionEffect currentEffect = this.getPlayerPreviousEffect(player, toAdd.getType());

        if(currentEffect != null) {
            this.queueEffectRestore(player, toAdd, currentEffect);
        }

        Tasks.sync(() -> this.playerEffectCache.put(player.getUniqueId(), player.getActivePotionEffects()));
    }

    @EventHandler
    public void onPotionEffectExpire(PotionEffectExpireEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        this.playerEffectCache.put(player.getUniqueId(), player.getActivePotionEffects());
        this.handleEffectRestore(player, ServerUtils.getEffect(event).getType());
    }

    @EventHandler
    public void onPotionEffectRemove(PotionEffectRemoveEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        Tasks.sync(() -> this.playerEffectCache.put(player.getUniqueId(), player.getActivePotionEffects()));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.cachePlayerEffects(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.playerEffectCache.remove(event.getPlayer().getUniqueId());
    }

    @Getter
    @AllArgsConstructor
    static class EffectRestore {

        private final PotionEffect effect;
        private final Predicate<Player> condition;
    }
}
