package me.qiooip.lazarus.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class ItemBuilder {

    private final ItemStack itemStack;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemBuilder(Material material) {
        this(material, 1, 0);
    }

    public ItemBuilder(Material material, int amount) {
        this(material, amount, 0);
    }

    public ItemBuilder(Material material, int amount, int durability) {
        this.itemStack = new ItemStack(material, amount, (short) durability);
    }

    public ItemBuilder setDurability(int durability) {
        this.itemStack.setDurability((short) durability);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder addAmount(int amount) {
        this.itemStack.setAmount(this.itemStack.getAmount() + amount);
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setDisplayName(Color.translate(name));
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        try {
            SkullMeta meta = (SkullMeta) this.itemStack.getItemMeta();
            meta.setOwner(owner);
            this.itemStack.setItemMeta(meta);
        } catch(ClassCastException ignored) { }

        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setLore(lore);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLoreLine(String line) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.getLore().add(Color.translate(line));
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addStoredEnchantment(Enchantment enchantment, int level) {
        try {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) this.itemStack.getItemMeta();
            meta.addStoredEnchant(enchantment, level, false);
            this.itemStack.setItemMeta(meta);
        } catch(ClassCastException ignored) { }

        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.itemStack.addEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment enchantment, int level) {
        this.itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemMeta getItemMeta() {
        return this.itemStack.getItemMeta();
    }

    public ItemStack build() {
        return this.itemStack;
    }
}
