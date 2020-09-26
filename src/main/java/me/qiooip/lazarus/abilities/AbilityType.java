package me.qiooip.lazarus.abilities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.TreeMap;

@Getter
@AllArgsConstructor
public enum AbilityType {

    AGGRESSIVE_PEARL("AggressivePearl", "&a&lAggressive Pearl"), // Kad perla landa dat mu neke efekte (configurable)
    ANTI_REDSTONE("AntiRedstone", "&a&lAnti Redstone"),
    ANTI_TRAP_STAR("AntiTrapStar", "&a&lAnti Trap Star"), // Ako ga je igrac hito u zadnjih 10 sekundi, starta counter i nakon 5 sekundi ga tpa to dog igraca
    COCAINE("Cocaine", "&a&lCocaine"),
    EXOTIC_BONE("ExoticBone", "&a&lExotic Bone"),
    FAKE_PEARL("FakePearl", "&a&lFake Pearl"),
    FAST_PEARL("FastPearl", "&a&lFast Pearl"), // Duplo manji enderpearl cooldown
    GUARDIAN_ANGEL("GuardianAngel", "&a&lGuardian Angel"),
    HEALER("Healer", "&a&lHealer"), // Udari svog faction membera i heala ga i da mu neke efekte
    INVISIBILITY("Invisibility", "&a&lInvisibility"),
    LUCKY_INGOT("LuckyIngot", "&a&lLucky Ingot"),
    POCKET_BARD("PocketBard", "&a&lPocket Bard"),
    POTION_COUNTER("PotionCounter", "&a&lPotion Counter"),
    PRE_PEARL("PrePearl", "&a&lPre-Pearl"), // Kad right clickas tu perlu, treba onaj big explosion particle playat i saveat tu lokaciju i onda nakon 10 sec tpat ga na tu lokaciju
    RAGE("Rage", "&a&lRage"), // Svaki hit nakon aktivacije unutar 5 sekundi = 1 sekunda strengtha, minimalno 3 sekunde max 10
    ROCKET("Rocket", "&a&lRocket"), // Jednostavno ga boosta u zrak
    SCRAMBLER("Scrambler", "&a&lScrambler"), // Randomizea hotbar
    SWITCHER("Switcher", "&a&lSwitcher"),
    TANK_INGOT("TankIngot", "&a&lTank Ingot"),
    WEB_GUN("WebGun", "&a&lWeb Gun"); // 2x2 cobweb koji se makne nakon 10 sekundi

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
