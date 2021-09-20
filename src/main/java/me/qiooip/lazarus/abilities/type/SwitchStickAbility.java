package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SwitchStickAbility extends AbilityItem {

    private int degrees;

    public SwitchStickAbility(ConfigFile config) {
        super(AbilityType.SWITCH_STICK, "SWITCH_STICK", config);

        this.overrideActivationMessage();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.degrees = abilitySection.getInt("DEGREES");
    }

    public void sendActivationMessage(Player player, Player target, int amount) {
        this.activationMessage.forEach(line -> player.sendMessage(line
            .replace("<abilityName>", this.displayName)
            .replace("<target>", target.getName())
            .replace("<amount>", String.valueOf(amount))
            .replace("<cooldown>", StringUtils.formatDurationWords(this.cooldown * 1000L))));
    }

    @Override
    protected boolean onPlayerItemHit(Player damager, Player target, EntityDamageByEntityEvent event) {
        Location location = target.getLocation().clone();
        location.setYaw(location.getYaw() + this.degrees);

        target.teleport(location);

        this.sendActivationMessage(damager, target, this.degrees);
        return true;
    }
}
