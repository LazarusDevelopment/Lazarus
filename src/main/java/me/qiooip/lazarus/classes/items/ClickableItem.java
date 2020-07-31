package me.qiooip.lazarus.classes.items;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

@Getter
@Setter
public class ClickableItem {

    private ItemStack item;
    private PotionEffect potionEffect;
    private int cooldown;
}
