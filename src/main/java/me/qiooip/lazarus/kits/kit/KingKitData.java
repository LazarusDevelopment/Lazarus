package me.qiooip.lazarus.kits.kit;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class KingKitData extends KitData {

    private ItemStack[] armor;

    @Override
    public void applyKit(Player target) {
        target.getInventory().setContents(this.contents);
        target.getInventory().setArmorContents(this.armor);
    }

    @Override
    public String getType() {
        return "SPECIAL";
    }

    @Override
    public boolean shouldCancelEvent(int slot) {
        return slot >= 36 && (slot != 45 && slot != 46 && slot != 47 && slot != 48);
    }
}
