package me.qiooip.lazarus.handlers;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.handlers.manager.Handler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class CustomRecipeHandler extends Handler {

    public CustomRecipeHandler() {
        this.createChainArmorRecipe();
    }

    private void createChainArmorRecipe() {
        if(!Config.CHAIN_ARMOR_RECIPE_ENABLED) return;

        ShapedRecipe helmet = new ShapedRecipe(new ItemStack(Material.CHAINMAIL_HELMET, 1));
        helmet.shape("AAA", "A A", "   ");
        helmet.setIngredient('A', Config.CHAIN_ARMOR_RECIPE_MATERIAL);
        Bukkit.addRecipe(helmet);

        ShapedRecipe chestplate = new ShapedRecipe(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1));
        chestplate.shape("A A", "AAA", "AAA");
        chestplate.setIngredient('A', Config.CHAIN_ARMOR_RECIPE_MATERIAL);
        Bukkit.addRecipe(chestplate);

        ShapedRecipe leggings = new ShapedRecipe(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1));
        leggings.shape("AAA", "A A", "A A");
        leggings.setIngredient('A', Config.CHAIN_ARMOR_RECIPE_MATERIAL);
        Bukkit.addRecipe(leggings);

        ShapedRecipe boots = new ShapedRecipe(new ItemStack(Material.CHAINMAIL_BOOTS, 1));
        boots.shape("   ", "A A", "A A");
        boots.setIngredient('A', Config.CHAIN_ARMOR_RECIPE_MATERIAL);
        Bukkit.addRecipe(boots);
    }
}
