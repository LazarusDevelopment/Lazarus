package me.qiooip.lazarus.kits.kit;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@Setter
public class KitData {

	private String name;
	private String permission;
	private int delay;
	protected ItemStack[] contents;

	public void applyKit(Player target) {
		Stream.of(this.contents).filter(Objects::nonNull).forEach(item -> {
			if(target.getInventory().firstEmpty() == -1) {
				target.getWorld().dropItemNaturally(target.getLocation(), item);
				return;
			}

			target.getInventory().addItem(item);
		});
	}

	public String getType() {
		return "NORMAL";
	}

	public boolean shouldCancelEvent(int slot) {
		return slot >= 36;
	}
}
