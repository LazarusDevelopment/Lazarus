package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.abilities.utils.AbilityUtils;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class HealerAbility extends AbilityItem {

    private int healAmount;
    private List<PotionEffect> effects;

    public HealerAbility(ConfigFile config) {
        super(AbilityType.HEALER, "HEALER", config);

        this.overrideActivationMessage();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.healAmount = abilitySection.getInt("HEAL_AMOUNT");
        this.effects = AbilityUtils.loadEffects(abilitySection);
    }

    public void sendActivationMessage(Player player, Player target, int healerHealAmount, List<PotionEffect> givenEffects) {
        this.activationMessage.forEach(line -> player.sendMessage(line
            .replace("<abilityName>", this.displayName)
            .replace("<player>", target.getName())
            .replace("<amount>", String.valueOf(healerHealAmount))
            .replace("<effects>", AbilityUtils.getEffectList(givenEffects, Language.ABILITIES_HEALER_EFFECT_FORMAT))
            .replace("<cooldown>", DurationFormatUtils.formatDurationWords(this.cooldown * 1000, true, true))));
    }

    @Override
    protected boolean onPlayerItemHit(Player damager, Player target, EntityDamageByEntityEvent event) {
        PlayerFaction damagerFaction = FactionsManager.getInstance().getPlayerFaction(damager);

        if(damagerFaction == null) {
            damager.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_HEALER_NOT_IN_FACTION);
            return false;
        }

        PlayerFaction targetFaction = FactionsManager.getInstance().getPlayerFaction(target);

        if(targetFaction == null || targetFaction != damagerFaction) {
            damager.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_HEALER_NOT_TEAM_MATE.replace("<target>", target.getName()));
            return false;
        }

        target.setHealth(Math.min(target.getHealth() + this.healAmount, target.getMaxHealth()));
        this.addEffects(target, effects);

        // TODO: send message to target
        this.sendActivationMessage(damager, target, this.healAmount, this.effects);

        event.setCancelled(true);
        return true;
    }
}
