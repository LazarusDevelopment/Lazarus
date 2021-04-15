package me.qiooip.lazarus.kits.kit;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.utils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@Getter
@Setter
public class KitData {

	private String name;
	private String permission;
	private int delay;
	private KitType type;

	protected ItemStack[] contents;
	protected ItemStack[] armor;

	public void applyKit(Player target) {
		if(this.type == KitType.NORMAL) {
			this.applyNormalKit(target);
		} else {
			this.applyKitmapKit(target);
		}
	}

	public boolean shouldCancelEvent(int slot) {
		return slot >= 36 && (slot != 45 && slot != 46 && slot != 47 && slot != 48);
	}

	private void applyKitmapKit(Player target) {
		target.getInventory().setContents(this.contents);
		target.getInventory().setArmorContents(this.armor);
	}

	private void applyNormalKit(Player target) {
		for(ItemStack item : this.contents) {
			if(item == null) continue;
			PlayerUtils.addToInventoryOrDropToFloor(target, item);
		}

		PlayerInventory inventory = target.getInventory();

		for(int i = 0; i < 4; i++) {
			int index = inventory.getSize() + i;

			ItemStack armorPart = this.armor[i];
			if(armorPart == null) continue;

			if(inventory.getItem(inventory.getSize() + i) != null) {
				PlayerUtils.addToInventoryOrDropToFloor(target, armorPart);
			} else {
				inventory.setItem(index, armorPart);
			}
		}
	}
}
