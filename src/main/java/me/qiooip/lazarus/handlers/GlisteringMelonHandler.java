package me.qiooip.lazarus.handlers;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

public class GlisteringMelonHandler extends Handler implements Listener {

    public GlisteringMelonHandler() {
        if(!Config.GLISTERING_MELON_CRAFTING) return;

        this.registerEasyRecipe();
    }

    private void registerEasyRecipe() {
        ItemStack easyMelon = new ItemStack(Material.SPECKLED_MELON, 1);

        ShapelessRecipe recipe = new ShapelessRecipe(easyMelon);
        recipe.addIngredient(1, Material.MELON);
        recipe.addIngredient(1, Material.GOLD_NUGGET);

        Bukkit.addRecipe(recipe);
    }

    @EventHandler(ignoreCancelled = true)
    public void onCraftItem(CraftItemEvent event) {
        if(!(event.getWhoClicked() instanceof Player)) return;
        if(event.getRecipe().getResult().getType() != Material.SPECKLED_MELON) return;

        Player player = (Player) event.getWhoClicked();
        ItemUtils.updateInventory(player);
    }
}
