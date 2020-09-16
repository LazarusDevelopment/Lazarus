package me.qiooip.lazarus.abilities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.TreeMap;

@Getter
@AllArgsConstructor
public enum AbilityType {

    COCAINE("Cocaine"),
    INVISIBILITY("Invisibility"),
    POCKET_BARD("PocketBard"),
    SWITCHER("Switcher");

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
