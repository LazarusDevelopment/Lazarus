package me.qiooip.lazarus.handlers.limiter;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnchantLimiterHandler extends Handler implements Listener {

    private final List<EnchantLimit> enchantLimits;

    public EnchantLimiterHandler() {
        this.enchantLimits = new ArrayList<>();

        this.loadEnchantLimits();
    }

    @Override
    public void disable() {
        this.enchantLimits.clear();
    }

    private void loadEnchantLimits() {
        this.enchantLimits.clear();

        ConfigurationSection section = Lazarus.getInstance().getLimitersFile()
            .getSection("ENCHANTMENT_LIMITER");

        section.getKeys(false).forEach(enchantment -> {
            int level = section.getInt(enchantment);
            if(level == -1) return;

            EnchantLimit enchantmentLimit = new EnchantLimit();
            enchantmentLimit.setEnchantment(Enchantment.getByName(enchantment));
            enchantmentLimit.setLevel(level);

            this.enchantLimits.add(enchantmentLimit);
        });
    }

    private EnchantLimit getEnchantLimit(Enchantment enchantment) {
        return this.enchantLimits.stream().filter(enchant -> enchant.getEnchantment()
        .equals(enchantment)).findFirst().orElse(null);
    }

    private void updateEnchantments(Player player, ItemStack item) {
        item.getEnchantments().keySet().forEach(enchantment -> {
            EnchantLimit limit = this.getEnchantLimit(enchantment);
            if(limit == null || item.getEnchantmentLevel(enchantment) <= limit.getLevel()) return;

            item.removeEnchantment(limit.getEnchantment());

            if(limit.getLevel() > 0) {
                try {
                    item.addEnchantment(limit.getEnchantment(), limit.getLevel());
                } catch(IllegalArgumentException ignored) { }
            }
        });

        if(player != null) {
            ItemUtils.updateInventory(player);
        }
    }

    private void updateEnchantmentBook(Player player, ItemStack book) {
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();

        meta.getStoredEnchants().keySet().forEach(enchantment -> {
            EnchantLimit limit = this.getEnchantLimit(enchantment);
            if(limit == null || meta.getStoredEnchantLevel(enchantment) <= limit.getLevel()) return;

            meta.removeStoredEnchant(limit.getEnchantment());

            if(limit.getLevel() > 0) {
                try {
                    meta.addStoredEnchant(limit.getEnchantment(), limit.getLevel(), false);
                } catch(IllegalArgumentException ignored) { }
            }
        });

        book.setItemMeta(meta);
        ItemUtils.updateInventory(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEnchantItem(EnchantItemEvent event) {
        Map<Enchantment, Integer> toAdd = event.getEnchantsToAdd();

        this.enchantLimits.forEach(limit -> {
            if(!toAdd.containsKey(limit.getEnchantment())) return;
            if(toAdd.get(limit.getEnchantment()) <= limit.getLevel()) return;

            toAdd.remove(limit.getEnchantment());
            if(limit.getLevel() < 1) return;

            toAdd.put(limit.getEnchantment(), limit.getLevel());
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getClickedInventory() == null || event.getClickedInventory().getType() != InventoryType.ANVIL) return;
        if(event.getSlotType() != SlotType.RESULT) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if(item.hasItemMeta() && item.getItemMeta() instanceof EnchantmentStorageMeta) {
            this.updateEnchantmentBook(player, item);
            return;
        }

        if(item.hasItemMeta() && item.getItemMeta().hasLore()) {
            if(!item.getItemMeta().getLore().contains(Config.UNREPAIRABLE_ITEM_LORE)) {
                this.updateEnchantments(player, item);
                return;
            }

            event.setCancelled(true);
            player.sendMessage(Language.PREFIX + Language.UNREPAIRABLE_ITEM_MESSAGE);
            return;
        }

        this.updateEnchantments(player, item);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerFish(PlayerFishEvent event) {
        if(!(event.getCaught() instanceof ItemStack)) return;

        ItemStack item = (ItemStack) event.getCaught();
        if(item.getEnchantments() == null || item.getEnchantments().isEmpty()) return;

        this.updateEnchantments(null, item);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(event.getEntity() instanceof Player) return;

        event.getDrops().forEach(item -> {
            if(item == null || item.getType() == Material.AIR) return;
            if(item.getEnchantments() == null || item.getEnchantments().isEmpty()) return;

            this.updateEnchantments(null, item);
        });
    }

    @Getter @Setter
    private static class EnchantLimit {

        private Enchantment enchantment;
        private int level;
    }
}
