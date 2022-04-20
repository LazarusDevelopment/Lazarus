package me.qiooip.lazarus.config;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.handlers.logger.CombatLoggerType;
import me.qiooip.lazarus.utils.LocationUtils;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class Config {

    public static long START_TIME;

    public static TimeZone TIMEZONE;
    public static String DATE_FORMAT;

    public static boolean SEND_TO_HUB_ON_KICK_ENABLED;
    public static List<String> SEND_TO_HUB_ON_KICK_HUBS;

    public static boolean JOIN_WELCOME_MESSAGE_ENABLED;
    public static int COMMANDS_COOLDOWN;

    public static File DEATHBAN_DIR;
    public static File RESTORE_DIR;
    public static File FACTIONS_DIR;
    public static File GAMES_DIR;
    public static File USERDATA_DIR;
    public static File HOLOGRAMS_DIR;
    public static File LEADERBOARDS_DIR;

    public static boolean NETHER_PORTAL_TRANSLATION_ENABLED;
    public static int NETHER_PORTAL_TRANSLATION_VALUE;

    public static Map<Environment, Integer> BORDER_SIZE;
    public static Map<Environment, Location> WORLD_SPAWNS;
    public static Map<Environment, Location> WORLD_EXITS;

    public static int MOB_LIMIT_PER_CHUNK;
    public static boolean MOBS_SPAWN_ONLY_FROM_SPAWNERS;
    public static boolean DISABLE_EXPLOSIONS_BLOCK_DAMAGE;
    public static boolean DISABLE_BED_BOMBING;
    public static boolean DISABLE_CREEPER_PLAYER_TARGETING;
    public static boolean SPAWN_MOBS_IN_WARZONE;
    public static boolean LIGHTNING_EFFECT_ON_DEATH;
    public static boolean REMOVE_EMPTY_BOTTLE_ON_POTION_USE;
    public static boolean ENDERPEARL_GLITCH_FIX_ENABLED;

    public static boolean DENY_SPAWNER_BREAK_IN_END;
    public static boolean DENY_SPAWNER_BREAK_IN_NETHER;

    public static boolean DENY_SPAWNER_PLACE_IN_END;
    public static boolean DENY_SPAWNER_PLACE_IN_NETHER;

    public static Set<Material> DISABLED_BLOCK_PLACEMENT;

    public static int DEFAULT_CHAT_DELAY;

    public static boolean CHAT_FORMAT_ENABLED;
    public static boolean CHAT_FORMAT_USE_PLAYER_DISPLAY_NAME;
    public static String CHAT_FORMAT;
    public static String CHAT_FORMAT_WITH_FACTION;

    public static boolean LUNAR_CLIENT_API_ENABLED;
    public static boolean LUNAR_CLIENT_API_FORCED_WAYPOINTS_ENABLED;
    public static boolean LUNAR_CLIENT_API_STAFF_MODULES_ENABLED;
    public static boolean LUNAR_CLIENT_API_TEAM_VIEW_ENABLED;
    public static boolean LUNAR_CLIENT_API_COOLDOWNS_ENABLED;
    public static boolean LUNAR_CLIENT_API_NAMETAGS_ENABLED;
    public static String LUNAR_CLIENT_API_NAMETAGS_FACTION;
    public static List<String> LUNAR_CLIENT_FORCE_DISABLED_MODS;

    public static Set<String> DISABLED_LAZARUS_COMMANDS;
    public static Set<String> DISABLED_FACTION_SUBCOMMANDS;

    public static boolean ONLINE_RANK_ANNOUNCER_ENABLED;
    public static int ONLINE_RANK_ANNOUNCER_INTERVAL;

    public static boolean REDUCED_DURABILITY_LOSS_ENABLED;
    public static int REDUCED_DURABILITY_LOSS_PERCENTAGE;
    public static Set<Material> REDUCED_DURABILITY_LOSS_MATERIALS;

    public static int FACTIONS_AUTO_SAVE;
    public static boolean SHOW_FACTION_INFO_ON_JOIN;

    public static double FACTION_DTR_PER_PLAYER;
    public static double FACTION_MAX_DTR;
    public static double FACTION_MIN_DTR;
    public static double FACTION_DTR_REGEN_PER_MINUTE;
    public static double FACTION_DTR_SOLO_FACTION_DTR;
    public static int FACTION_DTR_FREEZE_DURATION;
    public static String FACTION_DTR_CHARACTERS_FULL_DTR;
    public static String FACTION_DTR_CHARACTERS_REGENERATING;
    public static String FACTION_DTR_CHARACTERS_FROZEN;
    public static Map<Environment, Double> FACTION_DTR_DEATH_LOSS;

    public static int FACTION_PLAYER_LIMIT;
    public static boolean FACTION_JOIN_WHILE_FROZEN;
    public static boolean FACTION_LEAVE_WHILE_FROZEN;
    public static boolean FACTION_DISBAND_WHILE_FROZEN;
    public static boolean FACTION_UNCLAIM_WHILE_FROZEN;
    public static boolean FACTION_LEAVE_WHILE_IN_OWN_CLAIM;
    public static boolean FACTION_PLAYERS_TAKE_OWN_DAMAGE;
    public static boolean FACTION_ALLY_FRIENDLY_FIRE;
    public static int FACTION_WARZONE_BREAK_AFTER_OVERWORLD;
    public static int FACTION_WARZONE_BREAK_AFTER_NETHER;

    public static Map<String, Integer> FACTION_PVP_CLASS_LIMIT;

    public static int FACTION_MAX_ALLIES;

    public static int FACTION_MAP_RADIUS;

    public static boolean FACTION_CLAIMS_MUST_BE_CONNECTED;
    public static int FACTION_MAX_CLAIMS;
    public static int FACTION_CLAIMS_PER_PLAYER;
    public static int FACTION_MIN_CLAIM_SIZE;
    public static int FACTION_MAX_CLAIM_SIZE;
    public static int FACTION_CLAIM_BUFFER;
    public static boolean FACTION_CLAIM_BUFFER_IGNORE_ROADS;
    public static double FACTION_CLAIM_PRICE_PER_BLOCK;
    public static double FACTION_UNCLAIM_PRICE_MULTIPLIER;
    public static boolean FACTION_CLAIM_LIQUID_FLOW_FROM_WILDERNESS_TO_CLAIMS;

    public static Map<Environment, Integer> WARZONE_RADIUS;
    public static Map<Environment, Integer> HOME_WARMUPS;

    public static int HOME_WARMUP_ENEMY_TERRITORY;
    public static boolean DENY_HOME_TELEPORT_FROM_ENEMY_TERRITORY;

    public static boolean FACTION_TOP_ALLOW_NEGATIVE_POINTS;
    public static int FACTION_TOP_KILL;
    public static int FACTION_TOP_DEATH;
    public static int FACTION_TOP_KOTH_CAP;
    public static int FACTION_TOP_CONQUEST_CAP;
    public static int FACTION_TOP_DTC_DESTROY;

    public static int FACTION_RALLY_EXPIRE_AFTER;
    public static boolean FACTION_RALLY_INCLUDE_Y_COORDINATE;

    public static int FACTION_STUCK_WARMUP;
    public static int FACTION_STUCK_ALLOWED_MOVEMENT_RADIUS;

    public static int FACTION_CREATE_COOLDOWN;
    public static int FACTION_RENAME_COOLDOWN;
    public static int FACTION_OPEN_CHANGE_COOLDOWN;

    public static String WILDERNESS_NAME;
    public static String WARZONE_NAME;

    public static int FACTION_NAME_MINIMUM_LENGTH;
    public static int FACTION_NAME_MAXIMUM_LENGTH;
    public static List<String> FACTION_NAME_DISALLOWED_NAMES;

    public static String TEAMMATE_COLOR;
    public static String ALLY_COLOR;
    public static String ENEMY_COLOR;
    public static String ARCHER_TAGGED_COLOR;
    public static String FOCUSED_COLOR;
    public static String SOTW_COLOR;
    public static String STAFF_MODE_COLOR;

    public static int CONQUEST_CAP_TIME;
    public static int CONQUEST_POINTS_PER_CAP;
    public static int CONQUEST_DEATH_PENALTY;
    public static int CONQUEST_POINTS_TO_WIN;
    public static boolean CONQUEST_ALLOW_NEGATIVE_POINTS;

    public static boolean CONQUEST_CAPPED_SIGN_ENABLED;
    public static String CONQUEST_CAPPED_SIGN_NAME;
    public static List<String> CONQUEST_CAPPED_SIGN_LORE;

    public static int DTC_CORE_BREAKS;
    public static int DTC_BREAK_MESSAGE_INTERVAL;

    public static int KOTH_DEFAULT_CAP_TIME;
    public static int KOTH_CAPPING_MESSAGE_INTERVAL;
    public static int KOTH_NOBODY_CAPPING_MESSAGE_INTERVAL;
    public static long KOTH_ANTI_SPAM_MESSAGE_DELAY;
    public static int KOTH_MAX_RUNNING_KOTHS_AT_THE_SAME_TIME;

    public static boolean KOTH_CAPPED_SIGN_ENABLED;
    public static String KOTH_CAPPED_SIGN_NAME;
    public static List<String> KOTH_CAPPED_SIGN_LORE;

    public static boolean KILL_THE_KING_DENY_ITEM_DROP;
    public static boolean KILL_THE_KING_AUTO_REWARD_ENABLED;
    public static List<String> KILL_THE_KING_REWARD;

    public static int MOUNTAIN_RESPAWN_INTERVAL;
    public static int MOUNTAIN_MESSAGE_INTERVAL;

    public static String ENDER_DRAGON_NAME;
    public static int ENDER_DRAGON_HEALTH;

    public static int LOOT_DEFAULT_REWARD_AMOUNT;
    public static boolean LOOT_RANDOMIZE_REWARDS;
    public static String LOOT_INVENTORY_NAME;
    public static int LOOT_INVENTORY_SIZE;

    public static int SCHEDULE_LIST_EVENT_AMOUNT;

    public static int MAX_BALANCE;
    public static int DEFAULT_BALANCE_PLAYER;
    public static int DEFAULT_BALANCE_FACTION;

    public static List<String> BUY_SIGN_LINES;
    public static List<String> SELL_SIGN_LINES;

    public static boolean KITMAP_MODE_ENABLED;
    public static boolean KITMAP_MODE_DISABLE_DEATHBAN;
    public static boolean KITMAP_MODE_DISABLE_DTR_LOSS;
    public static boolean KITMAP_DISABLE_ITEM_DROP_IN_SAFEZONE;
    public static boolean KITMAP_PVP_TIMER_ENABLED;
    public static int KITMAP_SPAWN_TELEPORT_DELAY;

    public static boolean KITMAP_CLEAR_ITEMS_ENABLED;
    public static int KITMAP_CLEAR_ITEMS_INTERVAL;

    public static boolean KITMAP_KILL_REWARD_ENABLED;
    public static List<String> KITMAP_KILL_REWARD;

    public static boolean KITMAP_KILLSTREAK_ENABLED;

    public static boolean KITS_FIRST_JOIN_KIT_ENABLED;
    public static String KITS_FIRST_JOIN_KIT;
    public static List<String> KIT_SIGN_FORMAT;

    public static boolean BLOCKED_COMMANDS_DISABLE_COLON;
    public static List<String> BLOCKED_COMMANDS;

    public static boolean BOTTLE_DROP_ON_DEATH;
    public static String BOTTLE_NAME;
    public static List<String> BOTTLE_LORE;

    public static int BREWING_SPEED_MULTIPLIER;

    public static boolean COBWEB_REMOVER_ENABLED;
    public static int COBWEB_REMOVER_REMOVE_AFTER;

    public static CombatLoggerType COMBAT_LOGGER_TYPE;
    public static String COMBAT_LOGGER_NAME_FORMAT;
    public static int COMBAT_LOGGER_TIME;
    public static int COMBAT_LOGGER_VILLAGER_PROFESSION;

    public static int COMBAT_TAG_DURATION;
    public static boolean COMBAT_TAG_DISABLE_END_ENTRY;
    public static boolean COMBAT_TAG_DISABLE_ENDERCHESTS;
    public static boolean COMBAT_TAG_PLACE_BLOCKS;
    public static boolean COMBAT_TAG_BREAK_BLOCKS;
    public static List<String> COMBAT_TAG_DISABLED_COMMANDS;

    public static ItemStack CROWBAR_ITEM;
    public static String CROWBAR_NAME;
    public static List<String> CROWBAR_LORE;
    public static int CROWBAR_SPAWNER_USES;
    public static int CROWBAR_PORTAL_USES;
    public static String CROWBAR_SPAWNER_NAME_COLOR;
    public static boolean CROWBAR_DISABLE_IN_WARZONE;

    public static boolean INVENTORY_RESTORE_ENABLED;
    public static int INVENTORY_RESTORE_FILE_CACHE;

    public static boolean DEATHBAN_ENABLED;
    public static int DEATHBAN_DEFAULT_BAN_TIME;
    public static int DEFAULT_LIVES;

    public static String PLAYER_SETTINGS_INVENTORY_NAME;
    public static int PLAYER_SETTINGS_INVENTORY_SIZE;

    public static boolean DEATH_SIGN_ENABLED;
    public static String DEATH_SIGN_NAME;
    public static List<String> DEATH_SIGN_LORE;

    public static boolean ENDER_PEARL_COOLDOWN_ENABLED;
    public static int ENDER_PEARL_COOLDOWN_TIME;
    public static boolean ENDER_PEARL_REFUND_ENDER_PEARL_ON_CANCEL;

    public static boolean EOTW_CLEAR_DEATHBANS_ON_START;

    public static int EXP_AMPLIFIER_LOOTING;
    public static int EXP_AMPLIFIER_FORTUNE;

    public static boolean FOUND_ORE_ENABLED;

    public static int FREEZE_MESSAGE_INTERVAL;
    public static List<String> FREEZE_DISABLED_COMMANDS;

    public static int FURNACE_SPEED_MULTIPLIER;

    public static boolean GLISTERING_MELON_CRAFTING;

    public static boolean NORMAL_GOLDEN_APPLE_ON_SCOREBOARD;
    public static int NORMAL_GOLDEN_APPLE_COOLDOWN;
    public static boolean ENCHANTED_GOLDEN_APPLE_ON_SCOREBOARD;
    public static int ENCHANTED_GOLDEN_APPLE_COOLDOWN;

    public static boolean HEAD_DROP_ENABLED;
    public static String HEAD_DROP_NAME_FORMAT;

    public static int LFF_COMMAND_COOLDOWN;

    public static String LIST_NO_STAFF_ONLINE;
    public static boolean LIST_SHOW_VANISHED_STAFF;

    public static int LOGOUT_DELAY;

    public static String MAPKIT_INVENTORY_NAME;
    public static int MAPKIT_INVENTORY_SIZE;

    public static boolean MINECART_ELEVATOR_ENABLED;
    public static boolean MINECART_ELEVATOR_DISABLED_IN_COMBAT;
    public static boolean MINECART_ELEVATOR_OWN_CLAIM_ONLY;

    public static boolean MOB_STACK_ENABLED;
    public static String MOB_STACK_COLOR;
    public static int MOB_STACK_RADIUS;
    public static List<String> MOB_STACK_ENTITIES;
    public static int MOB_STACK_MAX_AMOUNT;

    public static int PVP_PROTECTION_DURATION;
    public static boolean PVP_PROTECTION_DISABLE_END_ENTRY;
    public static boolean PVP_PROTECTION_CAN_ENTER_OWN_CLAIM;

    public static int RENAME_MAX_LENGTH;
    public static List<String> RENAME_BLACKLISTED_WORDS;

    public static int REPORT_COOLDOWN;
    public static int REQUEST_COOLDOWN;

    public static boolean SIGN_ELEVATOR_ENABLED;
    public static boolean SIGN_ELEVATOR_DISABLED_IN_COMBAT;
    public static boolean SIGN_ELEVATOR_OWN_CLAIM_ONLY;
    public static List<String> SIGN_ELEVATOR_ELEVATOR_UP;
    public static List<String> SIGN_ELEVATOR_ELEVATOR_DOWN;

    public static int SOTW_DEFAULT_TIME;
    public static boolean SOTW_SPAWN_MOBS_FROM_SPAWNERS_ONLY;
    public static boolean SOTW_HIDE_PLAYERS;

    public static int SPAWN_CREDITS_INITIAL_SPAWN_CREDITS;
    public static int SPAWN_CREDITS_SPAWN_TELEPORT_DELAY;

    public static boolean STAFF_MODE_ON_JOIN_ENABLED;

    public static String STATS_INVENTORY_NAME;
    public static int STATS_INVENTORY_SIZE;

    public static boolean STATTRAK_ENABLED;
    public static String STATTRAK_KILLS_FORMAT;
    public static String STATTRAK_KILL_STRING;
    public static List<String> STATTRAK_TRACKING_ITEMS;

    public static boolean STRENGTH_NERF_ENABLED;
    public static int STRENGTH_1_NERF_PERCENTAGE;
    public static int STRENGTH_2_NERF_PERCENTAGE;
    public static boolean REMOVE_STRENGTH_ON_END_ENTER;

    public static String SUBCLAIMS_SIGN_TITLE;
    public static String SUBCLAIMS_LEADER_ONLY;
    public static String SUBCLAIMS_CO_LEADERS_ONLY;
    public static String SUBCLAIMS_CAPTAINS_ONLY;

    public static String UNREPAIRABLE_ITEM_LORE;

    public static boolean VANISH_ON_JOIN_ENABLED;

    public static boolean CHAIN_ARMOR_RECIPE_ENABLED;
    public static Material CHAIN_ARMOR_RECIPE_MATERIAL;

    public static boolean ARCHER_ACTIVATED;
    public static boolean BARD_ACTIVATED;
    public static boolean MINER_ACTIVATED;
    public static boolean ROGUE_ACTIVATED;
    public static boolean MAGE_ACTIVATED;
    public static boolean BOMBER_ACTIVATED;

    public static int ARCHER_WARMUP;
    public static int BARD_WARMUP;
    public static int MINER_WARMUP;
    public static int ROGUE_WARMUP;
    public static int MAGE_WARMUP;
    public static int BOMBER_WARMUP;

    public static boolean ARCHER_TAG_CAN_TAG_OTHER_ARCHERS;
    public static boolean ARCHER_TAG_REQUIRE_FULL_FORCE;
    public static int ARCHER_TAG_DURATION;
    public static double ARCHER_TAG_DAMAGE_MULTIPLIER;

    public static int BARD_MAX_ENERGY;
    public static boolean BARD_COMBAT_TAG_ON_CLICKABLE_ITEM;

    public static Material ROGUE_BACKSTAB_ITEM;
    public static int ROGUE_BACKSTAB_DAMAGE;
    public static int ROGUE_BACKSTAB_COOLDOWN;
    public static boolean ROGUE_BACKSTAB_EFFECTS_ENABLED;

    public static int MAGE_MAX_ENERGY;
    public static boolean MAGE_COMBAT_TAG_ON_CLICKABLE_ITEM;

    public static boolean BOMBER_COMBAT_TAG_ON_TNT_USE;

    public static Map<Environment, Boolean> ABILITIES_DENY_USAGE_WORLD;
    public static Map<Environment, Boolean> ABILITIES_DENY_USAGE_DISTANCE_ENABLED;
    public static Map<Environment, Integer> ABILITIES_DENY_USAGE_DISTANCE_BLOCKS;

    public static boolean ABILITIES_GLOBAL_COOLDOWN_ENABLED;
    public static int ABILITIES_GLOBAL_COOLDOWN_DURATION;

    public static boolean TAB_ENABLED;
    public static String TAB_HEADER;
    public static String TAB_FOOTER;

    public static String SCOREBOARD_TITLE;

    public static String SCOREBOARD_LINE_COLOR;
    public static boolean SCOREBOARD_LINE_INVISIBLE;
    public static boolean SCOREBOARD_LINE_AFTER_EVERY_SECTION;

    public static boolean SCOREBOARD_FOOTER_ENABLED;
    public static String SCOREBOARD_FOOTER_PLACEHOLDER;

    public static String CLAIM_PLACEHOLDER;
    public static String FACTION_RALLY_TITLE_PLACEHOLDER;
    public static String FACTION_RALLY_WORLD_PLACEHOLDER;
    public static String FACTION_RALLY_LOCATION_PLACEHOLDER;
    public static String COMBAT_TAG_PLACEHOLDER;
    public static String ENDERPEARL_PLACEHOLDER;
    public static String PVP_PROTECTION_PLACEHOLDER;
    public static String SOTW_PLACEHOLDER;
    public static String SOTW_ENABLE_PLACEHOLDER;
    public static String EOTW_START_PLACEHOLDER;
    public static String CONQUEST_PLACEHOLDER;
    public static String CONQUEST_FACTION_FORMAT;
    public static String KOTH_PLACEHOLDER;
    public static String DTC_PLACEHOLDER;
    public static String ENDER_DRAGON_PLACEHOLDER;
    public static String PVPCLASS_WARMUP_PLACEHOLDER;
    public static String PVPCLASS_ACTIVE_PLACEHOLDER;
    public static String ARCHER_TAG_PLACEHOLDER;
    public static String BARD_ENERGY_PLACEHOLDER;
    public static String MAGE_ENERGY_PLACEHOLDER;
    public static String SALE_PLACEHOLDER;
    public static String KEYSALE_PLACEHOLDER;
    public static String COOLDOWN_PLACEHOLDER;
    public static String TELEPORT_PLACEHOLDER;
    public static String LOGOUT_PLACEHOLDER;
    public static String NORMAL_GOLDEN_APPLE_PLACEHOLDER;
    public static String ENCHANTED_GOLDEN_APPLE_PLACEHOLDER;
    public static String PURGE_START_PLACEHOLDER;
    public static String PURGE_PLACEHOLDER;
    public static String STAFFMODE_PLACEHOLDER;
    public static String VANISH_PLACEHOLDER;
    public static String VISIBILITY_PLACEHOLDER;
    public static String CHATMODE_PLACEHOLDER;
    public static String GAMEMODE_PLACEHOLDER;
    public static String ONLINE_PLACEHOLDER;
    public static String MINER_DIAMOND_COUNT_PLACEHOLDER;
    public static String KITMAP_STATS_TITLE_PLACEHOLDER;
    public static String KITMAP_STATS_KILLS_PLACEHOLDER;
    public static String KITMAP_STATS_DEATHS_PLACEHOLDER;
    public static String KITMAP_STATS_BALANCE_PLACEHOLDER;
    public static String KITMAP_STATS_KILLSTREAK_PLACEHOLDER;
    public static String HOME_PLACEHOLDER;
    public static String STUCK_PLACEHOLDER;
    public static String REBOOT_PLACEHOLDER;

    public static String ABILITIES_TITLE_PLACEHOLDER;
    public static String ABILITIES_GLOBAL_COOLDOWN_PLACEHOLDER;
    public static String ABILITIES_ABILITY_COOLDOWN_PLACEHOLDER;

    public static String KING_TITLE_PLACEHOLDER;
    public static String KING_KING_PLACEHOLDER;
    public static String KING_TIME_LASTED_PLACEHOLDER;
    public static String KING_WORLD_PLACEHOLDER;
    public static String KING_LOCATION_PLACEHOLDER;

    public static String FACTION_FOCUS_TITLE_PLACEHOLDER;
    public static String FACTION_FOCUS_DTR_PLACEHOLDER;
    public static String FACTION_FOCUS_HQ_PLACEHOLDER;
    public static String FACTION_FOCUS_ONLINE_PLACEHOLDER;

    public static String STAFF_SB_VANISHED;
    public static String STAFF_SB_VISIBLE;
    public static String STAFF_SB_STAFFCHAT;
    public static String STAFF_SB_GLOBAL;
    public static String STAFF_SB_SURVIVAL;
    public static String STAFF_SB_CREATIVE;

    public Config() {
        ConfigFile config = Lazarus.getInstance().getConfig();
        ConfigFile scoreboard = Lazarus.getInstance().getScoreboardFile();
        ConfigFile tab = Lazarus.getInstance().getTabFile();
        ConfigFile classes = Lazarus.getInstance().getClassesFile();
        ConfigFile abilitiesFile = Lazarus.getInstance().getAbilitiesFile();
        ConfigFile utilities = Lazarus.getInstance().getUtilitiesFile();

        TIMEZONE = TimeZone.getTimeZone(config.getString("TIMEZONE"));
        DATE_FORMAT = config.getString("DATE_FORMAT");

        SEND_TO_HUB_ON_KICK_ENABLED = config.getBoolean("SEND_TO_HUB_ON_KICK.ENABLED");
        SEND_TO_HUB_ON_KICK_HUBS = config.getStringList("SEND_TO_HUB_ON_KICK.HUBS");

        JOIN_WELCOME_MESSAGE_ENABLED = config.getBoolean("JOIN_WELCOME_MESSAGE_ENABLED");
        COMMANDS_COOLDOWN = config.getInt("COMMANDS_COOLDOWN");

        DEATHBAN_DIR = new File(Lazarus.getInstance().getDataFolder(), "deathbans");
        RESTORE_DIR = new File(Lazarus.getInstance().getDataFolder(), "restore");
        FACTIONS_DIR = new File(Lazarus.getInstance().getDataFolder(), "factions");
        GAMES_DIR = new File(Lazarus.getInstance().getDataFolder(), "games");
        USERDATA_DIR = new File(Lazarus.getInstance().getDataFolder(), "userdata");
        HOLOGRAMS_DIR = new File(Lazarus.getInstance().getDataFolder(), "holograms");
        LEADERBOARDS_DIR = new File(Lazarus.getInstance().getDataFolder(), "leaderboards");

        NETHER_PORTAL_TRANSLATION_ENABLED = config.getBoolean("NETHER_PORTAL_POSITION_TRANSLATION.ENABLED");
        NETHER_PORTAL_TRANSLATION_VALUE = config.getInt("NETHER_PORTAL_POSITION_TRANSLATION.VALUE");

        BORDER_SIZE = new EnumMap<>(Environment.class);
        BORDER_SIZE.put(Environment.NORMAL, config.getInt("BORDER_SIZE.OVERWORLD"));
        BORDER_SIZE.put(Environment.NETHER, config.getInt("BORDER_SIZE.NETHER"));
        BORDER_SIZE.put(Environment.THE_END, config.getInt("BORDER_SIZE.END"));

        WORLD_SPAWNS = new EnumMap<>(Environment.class);
        String worldSpawn = utilities.getString("WORLD_SPAWN");
        WORLD_SPAWNS.put(Environment.NORMAL, worldSpawn.isEmpty() ? null : LocationUtils.stringToLocation(worldSpawn));
        String netherSpawn = utilities.getString("NETHER_SPAWN");
        WORLD_SPAWNS.put(Environment.NETHER, netherSpawn.isEmpty() ? null : LocationUtils.stringToLocation(netherSpawn));
        String endSpawn = utilities.getString("END_SPAWN");
        WORLD_SPAWNS.put(Environment.THE_END, endSpawn.isEmpty() ? null : LocationUtils.stringToLocation(endSpawn));

        WORLD_EXITS = new EnumMap<>(Environment.class);
        String netherExit = utilities.getString("NETHER_EXIT");
        WORLD_EXITS.put(Environment.NETHER, netherExit.isEmpty() ? null : LocationUtils.stringToLocation(netherExit));
        String endExit = utilities.getString("END_EXIT");
        WORLD_EXITS.put(Environment.THE_END, endExit.isEmpty() ? null : LocationUtils.stringToLocation(endExit));

        MOB_LIMIT_PER_CHUNK = config.getInt("MOB_LIMIT_PER_CHUNK");
        MOBS_SPAWN_ONLY_FROM_SPAWNERS = config.getBoolean("MOBS_SPAWN_ONLY_FROM_SPAWNERS");
        DISABLE_EXPLOSIONS_BLOCK_DAMAGE = config.getBoolean("DISABLE_EXPLOSIONS_BLOCK_DAMAGE");
        DISABLE_BED_BOMBING = config.getBoolean("DISABLE_BED_BOMBING");
        DISABLE_CREEPER_PLAYER_TARGETING = config.getBoolean("DISABLE_CREEPER_PLAYER_TARGETING");
        SPAWN_MOBS_IN_WARZONE = config.getBoolean("SPAWN_MOBS_IN_WARZONE");
        LIGHTNING_EFFECT_ON_DEATH = config.getBoolean("LIGHTNING_EFFECT_ON_DEATH");
        REMOVE_EMPTY_BOTTLE_ON_POTION_USE = config.getBoolean("REMOVE_EMPTY_BOTTLE_ON_POTION_USE");
        ENDERPEARL_GLITCH_FIX_ENABLED = config.getBoolean("ENDERPEARL_GLITCH_FIX_ENABLED");

        DENY_SPAWNER_BREAK_IN_END = config.getBoolean("DENY_SPAWNER_BREAK.IN_END");
        DENY_SPAWNER_BREAK_IN_NETHER = config.getBoolean("DENY_SPAWNER_BREAK.IN_NETHER");

        DENY_SPAWNER_PLACE_IN_END = config.getBoolean("DENY_SPAWNER_PLACE.IN_END");
        DENY_SPAWNER_PLACE_IN_NETHER = config.getBoolean("DENY_SPAWNER_PLACE.IN_END");

        List<Material> disabledBlockPlacementList = config.getStringList("DISABLED_BLOCK_PLACEMENT").stream()
            .map(ItemUtils::parseItem).filter(Objects::nonNull)
            .map(ItemStack::getType).collect(Collectors.toList());

        DISABLED_BLOCK_PLACEMENT = disabledBlockPlacementList.isEmpty()
            ? EnumSet.noneOf(Material.class)
            : EnumSet.copyOf(disabledBlockPlacementList);

        DEFAULT_CHAT_DELAY = config.getInt("DEFAULT_CHAT_DELAY");

        CHAT_FORMAT_ENABLED = config.getBoolean("CHAT_FORMAT.ENABLED");
        CHAT_FORMAT_USE_PLAYER_DISPLAY_NAME = config.getBoolean("CHAT_FORMAT.USE_PLAYER_DISPLAY_NAME");
        CHAT_FORMAT = config.getString("CHAT_FORMAT.FORMAT");
        CHAT_FORMAT_WITH_FACTION = config.getString("CHAT_FORMAT.FORMAT_WITH_FACTION");

        LUNAR_CLIENT_API_ENABLED = config.getBoolean("LUNAR_CLIENT_API_ENABLED");
        LUNAR_CLIENT_API_FORCED_WAYPOINTS_ENABLED = config.getBoolean("FORCED_WAYPOINTS_ENABLED");
        LUNAR_CLIENT_API_STAFF_MODULES_ENABLED = config.getBoolean("STAFF_MODULES_ENABLED");
        LUNAR_CLIENT_API_TEAM_VIEW_ENABLED = config.getBoolean("TEAM_VIEW_ENABLED");
        LUNAR_CLIENT_API_COOLDOWNS_ENABLED = config.getBoolean("COOLDOWNS_ENABLED");
        LUNAR_CLIENT_API_NAMETAGS_ENABLED = config.getBoolean("NAMETAGS.ENABLED");
        LUNAR_CLIENT_API_NAMETAGS_FACTION = config.getString("NAMETAGS.FACTION");
        LUNAR_CLIENT_FORCE_DISABLED_MODS = config.getStringList("FORCE_DISABLED_MODS");

        DISABLED_LAZARUS_COMMANDS = config.getStringList("DISABLED_LAZARUS_COMMANDS")
            .stream().map(String::toLowerCase).collect(Collectors.toSet());

        DISABLED_FACTION_SUBCOMMANDS = config.getStringList("DISABLED_FACTION_SUBCOMMANDS")
            .stream().map(String::toLowerCase).collect(Collectors.toSet());

        ONLINE_RANK_ANNOUNCER_ENABLED = config.getBoolean("ONLINE_RANK_ANNOUNCER_ENABLED");
        ONLINE_RANK_ANNOUNCER_INTERVAL = config.getInt("ONLINE_RANK_ANNOUNCER_INTERVAL");

        REDUCED_DURABILITY_LOSS_ENABLED = config.getBoolean("REDUCED_DURABILITY_LOSS.ENABLED");
        REDUCED_DURABILITY_LOSS_PERCENTAGE = config.getInt("REDUCED_DURABILITY_LOSS.REDUCED_PERCENTAGE");

        List<Material> reducedDurabilityItemList = config.getStringList("REDUCED_DURABILITY_MATERIALS").stream()
            .map(ItemUtils::parseItem).filter(Objects::nonNull)
            .map(ItemStack::getType).collect(Collectors.toList());

        REDUCED_DURABILITY_LOSS_MATERIALS = reducedDurabilityItemList.isEmpty()
            ? EnumSet.noneOf(Material.class)
            : EnumSet.copyOf(reducedDurabilityItemList);

        FACTIONS_AUTO_SAVE = config.getInt("FACTIONS_AUTO_SAVE");
        SHOW_FACTION_INFO_ON_JOIN = config.getBoolean("SHOW_FACTION_INFO_ON_JOIN");

        FACTION_DTR_PER_PLAYER = config.getDouble("FACTION_DTR.DTR_PER_PLAYER");
        FACTION_MAX_DTR = config.getDouble("FACTION_DTR.MAX_DTR");
        FACTION_MIN_DTR = config.getDouble("FACTION_DTR.MIN_DTR");
        FACTION_DTR_REGEN_PER_MINUTE = config.getDouble("FACTION_DTR.REGEN_PER_MINUTE");
        FACTION_DTR_SOLO_FACTION_DTR = config.getDouble("FACTION_DTR.SOLO_FACTION_DTR");
        FACTION_DTR_FREEZE_DURATION = config.getInt("FACTION_DTR.FREEZE_DURATION");
        FACTION_DTR_CHARACTERS_FULL_DTR = config.getString("FACTION_DTR.DTR_CHARACTERS.FULL_DTR");
        FACTION_DTR_CHARACTERS_REGENERATING = config.getString("FACTION_DTR.DTR_CHARACTERS.REGENERATING");
        FACTION_DTR_CHARACTERS_FROZEN = config.getString("FACTION_DTR.DTR_CHARACTERS.FROZEN");

        FACTION_DTR_DEATH_LOSS = new EnumMap<>(Environment.class);
        FACTION_DTR_DEATH_LOSS.put(Environment.NORMAL, config.getDouble("FACTION_DTR.DTR_DEATH_LOSS.OVERWORLD"));
        FACTION_DTR_DEATH_LOSS.put(Environment.NETHER, config.getDouble("FACTION_DTR.DTR_DEATH_LOSS.NETHER"));
        FACTION_DTR_DEATH_LOSS.put(Environment.THE_END, config.getDouble("FACTION_DTR.DTR_DEATH_LOSS.END"));

        FACTION_PLAYER_LIMIT = config.getInt("FACTION_PLAYER.FACTION_LIMIT");
        FACTION_JOIN_WHILE_FROZEN = config.getBoolean("FACTION_PLAYER.JOIN_WHILE_FROZEN");
        FACTION_LEAVE_WHILE_FROZEN = config.getBoolean("FACTION_PLAYER.LEAVE_WHILE_FROZEN");
        FACTION_DISBAND_WHILE_FROZEN = config.getBoolean("FACTION_PLAYER.DISBAND_WHILE_FROZEN");
        FACTION_UNCLAIM_WHILE_FROZEN = config.getBoolean("FACTION_PLAYER.UNCLAIM_WHILE_FROZEN");
        FACTION_LEAVE_WHILE_IN_OWN_CLAIM = config.getBoolean("FACTION_PLAYER.LEAVE_WHILE_IN_OWN_CLAIM");
        FACTION_PLAYERS_TAKE_OWN_DAMAGE = config.getBoolean("FACTION_PLAYER.PLAYERS_TAKE_OWN_DAMAGE");
        FACTION_ALLY_FRIENDLY_FIRE = config.getBoolean("FACTION_PLAYER.ALLY_FRIENDLY_FIRE");
        FACTION_WARZONE_BREAK_AFTER_OVERWORLD = config.getInt("FACTION_PLAYER.WARZONE_BREAK_AFTER.OVERWORLD");
        FACTION_WARZONE_BREAK_AFTER_NETHER = config.getInt("FACTION_PLAYER.WARZONE_BREAK_AFTER.NETHER");

        FACTION_PVP_CLASS_LIMIT = new HashMap<>();
        FACTION_PVP_CLASS_LIMIT.put("Archer", config.getInt("FACTION_PVP_CLASS_LIMIT.ARCHER"));
        FACTION_PVP_CLASS_LIMIT.put("Bard", config.getInt("FACTION_PVP_CLASS_LIMIT.BARD"));
        FACTION_PVP_CLASS_LIMIT.put("Miner", config.getInt("FACTION_PVP_CLASS_LIMIT.MINER"));
        FACTION_PVP_CLASS_LIMIT.put("Rogue", config.getInt("FACTION_PVP_CLASS_LIMIT.ROGUE"));
        FACTION_PVP_CLASS_LIMIT.put("Mage", config.getInt("FACTION_PVP_CLASS_LIMIT.MAGE"));
        FACTION_PVP_CLASS_LIMIT.put("Bomber", config.getInt("FACTION_PVP_CLASS_LIMIT.BOMBER"));

        FACTION_MAX_ALLIES = config.getInt("FACTION_MAX_ALLIES");

        FACTION_MAP_RADIUS = config.getInt("FACTION_MAP_RADIUS");

        FACTION_CLAIMS_MUST_BE_CONNECTED = config.getBoolean("FACTION_CLAIM.MUST_BE_CONNECTED");
        FACTION_MAX_CLAIMS = config.getInt("FACTION_CLAIM.MAX_CLAIMS");
        FACTION_CLAIMS_PER_PLAYER = config.getInt("FACTION_CLAIM.CLAIMS_PER_PLAYER");
        FACTION_MIN_CLAIM_SIZE = config.getInt("FACTION_CLAIM.MIN_CLAIM_SIZE");
        FACTION_MAX_CLAIM_SIZE = config.getInt("FACTION_CLAIM.MAX_CLAIM_SIZE");
        FACTION_CLAIM_BUFFER = config.getInt("FACTION_CLAIM.CLAIM_BUFFER");
        FACTION_CLAIM_BUFFER_IGNORE_ROADS = config.getBoolean("FACTION_CLAIM.CLAIM_BUFFER_IGNORE_ROADS");
        FACTION_CLAIM_PRICE_PER_BLOCK = config.getDouble("FACTION_CLAIM.CLAIM_PRICE_PER_BLOCK");
        FACTION_UNCLAIM_PRICE_MULTIPLIER = config.getDouble("FACTION_CLAIM.UNCLAIM_PRICE_MULTIPLIER");
        FACTION_CLAIM_LIQUID_FLOW_FROM_WILDERNESS_TO_CLAIMS = config.getBoolean("FACTION_CLAIM.LIQUID_FLOW_FROM_WILDERNESS_TO_CLAIMS");

        WARZONE_RADIUS = new EnumMap<>(Environment.class);
        WARZONE_RADIUS.put(Environment.NORMAL, config.getInt("WARZONE_RADIUS.OVERWORLD"));
        WARZONE_RADIUS.put(Environment.NETHER, config.getInt("WARZONE_RADIUS.NETHER"));
        WARZONE_RADIUS.put(Environment.THE_END, config.getInt("WARZONE_RADIUS.END"));

        FACTION_TOP_ALLOW_NEGATIVE_POINTS = config.getBoolean("FACTION_TOP.ALLOW_NEGATIVE_POINTS");
        FACTION_TOP_KILL = config.getInt("FACTION_TOP.KILL");
        FACTION_TOP_DEATH = config.getInt("FACTION_TOP.DEATH");
        FACTION_TOP_KOTH_CAP = config.getInt("FACTION_TOP.KOTH_CAP");
        FACTION_TOP_CONQUEST_CAP = config.getInt("FACTION_TOP.CONQUEST_CAP");
        FACTION_TOP_DTC_DESTROY = config.getInt("FACTION_TOP.DTC_DESTROY");

        FACTION_RALLY_EXPIRE_AFTER = config.getInt("FACTION_RALLY.EXPIRE_AFTER");
        FACTION_RALLY_INCLUDE_Y_COORDINATE = config.getBoolean("FACTION_RALLY.INCLUDE_Y_COORDINATE");

        WILDERNESS_NAME = config.getString("SYSTEM_FACTION_NAMES.WILDERNESS");
        WARZONE_NAME = config.getString("SYSTEM_FACTION_NAMES.WARZONE");

        HOME_WARMUPS = new EnumMap<>(Environment.class);
        HOME_WARMUPS.put(Environment.NORMAL, config.getInt("HOME_WARMUP.OVERWORLD"));
        HOME_WARMUPS.put(Environment.NETHER, config.getInt("HOME_WARMUP.NETHER"));
        HOME_WARMUPS.put(Environment.THE_END, config.getInt("HOME_WARMUP.END"));

        HOME_WARMUP_ENEMY_TERRITORY = config.getInt("HOME_WARMUP.ENEMY_TERRITORY");
        DENY_HOME_TELEPORT_FROM_ENEMY_TERRITORY = config.getBoolean("DENY_HOME_TELEPORT_FROM_ENEMY_TERRITORY");

        FACTION_STUCK_WARMUP = config.getInt("FACTION_STUCK.WARMUP");

        int movementRadius = config.getInt("FACTION_STUCK.ALLOWED_MOVEMENT_RADIUS");
        FACTION_STUCK_ALLOWED_MOVEMENT_RADIUS = movementRadius * movementRadius;

        FACTION_CREATE_COOLDOWN = config.getInt("FACTION_CREATE_COOLDOWN");
        FACTION_RENAME_COOLDOWN = config.getInt("FACTION_RENAME_COOLDOWN");
        FACTION_OPEN_CHANGE_COOLDOWN = config.getInt("FACTION_OPEN_CHANGE_COOLDOWN");

        FACTION_NAME_MINIMUM_LENGTH = config.getInt("FACTION_NAME.MINIMUM_LENGTH");
        FACTION_NAME_MAXIMUM_LENGTH = config.getInt("FACTION_NAME.MAXIMUM_LENGTH");
        FACTION_NAME_DISALLOWED_NAMES = config.getStringList("FACTION_NAME.DISALLOWED_NAMES")
            .stream().map(String::toLowerCase).collect(Collectors.toList());

        TEAMMATE_COLOR = config.getString("RELATION_COLORS.TEAMMATE_COLOR");
        ALLY_COLOR = config.getString("RELATION_COLORS.ALLY_COLOR");
        ENEMY_COLOR = config.getString("RELATION_COLORS.ENEMY_COLOR");
        ARCHER_TAGGED_COLOR = config.getString("RELATION_COLORS.ARCHER_TAGGED_COLOR");
        FOCUSED_COLOR = config.getString("RELATION_COLORS.FOCUSED_COLOR");
        SOTW_COLOR = config.getString("RELATION_COLORS.SOTW_COLOR");
        STAFF_MODE_COLOR = config.getString("RELATION_COLORS.STAFF_MODE_COLOR");

        CONQUEST_CAP_TIME = config.getInt("CONQUEST.CAP_TIME");
        CONQUEST_POINTS_PER_CAP = config.getInt("CONQUEST.POINTS_PER_CAP");
        CONQUEST_DEATH_PENALTY = config.getInt("CONQUEST.DEATH_PENALTY");
        CONQUEST_POINTS_TO_WIN = config.getInt("CONQUEST.POINTS_TO_WIN");
        CONQUEST_ALLOW_NEGATIVE_POINTS = config.getBoolean("CONQUEST.ALLOW_NEGATIVE_POINTS");

        CONQUEST_CAPPED_SIGN_ENABLED = config.getBoolean("CONQUEST_CAPPED_SIGN.ENABLED");
        CONQUEST_CAPPED_SIGN_NAME = config.getString("CONQUEST_CAPPED_SIGN.NAME");
        CONQUEST_CAPPED_SIGN_LORE = config.getStringList("CONQUEST_CAPPED_SIGN.LORE");

        DTC_CORE_BREAKS = config.getInt("DTC.CORE_BREAKS");
        DTC_BREAK_MESSAGE_INTERVAL = config.getInt("DTC.BREAK_MESSAGE_INTERVAL");

        KOTH_DEFAULT_CAP_TIME = config.getInt("KOTH.DEFAULT_CAP_TIME");
        KOTH_CAPPING_MESSAGE_INTERVAL = config.getInt("KOTH.CAPPING_MESSAGE_INTERVAL");
        KOTH_NOBODY_CAPPING_MESSAGE_INTERVAL = config.getInt("KOTH.NOBODY_CAPPING_MESSAGE_INTERVAL");
        KOTH_ANTI_SPAM_MESSAGE_DELAY = config.getInt("KOTH.ANTI_SPAM_MESSAGE_DELAY") * 1000L;
        KOTH_MAX_RUNNING_KOTHS_AT_THE_SAME_TIME = config.getInt("KOTH.MAX_RUNNING_KOTHS_AT_THE_SAME_TIME");

        KOTH_CAPPED_SIGN_ENABLED = config.getBoolean("KOTH_CAPPED_SIGN.ENABLED");
        KOTH_CAPPED_SIGN_NAME = config.getString("KOTH_CAPPED_SIGN.NAME");
        KOTH_CAPPED_SIGN_LORE = config.getStringList("KOTH_CAPPED_SIGN.LORE");

        KILL_THE_KING_DENY_ITEM_DROP = config.getBoolean("KILL_THE_KING.DENY_ITEM_DROP");
        KILL_THE_KING_AUTO_REWARD_ENABLED = config.getBoolean("KILL_THE_KING.AUTO_REWARD_ENABLED");
        KILL_THE_KING_REWARD = config.getStringList("KILL_THE_KING.REWARD");

        MOUNTAIN_RESPAWN_INTERVAL = config.getInt("MOUNTAIN_RESPAWN_INTERVAL");
        MOUNTAIN_MESSAGE_INTERVAL = config.getInt("MOUNTAIN_MESSAGE_INTERVAL");

        ENDER_DRAGON_NAME = config.getString("ENDER_DRAGON.NAME");
        ENDER_DRAGON_HEALTH = config.getInt("ENDER_DRAGON.HEALTH");

        LOOT_DEFAULT_REWARD_AMOUNT = config.getInt("LOOT.DEFAULT_REWARD_AMOUNT");
        LOOT_RANDOMIZE_REWARDS = config.getBoolean("LOOT.RANDOMIZE_REWARDS");
        LOOT_INVENTORY_NAME = config.getString("LOOT.INVENTORY_NAME");
        LOOT_INVENTORY_SIZE = config.getInt("LOOT.INVENTORY_SIZE");

        SCHEDULE_LIST_EVENT_AMOUNT = config.getInt("SCHEDULE.LIST_EVENT_AMOUNT");

        MAX_BALANCE = config.getInt("MAX_BALANCE");
        DEFAULT_BALANCE_PLAYER = config.getInt("DEFAULT_BALANCE.PLAYER");
        DEFAULT_BALANCE_FACTION = config.getInt("DEFAULT_BALANCE.FACTION");

        BUY_SIGN_LINES = config.getStringList("BUY_SIGN.LINES");
        SELL_SIGN_LINES = config.getStringList("SELL_SIGN.LINES");

        KITMAP_MODE_ENABLED = config.getBoolean("KITMAP_MODE.ENABLED");
        KITMAP_MODE_DISABLE_DEATHBAN = config.getBoolean("KITMAP_MODE.DISABLE_DEATHBAN");
        KITMAP_MODE_DISABLE_DTR_LOSS = config.getBoolean("KITMAP_MODE.DISABLE_DTR_LOSS");
        KITMAP_DISABLE_ITEM_DROP_IN_SAFEZONE = config.getBoolean("KITMAP_MODE.DISABLE_ITEM_DROP_IN_SAFEZONE");
        KITMAP_PVP_TIMER_ENABLED = config.getBoolean("KITMAP_MODE.PVP_TIMER_ENABLED");
        KITMAP_SPAWN_TELEPORT_DELAY = config.getInt("KITMAP_MODE.SPAWN_TELEPORT_DELAY");

        KITMAP_CLEAR_ITEMS_ENABLED = config.getBoolean("KITMAP_CLEAR_ITEMS.ENABLED");
        KITMAP_CLEAR_ITEMS_INTERVAL = config.getInt("KITMAP_CLEAR_ITEMS.INTERVAL");

        KITMAP_KILL_REWARD_ENABLED = config.getBoolean("KITMAP_KILL_REWARD.ENABLED");
        KITMAP_KILL_REWARD = config.getStringList("KITMAP_KILL_REWARD.COMMANDS");

        KITMAP_KILLSTREAK_ENABLED = config.getBoolean("KITMAP_KILLSTREAK.ENABLED");

        KITS_FIRST_JOIN_KIT_ENABLED = config.getBoolean("KITS.FIRST_JOIN_KIT_ENABLED");
        KITS_FIRST_JOIN_KIT = config.getString("KITS.FIRST_JOIN_KIT");
        KIT_SIGN_FORMAT = config.getStringList("KITS.KIT_SIGN_FORMAT");

        BLOCKED_COMMANDS_DISABLE_COLON = config.getBoolean("BLOCKED_COMMANDS.DISABLE_COLON");
        BLOCKED_COMMANDS = config.getStringList("BLOCKED_COMMANDS.COMMANDS");

        BOTTLE_DROP_ON_DEATH = config.getBoolean("BOTTLE.DROP_ON_DEATH");
        BOTTLE_NAME = config.getString("BOTTLE.NAME");
        BOTTLE_LORE = config.getStringList("BOTTLE.LORE");

        BREWING_SPEED_MULTIPLIER = config.getInt("BREWING_SPEED_MULTIPLIER");

        COBWEB_REMOVER_ENABLED = config.getBoolean("COBWEB_REMOVER.ENABLED");
        COBWEB_REMOVER_REMOVE_AFTER = config.getInt("COBWEB_REMOVER.REMOVE_AFTER");

        COMBAT_LOGGER_TYPE = CombatLoggerType.getByName(config.getString("COMBAT_LOGGER.TYPE"));
        COMBAT_LOGGER_NAME_FORMAT = config.getString("COMBAT_LOGGER.NAME_FORMAT");
        COMBAT_LOGGER_TIME = config.getInt("COMBAT_LOGGER.TIME");
        COMBAT_LOGGER_VILLAGER_PROFESSION = config.getInt("COMBAT_LOGGER.VILLAGER_PROFESSION", 2);

        COMBAT_TAG_DURATION = config.getInt("COMBAT_TAG.DURATION");
        COMBAT_TAG_DISABLE_END_ENTRY = config.getBoolean("COMBAT_TAG.DISABLE_END_ENTRY");
        COMBAT_TAG_DISABLE_ENDERCHESTS = config.getBoolean("COMBAT_TAG.DISABLE_ENDERCHESTS");
        COMBAT_TAG_PLACE_BLOCKS = config.getBoolean("COMBAT_TAG.PLACE_BLOCKS");
        COMBAT_TAG_BREAK_BLOCKS = config.getBoolean("COMBAT_TAG.BREAK_BLOCKS");
        COMBAT_TAG_DISABLED_COMMANDS = config.getStringList("COMBAT_TAG.DISABLED_COMMANDS");

        CROWBAR_ITEM = ItemUtils.parseItem(config.getString("CROWBAR.MATERIAL_ID"));
        CROWBAR_NAME = config.getString("CROWBAR.NAME");
        CROWBAR_LORE = config.getStringList("CROWBAR.LORE");
        CROWBAR_SPAWNER_USES = config.getInt("CROWBAR.SPAWNER_USES");
        CROWBAR_PORTAL_USES = config.getInt("CROWBAR.PORTAL_USES");
        CROWBAR_SPAWNER_NAME_COLOR = config.getString("CROWBAR.SPAWNER_NAME_COLOR");
        CROWBAR_DISABLE_IN_WARZONE = config.getBoolean("CROWBAR.DISABLE_IN_WARZONE");

        INVENTORY_RESTORE_ENABLED = config.getBoolean("INVENTORY_RESTORE.ENABLED");
        INVENTORY_RESTORE_FILE_CACHE = config.getInt("INVENTORY_RESTORE.FILE_CACHE");

        DEATHBAN_ENABLED = config.getBoolean("DEATHBAN.ENABLED");
        DEATHBAN_DEFAULT_BAN_TIME = config.getInt("DEATHBAN.DEFAULT_BAN_TIME");
        DEFAULT_LIVES = config.getInt("DEFAULT_LIVES");

        PLAYER_SETTINGS_INVENTORY_NAME = config.getString("PLAYER_SETTINGS.INVENTORY_NAME");
        PLAYER_SETTINGS_INVENTORY_SIZE = config.getInt("PLAYER_SETTINGS.INVENTORY_SIZE");

        DEATH_SIGN_ENABLED = config.getBoolean("DEATH_SIGN.ENABLED");
        DEATH_SIGN_NAME = config.getString("DEATH_SIGN.NAME");
        DEATH_SIGN_LORE = config.getStringList("DEATH_SIGN.LORE");

        ENDER_PEARL_COOLDOWN_ENABLED = config.getBoolean("ENDER_PEARL.COOLDOWN_ENABLED");
        ENDER_PEARL_COOLDOWN_TIME = config.getInt("ENDER_PEARL.COOLDOWN_TIME");
        ENDER_PEARL_REFUND_ENDER_PEARL_ON_CANCEL = config.getBoolean("ENDER_PEARL.REFUND_ENDER_PEARL_ON_CANCEL");

        EOTW_CLEAR_DEATHBANS_ON_START = config.getBoolean("EOTW_TIMER.CLEAR_DEATHBANS_ON_START");

        EXP_AMPLIFIER_LOOTING = config.getInt("EXPERIENCE_AMPLIFIER.LOOTING");
        EXP_AMPLIFIER_FORTUNE = config.getInt("EXPERIENCE_AMPLIFIER.FORTUNE");

        FOUND_ORE_ENABLED = config.getBoolean("FOUND_ORE.ENABLED");

        FREEZE_MESSAGE_INTERVAL = config.getInt("FREEZE.MESSAGE_INTERVAL");
        FREEZE_DISABLED_COMMANDS = config.getStringList("FREEZE.DISABLED_COMMANDS");

        FURNACE_SPEED_MULTIPLIER = config.getInt("FURNACE_SPEED_MULTIPLIER");

        GLISTERING_MELON_CRAFTING = config.getBoolean("GLISTERING_MELON.EASY_CRAFTING");

        NORMAL_GOLDEN_APPLE_ON_SCOREBOARD = config.getBoolean("GOLDEN_APPLE.NORMAL.ON_SCOREBOARD");
        NORMAL_GOLDEN_APPLE_COOLDOWN = config.getInt("GOLDEN_APPLE.NORMAL.COOLDOWN");
        ENCHANTED_GOLDEN_APPLE_ON_SCOREBOARD = config.getBoolean("GOLDEN_APPLE.ENCHANTED.ON_SCOREBOARD");
        ENCHANTED_GOLDEN_APPLE_COOLDOWN = config.getInt("GOLDEN_APPLE.ENCHANTED.COOLDOWN");

        HEAD_DROP_ENABLED = config.getBoolean("HEAD_DROP.ENABLED");
        HEAD_DROP_NAME_FORMAT = config.getString("HEAD_DROP.SKULL_NAME_FORMAT");

        LFF_COMMAND_COOLDOWN = config.getInt("LFF_COMMAND.COOLDOWN");

        LIST_NO_STAFF_ONLINE = config.getString("LIST_COMMAND.NO_STAFF_ONLINE");
        LIST_SHOW_VANISHED_STAFF = config.getBoolean("LIST_COMMAND.SHOW_VANISHED_STAFF");

        LOGOUT_DELAY = config.getInt("LOGOUT_DELAY");

        MAPKIT_INVENTORY_NAME = config.getString("MAPKIT.INVENTORY_NAME");
        MAPKIT_INVENTORY_SIZE = config.getInt("MAPKIT.INVENTORY_SIZE");

        MINECART_ELEVATOR_ENABLED = config.getBoolean("MINECART_ELEVATOR.ENABLED");
        MINECART_ELEVATOR_DISABLED_IN_COMBAT = config.getBoolean("MINECART_ELEVATOR.DISABLED_IN_COMBAT");
        MINECART_ELEVATOR_OWN_CLAIM_ONLY = config.getBoolean("MINECART_ELEVATOR.OWN_CLAIM_ONLY");

        MOB_STACK_ENABLED = config.getBoolean("MOB_STACK.ENABLED");
        MOB_STACK_COLOR = config.getString("MOB_STACK.COLOR");
        MOB_STACK_RADIUS = config.getInt("MOB_STACK.RADIUS");
        MOB_STACK_MAX_AMOUNT = config.getInt("MOB_STACK.MAX_AMOUNT");
        MOB_STACK_ENTITIES = config.getStringList("MOB_STACK.ENTITIES")
            .stream().map(String::toUpperCase).collect(Collectors.toList());

        PVP_PROTECTION_DURATION = config.getInt("PVP_PROTECTION.DURATION");
        PVP_PROTECTION_DISABLE_END_ENTRY = config.getBoolean("PVP_PROTECTION.DISABLE_END_ENTRY");
        PVP_PROTECTION_CAN_ENTER_OWN_CLAIM = config.getBoolean("PVP_PROTECTION.CAN_ENTER_OWN_CLAIM");

        RENAME_MAX_LENGTH = config.getInt("RENAME_COMMAND.MAX_LENGTH");
        RENAME_BLACKLISTED_WORDS = config.getStringList("RENAME_COMMAND.BLACKLISTED_WORDS")
            .stream().map(String::toLowerCase).collect(Collectors.toList());

        REPORT_COOLDOWN = config.getInt("REPORT_COMMAND.COOLDOWN");
        REQUEST_COOLDOWN = config.getInt("REQUEST_COMMAND.COOLDOWN");

        SIGN_ELEVATOR_ENABLED = config.getBoolean("SIGN_ELEVATOR.ENABLED");
        SIGN_ELEVATOR_DISABLED_IN_COMBAT = config.getBoolean("SIGN_ELEVATOR.DISABLED_IN_COMBAT");
        SIGN_ELEVATOR_OWN_CLAIM_ONLY = config.getBoolean("SIGN_ELEVATOR.OWN_CLAIM_ONLY");
        SIGN_ELEVATOR_ELEVATOR_UP = config.getStringList("SIGN_ELEVATOR.ELEVATOR_UP");
        SIGN_ELEVATOR_ELEVATOR_DOWN = config.getStringList("SIGN_ELEVATOR.ELEVATOR_DOWN");

        SOTW_DEFAULT_TIME = config.getInt("SOTW_TIMER.DEFAULT_TIME");
        SOTW_SPAWN_MOBS_FROM_SPAWNERS_ONLY = config.getBoolean("SOTW_TIMER.SPAWN_MOBS_FROM_SPAWNERS_ONLY");
        SOTW_HIDE_PLAYERS = config.getBoolean("SOTW_TIMER.HIDE_PLAYERS");

        SPAWN_CREDITS_INITIAL_SPAWN_CREDITS = config.getInt("SPAWN_CREDITS.INITIAL_SPAWN_CREDITS");
        SPAWN_CREDITS_SPAWN_TELEPORT_DELAY = config.getInt("SPAWN_CREDITS.SPAWN_TELEPORT_DELAY");

        STAFF_MODE_ON_JOIN_ENABLED = config.getBoolean("STAFF_MODE.STAFF_MODE_ON_JOIN_ENABLED");

        STATS_INVENTORY_NAME = config.getString("STATS_COMMAND.INVENTORY_NAME");
        STATS_INVENTORY_SIZE = config.getInt("STATS_COMMAND.INVENTORY_SIZE");

        STATTRAK_ENABLED = config.getBoolean("STAT_TRAK.ENABLED");
        STATTRAK_KILLS_FORMAT = config.getString("STAT_TRAK.KILLS_FORMAT");
        STATTRAK_KILL_STRING = config.getString("STAT_TRAK.KILL_STRING");
        STATTRAK_TRACKING_ITEMS = config.getStringList("STAT_TRAK.TRACKING_ITEMS")
            .stream().map(String::toUpperCase).collect(Collectors.toList());

        STRENGTH_NERF_ENABLED = config.getBoolean("STRENGTH_NERF.ENABLED");
        STRENGTH_1_NERF_PERCENTAGE = config.getInt("STRENGTH_NERF.STRENGTH_1_NERF_PERCENTAGE");
        STRENGTH_2_NERF_PERCENTAGE = config.getInt("STRENGTH_NERF.STRENGTH_2_NERF_PERCENTAGE");
        REMOVE_STRENGTH_ON_END_ENTER = config.getBoolean("STRENGTH_NERF.REMOVE_STRENGTH_ON_END_ENTER");

        SUBCLAIMS_SIGN_TITLE = config.getString("SUBCLAIMS.SIGN_TITLE");
        SUBCLAIMS_LEADER_ONLY = config.getString("SUBCLAIMS.LEADER_ONLY");
        SUBCLAIMS_CO_LEADERS_ONLY = config.getString("SUBCLAIMS.CO_LEADERS_ONLY");
        SUBCLAIMS_CAPTAINS_ONLY = config.getString("SUBCLAIMS.CAPTAINS_ONLY");

        UNREPAIRABLE_ITEM_LORE = config.getString("UNREPAIRABLE_ITEM_LORE");

        VANISH_ON_JOIN_ENABLED = config.getBoolean("VANISH.VANISH_ON_JOIN_ENABLED");

        CHAIN_ARMOR_RECIPE_ENABLED = classes.getBoolean("CHAIN_ARMOR_RECIPE.ENABLED");
        CHAIN_ARMOR_RECIPE_MATERIAL = Material.getMaterial(classes.getString("CHAIN_ARMOR_RECIPE.MATERIAL"));

        ARCHER_ACTIVATED = classes.getBoolean("ARCHER_CLASS.ENABLED");
        BARD_ACTIVATED = classes.getBoolean("BARD_CLASS.ENABLED");
        MINER_ACTIVATED = classes.getBoolean("MINER_CLASS.ENABLED");
        ROGUE_ACTIVATED = classes.getBoolean("ROGUE_CLASS.ENABLED");
        MAGE_ACTIVATED = classes.getBoolean("MAGE_CLASS.ENABLED");
        BOMBER_ACTIVATED = classes.getBoolean("BOMBER_CLASS.ENABLED");

        ARCHER_WARMUP = classes.getInt("ARCHER_CLASS.WARMUP");
        BARD_WARMUP = classes.getInt("BARD_CLASS.WARMUP");
        MINER_WARMUP = classes.getInt("MINER_CLASS.WARMUP");
        ROGUE_WARMUP = classes.getInt("ROGUE_CLASS.WARMUP");
        MAGE_WARMUP = classes.getInt("MAGE_CLASS.WARMUP");
        BOMBER_WARMUP = classes.getInt("BOMBER_CLASS.WARMUP");

        ARCHER_TAG_CAN_TAG_OTHER_ARCHERS = classes.getBoolean("ARCHER_CLASS.ARCHER_TAG.CAN_TAG_OTHER_ARCHERS");
        ARCHER_TAG_REQUIRE_FULL_FORCE = classes.getBoolean("ARCHER_CLASS.ARCHER_TAG.REQUIRE_FULL_FORCE");
        ARCHER_TAG_DURATION = classes.getInt("ARCHER_CLASS.ARCHER_TAG.DURATION");
        ARCHER_TAG_DAMAGE_MULTIPLIER = classes.getDouble("ARCHER_CLASS.ARCHER_TAG.DAMAGE_MULTIPLIER");

        BARD_MAX_ENERGY = classes.getInt("BARD_CLASS.MAX_ENERGY") * 1000;
        BARD_COMBAT_TAG_ON_CLICKABLE_ITEM = classes.getBoolean("BARD_CLASS.COMBAT_TAG_ON_CLICKABLE_ITEM");

        ROGUE_BACKSTAB_ITEM = Material.getMaterial(classes.getString("ROGUE_CLASS.BACKSTAB.ITEM"));
        ROGUE_BACKSTAB_DAMAGE = classes.getInt("ROGUE_CLASS.BACKSTAB.DAMAGE");
        ROGUE_BACKSTAB_COOLDOWN = classes.getInt("ROGUE_CLASS.BACKSTAB.COOLDOWN");
        ROGUE_BACKSTAB_EFFECTS_ENABLED = classes.getBoolean("ROGUE_CLASS.BACKSTAB.EFFECTS_ENABLED");

        MAGE_MAX_ENERGY = classes.getInt("MAGE_CLASS.MAX_ENERGY") * 1000;
        MAGE_COMBAT_TAG_ON_CLICKABLE_ITEM = classes.getBoolean("MAGE_CLASS.COMBAT_TAG_ON_CLICKABLE_ITEM");

        BOMBER_COMBAT_TAG_ON_TNT_USE = classes.getBoolean("BOMBER_CLASS.COMBAT_TAG_ON_TNT_USE");

        ABILITIES_DENY_USAGE_WORLD = new EnumMap<>(Environment.class);
        ABILITIES_DENY_USAGE_WORLD.put(Environment.NORMAL, abilitiesFile.getBoolean("DENY_USAGE.OVERWORLD"));
        ABILITIES_DENY_USAGE_WORLD.put(Environment.NETHER, abilitiesFile.getBoolean("DENY_USAGE.NETHER"));
        ABILITIES_DENY_USAGE_WORLD.put(Environment.THE_END, abilitiesFile.getBoolean("DENY_USAGE.END"));

        ABILITIES_DENY_USAGE_DISTANCE_ENABLED = new EnumMap<>(Environment.class);
        ABILITIES_DENY_USAGE_DISTANCE_ENABLED.put(Environment.NORMAL, abilitiesFile.getBoolean("DENY_USAGE_DISTANCE.OVERWORLD.ENABLED"));
        ABILITIES_DENY_USAGE_DISTANCE_ENABLED.put(Environment.NETHER, abilitiesFile.getBoolean("DENY_USAGE_DISTANCE.NETHER.ENABLED"));
        ABILITIES_DENY_USAGE_DISTANCE_ENABLED.put(Environment.THE_END, abilitiesFile.getBoolean("DENY_USAGE_DISTANCE.END.ENABLED"));

        ABILITIES_DENY_USAGE_DISTANCE_BLOCKS = new EnumMap<>(Environment.class);
        ABILITIES_DENY_USAGE_DISTANCE_BLOCKS.put(Environment.NORMAL, abilitiesFile.getInt("DENY_USAGE_DISTANCE.OVERWORLD.BLOCKS"));
        ABILITIES_DENY_USAGE_DISTANCE_BLOCKS.put(Environment.NETHER, abilitiesFile.getInt("DENY_USAGE_DISTANCE.NETHER.BLOCKS"));
        ABILITIES_DENY_USAGE_DISTANCE_BLOCKS.put(Environment.THE_END, abilitiesFile.getInt("DENY_USAGE_DISTANCE.END.BLOCKS"));

        ABILITIES_GLOBAL_COOLDOWN_ENABLED = abilitiesFile.getBoolean("GLOBAL_COOLDOWN.ENABLED");
        ABILITIES_GLOBAL_COOLDOWN_DURATION = abilitiesFile.getInt("GLOBAL_COOLDOWN.DURATION");

        TAB_ENABLED = tab.getBoolean("TAB_ENABLED");
        TAB_HEADER = String.join("\n", tab.getStringList("TAB_HEADER"));
        TAB_FOOTER = String.join("\n", tab.getStringList("TAB_FOOTER"));

        SCOREBOARD_TITLE = scoreboard.getString("SCOREBOARD_TITLE");

        SCOREBOARD_LINE_COLOR = scoreboard.getString("SCOREBOARD_LINE.COLOR");
        SCOREBOARD_LINE_INVISIBLE = scoreboard.getBoolean("SCOREBOARD_LINE.INVISIBLE");
        SCOREBOARD_LINE_AFTER_EVERY_SECTION = scoreboard.getBoolean("SCOREBOARD_LINE.AFTER_EVERY_SECTION");

        SCOREBOARD_FOOTER_ENABLED = scoreboard.getBoolean("SCOREBOARD_FOOTER.ENABLED");
        SCOREBOARD_FOOTER_PLACEHOLDER = scoreboard.getString("SCOREBOARD_FOOTER.PLACEHOLDER");

        CLAIM_PLACEHOLDER = scoreboard.getString("CLAIM_PLACEHOLDER");
        FACTION_RALLY_TITLE_PLACEHOLDER = scoreboard.getString("FACTION_RALLY.TITLE_PLACEHOLDER");
        FACTION_RALLY_WORLD_PLACEHOLDER = scoreboard.getString("FACTION_RALLY.WORLD_PLACEHOLDER");
        FACTION_RALLY_LOCATION_PLACEHOLDER = scoreboard.getString("FACTION_RALLY.LOCATION_PLACEHOLDER");
        COMBAT_TAG_PLACEHOLDER = scoreboard.getString("COMBAT_TAG_PLACEHOLDER");
        ENDERPEARL_PLACEHOLDER = scoreboard.getString("ENDERPEARL_PLACEHOLDER");
        PVP_PROTECTION_PLACEHOLDER = scoreboard.getString("PVP_PROTECTION_PLACEHOLDER");
        SOTW_PLACEHOLDER = scoreboard.getString("SOTW_PLACEHOLDER");
        SOTW_ENABLE_PLACEHOLDER = scoreboard.getString("SOTW_ENABLE_PLACEHOLDER");
        EOTW_START_PLACEHOLDER = scoreboard.getString("EOTW_START_PLACEHOLDER");
        CONQUEST_PLACEHOLDER = scoreboard.getString("CONQUEST_PLACEHOLDER");
        CONQUEST_FACTION_FORMAT = scoreboard.getString("CONQUEST_FACTION_FORMAT");
        KOTH_PLACEHOLDER = scoreboard.getString("KOTH_PLACEHOLDER");
        DTC_PLACEHOLDER = scoreboard.getString("DTC_PLACEHOLDER");
        ENDER_DRAGON_PLACEHOLDER = scoreboard.getString("ENDER_DRAGON_PLACEHOLDER");
        PVPCLASS_WARMUP_PLACEHOLDER = scoreboard.getString("PVPCLASS_WARMUP_PLACEHOLDER");
        PVPCLASS_ACTIVE_PLACEHOLDER = scoreboard.getString("PVPCLASS_ACTIVE_PLACEHOLDER");
        ARCHER_TAG_PLACEHOLDER = scoreboard.getString("ARCHER_TAG_PLACEHOLDER");
        BARD_ENERGY_PLACEHOLDER = scoreboard.getString("BARD_ENERGY_PLACEHOLDER");
        MAGE_ENERGY_PLACEHOLDER = scoreboard.getString("MAGE_ENERGY_PLACEHOLDER");
        SALE_PLACEHOLDER = scoreboard.getString("SALE_PLACEHOLDER");
        KEYSALE_PLACEHOLDER = scoreboard.getString("KEYSALE_PLACEHOLDER");
        COOLDOWN_PLACEHOLDER = scoreboard.getString("COOLDOWN_PLACEHOLDER");
        TELEPORT_PLACEHOLDER = scoreboard.getString("TELEPORT_PLACEHOLDER");
        LOGOUT_PLACEHOLDER = scoreboard.getString("LOGOUT_PLACEHOLDER");
        NORMAL_GOLDEN_APPLE_PLACEHOLDER = scoreboard.getString("NORMAL_GOLDEN_APPLE_PLACEHOLDER");
        ENCHANTED_GOLDEN_APPLE_PLACEHOLDER = scoreboard.getString("ENCHANTED_GOLDEN_APPLE_PLACEHOLDER");
        PURGE_START_PLACEHOLDER = scoreboard.getString("PURGE_START_PLACEHOLDER");
        PURGE_PLACEHOLDER = scoreboard.getString("PURGE_PLACEHOLDER");
        STAFFMODE_PLACEHOLDER = scoreboard.getString("STAFFMODE_PLACEHOLDER");
        VANISH_PLACEHOLDER = scoreboard.getString("VANISH_PLACEHOLDER");
        VISIBILITY_PLACEHOLDER = scoreboard.getString("VISIBILITY_PLACEHOLDER");
        CHATMODE_PLACEHOLDER = scoreboard.getString("CHATMODE_PLACEHOLDER");
        GAMEMODE_PLACEHOLDER = scoreboard.getString("GAMEMODE_PLACEHOLDER");
        ONLINE_PLACEHOLDER = scoreboard.getString("ONLINE_PLACEHOLDER");
        MINER_DIAMOND_COUNT_PLACEHOLDER = scoreboard.getString("MINER_DIAMOND_COUNT_PLACEHOLDER");
        KITMAP_STATS_TITLE_PLACEHOLDER = scoreboard.getString("KITMAP_STATS.TITLE_PLACEHOLDER");
        KITMAP_STATS_KILLS_PLACEHOLDER = scoreboard.getString("KITMAP_STATS.KILLS_PLACEHOLDER");
        KITMAP_STATS_DEATHS_PLACEHOLDER = scoreboard.getString("KITMAP_STATS.DEATHS_PLACEHOLDER");
        KITMAP_STATS_BALANCE_PLACEHOLDER = scoreboard.getString("KITMAP_STATS.BALANCE_PLACEHOLDER");
        KITMAP_STATS_KILLSTREAK_PLACEHOLDER = scoreboard.getString("KITMAP_STATS.KILLSTREAK_PLACEHOLDER");
        HOME_PLACEHOLDER = scoreboard.getString("HOME_PLACEHOLDER");
        STUCK_PLACEHOLDER = scoreboard.getString("STUCK_PLACEHOLDER");
        REBOOT_PLACEHOLDER = scoreboard.getString("REBOOT_PLACEHOLDER");

        ABILITIES_TITLE_PLACEHOLDER = scoreboard.getString("ABILITIES.TITLE_PLACEHOLDER");
        ABILITIES_GLOBAL_COOLDOWN_PLACEHOLDER = scoreboard.getString("ABILITIES.GLOBAL_COOLDOWN_PLACEHOLDER");
        ABILITIES_ABILITY_COOLDOWN_PLACEHOLDER = scoreboard.getString("ABILITIES.ABILITY_COOLDOWN_PLACEHOLDER");

        KING_TITLE_PLACEHOLDER = scoreboard.getString("KILL_THE_KING.TITLE_PLACEHOLDER");
        KING_KING_PLACEHOLDER = scoreboard.getString("KILL_THE_KING.KING_PLACEHOLDER");
        KING_TIME_LASTED_PLACEHOLDER = scoreboard.getString("KILL_THE_KING.TIME_LASTED_PLACEHOLDER");
        KING_WORLD_PLACEHOLDER = scoreboard.getString("KILL_THE_KING.WORLD_PLACEHOLDER");
        KING_LOCATION_PLACEHOLDER = scoreboard.getString("KILL_THE_KING.LOCATION_PLACEHOLDER");

        FACTION_FOCUS_TITLE_PLACEHOLDER = scoreboard.getString("FACTION_FOCUS.TITLE_PLACEHOLDER");
        FACTION_FOCUS_DTR_PLACEHOLDER = scoreboard.getString("FACTION_FOCUS.DTR_PLACEHOLDER");
        FACTION_FOCUS_HQ_PLACEHOLDER = scoreboard.getString("FACTION_FOCUS.HQ_PLACEHOLDER");
        FACTION_FOCUS_ONLINE_PLACEHOLDER = scoreboard.getString("FACTION_FOCUS.ONLINE_PLACEHOLDER");

        STAFF_SB_VANISHED = scoreboard.getString("STAFF_SCOREBOARD.VISIBILITY_VANISHED");
        STAFF_SB_VISIBLE = scoreboard.getString("STAFF_SCOREBOARD.VISIBILITY_VISIBLE");
        STAFF_SB_STAFFCHAT = scoreboard.getString("STAFF_SCOREBOARD.CHAT_STAFFCHAT");
        STAFF_SB_GLOBAL = scoreboard.getString("STAFF_SCOREBOARD.CHAT_GLOBAL");
        STAFF_SB_SURVIVAL = scoreboard.getString("STAFF_SCOREBOARD.GAMEMODE_SURVIVAL");
        STAFF_SB_CREATIVE = scoreboard.getString("STAFF_SCOREBOARD.GAMEMODE_CREATIVE");
    }
}
