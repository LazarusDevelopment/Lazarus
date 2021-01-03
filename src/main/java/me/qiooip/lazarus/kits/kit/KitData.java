package me.qiooip.lazarus.kits.kit;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

	}

	public boolean shouldCancelEvent(int slot) {
		return slot >= 36 && (slot != 45 && slot != 46 && slot != 47 && slot != 48);
	}
}
