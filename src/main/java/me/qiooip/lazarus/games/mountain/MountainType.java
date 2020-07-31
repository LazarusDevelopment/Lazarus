package me.qiooip.lazarus.games.mountain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.function.Predicate;

@AllArgsConstructor
public enum MountainType {
    GLOWSTONE(material -> material == Material.GLOWSTONE),
    ORE(material -> material.name().endsWith("_ORE"));

    @Getter private final Predicate<Material> materials;
}
