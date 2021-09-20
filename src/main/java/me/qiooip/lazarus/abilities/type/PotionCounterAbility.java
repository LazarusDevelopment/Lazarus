package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.item.ItemBuilder;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class PotionCounterAbility extends AbilityItem {

    private final ItemStack splashPotion;

    public PotionCounterAbility(ConfigFile config) {
        super(AbilityType.POTION_COUNTER, "POTION_COUNTER", config);

        this.splashPotion = new ItemBuilder(Material.POTION).setDurability(16421).build();
        this.overrideActivationMessage();
    }

    public void sendActivationMessage(Player player, Player target, int potionAmount) {
        this.activationMessage.forEach(line -> player.sendMessage(line
            .replace("<abilityName>", this.displayName)
            .replace("<target>", target.getName())
            .replace("<amount>", String.valueOf(potionAmount))
            .replace("<cooldown>", StringUtils.formatDurationWords(this.cooldown * 1000L))));
    }

    @Override
    protected boolean onPlayerItemHit(Player damager, Player target, EntityDamageByEntityEvent event) {
        int potionAmount = ItemUtils.getItemAmount(target, this.splashPotion.getData());
        this.sendActivationMessage(damager, target, potionAmount);
        return true;
    }
}
