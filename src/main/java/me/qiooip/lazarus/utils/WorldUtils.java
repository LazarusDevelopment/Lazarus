package me.qiooip.lazarus.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.util.function.Consumer;

public class WorldUtils {

    public static void forEachEntity(Consumer<Entity> consumer) {
        Bukkit.getWorlds().forEach(world -> world.getEntities().forEach(consumer));
    }
}
