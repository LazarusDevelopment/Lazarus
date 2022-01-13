package me.qiooip.lazarus.classes.manager;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.classes.Miner;
import me.qiooip.lazarus.classes.event.PvpClassEquipEvent;
import me.qiooip.lazarus.classes.event.PvpClassUnequipEvent;
import me.qiooip.lazarus.classes.items.ClickableItem;
import me.qiooip.lazarus.classes.utils.PvpClassUtils;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.scoreboard.PlayerScoreboard;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.scoreboard.PvpClassWarmupTimer;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.item.ItemUtils;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter
public abstract class PvpClass implements Listener {

    private final PvpClassManager manager;

    private final String name;

    private final Material helmet;
    private final Material chestplate;
    private final Material leggings;
    private final Material boots;

    private final Set<UUID> players;
    private final List<PotionEffect> effects;
    private final Map<UUID, Integer> factionLimit;

    @Setter protected int warmup;

    protected PvpClass(PvpClassManager manager, String name, Material helmet, Material chestplate, Material leggings, Material boots) {
        this.manager = manager;
        this.name = name;

        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;

        this.players = new HashSet<>();
        this.effects = PvpClassUtils.loadPassiveEffects(this, "PASSIVE_EFFECTS");

        this.factionLimit = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.players.forEach(uuid -> this.deactivateClass(Bukkit.getPlayer(uuid), true));

        this.players.clear();
        this.effects.clear();

        this.factionLimit.clear();
    }

    public String getDisplayName() {
        return this.name;
    }

    public void activateClass(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if(player == null || !this.isWearingFull(player)) return;

        PvpClassEquipEvent event = new PvpClassEquipEvent(uuid, this);
        if(event.isCancelled()) return;

        this.players.add(uuid);
        this.effects.forEach(effect -> NmsUtils.getInstance().addPotionEffect(player, effect));

        player.sendMessage(Language.PVP_CLASSES_ACTIVATED.replace("<name>", this.getDisplayName()));

        if(this instanceof Miner) {
            Miner miner = (Miner) this;
            int diamondsMined = player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE);

            miner.getDiamondData(diamondsMined).forEach(data -> data.getEffects()
                .forEach(effect -> NmsUtils.getInstance().addPotionEffect(player, effect)));
        }
    }

    void deactivateClass(Player player, boolean disable) {
        if(!disable) {
            this.players.remove(player.getUniqueId());
        }

        this.getEffects().forEach(effect -> NmsUtils.getInstance().removeInfinitePotionEffect(player, effect));

        if(this instanceof Miner) {
			Miner miner = (Miner) this;
            int diamondsMined = player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE);

			miner.getDiamondData(diamondsMined).forEach(data -> data.getEffects()
                .forEach(effect -> NmsUtils.getInstance().removeInfinitePotionEffect(player, effect)));
		}

        new PvpClassUnequipEvent(player.getUniqueId(), this);
    }

    protected void applyClickableEffect(Player player, ClickableItem clickable, boolean archer) {
        String effect = StringUtils.getPotionEffectName(clickable.getPotionEffect());

        this.manager.addPotionEffect(player, clickable.getPotionEffect());
        ItemUtils.removeOneItem(player);

        String message = archer ? Language.ARCHER_CLICKABLE_ACTIVATED : Language.ROGUE_CLICKABLE_ACTIVATED;

        player.sendMessage(Language.PREFIX + message.replace("<effect>", effect)
            .replace("<seconds>", String.valueOf(clickable.getPotionEffect().getDuration() / 20)));
    }

    public boolean isActive(Player player) {
        return this.players.contains(player.getUniqueId());
    }

    boolean isWarmupOrActive(Player player) {
        PvpClassWarmupTimer warmupTimer = TimerManager.getInstance().getPvpClassWarmupTimer();
        return warmupTimer.isActive(player, this.name) || this.isActive(player);
    }

    private boolean isWearingFull(Player player) {
        for(ItemStack armor : player.getInventory().getArmorContents()) {
            if(armor == null) return false;
        }

        ItemStack[] armor = player.getInventory().getArmorContents();

        return armor[3].getType() == helmet && armor[2].getType() == chestplate &&
               armor[1].getType() == leggings && armor[0].getType() == boots;
    }

    boolean isAtFactionLimit(PlayerFaction faction) {
        Integer limit = Config.FACTION_PVP_CLASS_LIMIT.get(this.getName());
        return limit != null && limit != -1 && this.factionLimit.getOrDefault(faction.getId(), -1) >= limit;
    }

    public void checkEquipmentChange(Player player) {
        if(this.isWearingFull(player)) {
            if(this.isWarmupOrActive(player)) return;

            if(this.warmup <= 0) {
                this.activateClass(player.getUniqueId());
                return;
            }

            TimerManager.getInstance().getPvpClassWarmupTimer().activate(player, this.warmup, this);
            player.sendMessage(Language.PVP_CLASSES_WARMING_UP.replace("<name>", this.getDisplayName()));
            return;
        }

        PvpClassWarmupTimer warmupTimer = TimerManager.getInstance().getPvpClassWarmupTimer();

        if(warmupTimer.isActive(player, this.name)) {
            warmupTimer.cancel(player, this.name);
            player.sendMessage(Language.PVP_CLASSES_WARMUP_CANCELLED.replace("<name>", this.getDisplayName()));
            return;
        }

        if(this.isActive(player)) {
            this.deactivateClass(player, false);
            player.sendMessage(Language.PVP_CLASSES_DEACTIVATED.replace("<name>", this.getDisplayName()));
        }
    }

    public final void applyScoreboardLines(Player player, PlayerScoreboard scoreboard) {
        if(this.isActive(player)) {
            this.applyActiveScoreboardLines(player, scoreboard);
            return;
        }

        PvpClassWarmupTimer timer = TimerManager.getInstance().getPvpClassWarmupTimer();

        if(timer.isActive(player, this.getName())) {
            scoreboard.addLine(ChatColor.BLUE);
            scoreboard.add(timer.getPlaceholder(), timer.getScoreboardEntry(player, this.getName()));
        }
    }

    public void applyActiveScoreboardLines(Player player, PlayerScoreboard scoreboard) {
        scoreboard.addLine(ChatColor.BLUE);
        scoreboard.add(Config.PVPCLASS_ACTIVE_PLACEHOLDER, this.getDisplayName());
    }
}
