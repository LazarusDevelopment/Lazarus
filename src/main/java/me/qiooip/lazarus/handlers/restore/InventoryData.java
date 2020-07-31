package me.qiooip.lazarus.handlers.restore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public class InventoryData {

    private final ItemStack[] contents;
    private final ItemStack[] armor;
}
