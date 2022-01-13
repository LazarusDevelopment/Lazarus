package me.qiooip.lazarus.classes;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.classes.items.MageClickableItem;
import me.qiooip.lazarus.classes.manager.PvpClass;
import me.qiooip.lazarus.classes.manager.PvpClassManager;
import me.qiooip.lazarus.classes.utils.PvpClassUtils;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.scoreboard.PlayerScoreboard;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.cooldown.CooldownTimer;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Mage extends PvpClass {

    public static final String COOLDOWN_KEY = "MAGE_BUFF";

    @Getter private final Map<UUID, MagePower> magePowers;
    private final List<MageClickableItem> clickables;

    public Mage(PvpClassManager manager) {
        super(manager, "Mage",
            Material.GOLD_HELMET,
            Material.CHAINMAIL_CHESTPLATE,
            Material.CHAINMAIL_LEGGINGS,
            Material.GOLD_BOOTS
        );

        this.warmup = Config.MAGE_WARMUP;

        this.magePowers = new HashMap<>();
        this.clickables = PvpClassUtils.loadMageClickableItems();
    }

    @Override
    public void disable() {
        super.disable();

        this.magePowers.clear();
        this.clickables.clear();
    }

    private MageClickableItem getClickableItem(ItemStack item) {
        return this.clickables.stream().filter(clickable -> clickable.getItem().getType() == item.getType()
            && clickable.getItem().getDurability() == item.getDurability()).findFirst().orElse(null);
    }

    private double getPower(UUID uuid) {
        return this.magePowers.get(uuid).getPower() / 1000;
    }

    public String getMagePower(UUID uuid) {
        return String.valueOf((double) Math.round(this.getPower(uuid) * 10d) / 10d);
    }

    private void modifyPower(Player player, int amount) {
        this.magePowers.get(player.getUniqueId()).withdrawPower(amount);
    }

    @Override
    public void applyActiveScoreboardLines(Player player, PlayerScoreboard scoreboard) {
        super.applyActiveScoreboardLines(player, scoreboard);
        scoreboard.add(Config.MAGE_ENERGY_PLACEHOLDER, this.getMagePower(player.getUniqueId()));

        CooldownTimer timer = TimerManager.getInstance().getCooldownTimer();

        if(timer.isActive(player, COOLDOWN_KEY)) {
            scoreboard.add(Config.COOLDOWN_PLACEHOLDER , timer.getTimeLeft(player, COOLDOWN_KEY) + 's');
        }
    }

    private boolean canApplyMageEffect(Player player) {
        if(Lazarus.getInstance().getStaffModeManager().isInStaffModeOrVanished(player)) {
            player.sendMessage(Language.PREFIX + Language.MAGE_VANISHED_OR_IN_STAFFMODE);
            return false;
        }

        if(TimerManager.getInstance().getPvpProtTimer().isActive(player)) {
            player.sendMessage(Language.PREFIX + Language.MAGE_CAN_NOT_USE_MAGE_WITH_PVP_TIMER);
            return false;
        }

        if(ClaimManager.getInstance().getFactionAt(player).isSafezone()) {
            player.sendMessage(Language.PREFIX + Language.MAGE_CAN_NOT_USE_MAGE_IN_SAFEZONE);
            return false;
        }

        if(Lazarus.getInstance().getSotwHandler().isUnderSotwProtection(player)) {
            player.sendMessage(Language.PREFIX + Language.MAGE_CAN_NOT_USE_MAGE_WHEN_SOTW_NOT_ENABLED);
            return false;
        }

        return true;
    }

    private void applyClickableEffect(Player player, PlayerFaction faction, MageClickableItem item) {
        if(item.isApplyToHimself()) {
            this.getManager().addPotionEffect(player, item.getPotionEffect());
        }

        int amountOfEnemies = 0;

        for(Entity nearby : player.getNearbyEntities(item.getDistance(), item.getDistance(), item.getDistance())) {
            if(!(nearby instanceof Player)) continue;

            Player enemy = (Player) nearby;
            if(Lazarus.getInstance().getStaffModeManager().isInStaffModeOrVanished(enemy)) continue;
            if(ClaimManager.getInstance().getFactionAt(enemy).isSafezone()) continue;
            if(TimerManager.getInstance().getPvpProtTimer().isActive(enemy)) continue;

            PlayerFaction enemyFaction = FactionsManager.getInstance().getPlayerFaction(enemy);
            if(faction != null && (faction == enemyFaction || faction.isAlly(enemyFaction))) continue;

            amountOfEnemies++;

            this.getManager().addPotionEffect(enemy, item.getPotionEffect());
            TimerManager.getInstance().getCombatTagTimer().activate(enemy.getUniqueId());

            enemy.sendMessage(Language.PREFIX + Language.MAGE_CLICKABLE_MESSAGE_OTHERS
                .replace("<effect>", item.getChatColor() + StringUtils.getPotionEffectName(item.getPotionEffect())));
        }

        player.sendMessage(Language.PREFIX + Language.MAGE_CLICKABLE_MESSAGE_ENEMY
            .replace("<effect>", item.getChatColor() + StringUtils.getPotionEffectName(item.getPotionEffect()))
            .replace("<amount>", String.valueOf(amountOfEnemies)));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.useInteractedBlock() == Event.Result.DENY && event.useItemInHand() == Event.Result.DENY) return;

        if(!this.isActive(event.getPlayer()) || !event.hasItem()) return;
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();

        MageClickableItem clickableItem = this.getClickableItem(event.getItem());
        if(clickableItem == null || !this.canApplyMageEffect(player)) return;

        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);
        CooldownTimer timer = TimerManager.getInstance().getCooldownTimer();

        if(timer.isActive(player, COOLDOWN_KEY)) {
            player.sendMessage(Language.PREFIX + Language.MAGE_CLICKABLE_ACTIVE_COOLDOWN
                .replace("<seconds>", timer.getTimeLeft(player, COOLDOWN_KEY)));
            return;
        }

        int currentPower = (int) this.getPower(player.getUniqueId());

        if(currentPower < clickableItem.getEnergyNeeded()) {
            player.sendMessage(Language.PREFIX + Language.MAGE_CLICKABLE_NOT_ENOUGH_ENERGY
                .replace("<energy>", String.valueOf(clickableItem.getEnergyNeeded()))
                .replace("<currentEnergy>", String.valueOf(currentPower)));

            return;
        }

        if(Config.MAGE_COMBAT_TAG_ON_CLICKABLE_ITEM) {
            TimerManager.getInstance().getCombatTagTimer().activate(player.getUniqueId());
        }

        this.modifyPower(player, clickableItem.getEnergyNeeded());
        ItemUtils.removeOneItem(player);

        timer.activate(player, COOLDOWN_KEY, clickableItem.getCooldown(),
            Language.PREFIX + Language.MAGE_CLICKABLE_COOLDOWN_EXPIRED);

        this.applyClickableEffect(player, faction, clickableItem);
    }

    public static class MagePower {

        private double power;

        public MagePower() {
            this.power = System.currentTimeMillis();
        }

        double getPower() {
            return Math.min(Config.MAGE_MAX_ENERGY, System.currentTimeMillis() - this.power);
        }

        void withdrawPower(int amount) {
            this.power = System.currentTimeMillis() - (this.getPower() - (amount * 1000));
        }
    }
}
