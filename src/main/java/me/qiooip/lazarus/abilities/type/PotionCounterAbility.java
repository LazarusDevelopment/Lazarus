package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.utils.item.ItemBuilder;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PotionCounterAbility extends AbilityItem {

    private final ItemStack splashPotion;

    public PotionCounterAbility(ConfigFile config) {
        super(AbilityType.POTION_COUNTER, "POTION_COUNTER", config);

        this.splashPotion = new ItemBuilder(Material.POTION).setDurability(16421).build();
    }

    @Override
    protected boolean onPlayerItemHit(Player damager, Player target) {
        int amount = ItemUtils.getItemAmount(target, this.splashPotion.getData());

        // TODO: message

        return true;
    }
}
