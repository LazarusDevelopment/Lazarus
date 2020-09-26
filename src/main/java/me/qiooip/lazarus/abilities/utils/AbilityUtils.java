package me.qiooip.lazarus.abilities.utils;

import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class AbilityUtils {

    public static List<PotionEffect> loadEffects(ConfigurationSection abilitySection) {
        return loadEffects(abilitySection, "EFFECTS");
    }

    public static List<PotionEffect> loadEffects(ConfigurationSection abilitySection, String section) {
        List<PotionEffect> effects = new ArrayList<>();
        ConfigurationSection effectSection = abilitySection.getConfigurationSection(section);

        effectSection.getKeys(false).forEach(effect -> {
            if(PotionEffectType.getByName(effect) == null) return;

            effects.add(new PotionEffect(PotionEffectType.getByName(effect),
                effectSection.getInt(effect + ".DURATION") * 20,
                effectSection.getInt(effect + ".LEVEL") - 1));
        });

        return effects;
    }

    public static String getEffectList(List<PotionEffect> effects, String effectFormat) {
        StringJoiner joiner = new StringJoiner("\n");

        effects.forEach(effect -> joiner.add(effectFormat
            .replace("<effectName>", StringUtils.getPotionEffectName(effect))
            .replace("<duration>", String.valueOf(effect.getDuration() / 20))));

        return joiner.toString();
    }
}
