package me.qiooip.lazarus.classes;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.classes.items.BardClickableItem;
import me.qiooip.lazarus.classes.items.BardHoldableItem;
import me.qiooip.lazarus.classes.manager.PvpClass;
import me.qiooip.lazarus.classes.manager.PvpClassManager;
import me.qiooip.lazarus.classes.utils.PvpClassUtils;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.cooldown.CooldownTimer;
import me.qiooip.lazarus.utils.ItemUtils;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class Bard extends PvpClass {

    @Getter private final Map<UUID, BardPower> bardPowers;

    private final List<BardClickableItem> clickables;
    private final List<BardHoldableItem> holdables;

    private final BardHoldableTask holdableTask;

    public Bard(PvpClassManager manager) {
        super(manager, "Bard",
            Material.GOLD_HELMET,
            Material.GOLD_CHESTPLATE,
            Material.GOLD_LEGGINGS,
            Material.GOLD_BOOTS
        );

        this.bardPowers = new HashMap<>();

        this.clickables = PvpClassUtils.loadBardClickableItems();
        this.holdables = PvpClassUtils.loadBardHoldableItems();

        this.holdableTask = new BardHoldableTask();
    }

    @Override
    public void disable() {
        super.disable();

        this.bardPowers.clear();
        this.clickables.clear();
        this.holdables.clear();

        this.holdableTask.cancel();
    }

    private BardClickableItem getClickableItem(ItemStack item) {
        return this.clickables.stream().filter(clickable -> clickable.getItem().getType() == item.getType()
        && clickable.getItem().getDurability() == item.getDurability()).findFirst().orElse(null);
    }

    private BardHoldableItem getHoldableItem(ItemStack item) {
        return this.holdables.stream().filter(holdable -> holdable.getItem().getType() == item.getType()
        && holdable.getItem().getDurability() == item.getDurability()).findFirst().orElse(null);
    }

    private double getPower(UUID uuid) {
        return this.bardPowers.get(uuid).getPower() / 1000;
    }

    public String getBardPower(UUID uuid) {
        return String.format(Locale.ROOT, "%.1f", this.getPower(uuid));
    }

    private void modifyPower(Player player, int amount) {
        this.bardPowers.get(player.getUniqueId()).withdrawPower(amount);
    }

    private void applyHoldableEffect(Player player, PlayerFaction faction, BardHoldableItem item) {
        if(faction == null) {
            if(!item.isCanBardHimself()) return;

            this.getManager().addPotionEffect(player, item.getPotionEffect());
            return;
        }

        for(Player member : faction.getOnlinePlayers()) {
            if(player.getWorld() != member.getWorld() || (!item.isCanBardHimself() && player == member)) continue;
            if(player.getLocation().distance(member.getLocation()) > item.getDistance()) continue;

            this.getManager().addPotionEffect(member, item.getPotionEffect());
        }
    }

    private void applyClickableEffect(Player player, PlayerFaction faction, BardClickableItem item) {
        if(item.isApplyToEnemy()) {
            player.sendMessage(Language.PREFIX + Language.BARD_CLICKABLE_MESSAGE_ENEMY.replace("<effect>",
            StringUtils.getPotionEffectName(item.getPotionEffect())));

            this.getManager().addPotionEffect(player, item.getPotionEffect());

            for(Entity nearby : player.getNearbyEntities(item.getDistance(), item.getDistance(), item.getDistance())) {
                if(!(nearby instanceof Player)) continue;

                Player enemy = (Player) nearby;
                if(Lazarus.getInstance().getStaffModeManager().isInStaffModeOrVanished(enemy)) continue;
                if(ClaimManager.getInstance().getFactionAt(enemy).isSafezone()) continue;
                if(TimerManager.getInstance().getPvpProtTimer().isActive(enemy)) continue;

                PlayerFaction enemyFaction = FactionsManager.getInstance().getPlayerFaction(enemy);
                if(faction != null && (faction == enemyFaction || faction.isAlly(enemyFaction))) continue;

                this.getManager().addPotionEffect(enemy, item.getPotionEffect());
                TimerManager.getInstance().getCombatTagTimer().activate(enemy.getUniqueId());

                enemy.sendMessage(Language.PREFIX + Language.BARD_CLICKABLE_MESSAGE_OTHERS.replace("<effect>",
                StringUtils.getPotionEffectName(item.getPotionEffect())));
            }
        } else {
            if(faction != null) {
                player.sendMessage(Language.PREFIX + Language.BARD_CLICKABLE_MESSAGE_FRIENDLY.replace("<effect>",
                StringUtils.getPotionEffectName(item.getPotionEffect())));

                for(Player member : faction.getOnlinePlayers()) {
                    if(player.getWorld() != member.getWorld() || (!item.isCanBardHimself() && player == member)) continue;
                    if(player.getLocation().distance(member.getLocation()) > item.getDistance()) continue;

                    if(player != member) {
                        member.sendMessage(Language.PREFIX + Language.BARD_CLICKABLE_MESSAGE_OTHERS.replace("<effect>",
                        StringUtils.getPotionEffectName(item.getPotionEffect())));
                    }

                    this.getManager().addPotionEffect(member, item.getPotionEffect());
                }
            } else {
                if(!item.isCanBardHimself()) {
                    player.sendMessage(Language.PREFIX + Language.BARD_CAN_NOT_BARD_TO_YOURSELF);
                    return;
                }

                player.sendMessage(Language.PREFIX + Language.BARD_CLICKABLE_MESSAGE_OTHERS.replace("<effect>",
                StringUtils.getPotionEffectName(item.getPotionEffect())));

                this.getManager().addPotionEffect(player, item.getPotionEffect());
            }
        }
    }

    private boolean canBard(Player player) {
        if(Lazarus.getInstance().getStaffModeManager().isInStaffModeOrVanished(player)) {
            player.sendMessage(Language.PREFIX + Language.BARD_VANISHED_OR_IN_STAFFMODE);
            return false;
        }

        if(TimerManager.getInstance().getPvpProtTimer().isActive(player)) {
            player.sendMessage(Language.PREFIX + Language.BARD_CAN_NOT_BARD_WITH_PVP_TIMER);
            return false;
        }

        if(ClaimManager.getInstance().getFactionAt(player).isSafezone()) {
            player.sendMessage(Language.PREFIX + Language.BARD_CAN_NOT_BARD_IN_SAFEZONE);
            return false;
        }

        if(Lazarus.getInstance().getSotwHandler().isUnderSotwProtection(player)) {
            player.sendMessage(Language.PREFIX + Language.BARD_CAN_NOT_BARD_WHEN_SOTW_NOT_ENABLED);
            return false;
        }

        return true;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if(!this.isActive(player)) return;

        BardHoldableItem holdableItem = this.getHoldableItem(player.getItemInHand());
        if(holdableItem == null || !this.canBard(player)) return;

        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);
        this.applyHoldableEffect(player, faction, holdableItem);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.useInteractedBlock() == Result.DENY && event.useItemInHand() == Result.DENY) return;

        if(!this.isActive(event.getPlayer()) || !event.hasItem()) return;
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();

        BardClickableItem clickableItem = this.getClickableItem(event.getItem());
        if(clickableItem == null || !this.canBard(player)) return;

        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);
        CooldownTimer timer = TimerManager.getInstance().getCooldownTimer();

        if(timer.isActive(player, "BARDBUFF")) {
            player.sendMessage(Language.PREFIX + Language.BARD_CLICKABLE_ACTIVE_COOLDOWN
            .replace("<seconds>", timer.getTimeLeft(player, "BARDBUFF")));
            return;
        }

        if(this.getPower(player.getUniqueId()) < clickableItem.getEnergyNeeded()) {
            player.sendMessage(Language.PREFIX + Language.BARD_CLICKABLE_NOT_ENOUGH_ENERGY
            .replace("<energy>", String.valueOf(clickableItem.getEnergyNeeded())));
            return;
        }

        if(Config.BARD_COMBAT_TAG_ON_CLICKABLE_ITEM || clickableItem.isApplyToEnemy()) {
            TimerManager.getInstance().getCombatTagTimer().activate(player.getUniqueId());
        }

        this.modifyPower(player, clickableItem.getEnergyNeeded());
        ItemUtils.removeOneItem(player);

        timer.activate(player, "BARDBUFF", clickableItem.getCooldown(),
        Language.PREFIX + Language.BARD_CLICKABLE_COOLDOWN_EXPIRED);

        this.applyClickableEffect(player, faction, clickableItem);
    }

    class BardHoldableTask extends BukkitRunnable {

        BardHoldableTask() {
            this.runTaskTimerAsynchronously(Lazarus.getInstance(), 0L, 20L);
        }

        @Override
        public void run() {
            getPlayers().forEach(uuid -> {
                Player player = Bukkit.getPlayer(uuid);
                if(player == null) return;

                BardHoldableItem holdableItem = getHoldableItem(player.getItemInHand());
                if(holdableItem == null || !canBard(player)) return;

                PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);
                applyHoldableEffect(player, faction, holdableItem);
            });
        }
    }

    public static class BardPower {

        private double power;

        public BardPower() {
            this.power = System.currentTimeMillis();
        }

        double getPower() {
            return Math.min(Config.BARD_MAX_ENERGY, System.currentTimeMillis() - this.power);
        }

        void withdrawPower(int amount) {
            this.power = System.currentTimeMillis() - (this.getPower() - (amount * 1000));
        }
    }
}