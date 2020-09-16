package me.qiooip.lazarus.utils;

import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.PotionEffectEvent;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.Set;

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
}
