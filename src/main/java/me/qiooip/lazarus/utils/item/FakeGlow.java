package me.qiooip.lazarus.utils.item;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

public class FakeGlow extends EnchantmentWrapper {

    public FakeGlow(int id) {
        super(id);
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true;
    }

    @Override
    public boolean conflictsWith(Enchantment item) {
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public String getName() {
        return "FakeGlow";
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }
}