package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.abilities.utils.AbilityUtils;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class CocaineAbility extends AbilityItem {

    private List<PotionEffect> effects;

    public CocaineAbility(ConfigFile config) {
        super(AbilityType.COCAINE, "COCAINE", config);

        this.overrideActivationMessage();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.effects = AbilityUtils.loadEffects(abilitySection);
    }

    public void sendActivationMessage(Player player) {
        this.activationMessage.forEach(line -> player.sendMessage(line
            .replace("<abilityName>", this.displayName)
            .replace("<effects>", AbilityUtils.getEffectList(this.effects, Language.ABILITIES_COCAINE_EFFECT_FORMAT))
            .replace("<cooldown>", DurationFormatUtils.formatDurationWords(this.cooldown * 1000, true, true))));
    }

    @Override
    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        this.effects.forEach(effect -> Lazarus.getInstance().getPvpClassManager().addPotionEffect(player, effect));
        this.sendActivationMessage(player);

        event.setCancelled(true);
        return true;
    }
}
