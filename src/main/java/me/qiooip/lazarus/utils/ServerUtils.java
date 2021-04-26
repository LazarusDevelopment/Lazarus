package me.qiooip.lazarus.utils;

import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.PotionEffectEvent;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Function;

public class ServerUtils {

    public static final Set<SpawnReason> ALLOWED_SPAWN_REASONS = EnumSet.of(SpawnReason.SPAWNER_EGG, SpawnReason.EGG,
        SpawnReason.SPAWNER, SpawnReason.BREEDING, SpawnReason.CUSTOM, SpawnReason.SLIME_SPLIT);

    public static PotionEffect getEffect(PotionEffectEvent event) {
        try {
            return event.getEffect();
        } catch(NoSuchMethodError e) {
            try {
                Method effectMethod = event.getClass().getSuperclass().getDeclaredMethod("getPotionEffect");
                return (PotionEffect) effectMethod.invoke(event);
            } catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static Function<String, String> parsePlaceholderFunction(String value, String placeholder) {
        if(value.isEmpty()) {
            return null;
        } else {
            int indexOf = value.indexOf(placeholder);

            if(indexOf == 0) {
                String finalPlaceholder = value.replace(placeholder, "");
                return s -> s + finalPlaceholder;
            } else {
                String[] placeholderParts = value.split(placeholder);

                if(placeholderParts.length == 1) {
                    return s -> placeholderParts[0] + s;
                } else {
                    return s -> placeholderParts[0] + s + placeholderParts[1];
                }
            }
        }
    }
}
