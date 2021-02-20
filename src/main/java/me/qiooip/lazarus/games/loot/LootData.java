package me.qiooip.lazarus.games.loot;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.utils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

@Getter
@Setter
public class LootData {

    private String name;
    private int amount;

    private transient Inventory inventory;
    private transient ItemStack[] items;

    public void handleRewards(Player player) {
        if(this.getItems().length == 0) return;

        int amount = this.getAmount() == -1 ? Config.LOOT_DEFAULT_REWARD_AMOUNT : this.getAmount();

        if(Config.LOOT_RANDOMIZE_REWARDS) {
            this.handleRandomizedLoot(player, amount);
        } else {
            this.handleNormalLoot(player, amount);
        }
    }

    private void handleRandomizedLoot(Player player, int amount) {
        Random random = new Random();
        int index;

        for(int i = 0; i < amount; i++) {
            index = random.nextInt(this.items.length);
            PlayerUtils.addToInventoryOrDropToFloor(player, this.getItems()[index]);
        }
    }

    private void handleNormalLoot(Player player, int amount) {
        int index;

        for(int i = 0; i < amount; i++) {
            index = i % this.getItems().length;
            PlayerUtils.addToInventoryOrDropToFloor(player, this.getItems()[index]);
        }
    }
}
