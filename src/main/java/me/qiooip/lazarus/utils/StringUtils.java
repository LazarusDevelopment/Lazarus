package me.qiooip.lazarus.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.type.PlayerTimer;
import me.qiooip.lazarus.userdata.settings.Settings;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class StringUtils {

    private static final Pattern ALPHANUMERIC = Pattern.compile("^[a-zA-Z0-9]+$");

    public static final String[] ENCHANTMENT_NAMES = new String[] { "AQUA_AFFINITY", "BANE_OF_ARTHROPODS", "BLAST_PROTECTION",
        "DURABILITY", "EFFICIENCY", "FEATHER_FALLING", "FIRE_ASPECT", "FIRE_PROTECTION", "FLAME", "FORTUNE", "INFINITY", "KNOCKBACK",
        "LOOTING", "LUCK", "LURE", "POWER", "PROJECTILE_PROTECTION", "PROTECTION", "PUNCH", "RESPIRATION", "SHARPNESS",
        "SILK_TOUCH", "SMITE", "THORNS", "UNBREAKING" };

    public static String joinList(List<String> list, String delimiter, int start) {
        if(list.size() < start) return "";

        return String.join(Color.translate(delimiter), list.subList(start - 1, list.size()));
    }

    public static String joinArray(String[] array, String delimiter, int start) {
        if(array.length < start) return "";

        return String.join(Color.translate(delimiter), Arrays.copyOfRange(array, start - 1, array.length));
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static String capitalize(String string) {
        return String.valueOf(string.charAt(0)).toUpperCase() + string.substring(1);
    }

    public static boolean containsIgnoreCase(String line, String value) {
        return line.toLowerCase().contains(value.toLowerCase());
    }

    public static String getMaterialName(Material material) {
        return material.name().toLowerCase().replace("_", " ");
    }

    public static boolean isAlphaNumeric(String value) {
        return ALPHANUMERIC.matcher(value).find();
    }

    public static String getWorldName(Location location) {
        return getWorldName(location.getWorld());
    }

    public static String getWorldName(World world) {
        switch(world.getEnvironment()) {
            case NORMAL: return Language.WORLD_NAMES_OVERWORLD;
            case NETHER: return Language.WORLD_NAMES_NETHER;
            case THE_END: return Language.WORLD_NAMES_THE_END;
            default: return world.getName();
        }
    }

    public static String getWorldName(Environment environment) {
        switch(environment) {
            case NETHER: return Language.WORLD_NAMES_NETHER;
            case THE_END: return Language.WORLD_NAMES_THE_END;
            default: return Language.WORLD_NAMES_OVERWORLD;
        }
    }

    public static String center(String value, int maxLength) {
        StringBuilder builder = new StringBuilder(maxLength - value.length());
        IntStream.range(0, maxLength - value.length()).forEach(i -> builder.append(" "));

        builder.insert((builder.length() / 2) + 1, value);
        return builder.toString();
    }

    public static String getEntityName(String type) {
        type = type.toLowerCase();
        if(!type.contains("_")) return capitalize(type);

        return String.join(" ", type.split("_"));
    }

    public static String getBlockName(Block block) {
        return block.getType().name().replace("_" , " ").toLowerCase();
    }

    public static String getLocationNameWithWorld(Location location) {
        return getLocationName(location) + " (" + getWorldName(location) +")";
    }

    public static String getLocationName(Location location) {
        return location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ();
    }

    public static String getLocationNameWithWorldWithoutY(Location location) {
        return getLocationNameWithoutY(location) + " (" + getWorldName(location) +")";
    }

    public static String getLocationNameWithoutY(Location location) {
        return location.getBlockX() + ", " + location.getBlockZ();
    }

    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    public static Integer tryParseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException e) {
            return null;
        }
    }

    public static boolean isBoolean(String value) {
        return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
    }

    public static String formatDeathban(long bannedUntil) {
        return formatDurationWords(bannedUntil - System.currentTimeMillis());
    }

    public static String formatDurationWords(long cooldown) {
        return DurationFormatUtils.formatDurationWords(cooldown, true, true);
    }

    public static String formatMillis(long millis) {
        String value = "";

        int days = (int) (millis / 86400000L);
        if(days > 0) {
            millis -= (long) days * 86400000L;
            value += days + "d ";
        }

        int hours = (int) (millis / 3600000L);
        if(hours > 0) {
            millis -= (long) hours * 3600000L;
            value += hours + "h ";
        }

        int minutes = (int) (millis / 60000L);
        if(minutes > 0) {
            millis -= (long) minutes * 60000L;
            value += minutes + "m ";
        }

        int seconds = (int) (millis / 1000L);
        if(seconds > 0) {
            value += seconds + "s";
        }

        return value.isEmpty() ? "0s" : value.trim();
    }

    public static String getPotionEffectName(PotionEffect effect) {
        return effect.getAmplifier() == 0 ? StringUtils.getPotionEffectName(effect.getType()) :
        StringUtils.getPotionEffectName(effect.getType()) + " " + StringUtils.getPotionEffectLevel(effect);
    }

    public static String settingsToString(Settings settings) {
        return String.valueOf(new char[] {
            getBooleanChar(settings.isFoundOre()),
            getBooleanChar(settings.isSounds()),
            getBooleanChar(settings.isMessages()),
            getBooleanChar(settings.isScoreboard()),
            getBooleanChar(settings.isDeathMessages()),
            getBooleanChar(settings.isPublicChat()),
            getBooleanChar(settings.isCobble()),
            getBooleanChar(settings.isLightning())
        });
    }

    public static Settings settingsFromString(String value) {
        if(StringUtils.isNullOrEmpty(value)) {
            return new Settings();
        }

        return new Settings(
            getBoolean(value.charAt(0)),
            getBoolean(value.charAt(1)),
            getBoolean(value.charAt(2)),
            getBoolean(value.charAt(3)),
            getBoolean(value.charAt(4)),
            getBoolean(value.charAt(5)),
            getBoolean(value.charAt(6)),
            getBoolean(value.charAt(7))
        );
    }

    private static char getBooleanChar(boolean value) {
        return value ? '1' : '0';
    }

    private static boolean getBoolean(char value) {
        return value != '0';
    }

    private static String getPotionEffectName(PotionEffectType type) {
        switch(type.getName()) {
            case "ABSORPTION": return "Absorption";
            case "BLINDNESS": return "Blindness";
            case "CONFUSION": return "Confusion";
            case "DAMAGE_RESISTANCE": return "Resistance";
            case "FAST_DIGGING": return "Haste";
            case "FIRE_RESISTANCE": return "Fire Resistance";
            case "HARM": return "Instant Damage";
            case "HEAL": return "Instant Health";
            case "HEALTH_BOOST": return "Health Boost";
            case "HUNGER": return "Hunger";
            case "INCREASE_DAMAGE": return "Strength";
            case "INVISIBILITY": return "Invisibility";
            case "JUMP": return "Jump";
            case "NIGHT_VISION": return "Night Vision";
            case "POISON": return "Poison";
            case "REGENERATION": return "Regeneration";
            case "SATURATION": return "Saturation";
            case "SLOW": return "Slowness";
            case "SLOW_DIGGING": return "Slow Digging";
            case "SPEED": return "Speed";
            case "WATER_BREATHING": return "Water Breathing";
            case "WEAKNESS": return "Weakness";
            case "WITHER": return "Wither";
            default: return "Unknown potion effect";
        }
    }

    private static String getPotionEffectLevel(PotionEffect effect) {
        switch(effect.getAmplifier()) {
            case 0: return "";
            case 1: return "II";
            case 2: return "III";
            case 3: return "IV";
            case 4: return "V";
            case 5: return "VI";
            case 6: return "VII";
            case 7: return "VIII";
            case 8: return "IX";
            case 9: return "X";
            default: return "Unknown potion effect level";
        }
    }

    public static String getEnchantName(String enchant) {
        switch(enchant) {
            case "PROTECTION": return "PROTECTION_ENVIRONMENTAL";
            case "FIRE_PROTECTION": return "PROTECTION_FIRE";
            case "FEATHER_FALLING": return "PROTECTION_FALL";
            case "BLAST_PROTECTION": return "PROTECTION_EXPLOSIONS";
            case "PROJECTILE_PROTECTION": return "PROTECTION_PROJECTILE";
            case "RESPIRATION": return "OXYGEN";
            case "AQUA_AFFINITY": return "WATER_WORKER";
            case "THORNS": return "THORNS";
            case "SHARPNESS": return "DAMAGE_ALL";
            case "SMITE": return "DAMAGE_UNDEAD";
            case "BANE_OF_ARTHROPODS": return "DAMAGE_ARTHROPODS";
            case "KNOCKBACK": return "KNOCKBACK";
            case "FIRE_ASPECT": return "FIRE_ASPECT";
            case "LOOTING": return "LOOT_BONUS_MOBS";
            case "EFFICIENCY": return "DIG_SPEED";
            case "SILK_TOUCH": return "SILK_TOUCH";
            case "UNBREAKING": return "DURABILITY";
            case "DURABILITY": return "DURABILITY";
            case "FORTUNE": return "LOOT_BONUS_BLOCKS";
            case "POWER": return "ARROW_DAMAGE";
            case "PUNCH": return "ARROW_KNOCKBACK";
            case "FLAME": return "ARROW_FIRE";
            case "INFINITY": return "ARROW_INFINITE";
            case "LUCK": return "LUCK";
            case "LURE": return "LURE";
            default: return "Unknown enchantment";
        }
    }

    public static String getEnchantName(Enchantment enchantment) {
        switch(enchantment.getName()) {
            case "PROTECTION_ENVIRONMENTAL": return "Protection";
            case "PROTECTION_FIRE": return "Fire Protection";
            case "PROTECTION_FALL": return "Feather Falling";
            case "PROTECTION_EXPLOSIONS": return "Blast Protection";
            case "PROTECTION_PROJECTILE": return "Projectile Protection";
            case "OXYGEN": return "Respiration";
            case "WATER_WORKER": return "Aqua Affinity";
            case "THORNS": return "Thorns";
            case "DAMAGE_ALL": return "Sharpness";
            case "DAMAGE_UNDEAD": return "Smite";
            case "DAMAGE_ARTHROPODS": return "Bane of Arthropods";
            case "KNOCKBACK": return "Knockback";
            case "FIRE_ASPECT": return "Fire Aspect";
            case "LOOT_BONUS_MOBS": return "Looting";
            case "DIG_SPEED": return "Efficiency";
            case "SILK_TOUCH": return "Silk Touch";
            case "DURABILITY": return "Unbreaking";
            case "LOOT_BONUS_BLOCKS": return "Fortune";
            case "ARROW_DAMAGE": return "Power";
            case "ARROW_KNOCKBACK": return "Punch";
            case "ARROW_FIRE": return "Flame";
            case "ARROW_INFINITE": return "Infinity";
            case "LUCK": return "Luck";
            case "LURE": return "Lure";
            default: return "Unknown enchantment";
        }
    }

    public static PlayerTimer getPlayerTimerByName(String name) {
        switch(name.toUpperCase()) {
            case "APPLE": return TimerManager.getInstance().getAppleTimer();
            case "COMBAT": return TimerManager.getInstance().getCombatTagTimer();
            case "ENDERPEARL": return TimerManager.getInstance().getEnderPearlTimer();
            case "GAPPLE": return TimerManager.getInstance().getGAppleTimer();
            case "PVPPROT": return TimerManager.getInstance().getPvpProtTimer();
            default: return null;
        }
    }

    public static int parseSeconds(String value) {
        if(isInteger(value)) return Math.abs(Integer.parseInt(value));
        if(value.equalsIgnoreCase("0s")) return 0;

        value = value.toLowerCase();
        int seconds = 0;

        for(TimeFormat format : TimeFormat.values()) {
            if(!value.contains(format.getTimeChar())) continue;

            String[] split = value.split(format.getTimeChar());
            if(!isInteger(split[0])) continue;

            seconds += Math.abs(Integer.parseInt(split[0])) * format.getSeconds();
            if(split.length > 1) value = split[1];
        }

        return seconds == 0 ? -1 : seconds;
    }

    private static String secondsToMinutes(int seconds) {
        if(seconds < 60) {
            return "00:" + (seconds < 10 ? "0" + seconds : "" + seconds);
        }

        int secondsModulo = seconds % 60;
        int minutes = seconds / 60;

        return (minutes < 10 ? "0" + minutes : minutes) +
            ":" + (secondsModulo < 10 ? "0" + secondsModulo : secondsModulo);
    }

    private static String secondsToHours(int seconds) {
        if(seconds < 60) {
            return "00:00:" + (seconds < 10 ? "0" + seconds : "" + seconds);
        }

        int secondsModulo = seconds % 60;
        int minutes = seconds / 60;

        String secondsDisplay = secondsModulo < 10 ? "0" + secondsModulo : "" + secondsModulo;

        if(minutes < 60) {
            return "00:" + (minutes < 10 ? "0" + minutes : "" + minutes) + ":" + secondsDisplay;
        }

        int minutesModulo = minutes % 60;
        int hours = minutes / 60;

        return (hours < 10 ? "0" + hours : hours) +
            ":" + (minutesModulo < 10 ? "0" + minutesModulo : "" + minutesModulo) +
            ":" + secondsDisplay;
    }

    public static String formatTime(long time, FormatType type) {
        switch(type) {
            case MILLIS_TO_SECONDS: return "" + Math.round((time / 1000f) * 10f) / 10f;
            case MILLIS_TO_MINUTES: return secondsToMinutes((int) (time / 1000));
            case MILLIS_TO_HOURS: return secondsToHours((int) (time / 1000));
            case SECONDS_TO_MINUTES: return secondsToMinutes((int) time);
            case SECONDS_TO_HOURS: return secondsToHours((int) time);

            default: return "";
        }
    }

    public enum FormatType {
        MILLIS_TO_SECONDS, MILLIS_TO_MINUTES, MILLIS_TO_HOURS, SECONDS_TO_MINUTES, SECONDS_TO_HOURS
    }

    @Getter
    @AllArgsConstructor
    public enum TimeFormat {

        DAY("d", TimeUnit.DAYS.toSeconds(1L)),
        HOUR("h", TimeUnit.HOURS.toSeconds(1L)),
        MINUTE("m", TimeUnit.MINUTES.toSeconds(1L)),
        SECOND("s", 1L);

        private final String timeChar;
        private final long seconds;
    }
}
