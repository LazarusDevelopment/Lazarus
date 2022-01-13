package me.qiooip.lazarus.classes;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.classes.items.ClickableItem;
import me.qiooip.lazarus.classes.manager.PvpClass;
import me.qiooip.lazarus.classes.manager.PvpClassManager;
import me.qiooip.lazarus.classes.utils.PvpClassUtils;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.cooldown.CooldownTimer;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.item.ItemUtils;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Rogue extends PvpClass implements Listener {

    public static final String BACKSTAB_COOLDOWN_KEY = "BACKSTAB";

    private final List<ClickableItem> clickables;
    private final List<PotionEffect> backstabEffects;

    public Rogue(PvpClassManager manager) {
        super(manager, "Rogue",
            Material.CHAINMAIL_HELMET,
            Material.CHAINMAIL_CHESTPLATE,
            Material.CHAINMAIL_LEGGINGS,
            Material.CHAINMAIL_BOOTS
        );

        this.warmup = Config.ROGUE_WARMUP;

        this.clickables = PvpClassUtils.loadClickableItems(this);
        this.backstabEffects = this.loadBackstabEffects();
    }

    @Override
    public void disable() {
        super.disable();

        this.clickables.clear();
        this.backstabEffects.clear();
    }

    private List<PotionEffect> loadBackstabEffects() {
        List<PotionEffect> effects = new ArrayList<>();

        ConfigurationSection section = Lazarus.getInstance().getClassesFile()
            .getSection("ROGUE_CLASS.BACKSTAB.EFFECTS");

        section.getKeys(false).forEach(effectName -> {
            PotionEffectType type = PotionEffectType.getByName(effectName);
            int duration = section.getInt(effectName + ".DURATION");
            int level = section.getInt(effectName + ".LEVEL");

            effects.add(new PotionEffect(type, duration * 20, level - 1));
        });

        return effects;
    }

    private ClickableItem getClickableItem(ItemStack item) {
        return this.clickables.stream().filter(clickable -> clickable.getItem().getType() == item.getType()
            && clickable.getItem().getDurability() == item.getDurability()).findFirst().orElse(null);
    }

    @EventHandler(ignoreCancelled = true)
    public void onCraftItem(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        Material type = event.getRecipe().getResult().getType();

        if(type.name().startsWith("CHAINMAIL_")) ItemUtils.updateInventory(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.useInteractedBlock() == Result.DENY && event.useItemInHand() == Result.DENY) return;

        if(!this.isActive(event.getPlayer()) || !event.hasItem()) return;
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ClickableItem clickable = this.getClickableItem(event.getItem());
        if(clickable == null) return;

        CooldownTimer timer = TimerManager.getInstance().getCooldownTimer();

        String cooldown = clickable.getItem().getType().name();
        String potionName = StringUtils.getPotionEffectName(clickable.getPotionEffect());

        Player player = event.getPlayer();

        if(timer.isActive(player, cooldown)) {
            player.sendMessage(Language.PREFIX + Language.ROGUE_CLICKABLE_COOLDOWN
                .replace("<effect>", potionName)
                .replace("<seconds>", timer.getTimeLeft(player, cooldown)));
            return;
        }

        this.applyClickableEffect(player, clickable, false);

        timer.activate(player, cooldown, clickable.getCooldown(), Language.PREFIX
            + Language.ROGUE_COOLDOWN_EXPIRED.replace("<effect>", potionName));
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;

        Player damager = (Player) event.getDamager();
        if(!this.isActive(damager)) return;
        if(damager.getItemInHand().getType() != Config.ROGUE_BACKSTAB_ITEM) return;

        Vector playerDirection = event.getEntity().getLocation().getDirection();
        Vector damagerDirection = damager.getLocation().getDirection();

        if(Math.abs(playerDirection.angle(damagerDirection)) < 1.4) {
            CooldownTimer timer = TimerManager.getInstance().getCooldownTimer();

            if(timer.isActive(damager, BACKSTAB_COOLDOWN_KEY)) {
                damager.sendMessage(Language.PREFIX + Language.ROGUE_BACKSTAB_COOLDOWN
                    .replace("<seconds>", timer.getTimeLeft(damager, BACKSTAB_COOLDOWN_KEY)));
                return;
            }

            event.setDamage(Config.ROGUE_BACKSTAB_DAMAGE * 2);
            event.setDamage(DamageModifier.ARMOR, 0);

            damager.playSound(damager.getLocation(), Sound.ITEM_BREAK, 10, 10);
            ItemUtils.removeOneItem(damager);

            timer.activate(damager, BACKSTAB_COOLDOWN_KEY, Config.ROGUE_BACKSTAB_COOLDOWN,
                Language.PREFIX + Language.ROGUE_BACKSTAB_COOLDOWN_EXPIRED);

            if(Config.ROGUE_BACKSTAB_EFFECTS_ENABLED) {
                this.backstabEffects.forEach(effect -> NmsUtils.getInstance().addPotionEffect(damager, effect));
            }
        }
    }
}
