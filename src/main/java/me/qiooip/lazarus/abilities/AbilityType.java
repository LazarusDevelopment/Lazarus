package me.qiooip.lazarus.abilities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.TreeMap;

@Getter
@AllArgsConstructor
public enum AbilityType {

    AGGRESSIVE_PEARL("AggressivePearl", "&a&lAggressive Pearl"),
    ANTI_REDSTONE("AntiRedstone", "&a&lAnti Redstone"),
    ANTI_ABILITY_BALL("AntiAbilityBall", "&a&lAnti Ability Ball"),
    ANTI_TRAP_STAR("AntiTrapStar", "&a&lAnti Trap Star"),
    COCAINE("Cocaine", "&a&lCocaine"),
    COMBO("Combo", "&a&lCombo"),
    DECOY("Decoy", "&a&lDecoy"),
    EXOTIC_BONE("ExoticBone", "&a&lExotic Bone"),
    FAKE_PEARL("FakePearl", "&a&lFake Pearl"),
    FAST_PEARL("FastPearl", "&a&lFast Pearl"),
    GUARDIAN_ANGEL("GuardianAngel", "&a&lGuardian Angel"),
    INVISIBILITY("Invisibility", "&a&lInvisibility"),
    LOGGER_BAIT("LoggerBait", "&a&lLogger Bait"),
    LUCKY_INGOT("LuckyIngot", "&a&lLucky Ingot"),
    POCKET_BARD("PocketBard", "&a&lPocket Bard"),
    POTION_COUNTER("PotionCounter", "&a&lPotion Counter"),
    PRE_PEARL("PrePearl", "&a&lPre-Pearl"),
    RAGE("Rage", "&a&lRage"),
    RAGE_BRICK("RageBrick", "&a&lRage Brick"),
    ROCKET("Rocket", "&a&lRocket"),
    SCRAMBLER("Scrambler", "&a&lScrambler"),
    SECOND_CHANCE("SecondChance", "&a&lSecondChance"),
    STARVATION_FLESH("StarvationFlesh", "&a&lStarvationFlesh"),
    SWITCHER("Switcher", "&a&lSwitcher"),
    SWITCH_STICK("SwitchStick", "&a&lSwitchStick"),
    TANK_INGOT("TankIngot", "&a&lTank Ingot"),
    TIME_WARP("TimeWarp", "&a&lTimeWarp"), // TODO
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
