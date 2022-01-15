package me.qiooip.lazarus.classes.items;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
@Accessors(chain = true)
public class BomberTntGun {

    private ItemStack item;
    private int cooldown;
    private int fuseTicks;
    private double tntVelocity;

    private double kbMaxY;
    private double kbYMultiplier;
}
