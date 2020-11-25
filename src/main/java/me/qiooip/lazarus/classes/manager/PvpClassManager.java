package me.qiooip.lazarus.classes.manager;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.classes.Archer;
import me.qiooip.lazarus.classes.Bard;
import me.qiooip.lazarus.classes.Bard.BardPower;
import me.qiooip.lazarus.classes.Miner;
import me.qiooip.lazarus.classes.Rogue;
import me.qiooip.lazarus.classes.event.PvpClassEquipEvent;
import me.qiooip.lazarus.classes.event.PvpClassUnequipEvent;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.event.FactionDisbandEvent;
import me.qiooip.lazarus.factions.event.PlayerJoinFactionEvent;
import me.qiooip.lazarus.factions.event.PlayerLeaveFactionEvent;
import me.qiooip.lazarus.factions.event.PlayerLeaveFactionEvent.LeaveReason;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.scoreboard.PvpClassWarmupTimer;
import me.qiooip.lazarus.utils.ManagerEnabler;
import me.qiooip.lazarus.utils.ServerUtils;
import me.qiooip.lazarus.utils.Tasks;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionEffectExpireEvent;
import org.bukkit.event.inventory.EquipmentSetEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PvpClassManager implements Listener, ManagerEnabler {

    @Getter private Miner miner;
    private final List<PvpClass> pvpClasses;
    private final Table<UUID, PotionEffectType, PotionEffect> restorers;

    public PvpClassManager() {
        this.pvpClasses = new ArrayList<>();
        this.restorers = HashBasedTable.create();

        if(Config.ARCHER_ACTIVATED) this.pvpClasses.add(new Archer(this));
        if(Config.BARD_ACTIVATED) this.pvpClasses.add(new Bard(this));
        if(Config.MINER_ACTIVATED) this.pvpClasses.add(this.miner = new Miner(this));
        if(Config.ROGUE_ACTIVATED) this.pvpClasses.add(new Rogue(this));

        Bukkit.getOnlinePlayers().forEach(player -> this.pvpClasses
            .forEach(pvpClass -> pvpClass.checkEquipmentChange(player)));

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.pvpClasses.forEach(PvpClass::disable);
        this.pvpClasses.clear();
    }

    public PvpClass getActivePvpClass(Player player) {
        for(PvpClass pvpClass : this.pvpClasses) {
            if(pvpClass.isActive(player)) return pvpClass;
        }

        return null;
    }

    public PvpClass getWarmupOrActivePvpClass(Player player) {
        for(PvpClass pvpClass : this.pvpClasses) {
            if(pvpClass.isWarmupOrActive(player)) return pvpClass;
        }

        return null;
    }

    public PotionEffect getPotionEffect(Player player, PotionEffectType effectType) {
        return restorers.remove(player.getUniqueId(), effectType);
    }

    public void addPotionEffect(Player player, PotionEffect toAdd) {
        if(!player.hasPotionEffect(toAdd.getType())) {
            NmsUtils.getInstance().addPotionEffect(player, toAdd);
            return;
        }

        PotionEffect effect = NmsUtils.getInstance().getPotionEffect(player, toAdd.getType());

        if(toAdd.getAmplifier() < effect.getAmplifier()) return;
        if(toAdd.getAmplifier() == effect.getAmplifier() && toAdd.getDuration() < effect.getDuration()) return;

        this.restorers.put(player.getUniqueId(), effect.getType(), effect);
        NmsUtils.getInstance().addPotionEffect(player, toAdd);
    }

    private void increaseFactionLimit(PvpClass pvpClass, PlayerFaction faction) {
        Map<UUID, Integer> factionLimit = pvpClass.getFactionLimit();
        factionLimit.put(faction.getId(), factionLimit.getOrDefault(faction.getId(), 0) + 1);
    }

    private void decreaseFactionLimit(PvpClass pvpClass, PlayerFaction faction) {
        Map<UUID, Integer> factionLimit = pvpClass.getFactionLimit();
        factionLimit.put(faction.getId(), factionLimit.getOrDefault(faction.getId(), 1) - 1);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPvpClassEquip(PvpClassEquipEvent event) {
        if(event.getPvpClass() instanceof Bard) {
            ((Bard) event.getPvpClass()).getBardPowers().put(event.getPlayer(), new BardPower());
        }

        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(event.getPlayer());
        if(faction == null) return;

        if(event.getPvpClass().isAtFactionLimit(faction)) {
            Player player = Bukkit.getPlayer(event.getPlayer());

            if(player != null) {
                player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_PVP_CLASS_LIMIT_DENY_EQUIP
                .replace("<pvpClass>", event.getPvpClass().getName()));
            }

            event.setCancelled(true);
            return;
        }

        this.increaseFactionLimit(event.getPvpClass(), faction);
    }

    @EventHandler
    public void onPvpClassUnequip(PvpClassUnequipEvent event) {
        if(event.getPvpClass() instanceof Bard) {
            ((Bard) event.getPvpClass()).getBardPowers().remove(event.getPlayer());
        }

        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(event.getPlayer());
        if(faction == null) return;

        this.decreaseFactionLimit(event.getPvpClass(), faction);
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionDisband(FactionDisbandEvent event) {
        if(!(event.getFaction() instanceof PlayerFaction)) return;

        this.pvpClasses.forEach(pvpClass -> pvpClass.getFactionLimit().remove(event.getFaction().getId()));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoinFactionEvent(PlayerJoinFactionEvent event) {
        Player player = event.getFactionPlayer().getPlayer();

        PvpClass pvpClass = this.getActivePvpClass(player);
        if(pvpClass == null) return;

        if(pvpClass.isAtFactionLimit(event.getFaction())) {
            pvpClass.deactivateClass(player, false);

            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_PVP_CLASS_LIMIT_CLASS_DEACTIVATED
            .replace("<pvpClass>", pvpClass.getName()));
            return;
        }

        this.increaseFactionLimit(pvpClass, event.getFaction());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLeaveFaction(PlayerLeaveFactionEvent event) {
        if(event.getReason() == LeaveReason.DISBAND) return;

        Player player = event.getFactionPlayer().getPlayer();
        if(player == null) return;

        PvpClass pvpClass = this.getActivePvpClass(player);
        if(pvpClass == null) return;

        this.decreaseFactionLimit(pvpClass, event.getFaction());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if(event.getItem().getType() != Material.MILK_BUCKET) return;

        Tasks.sync(() -> {
            Player player = event.getPlayer();
            if(player == null) return;

            PvpClass pvpClass = this.getActivePvpClass(player);
            if(pvpClass == null) return;

            pvpClass.getEffects().forEach(effect -> player.addPotionEffect(effect, true));

            if(pvpClass instanceof Miner) {
                Miner miner = (Miner) pvpClass;
                int diamondsMined = player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE);

                miner.getDiamondData(diamondsMined).forEach(data -> data.getEffects()
                    .forEach(effect -> player.addPotionEffect(effect, true)));
            }
        });
    }

    @EventHandler
    public void onPotionEffectExpire(PotionEffectExpireEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        PotionEffect effect = this.getPotionEffect(player, ServerUtils.getEffect(event).getType());
        if(effect == null) return;

        Tasks.sync(() -> {
            if(effect.getDuration() < 12_000) {
                player.addPotionEffect(effect);
            }

            PvpClass pvpClass = this.getActivePvpClass(player);

            if(pvpClass != null) {
                pvpClass.getEffects().forEach(player::addPotionEffect);
            }
        });
    }

    @EventHandler
    public void onEquipmentSet(EquipmentSetEvent event) {
        for(PvpClass pvpClass : this.pvpClasses) {
            pvpClass.checkEquipmentChange((Player) event.getHumanEntity());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for(PotionEffect effect : event.getPlayer().getActivePotionEffects()) {
            if(effect.getDuration() < 12000) continue;

            event.getPlayer().removePotionEffect(effect.getType());
        }

        if(event.getPlayer().hasPlayedBefore()) {
            this.pvpClasses.forEach(pvpClass -> pvpClass.checkEquipmentChange(event.getPlayer()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        PvpClassWarmupTimer warmupTimer = TimerManager.getInstance().getPvpClassWarmupTimer();

        if(warmupTimer.isActive(player)) {
            warmupTimer.cancel(player);
            return;
        }

        PvpClass pvpClass = this.getActivePvpClass(player);

        if(pvpClass != null) {
            pvpClass.deactivateClass(player, false);
        }
    }
}
