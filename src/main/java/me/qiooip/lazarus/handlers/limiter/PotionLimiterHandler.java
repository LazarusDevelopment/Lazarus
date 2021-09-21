package me.qiooip.lazarus.handlers.limiter;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class PotionLimiterHandler extends Handler implements Listener {

    private final List<PotionLimit> potionLimits;

    public PotionLimiterHandler() {
        this.potionLimits = new ArrayList<>();

        this.loadPotionLimits();
    }

    @Override
    public void disable() {
        this.potionLimits.clear();
    }

    private void loadPotionLimits() {
        this.potionLimits.clear();

        ConfigurationSection section = Lazarus.getInstance().getLimitersFile()
            .getSection("POTION_LIMITER");

        section.getKeys(false).forEach(type -> {
            if(section.getInt(type + ".LEVEL") == -1) return;

            PotionLimit potionLimit = new PotionLimit();
            potionLimit.setType(PotionEffectType.getByName(type));
            potionLimit.setLevel(section.getInt(type + ".LEVEL"));
            potionLimit.setExtended(section.getBoolean(type + ".EXTENDED"));

            this.potionLimits.add(potionLimit);
        });
    }

    private PotionLimit getPotionLimit(PotionEffectType type) {
        return this.potionLimits.stream().filter(limit -> limit.getType() == type).findFirst().orElse(null);
    }

    public boolean shouldNotCancelEffect(Potion potion) {
        if(potion == null) return true;

        for(PotionEffect effect : potion.getEffects()) {
            PotionLimit limit = this.getPotionLimit(effect.getType());
            if(limit == null) return true;

            if(limit.getLevel() == 0 || (effect.getAmplifier() + 1) > limit.getLevel()) return false;
            if(potion.hasExtendedDuration() && !limit.isExtended()) return false;
        }

        return true;
    }

    public boolean shouldNotCancelEffect(PotionEffect effect) {
        PotionLimit limit = this.getPotionLimit(effect.getType());
        if(limit == null) return true;

        return limit.getLevel() != 0 && (effect.getAmplifier() + 1) <= limit.getLevel();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPotionBrew(BrewEvent event) {
        BrewerInventory brewer = event.getContents();
        ItemStack ingredient = brewer.getIngredient().clone();

        ItemStack[] potions = new ItemStack[3];

        for(int i = 0; i < 3; i++) {
            if(event.getContents().getItem(i) == null) continue;

            potions[i] = brewer.getItem(i).clone();
        }

        Tasks.sync(() -> {
            for(int i = 0; i < 3; i++) {
                if(brewer.getItem(i) == null || brewer.getItem(i).getDurability() == 0) continue;
                if(this.shouldNotCancelEffect(Potion.fromItemStack(brewer.getItem(i)))) continue;

                brewer.setItem(i, potions[i]);
            }
        });
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();

        if(item.getType() != Material.POTION || item.getDurability() == 0) return;
        if(this.shouldNotCancelEffect(Potion.fromItemStack(item))) return;

        event.setCancelled(true);

        Player player = event.getPlayer();

        player.setItemInHand(new ItemStack(Material.AIR));
        player.sendMessage(Language.PREFIX + Language.POTION_LIMITER_DENY_MESSAGE);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPotionSplash(PotionSplashEvent event) {
        ThrownPotion thrownPotion = event.getPotion();
        if(this.shouldNotCancelEffect(Potion.fromItemStack(thrownPotion.getItem()))) return;

        event.setCancelled(true);

        if(!(thrownPotion.getShooter() instanceof Player)) return;
        Player player = (Player) thrownPotion.getShooter();

        player.setItemInHand(new ItemStack(Material.AIR));
        player.sendMessage(Language.PREFIX + Language.POTION_LIMITER_DENY_MESSAGE);
    }

    @Getter @Setter
    private static class PotionLimit {

        private PotionEffectType type;
        private int level;
        private boolean extended;
    }
}
