package me.qiooip.lazarus.games;

import lombok.AllArgsConstructor;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.games.conquest.ConquestData;
import me.qiooip.lazarus.games.conquest.ZoneType;
import me.qiooip.lazarus.games.dragon.EnderDragonManager;
import me.qiooip.lazarus.games.koth.KothData;
import me.qiooip.lazarus.games.koth.RunningKoth;
import me.qiooip.lazarus.games.mountain.MountainData;
import me.qiooip.lazarus.utils.StringUtils;

public class Placeholder {

    @AllArgsConstructor
    public enum ConquestReplacer {

        CAPTIME("<captime>"),
        POINTS("<points>"),
        RED_LOCATION("<red_location>"),
        BLUE_LOCATION("<blue_location>"),
        GREEN_LOCATION("<green_location>"),
        YELLOW_LOCATION("<yellow_location>");

        private final String placeholder;

        String getValue(ConquestData conquest) {
            switch(this) {
                case CAPTIME: return String.valueOf(Config.CONQUEST_CAP_TIME);
                case POINTS: return String.valueOf(Config.CONQUEST_POINTS_TO_WIN);
                case RED_LOCATION: return conquest.getLocationString(ZoneType.RED);
                case BLUE_LOCATION: return conquest.getLocationString(ZoneType.BLUE);
                case GREEN_LOCATION: return conquest.getLocationString(ZoneType.GREEN);
                case YELLOW_LOCATION: return conquest.getLocationString(ZoneType.YELLOW);
                default: return "";
            }
        }

        public static String parse(ConquestData conquest, String message) {
            for(ConquestReplacer placeholder : values()) {
                if(!message.contains(placeholder.placeholder)) continue;
                message = message.replace(placeholder.placeholder, placeholder.getValue(conquest));
            }

            return message;
        }
    }

    @AllArgsConstructor
    public enum KothReplacer {

        KOTH_NAME("<koth>"),
        KOTH_CAPTIME("<captime>"),
        KOTH_POINTS("<points>"),
        KOTH_WORLD("<world>"),
        KOTH_X_BLOCK("<x>"),
        KOTH_Y_BLOCK("<y>"),
        KOTH_Z_BLOCK("<z>");

        private final String placeholder;

        String getValue(KothData koth) {
            switch(this) {
                case KOTH_NAME: return koth.getColoredName();
                case KOTH_CAPTIME: return StringUtils.formatMillis(koth.getCaptime() * 1000L);
                case KOTH_POINTS: return String.valueOf(koth.getFactionPoints());
                case KOTH_WORLD: return StringUtils.getWorldName(koth.getCuboid().getCenter());
                case KOTH_X_BLOCK: return String.valueOf(koth.getCuboid().getCenter().getBlockX());
                case KOTH_Y_BLOCK: return String.valueOf(koth.getCuboid().getCenter().getBlockY());
                case KOTH_Z_BLOCK: return String.valueOf(koth.getCuboid().getCenter().getBlockZ());
                default: return "";
            }
        }

        public static String parse(KothData koth, String message) {
            for(KothReplacer placeholder : values()) {
                if(!message.contains(placeholder.placeholder)) continue;
                message = message.replace(placeholder.placeholder, placeholder.getValue(koth));
            }

            return message;
        }
    }

    @AllArgsConstructor
    public enum RunningKothReplacer {

        KOTH_NAME("<koth>"),
        KOTH_CAPPER("<capper>"),
        KOTH_TIME_LEFT("<time>"),
        KOTH_WORLD("<world>"),
        KOTH_X_BLOCK("<x>"),
        KOTH_Y_BLOCK("<y>"),
        KOTH_Z_BLOCK("<z>");

        private final String placeholder;

        String getValue(RunningKoth koth) {
            switch(this) {
                case KOTH_NAME: return koth.getKothData().getColoredName();
                case KOTH_CAPPER: return koth.getCapzone().getCapperName();
                case KOTH_TIME_LEFT: return koth.getCapzone().getTimeLeft();
                case KOTH_WORLD: return StringUtils.getWorldName(koth.getKothData().getCuboid().getCenter());
                case KOTH_X_BLOCK: return String.valueOf(koth.getKothData().getCuboid().getCenter().getBlockX());
                case KOTH_Y_BLOCK: return String.valueOf(koth.getKothData().getCuboid().getCenter().getBlockY());
                case KOTH_Z_BLOCK: return String.valueOf(koth.getKothData().getCuboid().getCenter().getBlockZ());
                default: return "";
            }
        }

        public static String parse(RunningKoth koth, String message) {
            for(RunningKothReplacer placeholder : values()) {
                if(!message.contains(placeholder.placeholder)) continue;
                message = message.replace(placeholder.placeholder, placeholder.getValue(koth));
            }

            return message;
        }
    }

    @AllArgsConstructor
    public enum EnderDragonReplacer {

        MAX_HEALTH("<health>"),
        DRAGON_WORLD("<world>"),
        DRAGON_X("<x>"),
        DRAGON_Y("<y>"),
        DRAGON_Z("<z>");

        private final String placeholder;

        String getValue(EnderDragonManager manager) {
            switch(this) {
                case MAX_HEALTH: return String.valueOf(Config.ENDER_DRAGON_HEALTH);
                case DRAGON_WORLD: return StringUtils.getWorldName(manager.getSpawnLocation());
                case DRAGON_X: return String.valueOf(manager.getSpawnLocation().getBlockX());
                case DRAGON_Y: return String.valueOf(manager.getSpawnLocation().getBlockY());
                case DRAGON_Z: return String.valueOf(manager.getSpawnLocation().getBlockZ());
                default: return "";
            }
        }

        public static String parse(EnderDragonManager manager, String message) {
            for(EnderDragonReplacer placeholder : values()) {
                if(!message.contains(placeholder.placeholder)) continue;
                message = message.replace(placeholder.placeholder, placeholder.getValue(manager));
            }

            return message;
        }
    }

    @AllArgsConstructor
    public enum MountainReplacer {

        MOUNTAIN_ID("<id>"),
        MOUNTAIN_NAME("<mountain>"),
        MOUNTAIN_TYPE("<type>"),
        MOUNTAIN_WORLD("<world>"),
        MOUNTAIN_X_BLOCK("<x>"),
        MOUNTAIN_Z_BLOCK("<z>");

        private final String placeholder;

        String getValue(MountainData mountain) {
            switch(this) {
                case MOUNTAIN_ID: return String.valueOf(mountain.getId());
                case MOUNTAIN_NAME: return mountain.getFaction().getName(null);
                case MOUNTAIN_TYPE: return mountain.getType().name();
                case MOUNTAIN_WORLD: return StringUtils.getWorldName(mountain.getCuboid().getCenter().getWorld());
                case MOUNTAIN_X_BLOCK: return String.valueOf(mountain.getCuboid().getCenter().getBlockX());
                case MOUNTAIN_Z_BLOCK: return String.valueOf(mountain.getCuboid().getCenter().getBlockZ());
                default: return "";
            }
        }

        public static String parse(MountainData mountain, String message) {
            for(MountainReplacer placeholder : values()) {
                if(!message.contains(placeholder.placeholder)) continue;
                message = message.replace(placeholder.placeholder, placeholder.getValue(mountain));
            }

            return message;
        }
    }
}
