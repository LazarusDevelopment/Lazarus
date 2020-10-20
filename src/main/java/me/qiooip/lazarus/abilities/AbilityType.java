package me.qiooip.lazarus.abilities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.TreeMap;

@Getter
@AllArgsConstructor
public enum AbilityType {

    AGGRESSIVE_PEARL("AggressivePearl", "&a&lAggressive Pearl"), // TODO: Kad perla landa dat mu neke efekte (configurable)
    ANTI_REDSTONE("AntiRedstone", "&a&lAnti Redstone"),
    ANTI_TRAP_STAR("AntiTrapStar", "&a&lAnti Trap Star"), // TODO: Ako ga je igrac hito u zadnjih 10 sekundi, starta counter i nakon 5 sekundi ga tpa to dog igraca
    COCAINE("Cocaine", "&a&lCocaine"),
    EXOTIC_BONE("ExoticBone", "&a&lExotic Bone"),
    FAKE_PEARL("FakePearl", "&a&lFake Pearl"),
    FAST_PEARL("FastPearl", "&a&lFast Pearl"),
    GUARDIAN_ANGEL("GuardianAngel", "&a&lGuardian Angel"),
    HEALER("Healer", "&a&lHealer"), // TODO: Udari svog faction membera i heala ga i da mu neke efekte
    INVISIBILITY("Invisibility", "&a&lInvisibility"),
    LUCKY_INGOT("LuckyIngot", "&a&lLucky Ingot"),
    POCKET_BARD("PocketBard", "&a&lPocket Bard"),
    POTION_COUNTER("PotionCounter", "&a&lPotion Counter"),
    PRE_PEARL("PrePearl", "&a&lPre-Pearl"),
    RAGE("Rage", "&a&lRage"),
    ROCKET("Rocket", "&a&lRocket"),
    SCRAMBLER("Scrambler", "&a&lScrambler"),
    STARVATION_FLESH("StarvationFlesh", "&a&lStarvationFlesh"),
    SWITCHER("Switcher", "&a&lSwitcher"),
    TANK_INGOT("TankIngot", "&a&lTank Ingot"),
    WEB_GUN("WebGun", "&a&lWeb Gun");

    private final String name;
    private final String displayName;

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
