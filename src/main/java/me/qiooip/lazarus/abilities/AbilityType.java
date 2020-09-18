package me.qiooip.lazarus.abilities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.TreeMap;

@Getter
@AllArgsConstructor
public enum AbilityType {

    AGGRESIVE_PEARL("AggresivePearl"),
    ANTI_TRAP_STAR("AntiTrapStar"),
    COCAINE("Cocaine"),
    EXOTIC_BONE("ExoticBone"),
    FAKE_PEARL("FakePearl"),
    GUARDIAN_ANGLE("GuardianAngle"),
    INVISIBILITY("Invisibility"),
    POCKET_BARD("PocketBard"),
    POTION_COUNTER("PotionCounter"),
    PRE_PEARL("PrePearl"),
    ROCKET("Rocket"),
    SCRAMBLER("Scrambler"),
    SWITCHER("Switcher"),
    TANK_INGOT("TankIngot"),
    WEB_GUN("WebGun");

    private final String name;
    private static final Map<String, AbilityType> BY_NAME;

    public static AbilityType getByName(String name) {
        return BY_NAME.get(name);
    }

    static {
        BY_NAME = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        for(AbilityType abilityType : values()) {
            BY_NAME.put(abilityType.name, abilityType);
        }
    }
}
