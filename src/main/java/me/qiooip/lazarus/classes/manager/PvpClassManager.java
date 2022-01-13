package me.qiooip.lazarus.classes.manager;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.classes.Archer;
import me.qiooip.lazarus.classes.Bard;
import me.qiooip.lazarus.classes.Bard.BardPower;
import me.qiooip.lazarus.classes.Bomber;
import me.qiooip.lazarus.classes.Mage;
import me.qiooip.lazarus.classes.Mage.MagePower;
import me.qiooip.lazarus.classes.Miner;
import me.qiooip.lazarus.classes.Rogue;
import me.qiooip.lazarus.classes.event.PvpClassEquipEvent;
import me.qiooip.lazarus.classes.event.PvpClassUnequipEvent;
import me.qiooip.lazarus.classes.utils.PotionEffectRestorer;
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
import me.qiooip.lazarus.utils.Tasks;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.EquipmentSetEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class PvpClassManager implements Listener, ManagerEnabler {

    private final PotionEffectRestorer potionEffectRestorer;
    private final Map<String, PvpClass> pvpClasses;

    public PvpClassManager() {
        this.potionEffectRestorer = new PotionEffectRestorer(this);
        this.pvpClasses = new HashMap<>();

        if(Config.ARCHER_ACTIVATED) this.registerPvpClass(PvpClassType.ARCHER.getName(), new Archer(this));
        if(Config.BARD_ACTIVATED) this.registerPvpClass(PvpClassType.BARD.getName(), new Bard(this));
        if(Config.MINER_ACTIVATED) this.registerPvpClass(PvpClassType.MINER.getName(), new Miner(this));
        if(Config.ROGUE_ACTIVATED) this.registerPvpClass(PvpClassType.ROGUE.getName(), new Rogue(this));
        if(Config.MAGE_ACTIVATED) this.registerPvpClass(PvpClassType.MAGE.getName(), new Mage(this));
        if(Config.BOMBER_ACTIVATED) this.registerPvpClass(PvpClassType.BOMBER.getName(), new Bomber(this));

        Bukkit.getOnlinePlayers().forEach(player -> {
            this.removeInfiniteEffects(player);
            this.pvpClasses.values().forEach(pvpClass -> pvpClass.checkEquipmentChange(player));
        });

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.pvpClasses.values().forEach(PvpClass::disable);
        this.pvpClasses.clear();
    }

    public void registerPvpClass(String name, PvpClass pvpClass) {
        PvpClass currentPvpClass = this.getPvpClassByName(name);

        if(currentPvpClass != null) {
            currentPvpClass.disable();
        }

        this.pvpClasses.put(name, pvpClass);
    }

    public PvpClass getPvpClassByName(String name) {
        return this.pvpClasses.get(name);
    }

    public PvpClass getActivePvpClass(Player player) {
        for(PvpClass pvpClass : this.pvpClasses.values()) {
            if(pvpClass.isActive(player)) return pvpClass;
        }

        return null;
    }

    public PvpClass getWarmupOrActivePvpClass(Player player) {
        for(PvpClass pvpClass : this.pvpClasses.values()) {
            if(pvpClass.isWarmupOrActive(player)) return pvpClass;
        }

        return null;
    }

    private void increaseFactionLimit(PvpClass pvpClass, PlayerFaction faction) {
        Map<UUID, Integer> factionLimit = pvpClass.getFactionLimit();
        factionLimit.put(faction.getId(), factionLimit.getOrDefault(faction.getId(), 0) + 1);
    }

    private void decreaseFactionLimit(PvpClass pvpClass, PlayerFaction faction) {
        Map<UUID, Integer> factionLimit = pvpClass.getFactionLimit();
        factionLimit.put(faction.getId(), factionLimit.getOrDefault(faction.getId(), 1) - 1);
    }

    private void removeInfiniteEffects(Player player) {
        for(PotionEffect effect : player.getActivePotionEffects()) {
            if(effect.getDuration() < 12000) continue;

            player.removePotionEffect(effect.getType());
        }
    }

    public void addPotionEffect(Player player, PotionEffect toAdd) {
        if(!player.hasPotionEffect(toAdd.getType())) {
            NmsUtils.getInstance().addPotionEffect(player, toAdd);
            return;
        }

        PotionEffect effect = NmsUtils.getInstance().getPotionEffect(player, toAdd.getType());

        if(toAdd.getAmplifier() < effect.getAmplifier()) return;
        if(toAdd.getAmplifier() == effect.getAmplifier() && toAdd.getDuration() < effect.getDuration()) return;

        NmsUtils.getInstance().addPotionEffect(player, toAdd);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPvpClassEquip(PvpClassEquipEvent event) {
        UUID playerUuid = event.getPlayer();

        if(event.getPvpClass() instanceof Bard) {
            ((Bard) event.getPvpClass()).getBardPowers().put(playerUuid, new BardPower());
        }

        if(event.getPvpClass() instanceof Mage) {
            ((Mage) event.getPvpClass()).getMagePowers().put(playerUuid, new MagePower());
        }

        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(playerUuid);
        if(faction == null) return;

        if(event.getPvpClass().isAtFactionLimit(faction)) {
            Player player = Bukkit.getPlayer(playerUuid);

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
        UUID playerUuid = event.getPlayer();

        if(event.getPvpClass() instanceof Bard) {
            ((Bard) event.getPvpClass()).getBardPowers().remove(playerUuid);
        }

        if(event.getPvpClass() instanceof Mage) {
            ((Mage) event.getPvpClass()).getMagePowers().remove(playerUuid);
        }

        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(playerUuid);
        if(faction == null) return;

        this.decreaseFactionLimit(event.getPvpClass(), faction);
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionDisband(FactionDisbandEvent event) {
        if(!(event.getFaction() instanceof PlayerFaction)) return;

        this.pvpClasses.values().forEach(pvpClass -> pvpClass.getFactionLimit().remove(event.getFaction().getId()));
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

            pvpClass.getEffects().forEach(effect -> NmsUtils.getInstance().addPotionEffect(player, effect));

            if(pvpClass instanceof Miner) {
                Miner miner = (Miner) pvpClass;
                int diamondsMined = player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE);

                miner.getDiamondData(diamondsMined).forEach(data -> data.getEffects()
                    .forEach(effect -> NmsUtils.getInstance().addPotionEffect(player, effect)));
            }
        });
    }

    @EventHandler
    public void onEquipmentSet(EquipmentSetEvent event) {
        for(PvpClass pvpClass : this.pvpClasses.values()) {
            pvpClass.checkEquipmentChange((Player) event.getHumanEntity());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        this.removeInfiniteEffects(player);
        this.potionEffectRestorer.cachePlayerEffects(player);

        if(player.hasPlayedBefore()) {
            this.pvpClasses.values().forEach(pvpClass -> pvpClass.checkEquipmentChange(player));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.potionEffectRestorer.removeEffectCache(player);

        PvpClassWarmupTimer warmupTimer = TimerManager.getInstance().getPvpClassWarmupTimer();

        if(warmupTimer.isActive(player)) {
            warmupTimer.cancel(player);
            return;
        }

        PvpClass pvpClass = this.getActivePvpClass(player);

        if(pvpClass != null) {
            pvpClass.deactivateClass(player, false);

            if(pvpClass instanceof Bard) {
                ((Bard) pvpClass).removePlayerMessageDelays(player);
            }
        }
    }
}
