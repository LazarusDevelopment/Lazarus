package me.qiooip.lazarus.config;

import me.qiooip.lazarus.Lazarus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Language {

    public static String PREFIX;
    public static String FACTION_PREFIX;
    public static String KIT_PREFIX;
    public static String ABILITIES_PREFIX;
    public static String CONQUEST_PREFIX;
    public static String DTC_PREFIX;
    public static String KING_PREFIX;
    public static String KOTH_PREFIX;
    public static String MOUNTAIN_PREFIX;
    public static String ENDER_DRAGON_PREFIX;
    public static String LOOT_PREFIX;
    public static String SCHEDULE_PREFIX;
    public static String HOLOGRAMS_PREFIX;

    public static String YES_PLACEHOLDER;
    public static String NO_PLACEHOLDER;

    public static String TRUE_PLACEHOLDER;
    public static String FALSE_PLACEHOLDER;

    public static String ENABLED_PLACEHOLDER;
    public static String DISABLED_PLACEHOLDER;

    public static String NONE_PLACEHOLDER;

    public static String WORLD_NAMES_OVERWORLD;
    public static String WORLD_NAMES_NETHER;
    public static String WORLD_NAMES_THE_END;

    public static String STAFF_JOIN_MESSAGE;
    public static String STAFF_JOIN_MESSAGE_VANISHED;
    public static String STAFF_QUIT_MESSAGE;

    public static List<String> JOIN_WELCOME_MESSAGE;

    public static List<String> HARD_RESET_COMMAND_USAGE;

    public static String USERDATA_FAILED_TO_LOAD;
    public static String PLAYER_ALREADY_ONLINE;

    public static String COMMANDS_FOR_PLAYERS_ONLY;
    public static String COMMANDS_FOR_CONSOLE_ONLY;
    public static String COMMANDS_NO_PERMISSION;
    public static String COMMANDS_PLAYER_NOT_ONLINE;
    public static String COMMANDS_PLAYER_NOT_FOUND;
    public static String COMMANDS_COMMAND_NOT_FOUND;
    public static String COMMANDS_INVALID_NUMBER;
    public static String COMMANDS_INVALID_DURATION;
    public static String COMMANDS_COOLDOWN;

    public static String PLUGIN_RELOAD_MESSAGE;

    public static List<String> ONLINE_RANK_ANNOUNCER_MESSAGE;
    public static String ONLINE_RANK_ANNOUNCER_DELIMITER;

    public static String ABILITIES_DENY_USAGE_WORLD;
    public static String ABILITIES_DENY_USAGE_DISTANCE;
    public static String ABILITIES_GLOBAL_COOLDOWN_ACTIVE;
    public static String ABILITIES_ABILITY_COOLDOWN_ACTIVE;
    public static String ABILITIES_GLOBAL_COOLDOWN_EXPIRED;
    public static String ABILITIES_ABILITY_COOLDOWN_EXPIRED;

    public static String ABILITIES_ABILITY_COMMAND_USAGE;
    public static String ABILITIES_ABILITY_COMMAND_NOT_FOUND;
    public static String ABILITIES_ABILITY_COMMAND_NOT_ENABLED;
    public static String ABILITIES_ABILITY_COMMAND_GIVE_ABILITY;
    public static String ABILITIES_ABILITY_COMMAND_INVENTORY_FULL;

    public static String ABILITIES_ABILITY_TIMER_COMMAND_USAGE;
    public static String ABILITIES_ABILITY_TIMER_COMMAND_NOT_FOUND;
    public static String ABILITIES_ABILITY_TIMER_COMMAND_NOT_ENABLED;
    public static String ABILITIES_ABILITY_TIMER_COMMAND_CHANGED;
    public static String ABILITIES_ABILITY_TIMER_COMMAND_CHANGED_SENDER;

    public static String ABILITIES_AGGRESSIVE_PEARL_EFFECT_FORMAT;

    public static String ABILITIES_ANTI_ABILITY_BALL_TARGET_ACTIVATED;

    public static String ABILITIES_ANTI_REDSTONE_TARGET_ACTIVATED;
    public static String ABILITIES_ANTI_REDSTONE_TARGET_EXPIRED;
    public static String ABILITIES_ANTI_REDSTONE_CANNOT_USE;

    public static String ABILITIES_ANTI_TRAP_STAR_TARGET_ACTIVATED;
    public static String ABILITIES_ANTI_TRAP_STAR_TARGET_TELEPORTED;
    public static String ABILITIES_ANTI_TRAP_STAR_PLAYER_TELEPORTED;
    public static String ABILITIES_ANTI_TRAP_STAR_CANNOT_USE;

    public static String ABILITIES_COCAINE_EFFECT_FORMAT;

    public static String ABILITIES_COMBO_EFFECT_FORMAT;
    public static List<String> ABILITIES_COMBO_APPLY_EFFECTS;

    public static String ABILITIES_DECOY_BECOME_VISIBLE_ON_DAMAGE;
    public static String ABILITIES_DECOY_BECOME_VISIBLE_ON_EXPIRE;

    public static String ABILITIES_EXOTIC_BONE_TARGET_ACTIVATED;
    public static String ABILITIES_EXOTIC_BONE_TARGET_EXPIRED;
    public static String ABILITIES_EXOTIC_BONE_CANNOT_INTERACT;

    public static String ABILITIES_GUARDIAN_ANGEL_HEALED;
    public static String ABILITIES_GUARDIAN_ANGEL_EXPIRED;

    public static String ABILITIES_LUCKY_INGOT_EFFECT_FORMAT;
    public static String ABILITIES_INVISIBILITY_BECOME_VISIBLE_ON_DAMAGE;
    public static String ABILITIES_POCKET_BARD_EFFECT_FORMAT;
    public static String ABILITIES_PRE_PEARL_TELEPORTED;

    public static String ABILITIES_RAGE_EFFECT_FORMAT;
    public static List<String> ABILITIES_RAGE_APPLY_EFFECTS;

    public static String ABILITIES_RAGE_BRICK_EFFECT_FORMAT;

    public static String ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_SAFEZONE;
    public static String ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_SAFEZONE_TARGET;
    public static String ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_PVP_TIMER;
    public static String ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_PVP_TIMER_TARGET;
    public static String ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_SOTW;
    public static String ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_SOTW_TARGET;
    public static String ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_TEAMMATES;
    public static String ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_ALLIES;

    public static String ABILITIES_SECOND_CHANCE_NOT_ON_COOLDOWN;

    public static String ABILITIES_STARVATION_FLESH_TARGET_ACTIVATED;
    public static String ABILITIES_STARVATION_FLESH_TARGET_EQUIPPED_CLASS;

    public static String ABILITIES_SWITCHER_SWITCH_DENIED_DISTANCE_TOO_FAR;
    public static String ABILITIES_SWITCHER_SWITCH_DENIED_SAFEZONE;
    public static String ABILITIES_SWITCHER_SWITCH_DENIED_SAFEZONE_TARGET;
    public static String ABILITIES_SWITCHER_SWITCH_DENIED_PVP_TIMER;
    public static String ABILITIES_SWITCHER_SWITCH_DENIED_PVP_TIMER_TARGET;
    public static String ABILITIES_SWITCHER_SWITCH_DENIED_SOTW;
    public static String ABILITIES_SWITCHER_SWITCH_DENIED_SOTW_TARGET;
    public static String ABILITIES_SWITCHER_SWITCH_DENIED_TEAMMATES;
    public static String ABILITIES_SWITCHER_SWITCH_DENIED_ALLIES;

    public static String ABILITIES_SWITCH_STICK_TARGET_ACTIVATED;

    public static String ABILITIES_TANK_INGOT_EFFECT_FORMAT;

    public static String ABILITIES_WEB_GUN_COBWEB_REMOVE;

    public static String BLOCKS_PLACEMENT_DISABLED;

    public static String ITEMS_NOT_HOLDING;
    public static String ITEMS_INVALID_NAME;

    public static String ENTITIES_INVALID_ENTITY;

    public static String ARCHER_CLICKABLE_ACTIVATED;
    public static String ARCHER_CLICKABLE_COOLDOWN;
    public static String ARCHER_COOLDOWN_EXPIRED;

    public static String ARCHER_TAG_EXPIRED_MESSAGE;
    public static String ARCHER_TAG_TAGGED_VICTIM;
    public static String ARCHER_TAG_TAGGED_TAGGER;

    public static String BARD_VANISHED_OR_IN_STAFFMODE;
    public static String BARD_CAN_NOT_BARD_TO_YOURSELF;
    public static String BARD_CAN_NOT_BARD_WITH_PVP_TIMER;
    public static String BARD_CAN_NOT_BARD_IN_SAFEZONE;
    public static String BARD_CAN_NOT_BARD_WHEN_SOTW_NOT_ENABLED;
    public static String BARD_CLICKABLE_ACTIVE_COOLDOWN;
    public static String BARD_CLICKABLE_COOLDOWN_EXPIRED;
    public static String BARD_CLICKABLE_NOT_ENOUGH_ENERGY;
    public static String BARD_CLICKABLE_MESSAGE_FRIENDLY;
    public static String BARD_CLICKABLE_MESSAGE_ENEMY;
    public static String BARD_CLICKABLE_MESSAGE_OTHERS;

    public static String MINER_EFFECT_REWARD;

    public static String ROGUE_CLICKABLE_ACTIVATED;
    public static String ROGUE_CLICKABLE_COOLDOWN;
    public static String ROGUE_COOLDOWN_EXPIRED;
    public static String ROGUE_BACKSTAB_COOLDOWN;
    public static String ROGUE_BACKSTAB_COOLDOWN_EXPIRED;

    public static String MAGE_VANISHED_OR_IN_STAFFMODE;
    public static String MAGE_CAN_NOT_USE_MAGE_WITH_PVP_TIMER;
    public static String MAGE_CAN_NOT_USE_MAGE_IN_SAFEZONE;
    public static String MAGE_CAN_NOT_USE_MAGE_WHEN_SOTW_NOT_ENABLED;
    public static String MAGE_CLICKABLE_ACTIVE_COOLDOWN;
    public static String MAGE_CLICKABLE_COOLDOWN_EXPIRED;
    public static String MAGE_CLICKABLE_NOT_ENOUGH_ENERGY;
    public static String MAGE_CLICKABLE_MESSAGE_ENEMY;
    public static String MAGE_CLICKABLE_MESSAGE_OTHERS;

    public static String BOMBER_VANISHED_OR_IN_STAFFMODE;
    public static String BOMBER_CAN_NOT_SHOOT_TNT_WITH_PVP_TIMER;
    public static String BOMBER_CAN_NOT_SHOOT_TNT_IN_SAFEZONE;
    public static String BOMBER_CAN_NOT_SHOOT_TNT_WHEN_SOTW_NOT_ENABLED;
    public static String BOMBER_TNT_GUN_ACTIVE_COOLDOWN;
    public static String BOMBER_TNT_GUN_COOLDOWN_EXPIRED;

    public static String HOLOGRAMS_COMMAND_HEADER;
    public static String HOLOGRAMS_COMMAND_FOOTER;
    public static List<String> HOLOGRAMS_COMMAND_USAGE;

    public static String HOLOGRAMS_EXCEPTIONS_DOESNT_EXIST;
    public static String HOLOGRAMS_EXCEPTIONS_TYPE_NOT_FOUND;
    public static String HOLOGRAMS_EXCEPTIONS_TYPE_MUST_BE_NORMAL;
    public static String HOLOGRAMS_EXCEPTIONS_LINE_DOESNT_EXIST;
    public static String HOLOGRAMS_EXCEPTIONS_CANT_INSERT_LINE;

    public static String HOLOGRAMS_ADD_LINE_USAGE;
    public static String HOLOGRAMS_ADD_LINE_LINE_ADDED;

    public static String HOLOGRAMS_CREATE_USAGE;
    public static String HOLOGRAMS_CREATE_CREATED;

    public static String HOLOGRAMS_DELETE_USAGE;
    public static String HOLOGRAMS_DELETE_DELETED;

    public static String HOLOGRAMS_INSERT_LINE_USAGE;
    public static String HOLOGRAMS_INSERT_LINE_INSERTED;

    public static String HOLOGRAMS_LIST_NO_HOLOGRAMS;
    public static String HOLOGRAMS_LIST_TITLE;
    public static String HOLOGRAMS_LIST_FORMAT;

    public static String HOLOGRAMS_REMOVE_LINE_USAGE;
    public static String HOLOGRAMS_REMOVE_LINE_REMOVED;

    public static String HOLOGRAMS_TELEPORT_USAGE;
    public static String HOLOGRAMS_TELEPORT_TELEPORTED;

    public static String HOLOGRAMS_TELEPORT_HERE_USAGE;
    public static String HOLOGRAMS_TELEPORT_HERE_TELEPORTED;

    public static String HOLOGRAMS_UPDATE_LINE_USAGE;
    public static String HOLOGRAMS_UPDATE_LINE_UPDATED;

    public static String HOLOGRAM_EMPTY_LINE_FORMAT;

    public static List<String> HOLOGRAM_TOP_KILLS_HEADER;
    public static List<String> HOLOGRAM_TOP_KILLS_FOOTER;
    public static String HOLOGRAM_TOP_KILLS_LINE_FORMAT;

    public static List<String> HOLOGRAM_TOP_DEATHS_HEADER;
    public static List<String> HOLOGRAM_TOP_DEATHS_FOOTER;
    public static String HOLOGRAM_TOP_DEATHS_LINE_FORMAT;

    public static List<String> HOLOGRAM_TOP_BALANCE_HEADER;
    public static List<String> HOLOGRAM_TOP_BALANCE_FOOTER;
    public static String HOLOGRAM_TOP_BALANCE_LINE_FORMAT;

    public static List<String> HOLOGRAM_TOP_KILLSTREAK_HEADER;
    public static List<String> HOLOGRAM_TOP_KILLSTREAK_FOOTER;
    public static String HOLOGRAM_TOP_KILLSTREAK_LINE_FORMAT;

    public static List<String> HOLOGRAM_FACTION_TOP_KILLS_HEADER;
    public static List<String> HOLOGRAM_FACTION_TOP_KILLS_FOOTER;
    public static String HOLOGRAM_FACTION_TOP_KILLS_LINE_FORMAT;

    public static List<String> HOLOGRAM_FACTION_TOP_POINTS_HEADER;
    public static List<String> HOLOGRAM_FACTION_TOP_POINTS_FOOTER;
    public static String HOLOGRAM_FACTION_TOP_POINTS_LINE_FORMAT;

    public static List<String> HOLOGRAM_FACTION_TOP_BALANCE_HEADER;
    public static List<String> HOLOGRAM_FACTION_TOP_BALANCE_FOOTER;
    public static String HOLOGRAM_FACTION_TOP_BALANCE_LINE_FORMAT;

    public static List<String> HOLOGRAM_FACTION_TOP_KOTHS_CAPPED_HEADER;
    public static List<String> HOLOGRAM_FACTION_TOP_KOTHS_CAPPED_FOOTER;
    public static String HOLOGRAM_FACTION_TOP_KOTHS_CAPPED_LINE_FORMAT;

    public static String LEADERBOARDS_COMMAND_USAGE;
    public static String LEADERBOARDS_FACTION_COMMAND_USAGE;
    public static String LEADERBOARDS_TYPE_DOESNT_EXIST;
    public static String LEADERBOARDS_KITMAP_MODE_ONLY;
    public static String LEADERBOARDS_NO_LEADERBOARDS;
    public static String LEADERBOARDS_COMMAND_HEADER;
    public static String LEADERBOARDS_COMMAND_FOOTER;

    public static String LEADERBOARDS_KILLS_TITLE;
    public static String LEADERBOARDS_KILLS_LINE_FORMAT;

    public static String LEADERBOARDS_DEATHS_TITLE;
    public static String LEADERBOARDS_DEATHS_LINE_FORMAT;

    public static String LEADERBOARDS_BALANCE_TITLE;
    public static String LEADERBOARDS_BALANCE_LINE_FORMAT;

    public static String LEADERBOARDS_KILLSTREAK_TITLE;
    public static String LEADERBOARDS_KILLSTREAK_LINE_FORMAT;

    public static String LEADERBOARDS_FACTION_KILLS_TITLE;
    public static String LEADERBOARDS_FACTION_KILLS_LINE_FORMAT;

    public static String LEADERBOARDS_FACTION_POINTS_TITLE;
    public static String LEADERBOARDS_FACTION_POINTS_LINE_FORMAT;

    public static String LEADERBOARDS_FACTION_BALANCE_TITLE;
    public static String LEADERBOARDS_FACTION_BALANCE_LINE_FORMAT;

    public static String LEADERBOARDS_FACTION_TOP_KOTHS_CAPPED_TITLE;
    public static String LEADERBOARDS_FACTION_TOP_KOTHS_CAPPED_LINE_FORMAT;

    public static String SPAWNERS_DISABLE_PLACE_NETHER;
    public static String SPAWNERS_DISABLE_PLACE_END;
    public static String SPAWNERS_DISABLE_BREAK_NETHER;
    public static String SPAWNERS_DISABLE_BREAK_END;

    public static String FACTIONS_NAME_TOO_SHORT;
    public static String FACTIONS_NAME_TOO_BIG;
    public static String FACTIONS_BLOCKED_FACTION_NAME;
    public static String FACTIONS_NAME_NOT_ALPHANUMERIC;
    public static String FACTIONS_FACTION_ALREADY_EXISTS;
    public static String FACTIONS_FACTION_DOESNT_EXIST;
    public static String FACTIONS_ALREADY_IN_FACTION_SELF;
    public static String FACTIONS_ALREADY_IN_FACTION_OTHERS;
    public static String FACTIONS_NOT_IN_FACTION;
    public static String FACTIONS_NOT_IN_FACTION_SELF;
    public static String FACTIONS_NOT_IN_FACTION_OTHERS;
    public static String FACTIONS_MUST_BE_LEADER;
    public static String FACTIONS_NO_PERMISSION;
    public static String FACTIONS_NO_PERMISSION_ROLE;
    public static String FACTIONS_MEMBER_ONLINE;
    public static String FACTIONS_MEMBER_OFFLINE;
    public static String FACTIONS_MEMBER_DEATH;
    public static String FACTIONS_ALLIES_DISABLED;
    public static String FACTIONS_DENY_DAMAGE_ALLIES;
    public static String FACTIONS_ENDERPEARL_USAGE_DENIED;
    public static String FACTIONS_ABILITIES_USAGE_DENIED;
    public static String FACTIONS_NO_LONGER_FROZEN;
    public static String FACTIONS_RALLY_EXPIRED;

    public static String FACTIONS_PVP_CLASS_LIMIT_DENY_EQUIP;
    public static String FACTIONS_PVP_CLASS_LIMIT_CLASS_DEACTIVATED;

    public static String FACTIONS_SAFEZONE_DENY_DAMAGE_ATTACKER;
    public static String FACTIONS_SAFEZONE_DENY_DAMAGE_OTHERS;

    public static String FACTIONS_PROTECTION_DENY_BUILD;
    public static String FACTIONS_PROTECTION_DENY_INTERACT;

    public static String FACTION_CHAT_FACTION_FORMAT;
    public static String FACTION_CHAT_ALLY_FORMAT;

    public static String FACTIONS_CHATSPY_ENABLED;
    public static String FACTIONS_CHATSPY_DISABLED;
    public static String FACTIONS_CHATSPY_FORMAT;

    public static String FACTIONS_CLEAR_CLAIMS_USAGE;
    public static String FACTIONS_CLEAR_CLAIMS_CLEARED_SENDER;
    public static String FACTIONS_CLEAR_CLAIMS_CLEARED_FACTION;
    public static String FACTIONS_CLEAR_CLAIMS_HOME_TELEPORT_CANCELLED;

    public static String FACTIONS_CLAIM_ENTERING;
    public static String FACTIONS_CLAIM_LEAVING;

    public static String FACTIONS_ABILITIES_USAGE;
    public static String FACTIONS_ABILITIES_NOT_SYSTEM_FACTION;
    public static String FACTIONS_ABILITIES_ENABLED;
    public static String FACTIONS_ABILITIES_DISABLED;

    public static String FACTIONS_ADD_DTR_USAGE;
    public static String FACTIONS_ADD_DTR_CHANGED_SENDER;
    public static String FACTIONS_ADD_DTR_CHANGED_FACTION;

    public static String FACTIONS_ALLY_USAGE;
    public static String FACTIONS_ALLY_MAX_ALLIES_SELF;
    public static String FACTIONS_ALLY_MAX_ALLIES_OTHERS;
    public static String FACTIONS_ALLY_ALREADY_ALLIES;
    public static String FACTIONS_ALLY_CANNOT_ALLY_SELF;
    public static String FACTIONS_ALLY_REQUEST_ALREADY_SENT;
    public static String FACTIONS_ALLY_REQUESTED_SELF;
    public static String FACTIONS_ALLY_REQUESTED_OTHERS;
    public static String FACTIONS_ALLY_ACCEPTED;

    public static String FACTIONS_ANNOUNCEMENT_USAGE;
    public static String FACTIONS_ANNOUNCEMENT_REMOVED;
    public static String FACTIONS_ANNOUNCEMENT_MESSAGE;

    public static String FACTIONS_AUTO_REVIVE_ENABLED;
    public static String FACTIONS_AUTO_REVIVE_DISABLED;

    public static String FACTIONS_BALANCE_MESSAGE;

    public static String FACTIONS_CHAT_COMMAND_CHANGED;

    public static String FACTIONS_CLAIM_MAX_CLAIMS_EXCEEDED;
    public static String FACTIONS_CLAIM_CLAIMING_WAND_RECEIVED;
    public static String FACTIONS_CLAIM_SELECTION_CLEARED;
    public static String FACTIONS_CLAIM_SELECTION_NOT_SET;
    public static String FACTIONS_CLAIM_POSITION_ONE_SET;
    public static String FACTIONS_CLAIM_POSITION_TWO_SET;
    public static String FACTIONS_CLAIM_PRICE_MESSAGE;
    public static String FACTIONS_CLAIM_CLAIM_OVERLAPPING;
    public static String FACTIONS_CLAIM_CLAIM_NOT_CONNECTED;
    public static String FACTIONS_CLAIM_CANNOT_CLAIM_WARZONE;
    public static String FACTIONS_CLAIM_LOCATION_ALREADY_CLAIMED;
    public static String FACTIONS_CLAIM_SPAWN_TOO_CLOSE;
    public static String FACTIONS_CLAIM_INVENTORY_FULL;
    public static String FACTIONS_CLAIM_CAN_CLAIM_ONLY_IN_OVERWORLD;
    public static String FACTIONS_CLAIM_NOT_ENOUGH_MONEY;
    public static String FACTIONS_CLAIM_CLAIM_MIN_SIZE;
    public static String FACTIONS_CLAIM_CLAIM_MAX_SIZE;
    public static String FACTIONS_CLAIM_CLAIM_BUFFER;
    public static String FACTIONS_CLAIM_CLAIM_CLAIMED;

    public static String FACTIONS_CLAIM_FOR_USAGE;
    public static String FACTIONS_CLAIM_FOR_NOT_SYSTEM_FACTION;
    public static String FACTIONS_CLAIM_FOR_MAKE_A_SELECTION;
    public static String FACTIONS_CLAIM_FOR_SET_BOTH_POSITIONS;
    public static String FACTIONS_CLAIM_FOR_CLAIM_OVERLAPPING;
    public static String FACTIONS_CLAIM_FOR_CLAIM_CLAIMED;

    public static String FACTIONS_COLOR_USAGE;
    public static String FACTIONS_COLOR_NOT_SYSTEM_FACTION;
    public static String FACTIONS_COLOR_DOESNT_EXIST;
    public static String FACTIONS_COLOR_CHANGED;

    public static String FACTIONS_CREATE_USAGE;
    public static String FACTIONS_CREATED;
    public static String FACTIONS_CREATE_COOLDOWN;

    public static String FACTIONS_DEATHBAN_USAGE;
    public static String FACTIONS_DEATHBAN_NOT_SYSTEM_FACTION;
    public static String FACTIONS_DEATHBAN_CHANGED;

    public static String FACTIONS_DEMOTE_USAGE;
    public static String FACTIONS_DEMOTE_DEMOTED;
    public static String FACTIONS_DEMOTE_MIN_DEMOTE;
    public static String FACTIONS_CANNOT_DEMOTE_SELF;

    public static String FACTIONS_DEPOSIT_USAGE;
    public static String FACTIONS_DEPOSIT_DEPOSITED;
    public static String FACTIONS_DEPOSIT_CAN_NOT_DEPOSIT_ZERO;
    public static String FACTIONS_DEPOSIT_NOT_ENOUGH_MONEY;

    public static String FACTIONS_DISBANDED;
    public static String FACTIONS_DISBAND_CLAIM_MONEY_REFUNDED;
    public static String FACTIONS_DISBAND_RAIDABLE_DENY;
    public static String FACTIONS_CANNOT_DISBAND_WHILE_REGENERATING;

    public static String FACTIONS_FOCUS_USAGE;
    public static String FACTIONS_FOCUS_ALREADY_FOCUSING;
    public static String FACTIONS_FOCUS_CANNOT_FOCUS;
    public static List<String> FACTIONS_FOCUS_FOCUSED;

    public static String FACTIONS_FRIENDLY_FIRE_TOGGLED_ON;
    public static String FACTIONS_FRIENDLY_FIRE_TOGGLED_OFF;

    public static String FACTIONS_ENDERPEARLS_USAGE;
    public static String FACTIONS_ENDERPEARLS_NOT_SYSTEM_FACTION;
    public static String FACTIONS_ENDERPEARLS_ENABLED;
    public static String FACTIONS_ENDERPEARLS_DISABLED;

    public static String FACTIONS_FORCE_DEMOTE_USAGE;
    public static String FACTIONS_FORCE_DEMOTED_SENDER;
    public static String FACTIONS_FORCE_DEMOTED_FACTION;
    public static String FACTIONS_FORCE_DEMOTE_MIN_DEMOTE;
    public static String FACTIONS_FORCE_DEMOTE_CANNOT_DEMOTE_LEADER;

    public static String FACTIONS_FORCE_JOIN_USAGE;
    public static String FACTIONS_FORCE_JOINED;

    public static String FACTIONS_FORCE_KICK_USAGE;
    public static String FACTIONS_FORCE_KICK_CANNOT_KICK_LEADER;
    public static String FACTIONS_FORCE_KICKED_SENDER;
    public static String FACTIONS_FORCE_KICKED_SELF;
    public static String FACTIONS_FORCE_KICKED_OTHERS;

    public static String FACTIONS_FORCE_LEADER_USAGE;
    public static String FACTIONS_FORCE_LEADER_CHANGED_SENDER;
    public static String FACTIONS_FORCE_LEADER_CHANGED_FACTION;
    public static String FACTIONS_FORCE_LEADER_ALREADY_LEADER;

    public static String FACTIONS_FORCE_PROMOTE_USAGE;
    public static String FACTIONS_FORCE_PROMOTED_SENDER;
    public static String FACTIONS_FORCE_PROMOTED_FACTION;
    public static String FACTIONS_FORCE_PROMOTE_MAX_PROMOTE;

    public static String FACTIONS_FORCE_RENAME_USAGE;
    public static String FACTIONS_FORCE_RENAMED;
    public static String FACTIONS_FORCE_RENAME_SAME_NAME;

    public static String FACTIONS_HELP_PAGE_NOT_FOUND;
    public static Map<Integer, List<String>> FACTIONS_HELP_PAGES;

    public static String FACTIONS_HOME_CANNOT_TELEPORT_ALREADY_TELEPORTING;
    public static String FACTIONS_HOME_CANNOT_TELEPORT_IN_COMBAT;
    public static String FACTIONS_HOME_CANNOT_TELEPORT_IN_ENEMY_TERRITORY;
    public static String FACTIONS_HOME_CANNOT_TELEPORT_WITH_PVP_TIMER;
    public static String FACTIONS_HOME_NOT_SET;
    public static String FACTIONS_HOME_TASK_STARTED;
    public static String FACTIONS_HOME_TELEPORTED;
    public static String FACTIONS_HOME_CANCELLED_MOVED;
    public static String FACTIONS_HOME_CANCELLED_DAMAGED;

    public static String FACTIONS_INVITE_USAGE;
    public static String FACTIONS_INVITED_SELF;
    public static String FACTIONS_INVITED_OTHERS;
    public static String FACTIONS_INVITE_ALREADY_INVITED;
    public static String FACTIONS_INVITE_FACTION_FULL;
    public static String FACTIONS_INVITE_HOVER_TEXT;

    public static String FACTIONS_JOIN_USAGE;
    public static String FACTIONS_JOINED;
    public static String FACTIONS_NOT_INVITED;
    public static String FACTIONS_JOIN_FACTION_FULL;
    public static String FACTIONS_CANNOT_JOIN_WHILE_REGENERATING;

    public static String FACTIONS_KICK_USAGE;
    public static String FACTIONS_KICKED_SELF;
    public static String FACTIONS_KICKED_OTHERS;

    public static String FACTIONS_LEADER_USAGE;
    public static String FACTIONS_LEADER_LEADER_CHANGED;
    public static String FACTIONS_LEADER_ALREADY_LEADER;

    public static String FACTIONS_LEFT_SELF;
    public static String FACTIONS_LEFT_OTHERS;
    public static String FACTIONS_LEADER_LEAVE;
    public static String FACTIONS_CANNOT_LEAVE_WHILE_REGENERATING;
    public static String FACTIONS_CANNOT_LEAVE_WHILE_IN_OWN_CLAIM;

    public static String FACTIONS_LIST_PAGE_NOT_FOUND;
    public static String FACTIONS_LIST_FACTION_FORMAT;
    public static List<String> FACTIONS_LIST_HEADER;
    public static List<String> FACTIONS_LIST_FOOTER;

    public static String FACTIONS_LIVES_SELF;
    public static String FACTIONS_LIVES_OTHERS;

    public static String FACTIONS_LIVES_DEPOSIT_USAGE;
    public static String FACTIONS_LIVES_DEPOSITED;
    public static String FACTIONS_LIVES_DEPOSIT_NOT_ENOUGH_LIVES;

    public static String FACTIONS_LIVES_REVIVE_USAGE;
    public static String FACTIONS_LIVES_REVIVED;
    public static String FACTIONS_LIVES_REVIVE_NOT_ENOUGH_LIVES;
    public static String FACTIONS_LIVES_REVIVE_NOT_DEATHBANNED;

    public static String FACTIONS_LIVES_WITHDRAW_USAGE;
    public static String FACTIONS_LIVES_WITHDRAWN;
    public static String FACTIONS_LIVES_WITHDRAW_NOT_ENOUGH_LIVES;

    public static String FACTIONS_MAP_NO_NEARBY_CLAIMS;
    public static String FACTIONS_MAP_OWNER_MESSAGE;
    public static String FACTIONS_MAP_DISABLED;

    public static String FACTIONS_OPEN_OPENED;
    public static String FACTIONS_OPEN_CLOSED;
    public static String FACTIONS_OPEN_COOLDOWN;

    public static String FACTIONS_PROMOTE_USAGE;
    public static String FACTIONS_PROMOTE_PROMOTED;
    public static String FACTIONS_PROMOTE_MAX_PROMOTE;
    public static String FACTIONS_CANNOT_PROMOTE_SELF;

    public static String FACTIONS_RALLY_SET;

    public static String FACTIONS_REMOVE_RALLY_NOT_SET;
    public static String FACTIONS_REMOVE_RALLY_REMOVED;

    public static String FACTIONS_REMOVE_CLAIM_NO_CLAIM;
    public static String FACTIONS_REMOVE_CLAIM_REMOVED_SENDER;
    public static String FACTIONS_REMOVE_CLAIM_REMOVED_FACTION;

    public static String FACTIONS_REMOVE_USAGE;
    public static String FACTIONS_REMOVE_CANNOT_REMOVE_THIS_TYPE;
    public static String FACTIONS_REMOVED_SENDER;
    public static String FACTIONS_REMOVED_FACTION;

    public static String FACTIONS_REMOVE_DTR_USAGE;
    public static String FACTIONS_REMOVE_DTR_CHANGED_SENDER;
    public static String FACTIONS_REMOVE_DTR_CHANGED_FACTION;

    public static String FACTIONS_RENAME_USAGE;
    public static String FACTIONS_RENAMED;
    public static String FACTIONS_RENAME_SAME_NAME;
    public static String FACTIONS_RENAME_COOLDOWN;

    public static String FACTIONS_SAFEZONE_USAGE;
    public static String FACTIONS_SAFEZONE_NOT_SYSTEM_FACTION;
    public static String FACTIONS_SAFEZONE_CHANGED;

    public static String FACTIONS_SAVED;

    public static String FACTIONS_SET_BALANCE_USAGE;
    public static String FACTIONS_SET_BALANCE_CHANGED_SENDER;
    public static String FACTIONS_SET_BALANCE_CHANGED_FACTION;

    public static String FACTIONS_SET_DTR_USAGE;
    public static String FACTIONS_SET_DTR_CHANGED_SENDER;
    public static String FACTIONS_SET_DTR_CHANGED_FACTION;

    public static String FACTIONS_SHOW_HOVER_TEXT;
    public static String FACTIONS_SHOW_NAME_FORMAT;
    public static List<String> FACTIONS_PLAYER_FACTION_SHOW;
    public static String FACTIONS_SYSTEM_CLAIM_FORMAT;
    public static List<String> FACTIONS_SYSTEM_FACTION_SHOW;

    public static String FACTIONS_STUCK_ALREADY_TELEPORTING;
    public static String FACTIONS_STUCK_ONLY_IN_OVERWORLD;
    public static String FACTIONS_STUCK_ONLY_FROM_ENEMY_CLAIMS;
    public static String FACTIONS_STUCK_IN_OWN_CLAIM;
    public static String FACTIONS_STUCK_TASK_STARTED;
    public static String FACTIONS_STUCK_TELEPORTED;
    public static String FACTIONS_STUCK_CANCELLED_MOVED;
    public static String FACTIONS_STUCK_CANCELLED_DAMAGED;

    public static String FACTIONS_SET_HOME_NOT_IN_OWN_CLAIM;
    public static String FACTIONS_SET_HOME_HOME_SET;

    public static String FACTIONS_SET_FREEZE_USAGE;
    public static String FACTIONS_SET_FREEZE_CHANGED_SENDER;
    public static String FACTIONS_SET_FREEZE_CHANGED_FACTION;

    public static String FACTIONS_SET_LIVES_USAGE;
    public static String FACTIONS_SET_LIVES_CHANGED_SENDER;
    public static String FACTIONS_SET_LIVES_CHANGED_FACTION;

    public static String FACTIONS_SET_KILLS_USAGE;
    public static String FACTIONS_SET_KILLS_CHANGED_SENDER;
    public static String FACTIONS_SET_KILLS_CHANGED_FACTION;

    public static String FACTIONS_SET_POINTS_USAGE;
    public static String FACTIONS_SET_POINTS_CHANGED_SENDER;
    public static String FACTIONS_SET_POINTS_CHANGED_FACTION;

    public static String FACTIONS_SYSTEM_CREATE_USAGE;
    public static String FACTIONS_SYSTEM_CREATED;

    public static String FACTIONS_SYSTEM_LIST_FACTION_FORMAT;
    public static List<String> FACTIONS_SYSTEM_LIST_HEADER;
    public static String FACTIONS_SYSTEM_LIST_FOOTER;

    public static String FACTIONS_SYSTEM_RENAME_USAGE;
    public static String FACTIONS_SYSTEM_RENAMED;
    public static String FACTIONS_SYSTEM_RENAME_NOT_SYSTEM_FACTION;
    public static String FACTIONS_SYSTEM_RENAME_CANNOT_RENAME_THIS_TYPE;
    public static String FACTIONS_SYSTEM_RENAME_SAME_NAME;

    public static String FACTIONS_THAW_USAGE;
    public static String FACTIONS_THAW_CHANGED_SENDER;
    public static String FACTIONS_THAW_CHANGED_FACTION;

    public static String FACTIONS_TP_HERE_USAGE;
    public static String FACTIONS_TP_HERE_TELEPORTED_SENDER;
    public static String FACTIONS_TP_HERE_TELEPORTED_FACTION;

    public static String FACTIONS_UNALLY_USAGE;
    public static String FACTIONS_UNALLY_NOT_ALLIES;
    public static String FACTIONS_UNALLY_REMOVED;

    public static String FACTIONS_UNCLAIM_ALL_NO_CLAIMS;
    public static String FACTIONS_UNCLAIMED_ALL;
    public static String FACTIONS_UNCLAIM_ALL_HOME_TELEPORT_CANCELLED;
    public static String FACTIONS_CANNOT_UNCLAIM_ALL_WHILE_REGENERATING;

    public static String FACTIONS_UNCLAIM_NO_CLAIMS;
    public static String FACTIONS_UNCLAIM_NOT_OWNER;
    public static String FACTIONS_UNCLAIMED;
    public static String FACTIONS_UNCLAIM_CLAIM_MONEY_REFUNDED;
    public static String FACTIONS_UNCLAIM_HOME_TELEPORT_CANCELLED;
    public static String FACTIONS_CANNOT_UNCLAIM_WHILE_REGENERATING;

    public static String FACTIONS_UNFOCUS_NOT_FOCUSING;
    public static String FACTIONS_UNFOCUS_UNFOCUSED;
    public static String FACTIONS_UNFOCUS_FACTION_DOESNT_EXIST;

    public static String FACTIONS_UNINVITE_USAGE;
    public static String FACTIONS_UNINVITE_UNINVITED;
    public static String FACTIONS_UNINVITE_ALL;
    public static String FACTIONS_UNINVITE_NOT_INVITED;

    public static String FACTIONS_WITHDRAW_USAGE;
    public static String FACTIONS_WITHDRAW_WITHDRAWN;
    public static String FACTIONS_WITHDRAW_NOT_ENOUGH_MONEY;

    public static List<String> CONQUEST_COMMAND_USAGE_PLAYER;
    public static List<String> CONQUEST_COMMAND_USAGE_ADMIN;

    public static List<String> CONQUEST_CAPPED;
    public static String CONQUEST_STARTED_CAPPING;
    public static String CONQUEST_STOPPED_CAPPING;

    public static String CONQUEST_EXCEPTION_NOT_RUNNING;
    public static String CONQUEST_EXCEPTION_INVALID_ZONE;

    public static String CONQUEST_AREA_MAKE_A_SELECTION;
    public static String CONQUEST_AREA_SET_BOTH_POSITIONS;
    public static String CONQUEST_AREA_CLAIM_OVERLAPPING;
    public static String CONQUEST_AREA_CREATED;

    public static String CONQUEST_INFO_NOT_SETUP;
    public static List<String> CONQUEST_INFO_MESSAGE;

    public static String CONQUEST_SET_CAPZONE_USAGE;
    public static String CONQUEST_SET_CAPZONE_MAKE_A_SELECTION;
    public static String CONQUEST_SET_CAPZONE_SET_BOTH_POSITIONS;
    public static String CONQUEST_SET_CAPZONE_CREATED;

    public static String CONQUEST_SET_POINTS_USAGE;
    public static String CONQUEST_SET_POINTS_INVALID_FACTION;
    public static String CONQUEST_SET_POINTS_CHANGED;

    public static String CONQUEST_START_ALREADY_RUNNING;
    public static String CONQUEST_START_NOT_SETUP;
    public static List<String> CONQUEST_START_STARTED;

    public static String CONQUEST_STOP_STOPPED;

    public static String CONQUEST_TELEPORT_USAGE;
    public static String CONQUEST_TELEPORT_ZONE_NOT_SET;

    public static List<String> DTC_COMMAND_USAGE_PLAYER;
    public static List<String> DTC_COMMAND_USAGE_ADMIN;

    public static String DTC_DESTROYED;
    public static String DTC_HEALTH_MESSAGE;

    public static String DTC_EXCEPTION_ALREADY_RUNNING;
    public static String DTC_EXCEPTION_NOT_RUNNING;

    public static String DTC_AREA_MAKE_A_SELECTION;
    public static String DTC_AREA_SET_BOTH_POSITIONS;
    public static String DTC_AREA_CLAIM_OVERLAPPING;
    public static String DTC_AREA_CREATED;

    public static String DTC_INFO_NOT_SETUP;
    public static List<String> DTC_INFO_MESSAGE;

    public static String DTC_SET_MUST_BE_OBSIDIAN;
    public static String DTC_SET_CORE_SET;

    public static String DTC_SET_HEALTH_USAGE;
    public static String DTC_SET_HEALTH_CHANGED;

    public static String DTC_START_NOT_SETUP;
    public static List<String> DTC_START_STARTED;

    public static String DTC_STOP_STOPPED;

    public static String DTC_TELEPORT_CORE_NOT_SET;

    public static String KOTH_COMMAND_HEADER;
    public static String KOTH_COMMAND_FOOTER;
    public static List<String> KOTH_COMMAND_USAGE_PLAYER;
    public static List<String> KOTH_COMMAND_USAGE_ADMIN;
    public static List<String> KOTH_CAPPED_NO_FACTION;
    public static List<String> KOTH_CAPPED_WITH_FACTION;
    public static String KOTH_KNOCKED;
    public static String KOTH_YOU_STARTED_CAPPING;
    public static String KOTH_SOMEONE_STARTED_CAPPING;
    public static String KOTH_NO_ONE_CAPPING;
    public static String KOTH_YOU_ARE_CAPPING;
    public static String KOTH_SOMEONE_IS_CAPPING;

    public static String KOTH_EXCEPTION_DOESNT_EXIST;
    public static String KOTH_EXCEPTION_NOT_RUNNING;
    public static String KOTH_EXCEPTION_MAX_RUNNING_KOTHS_AMOUNT_REACHED;

    public static String KOTH_AREA_USAGE;
    public static String KOTH_AREA_MAKE_A_SELECTION;
    public static String KOTH_AREA_SET_BOTH_POSITIONS;
    public static String KOTH_AREA_CLAIM_OVERLAPPING;
    public static String KOTH_AREA_CREATED;

    public static String KOTH_CREATE_USAGE;
    public static String KOTH_CREATE_ALREADY_EXISTS;
    public static String KOTH_CREATE_MAKE_A_SELECTION;
    public static String KOTH_CREATE_SET_BOTH_POSITIONS;
    public static String KOTH_CREATE_CREATED;

    public static String KOTH_LIST_COMMAND_TITLE;
    public static String KOTH_LIST_COMMAND_FORMAT;
    public static String KOTH_LIST_NO_KOTHS;

    public static String KOTH_LOOT_USAGE;

    public static String KOTH_REMOVE_USAGE;
    public static String KOTH_REMOVE_REMOVED;

    public static String KOTH_START_USAGE;
    public static String KOTH_START_ALREADY_RUNNING;
    public static List<String> KOTH_START_STARTED;

    public static String KOTH_STOP_USAGE;
    public static String KOTH_STOP_STOPPED;

    public static String KOTH_STOP_ALL_STOPPED_ALL;
    public static String KOTH_STOP_ALL_NO_RUNNING_KOTHS;

    public static String KOTH_SET_TIME_USAGE;
    public static String KOTH_SET_TIME_CHANGED;

    public static String KOTH_STARTING_TIME_USAGE;
    public static String KOTH_STARTING_TIME_CHANGED;

    public static String KOTH_END_USAGE;
    public static String KOTH_END_ENDED;

    public static String KOTH_FACTION_POINTS_USAGE;
    public static String KOTH_FACTION_POINTS_CHANGED;

    public static String KOTH_TELEPORT_USAGE;

    public static List<String> KING_COMMAND_USAGE_ADMIN;
    public static List<String> KING_KING_SLAIN;
    public static String KING_KINGS_LOCATION;

    public static String KING_EXCEPTION_ALREADY_RUNNING;
    public static String KING_EXCEPTION_NOT_RUNNING;
    public static String KING_EXCEPTION_ITEM_DROP_DENY;

    public static String KING_START_USAGE;
    public static String KING_START_STARTED;
    public static String KING_STOP_STOPPED;

    public static String MOUNTAIN_COMMAND_HEADER;
    public static String MOUNTAIN_COMMAND_FOOTER;
    public static List<String> MOUNTAIN_COMMAND_USAGE_ADMIN;
    public static List<String> MOUNTAIN_RESPAWNED;
    public static String MOUNTAIN_NEXT_RESPAWN;

    public static String MOUNTAIN_EXCEPTION_DOESNT_EXISTS;
    public static String MOUNTAIN_EXCEPTION_INCORRECT_TYPE;

    public static String MOUNTAIN_CREATE_USAGE;
    public static String MOUNTAIN_CREATE_MAKE_A_SELECTION;
    public static String MOUNTAIN_CREATE_SET_BOTH_POSITIONS;
    public static String MOUNTAIN_CREATE_CLAIM_OVERLAPPING;
    public static String MOUNTAIN_CREATE_CREATED;

    public static String MOUNTAIN_LIST_TITLE;
    public static String MOUNTAIN_LIST_ADMIN_FORMAT;
    public static String MOUNTAIN_LIST_PLAYER_FORMAT;
    public static String MOUNTAIN_LIST_NO_MOUNTAINS;

    public static String MOUNTAIN_REMOVE_USAGE;
    public static String MOUNTAIN_REMOVE_REMOVED;

    public static String MOUNTAIN_RESPAWN_USAGE;
    public static String MOUNTAIN_RESPAWN_RESPAWNED;

    public static String MOUNTAIN_TELEPORT_USAGE;

    public static String MOUNTAIN_UPDATE_USAGE;
    public static String MOUNTAIN_UPDATE_UPDATED;

    public static List<String> ENDER_DRAGON_COMMAND_USAGE_PLAYER;
    public static List<String> ENDER_DRAGON_COMMAND_USAGE_ADMIN;

    public static List<String> ENDER_DRAGON_ENDED;
    public static String ENDER_DRAGON_DAMAGER_FORMAT;

    public static String ENDER_DRAGON_EXCEPTION_NOT_RUNNING;
    public static String ENDER_DRAGON_EXCEPTION_SPAWN_NOT_SET;

    public static String ENDER_DRAGON_SPAWN_NOT_SET;
    public static List<String> ENDER_DRAGON_INFO_MESSAGE;

    public static String ENDER_DRAGON_START_ALREADY_RUNNING;
    public static List<String> ENDER_DRAGON_START_STARTED;

    public static String ENDER_DRAGON_STOP_STOPPED;

    public static String ENDER_DRAGON_SET_HEALTH_USAGE;
    public static String ENDER_DRAGON_SET_HEALTH_CHANGED;

    public static String ENDER_DRAGON_SET_SPAWN_NOT_IN_END;
    public static String ENDER_DRAGON_SET_SPAWN_SET;

    public static String LOOT_COMMAND_HEADER;
    public static String LOOT_COMMAND_FOOTER;

    public static List<String> LOOT_COMMAND_USAGE_PLAYER;
    public static List<String> LOOT_COMMAND_USAGE_ADMIN;

    public static String LOOT_EXCEPTION_DOESNT_EXIST;

    public static String LOOT_CLEAR_USAGE;
    public static String LOOT_CLEAR_CLEARED;

    public static String LOOT_EDIT_USAGE;
    public static String LOOT_EDIT_EDITED;

    public static String LOOT_LIST_TITLE;
    public static String LOOT_LIST_FORMAT;

    public static String LOOT_SET_AMOUNT_USAGE;
    public static String LOOT_SET_AMOUNT_CHANGED;

    public static String LOOT_VIEW_USAGE;

    public static String SCHEDULE_COMMAND_HEADER;
    public static String SCHEDULE_COMMAND_FOOTER;

    public static List<String> SCHEDULE_COMMAND_USAGE_ADMIN;

    public static String SCHEDULE_UPCOMING_EVENTS_FORMAT;

    public static String SCHEDULE_CLEAR_NO_SCHEDULES;
    public static String SCHEDULE_CLEAR_CLEARED;

    public static String SCHEDULE_CREATE_USAGE;
    public static String SCHEDULE_CREATE_CREATED;
    public static String SCHEDULE_CREATE_EVENT_DOESNT_EXIST;
    public static String SCHEDULE_CREATE_INCORRECT_DAY_FORMAT;
    public static String SCHEDULE_CREATE_INCORRECT_TIME_FORMAT;

    public static String SCHEDULE_DELETE_USAGE;
    public static String SCHEDULE_DELETE_DOESNT_EXIST;
    public static String SCHEDULE_DELETE_DELETED;

    public static String SCHEDULE_LIST_NO_SCHEDULES;
    public static String SCHEDULE_LIST_CURRENT_TIME;
    public static String SCHEDULE_LIST_DAY_FORMAT;
    public static String SCHEDULE_LIST_TITLE;
    public static String SCHEDULE_LIST_PLAYER_FORMAT;
    public static String SCHEDULE_LIST_ADMIN_FORMAT;

    public static String DISABLED_COMMANDS_COLON_DISABLED;
    public static String DISABLED_COMMANDS_MESSAGE;

    public static String BACK_TELEPORTED;
    public static String BACK_NO_LOCATION;

    public static String BOOK_DISENCHANT;

    public static String BORDER_REACHED_BORDER;
    public static String BORDER_BUCKET_FILL_DENY;
    public static String BORDER_BUCKET_EMPTY_DENY;
    public static String BORDER_BLOCK_PLACE_DENY;
    public static String BORDER_BLOCK_BREAK_DENY;
    public static String BORDER_TELEPORT_DENY;

    public static String BOTTLE_USAGE;
    public static String BOTTLE_NOT_ENOUGH_LEVELS;

    public static String BROADCAST_PREFIX;
    public static String BROADCAST_USAGE;

    public static String BROADCAST_RAW_USAGE;

    public static String CHAT_MUTED;
    public static String CHAT_UNMUTED;
    public static String CHAT_DELAY_BROADCAST;
    public static String CHAT_CLEAR_BROADCAST;
    public static String CHAT_EVENT_MUTED_MESSAGE;
    public static String CHAT_COOLDOWN_MESSAGE;
    public static String CHAT_FOUNDORE_ENABLED;
    public static String CHAT_FOUNDORE_DISABLED;
    public static List<String> CHAT_USAGE;

    public static String CLEARINVENTORY_MESSAGE_SELF;
    public static String CLEARINVENTORY_MESSAGE_OTHERS;

    public static String COMBAT_TAG_COMMAND_DENY;
    public static String COMBAT_TAG_EXPIRED;
    public static String COMBAT_TAG_END_PORTAL_TELEPORT_DENY;
    public static String COMBAT_TAG_ENDERCHEST_DENY;
    public static String COMBAT_TAG_BLOCK_BREAK_DENY;
    public static String COMBAT_TAG_BLOCK_PLACE_DENY;

    public static String COPY_INVENTORY_USAGE;
    public static String COPY_INVENTORY_COPIED;

    public static String CROWBAR_COMMAND_USAGE;
    public static String CROWBAR_ZERO_USAGES_SPAWNERS;
    public static String CROWBAR_ZERO_USAGES_PORTALS;
    public static String CROWBAR_DENY_USAGE_NETHER;
    public static String CROWBAR_DENY_USAGE_END;
    public static String CROWBAR_DENY_USAGE_WARZONE;
    public static String CROWBAR_GAVE;
    public static String CROWBAR_RECEIVED;

    public static List<String> CUSTOM_TIMER_USAGE;
    public static List<String> CUSTOM_TIMER_LIST_HEADER;
    public static List<String> CUSTOM_TIMER_LIST_FOOTER;
    public static String CUSTOM_TIMER_LIST_FORMAT;
    public static String CUSTOM_TIMER_NOT_RUNNING;
    public static String CUSTOM_TIMER_STARTED;
    public static String CUSTOM_TIMER_STOPPED;

    public static String DEATHBAN_BAN_MESSAGE;
    public static String DEATHBAN_JOIN_AGAIN_FOR_REVIVE;
    public static String DEATHBAN_PLAYER_NOT_DEATHBANNED;
    public static String DEATHBAN_REVIVED_PLAYER;
    public static String DEATHBAN_EOTW_MESSAGE;
    public static List<String> DEATHBAN_COMMAND_USAGE;
    public static List<String> DEATHBAN_COMMAND_CHECK;

    public static String DEATHMESSAGE_PLAYER_NAME_FORMAT;
    public static String DEATHMESSAGE_KILLER_NAME_FORMAT;
    public static String DEATHMESSAGE_REASON_BLOCK_EXPLOSION;
    public static String DEATHMESSAGE_REASON_CONTACT;
    public static String DEATHMESSAGE_REASON_CUSTOM;
    public static String DEATHMESSAGE_REASON_COMBATLOGGER_KILLER;
    public static String DEATHMESSAGE_REASON_COMBATLOGGER;
    public static String DEATHMESSAGE_REASON_DROWNING;
    public static String DEATHMESSAGE_REASON_ENTITY_ATTACK_ENTITY;
    public static String DEATHMESSAGE_REASON_ENTITY_ATTACK_PLAYER_ITEM;
    public static String DEATHMESSAGE_REASON_ENTITY_ATTACK_PLAYER_NO_ITEM;
    public static String DEATHMESSAGE_REASON_ENTITY_EXPLOSION;
    public static String DEATHMESSAGE_REASON_FALL;
    public static String DEATHMESSAGE_REASON_FALL_KILLER;
    public static String DEATHMESSAGE_REASON_FALLING_BLOCK;
    public static String DEATHMESSAGE_REASON_FIRE;
    public static String DEATHMESSAGE_REASON_FIRE_TICK;
    public static String DEATHMESSAGE_REASON_LAVA;
    public static String DEATHMESSAGE_REASON_LIGHTNING;
    public static String DEATHMESSAGE_REASON_MAGIC;
    public static String DEATHMESSAGE_REASON_MELTING;
    public static String DEATHMESSAGE_REASON_POISON;
    public static String DEATHMESSAGE_REASON_PROJECTILE_ENTITY;
    public static String DEATHMESSAGE_REASON_PROJECTILE_PLAYER_ITEM;
    public static String DEATHMESSAGE_REASON_PROJECTILE_PLAYER_NO_ITEM;
    public static String DEATHMESSAGE_REASON_STARVATION;
    public static String DEATHMESSAGE_REASON_SUFFOCATION;
    public static String DEATHMESSAGE_REASON_SUICIDE;
    public static String DEATHMESSAGE_REASON_THORNS;
    public static String DEATHMESSAGE_REASON_VOID;
    public static String DEATHMESSAGE_REASON_VOID_KILLER;
    public static String DEATHMESSAGE_REASON_WITHER;

    public static String DEL_WARP_USAGE;
    public static String DEL_WARP_DELETED;
    public static String DEL_WARP_DOESNT_EXIST;

    public static String ECONOMY_BALANCE_SELF;
    public static String ECONOMY_BALANCE_OTHERS;
    public static String ECONOMY_PAY_USAGE;
    public static String ECONOMY_BALANCE_CHANGED;
    public static String ECONOMY_BALANCE_CHANGED_STAFF;
    public static String ECONOMY_CANNOT_SEND_TO_YOURSELF;
    public static String TRANSACTION_INVALID_AMOUNT;
    public static String TRANSACTION_MAX_AMOUNT;
    public static String TRANSACTION_MIN_AMOUNT;
    public static String TRANSACTION_SUCCESS_SELF;
    public static String TRANSACTION_SUCCESS_OTHERS;
    public static List<String> ECONOMY_ADMIN_COMMAND_USAGE;

    public static String ECONOMY_SIGNS_INVALID_MATERIAL;
    public static String ECONOMY_SIGNS_INVALID_AMOUNT;
    public static String ECONOMY_SIGNS_INVALID_PRICE;
    public static String ECONOMY_SIGNS_CANNOT_AFFORD;
    public static String ECONOMY_SIGNS_NOT_CARRYING;
    public static String ECONOMY_SIGNS_NO_SPACE;
    public static String ECONOMY_SIGNS_BOUGHT;
    public static String ECONOMY_SIGNS_SOLD;

    public static String ENCHANT_USAGE;
    public static String ENCHANT_DOESNT_EXIST;
    public static String ENCHANT_NON_OP_LIMIT;
    public static String ENCHANT_ENCHANTED;
    public static String ENCHANT_ENCHANT_REMOVED;

    public static String ENDERPEARL_DENY_MESSAGE;
    public static String ENDERPEARL_DENY_REFUNDED;
    public static String ENDERPEARL_COOLDOWN_EXPIRED;
    public static String ENDERPEARL_GLITCH_DENY;

    public static String ENDPORTAL_SELECTOR_ADDED_TO_INVENTORY;
    public static String ENDPORTAL_NOT_END_PORTAL_FRAME;
    public static String ENDPORTAL_SELECTION_TOO_BIG_OR_SMALL;
    public static String ENDPORTAL_WRONG_ELEVATION;

    public static String EOTW_NO_EOTW_KOTH;
    public static String EOTW_NOT_RUNNING;
    public static String EOTW_ALREADY_RUNNING;
    public static String EOTW_TIMER_STARTED;
    public static String EOTW_BROADCAST_START;
    public static String EOTW_BROADCAST_STOP;
    public static String EOTW_PVP_TIMER_DISABLED;
    public static List<String> EOTW_COMMAND_USAGE;

    public static String EXIT_DOESNT_EXIST;
    public static String EXIT_TELEPORTED;

    public static String EXPERIENCE_ADDED;
    public static String EXPERIENCE_REMOVED;
    public static String EXPERIENCE_SET;
    public static List<String> EXPERIENCE_USAGE;

    public static String FEED_MESSAGE_SELF;
    public static String FEED_MESSAGE_OTHERS;

    public static String FILTER_ADDED;
    public static String FILTER_REMOVED;
    public static String FILTER_CLEARED;
    public static String FILTER_EMPTY;
    public static String FILTER_TOGGLED_ON;
    public static String FILTER_TOGGLED_OFF;
    public static String FILTER_ALREADY_FILTERED;
    public static String FILTER_NOT_FILTERED;
    public static List<String> FILTER_LIST_MESSAGE;
    public static List<String> FILTER_USAGE;

    public static String FILL_BOTTLE_NO_GLASS_BOTTLE;
    public static String FILL_BOTTLE_CHEST_NOT_FOUND;
    public static String FILL_BOTTLE_ALREADY_FULL;
    public static String FILL_BOTTLE_FILLED;

    public static String FLY_SELF_ENABLED;
    public static String FLY_SELF_DISABLED;
    public static String FLY_OTHERS_ENABLED;
    public static String FLY_OTHERS_DISABLED;

    public static String FOCUS_USAGE;
    public static String FOCUS_ALREADY_FOCUSING;
    public static String FOCUS_CANNOT_FOCUS;
    public static List<String> FOCUS_FOCUSED;

    public static String FOUND_ORE_MESSAGE;

    public static String FREEZE_USAGE;
    public static String FREEZE_DISABLED_COMMAND_MESSAGE;
    public static String FREEZE_FREEZED_ALL;
    public static String FREEZE_UNFREEZED_ALL;
    public static String FREEZE_FREEZED_STAFF_MESSAGE;
    public static String FREEZE_UNFREEZED_STAFF_MESSAGE;
    public static String FREEZE_UNFREEZED_MESSAGE_TARGET;
    public static String FREEZE_PVP_DENY_MESSAGE_DAMAGER;
    public static String FREEZE_PVP_DENY_MESSAGE_VICTIM;
    public static String FREEZE_QUIT_WHEN_FROZEN;
    public static String FREEZE_CAN_NOT_FREEZE_PLAYER;
    public static List<String> FROZEN_MESSAGE;

    public static String GAMEMODE_USAGE;
    public static String GAMEMODE_INVALID_GAMEMODE;
    public static String GAMEMODE_MESSAGE_SELF;
    public static String GAMEMODE_MESSAGE_OTHERS;

    public static String GIVE_USAGE;
    public static String GIVE_INVALID_ITEM;
    public static String GIVE_GIVEN_SENDER;
    public static String GIVE_GIVEN_OTHERS;
    public static String GIVE_INVENTORY_FULL;

    public static String GOD_SELF_ENABLED;
    public static String GOD_SELF_DISABLED;
    public static String GOD_OTHERS_ENABLED;
    public static String GOD_OTHERS_DISABLED;

    public static String GOLDEN_APPLE_NO_COOLDOWNS;
    public static String NORMAL_APPLE_COOLDOWN_STARTED;
    public static String NORMAL_APPLE_COOLDOWN_DENY;
    public static String NORMAL_APPLE_COOLDOWN_EXPIRED;
    public static String NORMAL_APPLE_COMMAND_MESSAGE;
    public static String ENCHANTED_APPLE_COOLDOWN_STARTED;
    public static String ENCHANTED_APPLE_COOLDOWN_DENY;
    public static String ENCHANTED_APPLE_COOLDOWN_EXPIRED;
    public static String ENCHANTED_APPLE_COMMAND_MESSAGE;

    public static String HEAD_DROP_CLICK_MESSAGE;

    public static String HEAL_MESSAGE_SELF;
    public static String HEAL_MESSAGE_OTHERS;

    public static String HIDE_STAFF_HIDDEN;
    public static String HIDE_STAFF_SHOWN;

    public static String IGNORE_USAGE;
    public static String IGNORE_ENABLED;
    public static String IGNORE_DISABLED;
    public static String IGNORE_CANNOT_IGNORE_SELF;

    public static String INVENTORY_INSPECT_USAGE;

    public static String INVENTORY_RESTORE_USAGE;
    public static String INVENTORY_RESTORE_NOTHING_TO_RESTORE;
    public static String INVENTORY_RESTORE_RESTORED_SENDER;
    public static String INVENTORY_RESTORE_RESTORED_PLAYER;

    public static String INVSEE_USAGE;

    public static String ITEM_USAGE;
    public static String ITEM_INVALID_ITEM;
    public static String ITEM_RECEIVED;
    public static String ITEM_INVENTORY_FULL;

    public static String JOIN_FULL_SERVER_MESSAGE;

    public static String KICKALL_USAGE;
    public static String KICKALL_STAFF_MESSAGE;

    public static String KILLALL_USAGE;
    public static String KILLALL_KILLED;
    public static String KILLALL_COUNT;

    public static String KITMAP_KILLSTREAK_ON_DEATH;
    public static String KITMAP_KILLSTREAK_MESSAGE;

    public static String KITMAP_DENY_ITEM_DROP;

    public static String KILL_USAGE;
    public static String KILL_MESSAGE_SELF;
    public static String KILL_MESSAGE_OTHERS;

    public static List<String> KITS_COMMAND_USAGE_PLAYER;
    public static List<String> KITS_COMMAND_USAGE_ADMIN;

    public static String KITS_APPLIED_OTHERS;
    public static String KITS_EQUIPPED;

    public static String KITS_EXCEPTION_ONE_TIME_ONLY;
    public static String KITS_EXCEPTION_COOLDOWN;
    public static String KITS_EXCEPTION_NO_PERMISSION;
    public static String KITS_EXCEPTION_ALREADY_EXISTS;
    public static String KITS_EXCEPTION_DOESNT_EXISTS;
    public static String KITS_EXCEPTION_FULL_INVENTORY;
    public static String KITS_EXCEPTION_KITMAP_ONLY_IN_SAFEZONE;

    public static String KITS_CREATE_USAGE;
    public static String KITS_CREATE_CREATED;
    public static String KITS_CREATE_INVALID_KIT_TYPE;

    public static String KITS_EDIT_USAGE;
    public static String KITS_EDIT_EDITED;

    public static String KITS_GIVE_USAGE;

    public static String KITS_LIST_NO_KITS;
    public static String KITS_LIST_FORMAT;

    public static String KITS_REMOVE_USAGE;
    public static String KITS_REMOVE_REMOVED;
    public static String KITS_REMOVE_CANNOT_REMOVE_SPECIAL_EVENT_KIT;

    public static String KITS_SET_DELAY_USAGE;
    public static String KITS_SET_DELAY_CHANGED;

    public static String LAG_COMMAND_WORLD_FORMAT;
    public static List<String> LAG_COMMAND_MESSAGE;

    public static String LAST_DEATHS_USAGE;
    public static String LAST_DEATHS_HEADER_FORMAT;
    public static String LAST_DEATHS_DEATH_MESSAGE_FORMAT;
    public static String LAST_DEATHS_NO_DEATHS_YET;

    public static String LAST_KILLS_COMMAND_USAGE;
    public static String LAST_KILLS_COMMAND_HEADER_FORMAT;
    public static String LAST_KILLS_COMMAND_KILL_MESSAGE_FORMAT;
    public static String LAST_KILLS_COMMAND_NO_KILLS_YET;

    public static String LFF_COMMAND_COOLDOWN;
    public static String LFF_COMMAND_COOLDOWN_EXPIRED;
    public static String LFF_COMMAND_ALREADY_IN_FACTION;
    public static List<String> LFF_COMMAND_MESSAGE;

    public static List<String> LIST_COMMAND;

    public static String LIVES_CHECK_SELF;
    public static String LIVES_CHECK_OTHERS;
    public static String LIVES_PLAYER_NOT_DEATHBANNED;
    public static String LIVES_SUCCESSFULLY_REVIVED_PLAYER;
    public static String LIVES_SUCCESSFULLY_SENT_LIVES;
    public static String LIVES_SUCCESSFULLY_RECEIVED_LIVES;
    public static String LIVES_CAN_NOT_SEND_LIVES_TO_YOURSELF;
    public static String LIVES_ADD_USAGE;
    public static String LIVES_ADDED;
    public static String LIVES_ADD_RECEIVED;
    public static String LIVES_SET_USAGE;
    public static String LIVES_SET;
    public static String LIVES_SET_RECEIVED;
    public static String LIVES_ZERO_LIVES;
    public static String LIVES_NOT_ENOUGH_LIVES;
    public static List<String> LIVES_USAGE;

    public static String LOGOUT_KICK_MESSAGE;
    public static String LOGOUT_TELEPORT_CANCELLED_MOVED;
    public static String LOGOUT_TELEPORT_CANCELLED_DAMAGED;
    public static String LOGOUT_ALREADY_RUNNING;
    public static String LOGOUT_START_MESSAGE;

    public static String MAPKIT_EDIT_MESSAGE;

    public static String MESSAGE_USAGE;
    public static String MESSAGE_SEND_FORMAT;
    public static String MESSAGE_RECEIVE_FORMAT;
    public static String MESSAGE_MESSAGES_DISABLED;

    public static String MINECART_ELEVATOR_DENY_COMBAT;
    public static String MINECART_ELEVATOR_OWN_CLAIM_ONLY;
    public static String MINECART_ELEVATOR_NO_VALID_LOCATION;

    public static String MINER_EFFECT_HEADER;
    public static String MINER_EFFECT_FOOTER;
    public static String MINER_EFFECT_TITLE;
    public static List<String> MINER_EFFECT_FORMAT;

    public static String MORE_COMMAND_MESSAGE;

    public static String NETHER_PORTAL_TRAP_FIX;

    public static String NOTES_COMMAND_MESSAGE;
    public static String NOTES_COMMAND_NO_NOTES;
    public static String NOTES_COMMAND_NOTE_ADDED;
    public static String NOTES_COMMAND_NOTE_REMOVED;
    public static String NOTES_COMMAND_NOTE_DOESNT_EXIST;
    public static String NOTES_COMMAND_FORMAT;
    public static List<String> NOTES_COMMAND_USAGE;

    public static String PING_MESSAGE_SELF;
    public static String PING_MESSAGE_OTHERS;

    public static String PLAYTIME_COMMAND_USAGE;
    public static String PLAYTIME_COMMAND_MESSAGE;

    public static String PURGE_STARTED;
    public static String PURGE_ENDED;
    public static String PURGE_NOT_RUNNING;
    public static String PURGE_ALREADY_RUNNING;
    public static String PURGE_BROADCAST_START;
    public static String PURGE_BROADCAST_STOP;
    public static List<String> PURGE_COMMAND_USAGE;

    public static String PVP_CLASSES_ACTIVATED;
    public static String PVP_CLASSES_DEACTIVATED;
    public static String PVP_CLASSES_WARMING_UP;
    public static String PVP_CLASSES_WARMUP_CANCELLED;

    public static String PVP_COMMAND_PROTECTION_DISABLED;
    public static String PVP_COMMAND_NOT_ACTIVE;
    public static String PVP_COMMAND_TIME_STATUS;
    public static List<String> PVP_COMMAND_USAGE;

    public static String PVP_PROT_DENY_TELEPORT;
    public static String PVP_PROT_PROTECTION_EXPIRED;
    public static String PVP_PROT_PVP_DENY_ATTACKER;
    public static String PVP_PROT_PVP_DENY_VICTIM;
    public static String PVP_PROT_ITEM_DENY_MESSAGE;
    public static String PVP_PROT_END_PORTAL_TELEPORT_DENY;
    public static String PVP_PROT_TELEPORT_DUE_TO_CLAIM;

    public static String RANK_REVIVE_USAGE;
    public static String RANK_REVIVE_NO_PERMISSION;
    public static String RANK_REVIVE_NOT_DEATHBANNED;
    public static String RANK_REVIVE_COOLDOWN_EXPIRED;
    public static String RANK_REVIVE_COOLDOWN_ACTIVE;
    public static String RANK_REVIVE_BROADCAST_MESSAGE;

    public static List<String> REBOOT_USAGE;
    public static String REBOOT_NOT_RUNNING;
    public static String REBOOT_ALREADY_RUNNING;
    public static String REBOOT_PLAYER_KICK_MESSAGE;
    public static List<String> REBOOT_BROADCAST;
    public static List<String> REBOOT_CANCELED;

    public static String SALE_STARTED;
    public static String SALE_STOPPED;
    public static String SALE_FINISHED;
    public static String SALE_EXCEPTION_ALREADY_RUNNING;
    public static String SALE_EXCEPTION_NOT_RUNNING;
    public static List<String> SALE_USAGE;

    public static String SALVAGE_NOT_SALVAGEABLE;
    public static String SALVAGE_SALVAGED;
    public static String SALVAGE_INGREDIENT_FORMAT;

    public static String POTION_LIMITER_DENY_MESSAGE;

    public static String RECLAIM_ALREADY_USED;
    public static String RECLAIM_BROADCAST_MESSAGE;
    public static String RECLAIM_NO_PERMISSION;

    public static String RENAME_USAGE;
    public static String RENAME_MAX_LENGTH_EXCEEDED;
    public static String RENAME_BLACKLISTED_WORD;
    public static String RENAME_RENAMED;

    public static String REPAIR_USAGE;
    public static String REPAIR_NOT_REPAIRABLE;
    public static String REPAIR_REPAIRED_ITEM;
    public static String REPAIR_REPAIRED_ALL;

    public static String REPLY_USAGE;
    public static String REPLY_NOBODY_TO_REPLY;

    public static String REPORT_USAGE;
    public static String REPORT_REPORTED;
    public static String REPORT_COOLDOWN;
    public static String REPORT_COOLDOWN_EXPIRED;
    public static List<String> REPORT_FORMAT;

    public static String REQUEST_USAGE;
    public static String REQUEST_REQUESTED;
    public static String REQUEST_COOLDOWN;
    public static String REQUEST_COOLDOWN_EXPIRED;
    public static List<String> REQUEST_FORMAT;

    public static String SEEN_USAGE;
    public static String SEEN_ONLINE;
    public static String SEEN_MESSAGE;

    public static String SELECTION_LOCATION_ONE_SET;
    public static String SELECTION_LOCATION_TWO_SET;

    public static List<String> SET_EXIT_USAGE;
    public static String SET_EXIT_EXIT_SET;

    public static String SET_RECLAIM_USAGE;
    public static String SET_RECLAIM_INVALID_BOOLEAN;
    public static String SET_RECLAIM_SET_USED_MESSAGE;
    public static String SET_RECLAIM_SET_NOT_USED_MESSAGE;

    public static String SET_SLOTS_USAGE;
    public static List<String> SET_SLOTS_MESSAGE;

    public static List<String> SET_SPAWN_USAGE;
    public static String SET_SPAWN_SPAWN_SET;
    public static String SET_SPAWN_NOT_IN_WORLD;

    public static String SET_WARP_USAGE;
    public static String SET_WARP_CREATED;
    public static String SET_WARP_ALREADY_EXIST;

    public static String SIGN_ELEVATOR_CREATED;
    public static String SIGN_ELEVATOR_DESTROYED;
    public static String SIGN_ELEVATOR_NO_VALID_LOCATION;

    public static String SOCIAL_SPY_ENABLED;
    public static String SOCIAL_SPY_DISABLED;
    public static String SOCIAL_SPY_MESSAGE_FORMAT;

    public static String SOTW_STARTED;
    public static String SOTW_ENDED;
    public static String SOTW_NOT_RUNNING;
    public static String SOTW_ALREADY_RUNNING;
    public static String SOTW_ENABLED;
    public static String SOTW_ALREADY_ENABLED;
    public static String SOTW_VOID_FIX;
    public static String SOTW_TIME_STATUS;
    public static String SOTW_HIDE_PLAYERS_ENABLED;
    public static String SOTW_HIDE_PLAYERS_DISABLED;
    public static List<String> SOTW_ADMIN_COMMAND_USAGE;
    public static List<String> SOTW_PLAYER_COMMAND_USAGE;

    public static List<String> SPAWN_CREDITS_PLAYER_USAGE;
    public static List<String> SPAWN_CREDITS_ADMIN_USAGE;
    public static String SPAWN_CREDITS_AMOUNT_CHANGED;
    public static String SPAWN_CREDITS_AMOUNT_CHANGED_STAFF;
    public static String SPAWN_CREDITS_AMOUNT_CHECK_SELF;
    public static String SPAWN_CREDITS_AMOUNT_CHECK_OTHERS;

    public static String SPAWN_CREDITS_NOT_ENOUGH_CREDITS;
    public static String SPAWN_CREDITS_TELEPORTED;

    public static String SPAWN_DOESNT_EXIST;
    public static String SPAWN_ALREADY_TELEPORTING;
    public static String SPAWN_TELEPORTED;
    public static String SPAWN_TELEPORT_STARTED;
    public static String SPAWN_TELEPORTED_KITMAP;
    public static String SPAWN_TELEPORT_CANCELLED_DAMAGE;
    public static String SPAWN_TELEPORT_CANCELLED_MOVED;

    public static String SPAWNER_USAGE;
    public static String SPAWNER_MUST_LOOK_AT_SPAWNER;
    public static String SPAWNER_UPDATED_SPAWNER;

    public static String SPEED_USAGE;
    public static String SPEED_LIMIT_REACHED;
    public static String SPEED_SPEED_CHANGED;

    public static String STAFF_CHAT_ENABLED;
    public static String STAFF_CHAT_DISABLED;
    public static String STAFF_CHAT_FORMAT;

    public static String STAFF_MODE_ENABLED;
    public static String STAFF_MODE_DISABLED;
    public static String STAFF_MODE_ENABLED_OTHERS;
    public static String STAFF_MODE_DISABLED_OTHERS;
    public static String STAFF_MODE_RANDOM_TELEPORT_MESSAGE;
    public static String STAFF_MODE_RANDOM_TELEPORT_NO_PLAYER_MESSAGE;
    public static String STAFF_MODE_INVENTORY_INSPECT_MESSAGE;
    public static String STAFF_MODE_DAMAGE_DENY;
    public static String STAFF_MODE_INTERACT_DENY;
    public static String STAFF_MODE_PLACE_DENY;
    public static String STAFF_MODE_BREAK_DENY;

    public static String STAFF_SCOREBOARD_ENABLED;
    public static String STAFF_SCOREBOARD_DISABLED;

    public static String SUBCLAIMS_SUCCESSFULLY_CREATED;
    public static String SUBCLAIMS_CAN_NOT_DESTROY_SIGN;
    public static String SUBCLAIMS_CAN_NOT_DESTROY_SUBCLAIM;
    public static String SUBCLAIMS_CAN_NOT_OPEN;
    public static String SUBCLAIMS_NOT_IN_OWN_CLAIM;
    public static String SUBCLAIMS_ALREADY_EXISTS;
    public static String SUBCLAIMS_MUST_BE_LEADER;
    public static String SUBCLAIMS_MUST_BE_CO_LEADER;
    public static String SUBCLAIMS_MUST_BE_CAPTAIN;

    public static String TELEPORT_USAGE;
    public static String TELEPORT_MESSAGE;

    public static String TELEPORT_ALL_MESSAGE;

    public static String TELEPORT_HERE_USAGE;
    public static String TELEPORT_HERE_MESSAGE;

    public static String TELEPORT_POSITION_USAGE;
    public static String TELEPORT_POSITION_MESSAGE;

    public static String TELL_LOCATION_MESSAGE;
    public static String TELL_LOCATION_NOT_IN_FACTION;

    public static String TIME_MESSAGE_DAY;
    public static String TIME_MESSAGE_NIGHT;

    public static String TIMER_USAGE;
    public static String TIMER_NOT_CHANGEABLE;
    public static String TIMER_CHANGED;
    public static String TIMER_CHANGED_SENDER;

    public static String TOGGLE_COBBLE_TOGGLED_ON;
    public static String TOGGLE_COBBLE_TOGGLED_OFF;

    public static String TOGGLE_DEATHMESSAGES_TOGGLED_ON;
    public static String TOGGLE_DEATHMESSAGES_TOGGLED_OFF;

    public static String TOGGLE_FOUND_ORE_TOGGLED_ON;
    public static String TOGGLE_FOUND_ORE_TOGGLED_OFF;

    public static String TOGGLE_LIGHTNING_TOGGLED_ON;
    public static String TOGGLE_LIGHTNING_TOGGLED_OFF;

    public static String TOGGLE_MESSAGES_TOGGLED_ON;
    public static String TOGGLE_MESSAGES_TOGGLED_OFF;

    public static String TOGGLE_PUBLIC_CHAT_TOGGLED_ON;
    public static String TOGGLE_PUBLIC_CHAT_TOGGLED_OFF;

    public static String TOGGLE_SCOREBOARD_TOGGLED_ON;
    public static String TOGGLE_SCOREBOARD_TOGGLED_OFF;

    public static String TOGGLE_SOUNDS_TOGGLED_ON;
    public static String TOGGLE_SOUNDS_TOGGLED_OFF;

    public static String TOP_COMMAND_MESSAGE;

    public static String UNFOCUS_NOT_FOCUSING;
    public static String UNFOCUS_UNFOCUSED;

    public static String UNREPAIRABLE_ITEM_MESSAGE;

    public static String VANISH_ENABLED;
    public static String VANISH_DISABLED;
    public static String VANISH_ENABLED_OTHERS;
    public static String VANISH_DISABLED_OTHERS;
    public static String VANISH_ENABLED_STAFF_BROADCAST;
    public static String VANISH_DISABLED_STAFF_BROADCAST;
    public static String VANISH_BUILD_ENABLED;
    public static String VANISH_BUILD_DISABLED;
    public static String VANISH_CHEST_MESSAGE;
    public static String VANISH_DAMAGE_DENY;
    public static String VANISH_INTERACT_DENY;
    public static String VANISH_PLACE_DENY;
    public static String VANISH_BREAK_DENY;

    public static String VIEW_DISTANCE_ALREADY_RUNNING;
    public static String VIEW_DISTANCE_FINISHED;
    public static List<String> VIEW_DISTANCE_STARTED;
    public static List<String> VIEW_DISTANCE_USAGE;

    public static String WARP_LIST;
    public static String WARP_TELEPORTED;
    public static String WARP_DOESNT_EXIST;

    public static String WORLD_COMMAND_USAGE;
    public static String WORLD_DOESNT_EXIST;

    public static List<String> COORDS_MESSAGE;
    public static List<String> HELP_MESSAGE;
    public static List<String> SUBCLAIM_MESSAGE;

    public static String getYesOrNo(boolean condition) {
        return condition ? YES_PLACEHOLDER : NO_PLACEHOLDER;
    }

    public static String getTrueOrFalse(boolean condition) {
        return condition ? TRUE_PLACEHOLDER : FALSE_PLACEHOLDER;
    }

    public static String getEnabledOrDisabled(boolean condition) {
        return condition ? ENABLED_PLACEHOLDER : DISABLED_PLACEHOLDER;
    }

    public Language() {
        ConfigFile language = Lazarus.getInstance().getLanguage();

        PREFIX = language.getString("PREFIX");
        FACTION_PREFIX = language.getString("FACTION_PREFIX");
        KIT_PREFIX = language.getString("KIT_PREFIX");
        ABILITIES_PREFIX = language.getString("ABILITIES_PREFIX");
        CONQUEST_PREFIX = language.getString("CONQUEST_PREFIX");
        DTC_PREFIX = language.getString("DTC_PREFIX");
        KING_PREFIX = language.getString("KING_PREFIX");
        KOTH_PREFIX = language.getString("KOTH_PREFIX");
        MOUNTAIN_PREFIX = language.getString("MOUNTAIN_PREFIX");
        ENDER_DRAGON_PREFIX = language.getString("ENDER_DRAGON_PREFIX");
        LOOT_PREFIX = language.getString("LOOT_PREFIX");
        SCHEDULE_PREFIX = language.getString("SCHEDULE_PREFIX");
        HOLOGRAMS_PREFIX = language.getString("HOLOGRAMS_PREFIX");

        YES_PLACEHOLDER = language.getString("YES_PLACEHOLDER");
        NO_PLACEHOLDER = language.getString("NO_PLACEHOLDER");

        TRUE_PLACEHOLDER = language.getString("TRUE_PLACEHOLDER");
        FALSE_PLACEHOLDER = language.getString("FALSE_PLACEHOLDER");

        ENABLED_PLACEHOLDER = language.getString("ENABLED_PLACEHOLDER");
        DISABLED_PLACEHOLDER = language.getString("DISABLED_PLACEHOLDER");

        NONE_PLACEHOLDER = language.getString("NONE_PLACEHOLDER");

        WORLD_NAMES_OVERWORLD = language.getString("WORLD_NAMES.OVERWORLD");
        WORLD_NAMES_NETHER = language.getString("WORLD_NAMES.NETHER");
        WORLD_NAMES_THE_END = language.getString("WORLD_NAMES.THE_END");

        STAFF_JOIN_MESSAGE = language.getString("STAFF_JOIN_MESSAGE");
        STAFF_JOIN_MESSAGE_VANISHED = language.getString("STAFF_JOIN_MESSAGE_VANISHED");
        STAFF_QUIT_MESSAGE = language.getString("STAFF_QUIT_MESSAGE");

        JOIN_WELCOME_MESSAGE = language.getStringList("JOIN_WELCOME_MESSAGE");

        HARD_RESET_COMMAND_USAGE = language.getStringList("HARD_RESET_COMMAND_USAGE");

        USERDATA_FAILED_TO_LOAD = language.getString("USERDATA_FAILED_TO_LOAD");
        PLAYER_ALREADY_ONLINE = language.getString("PLAYER_ALREADY_ONLINE");

        COMMANDS_FOR_PLAYERS_ONLY = language.getString("COMMANDS.FOR_PLAYERS_ONLY");
        COMMANDS_FOR_CONSOLE_ONLY = language.getString("COMMANDS.FOR_CONSOLE_ONLY");
        COMMANDS_NO_PERMISSION = language.getString("COMMANDS.NO_PERMISSION");
        COMMANDS_PLAYER_NOT_ONLINE = language.getString("COMMANDS.PLAYER_NOT_ONLINE");
        COMMANDS_PLAYER_NOT_FOUND = language.getString("COMMANDS.PLAYER_NOT_FOUND");
        COMMANDS_COMMAND_NOT_FOUND = language.getString("COMMANDS.COMMAND_NOT_FOUND");
        COMMANDS_INVALID_NUMBER = language.getString("COMMANDS.INVALID_NUMBER");
        COMMANDS_INVALID_DURATION = language.getString("COMMANDS.INVALID_DURATION");
        COMMANDS_COOLDOWN = language.getString("COMMANDS.COOLDOWN");

        PLUGIN_RELOAD_MESSAGE = language.getString("PLUGIN_RELOAD_MESSAGE");

        ONLINE_RANK_ANNOUNCER_MESSAGE = language.getStringList("ONLINE_RANK_ANNOUNCER.MESSAGE");
        ONLINE_RANK_ANNOUNCER_DELIMITER = language.getString("ONLINE_RANK_ANNOUNCER.DELIMITER");

        ABILITIES_DENY_USAGE_WORLD = language.getString("ABILITIES.DENY_USAGE_WORLD");
        ABILITIES_DENY_USAGE_DISTANCE = language.getString("ABILITIES.DENY_USAGE_DISTANCE");
        ABILITIES_GLOBAL_COOLDOWN_ACTIVE = language.getString("ABILITIES.GLOBAL_COOLDOWN.ACTIVE");
        ABILITIES_ABILITY_COOLDOWN_ACTIVE = language.getString("ABILITIES.ABILITY_COOLDOWN.ACTIVE");
        ABILITIES_GLOBAL_COOLDOWN_EXPIRED = language.getString("ABILITIES.GLOBAL_COOLDOWN.EXPIRED");
        ABILITIES_ABILITY_COOLDOWN_EXPIRED = language.getString("ABILITIES.ABILITY_COOLDOWN.EXPIRED");

        ABILITIES_ABILITY_COMMAND_USAGE = language.getString("ABILITIES.ABILITY_COMMAND.USAGE");
        ABILITIES_ABILITY_COMMAND_NOT_FOUND = language.getString("ABILITIES.ABILITY_COMMAND.NOT_FOUND");
        ABILITIES_ABILITY_COMMAND_NOT_ENABLED = language.getString("ABILITIES.ABILITY_COMMAND.NOT_ENABLED");
        ABILITIES_ABILITY_COMMAND_GIVE_ABILITY = language.getString("ABILITIES.ABILITY_COMMAND.GIVE_ABILITY");
        ABILITIES_ABILITY_COMMAND_INVENTORY_FULL = language.getString("ABILITIES.ABILITY_COMMAND.INVENTORY_FULL");

        ABILITIES_ABILITY_TIMER_COMMAND_USAGE = language.getString("ABILITIES.ABILITY_TIMER_COMMAND.USAGE");
        ABILITIES_ABILITY_TIMER_COMMAND_NOT_FOUND = language.getString("ABILITIES.ABILITY_TIMER_COMMAND.NOT_FOUND");
        ABILITIES_ABILITY_TIMER_COMMAND_NOT_ENABLED = language.getString("ABILITIES.ABILITY_TIMER_COMMAND.NOT_ENABLED");
        ABILITIES_ABILITY_TIMER_COMMAND_CHANGED = language.getString("ABILITIES.ABILITY_TIMER_COMMAND.CHANGED");
        ABILITIES_ABILITY_TIMER_COMMAND_CHANGED_SENDER = language.getString("ABILITIES.ABILITY_TIMER_COMMAND.CHANGED_SENDER");

        ABILITIES_AGGRESSIVE_PEARL_EFFECT_FORMAT = language.getString("ABILITIES.AGGRESSIVE_PEARL_ABILITY.EFFECT_FORMAT");

        ABILITIES_ANTI_ABILITY_BALL_TARGET_ACTIVATED = language.getString("ABILITIES.ANTI_ABILITY_BALL_ABILITY.TARGET_ACTIVATED");

        ABILITIES_ANTI_REDSTONE_TARGET_ACTIVATED = language.getString("ABILITIES.ANTI_REDSTONE_ABILITY.TARGET_ACTIVATED");
        ABILITIES_ANTI_REDSTONE_TARGET_EXPIRED = language.getString("ABILITIES.ANTI_REDSTONE_ABILITY.TARGET_EXPIRED");
        ABILITIES_ANTI_REDSTONE_CANNOT_USE = language.getString("ABILITIES.ANTI_REDSTONE_ABILITY.CANNOT_USE");

        ABILITIES_ANTI_TRAP_STAR_TARGET_ACTIVATED = language.getString("ABILITIES.ANTI_TRAP_STAR_ABILITY.TARGET_ACTIVATED");
        ABILITIES_ANTI_TRAP_STAR_TARGET_TELEPORTED = language.getString("ABILITIES.ANTI_TRAP_STAR_ABILITY.TARGET_TELEPORTED");
        ABILITIES_ANTI_TRAP_STAR_PLAYER_TELEPORTED = language.getString("ABILITIES.ANTI_TRAP_STAR_ABILITY.PLAYER_TELEPORTED");
        ABILITIES_ANTI_TRAP_STAR_CANNOT_USE = language.getString("ABILITIES.ANTI_TRAP_STAR_ABILITY.CANNOT_USE");

        ABILITIES_COCAINE_EFFECT_FORMAT = language.getString("ABILITIES.COCAINE_ABILITY.EFFECT_FORMAT");

        ABILITIES_COMBO_EFFECT_FORMAT = language.getString("ABILITIES.COMBO_ABILITY.EFFECT_FORMAT");
        ABILITIES_COMBO_APPLY_EFFECTS = language.getStringList("ABILITIES.COMBO_ABILITY.APPLY_EFFECTS");

        ABILITIES_DECOY_BECOME_VISIBLE_ON_DAMAGE = language.getString("ABILITIES.DECOY_ABILITY.BECOME_VISIBLE_ON_DAMAGE");
        ABILITIES_DECOY_BECOME_VISIBLE_ON_EXPIRE = language.getString("ABILITIES.DECOY_ABILITY.BECOME_VISIBLE_ON_EXPIRE");

        ABILITIES_EXOTIC_BONE_TARGET_ACTIVATED = language.getString("ABILITIES.EXOTIC_BONE_ABILITY.TARGET_ACTIVATED");
        ABILITIES_EXOTIC_BONE_TARGET_EXPIRED = language.getString("ABILITIES.EXOTIC_BONE_ABILITY.TARGET_EXPIRED");
        ABILITIES_EXOTIC_BONE_CANNOT_INTERACT = language.getString("ABILITIES.EXOTIC_BONE_ABILITY.CANNOT_INTERACT");

        ABILITIES_GUARDIAN_ANGEL_HEALED = language.getString("ABILITIES.GUARDIAN_ANGEL_ABILITY.HEALED");
        ABILITIES_GUARDIAN_ANGEL_EXPIRED = language.getString("ABILITIES.GUARDIAN_ANGEL_ABILITY.EXPIRED");

        ABILITIES_INVISIBILITY_BECOME_VISIBLE_ON_DAMAGE = language.getString("ABILITIES.INVISIBILITY_ABILITY.BECOME_VISIBLE_ON_DAMAGE");
        ABILITIES_LUCKY_INGOT_EFFECT_FORMAT = language.getString("ABILITIES.LUCKY_INGOT_ABILITY.EFFECT_FORMAT");
        ABILITIES_POCKET_BARD_EFFECT_FORMAT = language.getString("ABILITIES.POCKET_BARD_ABILITY.EFFECT_FORMAT");
        ABILITIES_PRE_PEARL_TELEPORTED = language.getString("ABILITIES.PRE_PEARL_ABILITY.TELEPORTED");

        ABILITIES_RAGE_EFFECT_FORMAT = language.getString("ABILITIES.RAGE_ABILITY.EFFECT_FORMAT");
        ABILITIES_RAGE_APPLY_EFFECTS = language.getStringList("ABILITIES.RAGE_ABILITY.APPLY_EFFECTS");

        ABILITIES_RAGE_BRICK_EFFECT_FORMAT = language.getString("ABILITIES.RAGE_BRICK_ABILITY.EFFECT_FORMAT");

        ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_SAFEZONE = language.getString("ABILITIES.SCRAMBLER_ABILITY.SCRAMBLE_DENIED.SAFEZONE");
        ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_SAFEZONE_TARGET = language.getString("ABILITIES.SCRAMBLER_ABILITY.SCRAMBLE_DENIED.SAFEZONE_TARGET");
        ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_PVP_TIMER = language.getString("ABILITIES.SCRAMBLER_ABILITY.SCRAMBLE_DENIED.PVP_TIMER");
        ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_PVP_TIMER_TARGET = language.getString("ABILITIES.SCRAMBLER_ABILITY.SCRAMBLE_DENIED.PVP_TIMER_TARGET");
        ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_SOTW = language.getString("ABILITIES.SCRAMBLER_ABILITY.SCRAMBLE_DENIED.SOTW");
        ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_SOTW_TARGET = language.getString("ABILITIES.SCRAMBLER_ABILITY.SCRAMBLE_DENIED.SOTW_TARGET");
        ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_TEAMMATES = language.getString("ABILITIES.SCRAMBLER_ABILITY.SCRAMBLE_DENIED.TEAMMATES");
        ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_ALLIES = language.getString("ABILITIES.SCRAMBLER_ABILITY.SCRAMBLE_DENIED.ALLIES");

        ABILITIES_SECOND_CHANCE_NOT_ON_COOLDOWN = language.getString("ABILITIES.SECOND_CHANCE_ABILITY.NOT_ON_COOLDOWN");

        ABILITIES_STARVATION_FLESH_TARGET_ACTIVATED = language.getString("ABILITIES.STARVATION_FLESH_ABILITY.TARGET_ACTIVATED");
        ABILITIES_STARVATION_FLESH_TARGET_EQUIPPED_CLASS = language.getString("ABILITIES.STARVATION_FLESH_ABILITY.TARGET_EQUIPPED_CLASS");

        ABILITIES_SWITCHER_SWITCH_DENIED_DISTANCE_TOO_FAR = language.getString("ABILITIES.SWITCHER_ABILITY.SWITCH_DENIED.DISTANCE_TOO_FAR");
        ABILITIES_SWITCHER_SWITCH_DENIED_SAFEZONE = language.getString("ABILITIES.SWITCHER_ABILITY.SWITCH_DENIED.SAFEZONE");
        ABILITIES_SWITCHER_SWITCH_DENIED_SAFEZONE_TARGET = language.getString("ABILITIES.SWITCHER_ABILITY.SWITCH_DENIED.SAFEZONE_TARGET");
        ABILITIES_SWITCHER_SWITCH_DENIED_PVP_TIMER = language.getString("ABILITIES.SWITCHER_ABILITY.SWITCH_DENIED.PVP_TIMER");
        ABILITIES_SWITCHER_SWITCH_DENIED_PVP_TIMER_TARGET = language.getString("ABILITIES.SWITCHER_ABILITY.SWITCH_DENIED.PVP_TIMER_TARGET");
        ABILITIES_SWITCHER_SWITCH_DENIED_SOTW = language.getString("ABILITIES.SWITCHER_ABILITY.SWITCH_DENIED.SOTW");
        ABILITIES_SWITCHER_SWITCH_DENIED_SOTW_TARGET = language.getString("ABILITIES.SWITCHER_ABILITY.SWITCH_DENIED.SOTW_TARGET");
        ABILITIES_SWITCHER_SWITCH_DENIED_TEAMMATES = language.getString("ABILITIES.SWITCHER_ABILITY.SWITCH_DENIED.TEAMMATES");
        ABILITIES_SWITCHER_SWITCH_DENIED_ALLIES = language.getString("ABILITIES.SWITCHER_ABILITY.SWITCH_DENIED.ALLIES");

        ABILITIES_SWITCH_STICK_TARGET_ACTIVATED = language.getString("ABILITIES.SWITCH_STICK_ABILITY.TARGET_ACTIVATED");

        ABILITIES_TANK_INGOT_EFFECT_FORMAT = language.getString("ABILITIES.TANK_INGOT_ABILITY.EFFECT_FORMAT");

        ABILITIES_WEB_GUN_COBWEB_REMOVE = language.getString("ABILITIES.WEB_GUN_ABILITY.COBWEB_REMOVE");

        BLOCKS_PLACEMENT_DISABLED = language.getString("BLOCKS.PLACEMENT_DISABLED");

        ITEMS_NOT_HOLDING = language.getString("ITEMS.NOT_HOLDING");
        ITEMS_INVALID_NAME = language.getString("ITEMS.INVALID_NAME");

        ENTITIES_INVALID_ENTITY = language.getString("ENTITIES.INVALID_ENTITY");

        ARCHER_CLICKABLE_ACTIVATED = language.getString("ARCHER_CLASS.CLICKABLE_ACTIVATED");
        ARCHER_CLICKABLE_COOLDOWN = language.getString("ARCHER_CLASS.CLICKABLE_COOLDOWN");
        ARCHER_COOLDOWN_EXPIRED = language.getString("ARCHER_CLASS.COOLDOWN_EXPIRED");

        ARCHER_TAG_EXPIRED_MESSAGE = language.getString("ARCHER_TAG.EXPIRED_MESSAGE");
        ARCHER_TAG_TAGGED_VICTIM = language.getString("ARCHER_TAG.TAGGED_VICTIM");
        ARCHER_TAG_TAGGED_TAGGER = language.getString("ARCHER_TAG.TAGGED_TAGGER");

        BARD_VANISHED_OR_IN_STAFFMODE = language.getString("BARD_CLASS.VANISHED_OR_IN_STAFFMODE");
        BARD_CAN_NOT_BARD_TO_YOURSELF = language.getString("BARD_CLASS.CAN_NOT_BARD_TO_YOURSELF");
        BARD_CAN_NOT_BARD_WITH_PVP_TIMER = language.getString("BARD_CLASS.CAN_NOT_BARD_WITH_PVP_TIMER");
        BARD_CAN_NOT_BARD_IN_SAFEZONE = language.getString("BARD_CLASS.CAN_NOT_BARD_IN_SAFEZONE");
        BARD_CAN_NOT_BARD_WHEN_SOTW_NOT_ENABLED = language.getString("BARD_CLASS.CAN_NOT_BARD_WHEN_SOTW_NOT_ENABLED");
        BARD_CLICKABLE_ACTIVE_COOLDOWN = language.getString("BARD_CLASS.CLICKABLE_ITEM.ACTIVE_COOLDOWN");
        BARD_CLICKABLE_COOLDOWN_EXPIRED = language.getString("BARD_CLASS.CLICKABLE_ITEM.COOLDOWN_EXPIRED");
        BARD_CLICKABLE_NOT_ENOUGH_ENERGY = language.getString("BARD_CLASS.CLICKABLE_ITEM.NOT_ENOUGH_ENERGY");
        BARD_CLICKABLE_MESSAGE_FRIENDLY = language.getString("BARD_CLASS.CLICKABLE_ITEM.MESSAGE_FRIENDLY");
        BARD_CLICKABLE_MESSAGE_ENEMY = language.getString("BARD_CLASS.CLICKABLE_ITEM.MESSAGE_ENEMY");
        BARD_CLICKABLE_MESSAGE_OTHERS = language.getString("BARD_CLASS.CLICKABLE_ITEM.MESSAGE_OTHERS");

        MINER_EFFECT_REWARD = language.getString("MINER_CLASS.EFFECT_REWARD");

        ROGUE_CLICKABLE_ACTIVATED = language.getString("ROGUE_CLASS.CLICKABLE_ACTIVATED");
        ROGUE_CLICKABLE_COOLDOWN = language.getString("ROGUE_CLASS.CLICKABLE_COOLDOWN");
        ROGUE_COOLDOWN_EXPIRED = language.getString("ROGUE_CLASS.COOLDOWN_EXPIRED");
        ROGUE_BACKSTAB_COOLDOWN = language.getString("ROGUE_CLASS.BACKSTAB_COOLDOWN");
        ROGUE_BACKSTAB_COOLDOWN_EXPIRED = language.getString("ROGUE_CLASS.BACKSTAB_COOLDOWN_EXPIRED");

        MAGE_VANISHED_OR_IN_STAFFMODE = language.getString("MAGE_CLASS.VANISHED_OR_IN_STAFFMODE");
        MAGE_CAN_NOT_USE_MAGE_WITH_PVP_TIMER = language.getString("MAGE_CLASS.CAN_NOT_USE_MAGE_WITH_PVP_TIMER");
        MAGE_CAN_NOT_USE_MAGE_IN_SAFEZONE = language.getString("MAGE_CLASS.CAN_NOT_USE_MAGE_IN_SAFEZONE");
        MAGE_CAN_NOT_USE_MAGE_WHEN_SOTW_NOT_ENABLED = language.getString("MAGE_CLASS.CAN_NOT_USE_MAGE_WHEN_SOTW_NOT_ENABLED");
        MAGE_CLICKABLE_ACTIVE_COOLDOWN = language.getString("MAGE_CLASS.CLICKABLE_ITEM.ACTIVE_COOLDOWN");
        MAGE_CLICKABLE_COOLDOWN_EXPIRED = language.getString("MAGE_CLASS.CLICKABLE_ITEM.COOLDOWN_EXPIRED");
        MAGE_CLICKABLE_NOT_ENOUGH_ENERGY = language.getString("MAGE_CLASS.CLICKABLE_ITEM.NOT_ENOUGH_ENERGY");
        MAGE_CLICKABLE_MESSAGE_ENEMY = language.getString("MAGE_CLASS.CLICKABLE_ITEM.MESSAGE_ENEMY");
        MAGE_CLICKABLE_MESSAGE_OTHERS = language.getString("MAGE_CLASS.CLICKABLE_ITEM.MESSAGE_OTHERS");

        BOMBER_VANISHED_OR_IN_STAFFMODE = language.getString("BOMBER_CLASS.VANISHED_OR_IN_STAFFMODE");
        BOMBER_CAN_NOT_SHOOT_TNT_WITH_PVP_TIMER = language.getString("BOMBER_CLASS.CAN_NOT_SHOOT_TNT_WITH_PVP_TIMER");
        BOMBER_CAN_NOT_SHOOT_TNT_IN_SAFEZONE = language.getString("BOMBER_CLASS.CAN_NOT_SHOOT_TNT_IN_SAFEZONE");
        BOMBER_CAN_NOT_SHOOT_TNT_WHEN_SOTW_NOT_ENABLED = language.getString("BOMBER_CLASS.CAN_NOT_SHOOT_TNT_WHEN_SOTW_NOT_ENABLED");
        BOMBER_TNT_GUN_ACTIVE_COOLDOWN = language.getString("BOMBER_CLASS.TNT_GUN.ACTIVE_COOLDOWN");
        BOMBER_TNT_GUN_COOLDOWN_EXPIRED = language.getString("BOMBER_CLASS.TNT_GUN.COOLDOWN_EXPIRED");

        HOLOGRAMS_COMMAND_HEADER = language.getString("HOLOGRAMS.COMMAND_HEADER");
        HOLOGRAMS_COMMAND_FOOTER =  language.getString("HOLOGRAMS.COMMAND_FOOTER");
        HOLOGRAMS_COMMAND_USAGE = language.getStringList("HOLOGRAMS.COMMAND_USAGE");

        HOLOGRAMS_EXCEPTIONS_DOESNT_EXIST = language.getString("HOLOGRAMS.EXCEPTIONS.DOESNT_EXIST");
        HOLOGRAMS_EXCEPTIONS_TYPE_NOT_FOUND = language.getString("HOLOGRAMS.EXCEPTIONS.TYPE_NOT_FOUND");
        HOLOGRAMS_EXCEPTIONS_TYPE_MUST_BE_NORMAL = language.getString("HOLOGRAMS.EXCEPTIONS.TYPE_MUST_BE_NORMAL");
        HOLOGRAMS_EXCEPTIONS_LINE_DOESNT_EXIST = language.getString("HOLOGRAMS.EXCEPTIONS.LINE_DOESNT_EXIST");
        HOLOGRAMS_EXCEPTIONS_CANT_INSERT_LINE = language.getString("HOLOGRAMS.EXCEPTIONS.CANT_INSERT_LINE");

        HOLOGRAMS_ADD_LINE_USAGE = language.getString("HOLOGRAMS.ADD_LINE_COMMAND.USAGE");
        HOLOGRAMS_ADD_LINE_LINE_ADDED = language.getString("HOLOGRAMS.ADD_LINE_COMMAND.LINE_ADDED");

        HOLOGRAMS_CREATE_USAGE = language.getString("HOLOGRAMS.CREATE_COMMAND.USAGE");
        HOLOGRAMS_CREATE_CREATED = language.getString("HOLOGRAMS.CREATE_COMMAND.CREATED");

        HOLOGRAMS_DELETE_USAGE = language.getString("HOLOGRAMS.DELETE_COMMAND.USAGE");
        HOLOGRAMS_DELETE_DELETED = language.getString("HOLOGRAMS.DELETE_COMMAND.DELETED");

        HOLOGRAMS_INSERT_LINE_USAGE = language.getString("HOLOGRAMS.INSERT_LINE_COMMAND.USAGE");
        HOLOGRAMS_INSERT_LINE_INSERTED = language.getString("HOLOGRAMS.INSERT_LINE_COMMAND.LINE_INSERTED");

        HOLOGRAMS_LIST_NO_HOLOGRAMS = language.getString("HOLOGRAMS.LIST_COMMAND.NO_HOLOGRAMS");
        HOLOGRAMS_LIST_TITLE = language.getString("HOLOGRAMS.LIST_COMMAND.TITLE");
        HOLOGRAMS_LIST_FORMAT = language.getString("HOLOGRAMS.LIST_COMMAND.FORMAT");

        HOLOGRAMS_REMOVE_LINE_USAGE = language.getString("HOLOGRAMS.REMOVE_LINE_COMMAND.USAGE");
        HOLOGRAMS_REMOVE_LINE_REMOVED = language.getString("HOLOGRAMS.REMOVE_LINE_COMMAND.LINE_REMOVED");

        HOLOGRAMS_TELEPORT_USAGE = language.getString("HOLOGRAMS.TELEPORT_COMMAND.USAGE");
        HOLOGRAMS_TELEPORT_TELEPORTED = language.getString("HOLOGRAMS.TELEPORT_COMMAND.TELEPORTED");

        HOLOGRAMS_TELEPORT_HERE_USAGE = language.getString("HOLOGRAMS.TELEPORT_HERE_COMMAND.USAGE");
        HOLOGRAMS_TELEPORT_HERE_TELEPORTED = language.getString("HOLOGRAMS.TELEPORT_HERE_COMMAND.TELEPORTED");

        HOLOGRAMS_UPDATE_LINE_USAGE = language.getString("HOLOGRAMS.UPDATE_LINE_COMMAND.USAGE");
        HOLOGRAMS_UPDATE_LINE_UPDATED = language.getString("HOLOGRAMS.UPDATE_LINE_COMMAND.LINE_UPDATED");

        HOLOGRAM_EMPTY_LINE_FORMAT = language.getString("HOLOGRAM_LEADERBOARDS.EMPTY_LINE_FORMAT");

        HOLOGRAM_TOP_KILLS_HEADER = language.getStringList("HOLOGRAM_LEADERBOARDS.TOP_KILLS.HEADER");
        HOLOGRAM_TOP_KILLS_FOOTER = language.getStringList("HOLOGRAM_LEADERBOARDS.TOP_KILLS.FOOTER");
        HOLOGRAM_TOP_KILLS_LINE_FORMAT = language.getString("HOLOGRAM_LEADERBOARDS.TOP_KILLS.LINE_FORMAT");

        HOLOGRAM_TOP_DEATHS_HEADER = language.getStringList("HOLOGRAM_LEADERBOARDS.TOP_DEATHS.HEADER");
        HOLOGRAM_TOP_DEATHS_FOOTER = language.getStringList("HOLOGRAM_LEADERBOARDS.TOP_DEATHS.FOOTER");
        HOLOGRAM_TOP_DEATHS_LINE_FORMAT = language.getString("HOLOGRAM_LEADERBOARDS.TOP_DEATHS.LINE_FORMAT");

        HOLOGRAM_TOP_BALANCE_HEADER = language.getStringList("HOLOGRAM_LEADERBOARDS.TOP_BALANCE.HEADER");
        HOLOGRAM_TOP_BALANCE_FOOTER = language.getStringList("HOLOGRAM_LEADERBOARDS.TOP_BALANCE.FOOTER");
        HOLOGRAM_TOP_BALANCE_LINE_FORMAT = language.getString("HOLOGRAM_LEADERBOARDS.TOP_BALANCE.LINE_FORMAT");

        HOLOGRAM_TOP_KILLSTREAK_HEADER = language.getStringList("HOLOGRAM_LEADERBOARDS.TOP_KILLSTREAK.HEADER");
        HOLOGRAM_TOP_KILLSTREAK_FOOTER = language.getStringList("HOLOGRAM_LEADERBOARDS.TOP_KILLSTREAK.FOOTER");
        HOLOGRAM_TOP_KILLSTREAK_LINE_FORMAT = language.getString("HOLOGRAM_LEADERBOARDS.TOP_KILLSTREAK.LINE_FORMAT");

        HOLOGRAM_FACTION_TOP_KILLS_HEADER = language.getStringList("HOLOGRAM_LEADERBOARDS.FACTION_TOP_KILLS.HEADER");
        HOLOGRAM_FACTION_TOP_KILLS_FOOTER = language.getStringList("HOLOGRAM_LEADERBOARDS.FACTION_TOP_KILLS.FOOTER");
        HOLOGRAM_FACTION_TOP_KILLS_LINE_FORMAT = language.getString("HOLOGRAM_LEADERBOARDS.FACTION_TOP_KILLS.LINE_FORMAT");

        HOLOGRAM_FACTION_TOP_POINTS_HEADER = language.getStringList("HOLOGRAM_LEADERBOARDS.FACTION_TOP_POINTS.HEADER");
        HOLOGRAM_FACTION_TOP_POINTS_FOOTER = language.getStringList("HOLOGRAM_LEADERBOARDS.FACTION_TOP_POINTS.FOOTER");
        HOLOGRAM_FACTION_TOP_POINTS_LINE_FORMAT = language.getString("HOLOGRAM_LEADERBOARDS.FACTION_TOP_POINTS.LINE_FORMAT");

        HOLOGRAM_FACTION_TOP_BALANCE_HEADER = language.getStringList("HOLOGRAM_LEADERBOARDS.FACTION_TOP_BALANCE.HEADER");
        HOLOGRAM_FACTION_TOP_BALANCE_FOOTER = language.getStringList("HOLOGRAM_LEADERBOARDS.FACTION_TOP_BALANCE.FOOTER");
        HOLOGRAM_FACTION_TOP_BALANCE_LINE_FORMAT = language.getString("HOLOGRAM_LEADERBOARDS.FACTION_TOP_BALANCE.LINE_FORMAT");

        HOLOGRAM_FACTION_TOP_KOTHS_CAPPED_HEADER = language.getStringList("HOLOGRAM_LEADERBOARDS.FACTION_TOP_KOTHS_CAPPED.HEADER");
        HOLOGRAM_FACTION_TOP_KOTHS_CAPPED_FOOTER = language.getStringList("HOLOGRAM_LEADERBOARDS.FACTION_TOP_KOTHS_CAPPED.FOOTER");
        HOLOGRAM_FACTION_TOP_KOTHS_CAPPED_LINE_FORMAT = language.getString("HOLOGRAM_LEADERBOARDS.FACTION_TOP_KOTHS_CAPPED.LINE_FORMAT");

        LEADERBOARDS_COMMAND_USAGE = language.getString("LEADERBOARDS.COMMAND_USAGE");
        LEADERBOARDS_FACTION_COMMAND_USAGE = language.getString("LEADERBOARDS.FACTION_COMMAND_USAGE");
        LEADERBOARDS_TYPE_DOESNT_EXIST = language.getString("LEADERBOARDS.TYPE_DOESNT_EXIST");
        LEADERBOARDS_KITMAP_MODE_ONLY = language.getString("LEADERBOARDS.KITMAP_MODE_ONLY");
        LEADERBOARDS_NO_LEADERBOARDS = language.getString("LEADERBOARDS.NO_LEADERBOARDS");
        LEADERBOARDS_COMMAND_HEADER = language.getString("LEADERBOARDS.COMMAND_HEADER");
        LEADERBOARDS_COMMAND_FOOTER = language.getString("LEADERBOARDS.COMMAND_FOOTER");

        LEADERBOARDS_KILLS_TITLE = language.getString("LEADERBOARDS.TOP_KILLS.TITLE");
        LEADERBOARDS_KILLS_LINE_FORMAT = language.getString("LEADERBOARDS.TOP_KILLS.LINE_FORMAT");

        LEADERBOARDS_DEATHS_TITLE = language.getString("LEADERBOARDS.TOP_DEATHS.TITLE");
        LEADERBOARDS_DEATHS_LINE_FORMAT = language.getString("LEADERBOARDS.TOP_DEATHS.LINE_FORMAT");

        LEADERBOARDS_BALANCE_TITLE = language.getString("LEADERBOARDS.TOP_BALANCE.TITLE");
        LEADERBOARDS_BALANCE_LINE_FORMAT = language.getString("LEADERBOARDS.TOP_BALANCE.LINE_FORMAT");

        LEADERBOARDS_KILLSTREAK_TITLE = language.getString("LEADERBOARDS.TOP_KILLSTREAK.TITLE");
        LEADERBOARDS_KILLSTREAK_LINE_FORMAT = language.getString("LEADERBOARDS.TOP_KILLSTREAK.LINE_FORMAT");

        LEADERBOARDS_FACTION_KILLS_TITLE = language.getString("LEADERBOARDS.FACTION_TOP_KILLS.TITLE");
        LEADERBOARDS_FACTION_KILLS_LINE_FORMAT = language.getString("LEADERBOARDS.FACTION_TOP_KILLS.LINE_FORMAT");

        LEADERBOARDS_FACTION_POINTS_TITLE = language.getString("LEADERBOARDS.FACTION_TOP_POINTS.TITLE");
        LEADERBOARDS_FACTION_POINTS_LINE_FORMAT = language.getString("LEADERBOARDS.FACTION_TOP_POINTS.LINE_FORMAT");

        LEADERBOARDS_FACTION_BALANCE_TITLE = language.getString("LEADERBOARDS.FACTION_TOP_BALANCE.TITLE");
        LEADERBOARDS_FACTION_BALANCE_LINE_FORMAT = language.getString("LEADERBOARDS.FACTION_TOP_BALANCE.LINE_FORMAT");

        LEADERBOARDS_FACTION_TOP_KOTHS_CAPPED_TITLE = language.getString("LEADERBOARDS.FACTION_TOP_KOTHS_CAPPED.TITLE");
        LEADERBOARDS_FACTION_TOP_KOTHS_CAPPED_LINE_FORMAT = language.getString("LEADERBOARDS.FACTION_TOP_KOTHS_CAPPED.LINE_FORMAT");

        SPAWNERS_DISABLE_PLACE_NETHER = language.getString("SPAWNERS.DISABLE_PLACE.NETHER");
        SPAWNERS_DISABLE_PLACE_END = language.getString("SPAWNERS.DISABLE_PLACE.END");
        SPAWNERS_DISABLE_BREAK_NETHER = language.getString("SPAWNERS.DISABLE_BREAK.NETHER");
        SPAWNERS_DISABLE_BREAK_END = language.getString("SPAWNERS.DISABLE_BREAK.END");

        FACTIONS_NAME_TOO_SHORT = language.getString("FACTIONS.NAME_TOO_SHORT");
        FACTIONS_NAME_TOO_BIG = language.getString("FACTIONS.NAME_TOO_BIG");
        FACTIONS_BLOCKED_FACTION_NAME = language.getString("FACTIONS.BLOCKED_FACTION_NAME");
        FACTIONS_NAME_NOT_ALPHANUMERIC = language.getString("FACTIONS.NAME_NOT_ALPHANUMERIC");
        FACTIONS_FACTION_ALREADY_EXISTS = language.getString("FACTIONS.FACTION_ALREADY_EXISTS");
        FACTIONS_FACTION_DOESNT_EXIST = language.getString("FACTIONS.FACTION_DOESNT_EXIST");
        FACTIONS_ALREADY_IN_FACTION_SELF = language.getString("FACTIONS.ALREADY_IN_FACTION_SELF");
        FACTIONS_ALREADY_IN_FACTION_OTHERS = language.getString("FACTIONS.ALREADY_IN_FACTION_OTHERS");
        FACTIONS_NOT_IN_FACTION = language.getString("FACTIONS.NOT_IN_FACTION");
        FACTIONS_NOT_IN_FACTION_SELF = language.getString("FACTIONS.NOT_IN_FACTION_SELF");
        FACTIONS_NOT_IN_FACTION_OTHERS = language.getString("FACTIONS.NOT_IN_FACTION_OTHERS");
        FACTIONS_MUST_BE_LEADER = language.getString("FACTIONS.MUST_BE_LEADER");
        FACTIONS_NO_PERMISSION = language.getString("FACTIONS.NO_PERMISSION");
        FACTIONS_NO_PERMISSION_ROLE = language.getString("FACTIONS.NO_PERMISSION_ROLE");
        FACTIONS_MEMBER_ONLINE = language.getString("FACTIONS.MEMBER_ONLINE");
        FACTIONS_MEMBER_OFFLINE = language.getString("FACTIONS.MEMBER_OFFLINE");
        FACTIONS_MEMBER_DEATH = language.getString("FACTIONS.MEMBER_DEATH");
        FACTIONS_ALLIES_DISABLED = language.getString("FACTIONS.ALLIES_DISABLED");
        FACTIONS_DENY_DAMAGE_ALLIES = language.getString("FACTIONS.DENY_DAMAGE_ALLIES");
        FACTIONS_ENDERPEARL_USAGE_DENIED = language.getString("FACTIONS.ENDERPEARLS_DISABLED");
        FACTIONS_ABILITIES_USAGE_DENIED = language.getString("FACTIONS.ABILITIES_DISABLED");
        FACTIONS_NO_LONGER_FROZEN = language.getString("FACTIONS.NO_LONGER_FROZEN");
        FACTIONS_RALLY_EXPIRED = language.getString("FACTIONS.RALLY_EXPIRED");

        FACTIONS_PVP_CLASS_LIMIT_DENY_EQUIP = language.getString("FACTIONS.PVP_CLASS_LIMIT.DENY_EQUIP");
        FACTIONS_PVP_CLASS_LIMIT_CLASS_DEACTIVATED = language.getString("FACTIONS.PVP_CLASS_LIMIT.CLASS_DEACTIVATED");

        FACTIONS_SAFEZONE_DENY_DAMAGE_ATTACKER = language.getString("FACTIONS.SAFEZONE.DENY_DAMAGE_ATTACKER");
        FACTIONS_SAFEZONE_DENY_DAMAGE_OTHERS = language.getString("FACTIONS.SAFEZONE.DENY_DAMAGE_OTHERS");

        FACTIONS_PROTECTION_DENY_BUILD = language.getString("FACTIONS.PROTECTION_MESSAGES.DENY_BUILD");
        FACTIONS_PROTECTION_DENY_INTERACT= language.getString("FACTIONS.PROTECTION_MESSAGES.DENY_INTERACT");

        FACTION_CHAT_FACTION_FORMAT = language.getString("FACTIONS.FACTION_CHAT.FACTION_FORMAT");
        FACTION_CHAT_ALLY_FORMAT = language.getString("FACTIONS.FACTION_CHAT.ALLY_FORMAT");

        FACTIONS_CHATSPY_ENABLED = language.getString("FACTIONS.CHATSPY_COMMAND.ENABLED");
        FACTIONS_CHATSPY_DISABLED = language.getString("FACTIONS.CHATSPY_COMMAND.DISABLED");
        FACTIONS_CHATSPY_FORMAT = language.getString("FACTIONS.CHATSPY_COMMAND.FORMAT");

        FACTIONS_CLEAR_CLAIMS_USAGE = language.getString("FACTIONS.CLEAR_CLAIMS_COMMAND.USAGE");
        FACTIONS_CLEAR_CLAIMS_CLEARED_SENDER = language.getString("FACTIONS.CLEAR_CLAIMS_COMMAND.CLEARED_SENDER");
        FACTIONS_CLEAR_CLAIMS_CLEARED_FACTION = language.getString("FACTIONS.CLEAR_CLAIMS_COMMAND.CLEARED_FACTION");
        FACTIONS_CLEAR_CLAIMS_HOME_TELEPORT_CANCELLED = language.getString("FACTIONS.CLEAR_CLAIMS_COMMAND.HOME_TELEPORT_CANCELLED");

        FACTIONS_CLAIM_ENTERING = language.getString("FACTIONS.CLAIM_MESSAGES.ENTERING");
        FACTIONS_CLAIM_LEAVING = language.getString("FACTIONS.CLAIM_MESSAGES.LEAVING");

        FACTIONS_ABILITIES_USAGE = language.getString("FACTIONS.ABILITIES_COMMAND.USAGE");
        FACTIONS_ABILITIES_NOT_SYSTEM_FACTION = language.getString("FACTIONS.ABILITIES_COMMAND.NOT_SYSTEM_FACTION");
        FACTIONS_ABILITIES_ENABLED = language.getString("FACTIONS.ABILITIES_COMMAND.ENABLED");
        FACTIONS_ABILITIES_DISABLED = language.getString("FACTIONS.ABILITIES_COMMAND.DISABLED");

        FACTIONS_ADD_DTR_USAGE = language.getString("FACTIONS.ADD_DTR_COMMAND.USAGE");
        FACTIONS_ADD_DTR_CHANGED_SENDER = language.getString("FACTIONS.ADD_DTR_COMMAND.CHANGED_SENDER");
        FACTIONS_ADD_DTR_CHANGED_FACTION = language.getString("FACTIONS.ADD_DTR_COMMAND.CHANGED_FACTION");

        FACTIONS_ALLY_USAGE = language.getString("FACTIONS.ALLY_COMMAND.USAGE");
        FACTIONS_ALLY_MAX_ALLIES_SELF = language.getString("FACTIONS.ALLY_COMMAND.MAX_ALLIES_SELF");
        FACTIONS_ALLY_MAX_ALLIES_OTHERS = language.getString("FACTIONS.ALLY_COMMAND.MAX_ALLIES_OTHERS");
        FACTIONS_ALLY_ALREADY_ALLIES = language.getString("FACTIONS.ALLY_COMMAND.ALREADY_ALLIES");
        FACTIONS_ALLY_CANNOT_ALLY_SELF = language.getString("FACTIONS.ALLY_COMMAND.CANNOT_ALLY_SELF");
        FACTIONS_ALLY_REQUEST_ALREADY_SENT = language.getString("FACTIONS.ALLY_COMMAND.REQUEST_ALREADY_SENT");
        FACTIONS_ALLY_REQUESTED_SELF = language.getString("FACTIONS.ALLY_COMMAND.REQUESTED_SELF");
        FACTIONS_ALLY_REQUESTED_OTHERS = language.getString("FACTIONS.ALLY_COMMAND.REQUESTED_OTHERS");
        FACTIONS_ALLY_ACCEPTED = language.getString("FACTIONS.ALLY_COMMAND.ACCEPTED");

        FACTIONS_ANNOUNCEMENT_USAGE = language.getString("FACTIONS.ANNOUNCEMENT_COMMAND.USAGE");
        FACTIONS_ANNOUNCEMENT_REMOVED = language.getString("FACTIONS.ANNOUNCEMENT_COMMAND.REMOVED");
        FACTIONS_ANNOUNCEMENT_MESSAGE = language.getString("FACTIONS.ANNOUNCEMENT_COMMAND.MESSAGE");

        FACTIONS_AUTO_REVIVE_ENABLED = language.getString("FACTIONS.AUTO_REVIVE_COMMAND.ENABLED");
        FACTIONS_AUTO_REVIVE_DISABLED = language.getString("FACTIONS.AUTO_REVIVE_COMMAND.DISABLED");

        FACTIONS_BALANCE_MESSAGE = language.getString("FACTIONS.BALANCE_COMMAND.MESSAGE");

        FACTIONS_CHAT_COMMAND_CHANGED = language.getString("FACTIONS.CHAT_COMMAND.CHANGED");

        FACTIONS_CLAIM_MAX_CLAIMS_EXCEEDED = language.getString("FACTIONS.CLAIM_COMMAND.MAX_CLAIMS_EXCEEDED");
        FACTIONS_CLAIM_CLAIMING_WAND_RECEIVED = language.getString("FACTIONS.CLAIM_COMMAND.CLAIMING_WAND_RECEIVED");
        FACTIONS_CLAIM_SELECTION_CLEARED = language.getString("FACTIONS.CLAIM_COMMAND.SELECTION_CLEARED");
        FACTIONS_CLAIM_SELECTION_NOT_SET = language.getString("FACTIONS.CLAIM_COMMAND.SELECTION_NOT_SET");
        FACTIONS_CLAIM_POSITION_ONE_SET = language.getString("FACTIONS.CLAIM_COMMAND.POSITION_ONE_SET");
        FACTIONS_CLAIM_POSITION_TWO_SET = language.getString("FACTIONS.CLAIM_COMMAND.POSITION_TWO_SET");
        FACTIONS_CLAIM_PRICE_MESSAGE = language.getString("FACTIONS.CLAIM_COMMAND.CLAIM_PRICE_MESSAGE");
        FACTIONS_CLAIM_CLAIM_OVERLAPPING = language.getString("FACTIONS.CLAIM_COMMAND.CLAIM_OVERLAPPING");
        FACTIONS_CLAIM_CLAIM_NOT_CONNECTED = language.getString("FACTIONS.CLAIM_COMMAND.CLAIM_NOT_CONNECTED");
        FACTIONS_CLAIM_CANNOT_CLAIM_WARZONE = language.getString("FACTIONS.CLAIM_COMMAND.CANNOT_CLAIM_WARZONE");
        FACTIONS_CLAIM_LOCATION_ALREADY_CLAIMED = language.getString("FACTIONS.CLAIM_COMMAND.LOCATION_ALREADY_CLAIMED");
        FACTIONS_CLAIM_SPAWN_TOO_CLOSE = language.getString("FACTIONS.CLAIM_COMMAND.SPAWN_TOO_CLOSE");
        FACTIONS_CLAIM_INVENTORY_FULL = language.getString("FACTIONS.CLAIM_COMMAND.INVENTORY_FULL");
        FACTIONS_CLAIM_CAN_CLAIM_ONLY_IN_OVERWORLD = language.getString("FACTIONS.CLAIM_COMMAND.CAN_CLAIM_ONLY_IN_OVERWORLD");
        FACTIONS_CLAIM_NOT_ENOUGH_MONEY = language.getString("FACTIONS.CLAIM_COMMAND.NOT_ENOUGH_MONEY");
        FACTIONS_CLAIM_CLAIM_MIN_SIZE = language.getString("FACTIONS.CLAIM_COMMAND.CLAIM_MIN_SIZE");
        FACTIONS_CLAIM_CLAIM_MAX_SIZE = language.getString("FACTIONS.CLAIM_COMMAND.CLAIM_MAX_SIZE");
        FACTIONS_CLAIM_CLAIM_BUFFER = language.getString("FACTIONS.CLAIM_COMMAND.CLAIM_BUFFER");
        FACTIONS_CLAIM_CLAIM_CLAIMED = language.getString("FACTIONS.CLAIM_COMMAND.CLAIM_CLAIMED");

        FACTIONS_CLAIM_FOR_USAGE = language.getString("FACTIONS.CLAIM_FOR_COMMAND.USAGE");
        FACTIONS_CLAIM_FOR_NOT_SYSTEM_FACTION = language.getString("FACTIONS.CLAIM_FOR_COMMAND.NOT_SYSTEM_FACTION");
        FACTIONS_CLAIM_FOR_MAKE_A_SELECTION = language.getString("FACTIONS.CLAIM_FOR_COMMAND.MAKE_A_SELECTION");
        FACTIONS_CLAIM_FOR_SET_BOTH_POSITIONS = language.getString("FACTIONS.CLAIM_FOR_COMMAND.SET_BOTH_POSITIONS");
        FACTIONS_CLAIM_FOR_CLAIM_OVERLAPPING = language.getString("FACTIONS.CLAIM_FOR_COMMAND.CLAIM_OVERLAPPING");
        FACTIONS_CLAIM_FOR_CLAIM_CLAIMED = language.getString("FACTIONS.CLAIM_FOR_COMMAND.CLAIM_CLAIMED");

        FACTIONS_COLOR_USAGE = language.getString("FACTIONS.COLOR_COMMAND.USAGE");
        FACTIONS_COLOR_NOT_SYSTEM_FACTION = language.getString("FACTIONS.COLOR_COMMAND.NOT_SYSTEM_FACTION");
        FACTIONS_COLOR_DOESNT_EXIST = language.getString("FACTIONS.COLOR_COMMAND.COLOR_DOESNT_EXIST");
        FACTIONS_COLOR_CHANGED = language.getString("FACTIONS.COLOR_COMMAND.CHANGED");

        FACTIONS_CREATE_USAGE = language.getString("FACTIONS.CREATE_COMMAND.USAGE");
        FACTIONS_CREATED = language.getString("FACTIONS.CREATE_COMMAND.CREATED");
        FACTIONS_CREATE_COOLDOWN = language.getString("FACTIONS.CREATE_COMMAND.COOLDOWN");

        FACTIONS_DEATHBAN_USAGE = language.getString("FACTIONS.DEATHBAN_COMMAND.USAGE");
        FACTIONS_DEATHBAN_NOT_SYSTEM_FACTION = language.getString("FACTIONS.DEATHBAN_COMMAND.NOT_SYSTEM_FACTION");
        FACTIONS_DEATHBAN_CHANGED = language.getString("FACTIONS.DEATHBAN_COMMAND.CHANGED");

        FACTIONS_DEMOTE_USAGE = language.getString("FACTIONS.DEMOTE_COMMAND.USAGE");
        FACTIONS_DEMOTE_DEMOTED = language.getString("FACTIONS.DEMOTE_COMMAND.DEMOTED");
        FACTIONS_DEMOTE_MIN_DEMOTE = language.getString("FACTIONS.DEMOTE_COMMAND.MIN_DEMOTE");
        FACTIONS_CANNOT_DEMOTE_SELF = language.getString("FACTIONS.DEMOTE_COMMAND.CANNOT_DEMOTE_SELF");

        FACTIONS_DEPOSIT_USAGE = language.getString("FACTIONS.DEPOSIT_COMMAND.USAGE");
        FACTIONS_DEPOSIT_DEPOSITED = language.getString("FACTIONS.DEPOSIT_COMMAND.DEPOSITED");
        FACTIONS_DEPOSIT_CAN_NOT_DEPOSIT_ZERO = language.getString("FACTIONS.DEPOSIT_COMMAND.CAN_NOT_DEPOSIT_ZERO");
        FACTIONS_DEPOSIT_NOT_ENOUGH_MONEY = language.getString("FACTIONS.DEPOSIT_COMMAND.NOT_ENOUGH_MONEY");

        FACTIONS_DISBANDED = language.getString("FACTIONS.DISBAND_COMMAND.DISBANDED");
        FACTIONS_DISBAND_CLAIM_MONEY_REFUNDED = language.getString("FACTIONS.DISBAND_COMMAND.CLAIM_MONEY_REFUNDED");
        FACTIONS_DISBAND_RAIDABLE_DENY = language.getString("FACTIONS.DISBAND_COMMAND.RAIDABLE_DENY");
        FACTIONS_CANNOT_DISBAND_WHILE_REGENERATING = language.getString("FACTIONS.DISBAND_COMMAND.CANNOT_DISBAND_WHILE_REGENERATING");

        FACTIONS_FOCUS_USAGE = language.getString("FACTIONS.FOCUS_COMMAND.USAGE");
        FACTIONS_FOCUS_ALREADY_FOCUSING = language.getString("FACTIONS.FOCUS_COMMAND.ALREADY_FOCUSING");
        FACTIONS_FOCUS_CANNOT_FOCUS = language.getString("FACTIONS.FOCUS_COMMAND.CANNOT_FOCUS");
        FACTIONS_FOCUS_FOCUSED = language.getStringList("FACTIONS.FOCUS_COMMAND.FOCUSED");

        FACTIONS_FRIENDLY_FIRE_TOGGLED_ON = language.getString("FACTIONS.FRIENDLY_FIRE_COMMAND.TOGGLED_ON");
        FACTIONS_FRIENDLY_FIRE_TOGGLED_OFF = language.getString("FACTIONS.FRIENDLY_FIRE_COMMAND.TOGGLED_OFF");

        FACTIONS_ENDERPEARLS_USAGE = language.getString("FACTIONS.ENDERPEARLS_COMMAND.USAGE");
        FACTIONS_ENDERPEARLS_NOT_SYSTEM_FACTION = language.getString("FACTIONS.ENDERPEARLS_COMMAND.NOT_SYSTEM_FACTION");
        FACTIONS_ENDERPEARLS_ENABLED = language.getString("FACTIONS.ENDERPEARLS_COMMAND.ENABLED");
        FACTIONS_ENDERPEARLS_DISABLED = language.getString("FACTIONS.ENDERPEARLS_COMMAND.DISABLED");

        FACTIONS_FORCE_DEMOTE_USAGE = language.getString("FACTIONS.FORCE_DEMOTE_COMMAND.USAGE");
        FACTIONS_FORCE_DEMOTED_SENDER = language.getString("FACTIONS.FORCE_DEMOTE_COMMAND.DEMOTED_SENDER");
        FACTIONS_FORCE_DEMOTED_FACTION = language.getString("FACTIONS.FORCE_DEMOTE_COMMAND.DEMOTED_FACTION");
        FACTIONS_FORCE_DEMOTE_MIN_DEMOTE = language.getString("FACTIONS.FORCE_DEMOTE_COMMAND.MIN_DEMOTE");
        FACTIONS_FORCE_DEMOTE_CANNOT_DEMOTE_LEADER = language.getString("FACTIONS.FORCE_DEMOTE_COMMAND.CANNOT_DEMOTE_LEADER");

        FACTIONS_FORCE_JOIN_USAGE = language.getString("FACTIONS.FORCE_JOIN_COMMAND.USAGE");
        FACTIONS_FORCE_JOINED = language.getString("FACTIONS.FORCE_JOIN_COMMAND.JOINED");

        FACTIONS_FORCE_KICK_USAGE = language.getString("FACTIONS.FORCE_KICK_COMMAND.USAGE");
        FACTIONS_FORCE_KICK_CANNOT_KICK_LEADER = language.getString("FACTIONS.FORCE_KICK_COMMAND.CANNOT_KICK_LEADER");
        FACTIONS_FORCE_KICKED_SENDER = language.getString("FACTIONS.FORCE_KICK_COMMAND.KICKED_SENDER");
        FACTIONS_FORCE_KICKED_SELF = language.getString("FACTIONS.FORCE_KICK_COMMAND.KICKED_SELF");
        FACTIONS_FORCE_KICKED_OTHERS = language.getString("FACTIONS.FORCE_KICK_COMMAND.KICKED_OTHERS");

        FACTIONS_FORCE_LEADER_USAGE = language.getString("FACTIONS.FORCE_LEADER_COMMAND.USAGE");
        FACTIONS_FORCE_LEADER_CHANGED_SENDER = language.getString("FACTIONS.FORCE_LEADER_COMMAND.CHANGED_SENDER");
        FACTIONS_FORCE_LEADER_CHANGED_FACTION = language.getString("FACTIONS.FORCE_LEADER_COMMAND.CHANGED_FACTION");
        FACTIONS_FORCE_LEADER_ALREADY_LEADER = language.getString("FACTIONS.FORCE_LEADER_COMMAND.ALREADY_LEADER");

        FACTIONS_FORCE_PROMOTE_USAGE = language.getString("FACTIONS.FORCE_PROMOTE_COMMAND.USAGE");
        FACTIONS_FORCE_PROMOTED_SENDER = language.getString("FACTIONS.FORCE_PROMOTE_COMMAND.PROMOTED_SENDER");
        FACTIONS_FORCE_PROMOTED_FACTION = language.getString("FACTIONS.FORCE_PROMOTE_COMMAND.PROMOTED_FACTION");
        FACTIONS_FORCE_PROMOTE_MAX_PROMOTE = language.getString("FACTIONS.FORCE_PROMOTE_COMMAND.MAX_PROMOTE");

        FACTIONS_FORCE_RENAME_USAGE = language.getString("FACTIONS.FORCE_RENAME_COMMAND.USAGE");
        FACTIONS_FORCE_RENAMED = language.getString("FACTIONS.FORCE_RENAME_COMMAND.RENAMED");
        FACTIONS_FORCE_RENAME_SAME_NAME = language.getString("FACTIONS.FORCE_RENAME_COMMAND.SAME_NAME");

        FACTIONS_HELP_PAGE_NOT_FOUND = language.getString("FACTIONS.HELP_COMMAND.PAGE_NOT_FOUND");
        FACTIONS_HELP_PAGES = new HashMap<>();
        language.getSection("FACTIONS.HELP_COMMAND.PAGES").getKeys(false).forEach(key ->
        FACTIONS_HELP_PAGES.put(Integer.parseInt(key), language.getStringList("FACTIONS.HELP_COMMAND.PAGES." + key)));

        FACTIONS_HOME_CANNOT_TELEPORT_ALREADY_TELEPORTING = language.getString("FACTIONS.HOME_COMMAND.CANNOT_TELEPORT.ALREADY_TELEPORTING");
        FACTIONS_HOME_CANNOT_TELEPORT_IN_COMBAT = language.getString("FACTIONS.HOME_COMMAND.CANNOT_TELEPORT.IN_COMBAT");
        FACTIONS_HOME_CANNOT_TELEPORT_IN_ENEMY_TERRITORY = language.getString("FACTIONS.HOME_COMMAND.CANNOT_TELEPORT.IN_ENEMY_TERRITORY");
        FACTIONS_HOME_CANNOT_TELEPORT_WITH_PVP_TIMER = language.getString("FACTIONS.HOME_COMMAND.CANNOT_TELEPORT.WITH_PVP_TIMER");
        FACTIONS_HOME_NOT_SET = language.getString("FACTIONS.HOME_COMMAND.HOME_NOT_SET");
        FACTIONS_HOME_TASK_STARTED = language.getString("FACTIONS.HOME_COMMAND.TASK_STARTED");
        FACTIONS_HOME_TELEPORTED = language.getString("FACTIONS.HOME_COMMAND.TELEPORTED");
        FACTIONS_HOME_CANCELLED_MOVED = language.getString("FACTIONS.HOME_COMMAND.TELEPORT_CANCELLED.MOVED");
        FACTIONS_HOME_CANCELLED_DAMAGED = language.getString("FACTIONS.HOME_COMMAND.TELEPORT_CANCELLED.DAMAGED");

        FACTIONS_INVITE_USAGE = language.getString("FACTIONS.INVITE_COMMAND.USAGE");
        FACTIONS_INVITED_SELF = language.getString("FACTIONS.INVITE_COMMAND.INVITED_SELF");
        FACTIONS_INVITED_OTHERS = language.getString("FACTIONS.INVITE_COMMAND.INVITED_OTHERS");
        FACTIONS_INVITE_ALREADY_INVITED = language.getString("FACTIONS.INVITE_COMMAND.ALREADY_INVITED");
        FACTIONS_INVITE_FACTION_FULL = language.getString("FACTIONS.INVITE_COMMAND.FACTION_FULL");
        FACTIONS_INVITE_HOVER_TEXT = language.getString("FACTIONS.INVITE_COMMAND.HOVER_TEXT");

        FACTIONS_JOIN_USAGE = language.getString("FACTIONS.JOIN_COMMAND.USAGE");
        FACTIONS_JOINED = language.getString("FACTIONS.JOIN_COMMAND.JOINED");
        FACTIONS_NOT_INVITED = language.getString("FACTIONS.JOIN_COMMAND.NOT_INVITED");
        FACTIONS_JOIN_FACTION_FULL = language.getString("FACTIONS.JOIN_COMMAND.FACTION_FULL");
        FACTIONS_CANNOT_JOIN_WHILE_REGENERATING = language.getString("FACTIONS.JOIN_COMMAND.CANNOT_JOIN_WHILE_REGENERATING");

        FACTIONS_KICK_USAGE = language.getString("FACTIONS.KICK_COMMAND.USAGE");
        FACTIONS_KICKED_SELF = language.getString("FACTIONS.KICK_COMMAND.KICKED_SELF");
        FACTIONS_KICKED_OTHERS = language.getString("FACTIONS.KICK_COMMAND.KICKED_OTHERS");

        FACTIONS_LEADER_USAGE = language.getString("FACTIONS.LEADER_COMMAND.USAGE");
        FACTIONS_LEADER_LEADER_CHANGED = language.getString("FACTIONS.LEADER_COMMAND.LEADER_CHANGED");
        FACTIONS_LEADER_ALREADY_LEADER = language.getString("FACTIONS.LEADER_COMMAND.ALREADY_LEADER");

        FACTIONS_LEFT_SELF = language.getString("FACTIONS.LEAVE_COMMAND.LEFT_SELF");
        FACTIONS_LEFT_OTHERS = language.getString("FACTIONS.LEAVE_COMMAND.LEFT_OTHERS");
        FACTIONS_LEADER_LEAVE = language.getString("FACTIONS.LEAVE_COMMAND.LEADER_LEAVE");
        FACTIONS_CANNOT_LEAVE_WHILE_REGENERATING = language.getString("FACTIONS.LEAVE_COMMAND.CANNOT_LEAVE.WHILE_REGENERATING");
        FACTIONS_CANNOT_LEAVE_WHILE_IN_OWN_CLAIM = language.getString("FACTIONS.LEAVE_COMMAND.CANNOT_LEAVE.WHILE_IN_OWN_CLAIM");

        FACTIONS_LIST_PAGE_NOT_FOUND = language.getString("FACTIONS.LIST_COMMAND.PAGE_NOT_FOUND");
        FACTIONS_LIST_FACTION_FORMAT = language.getString("FACTIONS.LIST_COMMAND.FACTION_FORMAT");
        FACTIONS_LIST_HEADER = language.getStringList("FACTIONS.LIST_COMMAND.HEADER");
        FACTIONS_LIST_FOOTER = language.getStringList("FACTIONS.LIST_COMMAND.FOOTER");

        FACTIONS_LIVES_SELF = language.getString("FACTIONS.LIVES_COMMAND.LIVES_SELF");
        FACTIONS_LIVES_OTHERS = language.getString("FACTIONS.LIVES_COMMAND.LIVES_OTHERS");

        FACTIONS_LIVES_DEPOSIT_USAGE = language.getString("FACTIONS.LIVES_DEPOSIT_COMMAND.USAGE");
        FACTIONS_LIVES_DEPOSITED = language.getString("FACTIONS.LIVES_DEPOSIT_COMMAND.DEPOSITED");
        FACTIONS_LIVES_DEPOSIT_NOT_ENOUGH_LIVES = language.getString("FACTIONS.LIVES_DEPOSIT_COMMAND.NOT_ENOUGH_LIVES");

        FACTIONS_LIVES_REVIVE_USAGE = language.getString("FACTIONS.LIVES_REVIVE_COMMAND.USAGE");
        FACTIONS_LIVES_REVIVED = language.getString("FACTIONS.LIVES_REVIVE_COMMAND.REVIVED");
        FACTIONS_LIVES_REVIVE_NOT_ENOUGH_LIVES = language.getString("FACTIONS.LIVES_REVIVE_COMMAND.NOT_ENOUGH_LIVES");
        FACTIONS_LIVES_REVIVE_NOT_DEATHBANNED = language.getString("FACTIONS.LIVES_REVIVE_COMMAND.NOT_DEATHBANNED");

        FACTIONS_LIVES_WITHDRAW_USAGE = language.getString("FACTIONS.LIVES_WITHDRAW_COMMAND.USAGE");
        FACTIONS_LIVES_WITHDRAWN = language.getString("FACTIONS.LIVES_WITHDRAW_COMMAND.WITHDRAWN");
        FACTIONS_LIVES_WITHDRAW_NOT_ENOUGH_LIVES = language.getString("FACTIONS.LIVES_WITHDRAW_COMMAND.NOT_ENOUGH_LIVES");

        FACTIONS_MAP_NO_NEARBY_CLAIMS = language.getString("FACTIONS.MAP_COMMAND.NO_NEARBY_CLAIMS");
        FACTIONS_MAP_OWNER_MESSAGE = language.getString("FACTIONS.MAP_COMMAND.OWNER_MESSAGE");
        FACTIONS_MAP_DISABLED = language.getString("FACTIONS.MAP_COMMAND.DISABLED");

        FACTIONS_OPEN_OPENED = language.getString("FACTIONS.OPEN_COMMAND.OPENED");
        FACTIONS_OPEN_CLOSED = language.getString("FACTIONS.OPEN_COMMAND.CLOSED");
        FACTIONS_OPEN_COOLDOWN = language.getString("FACTIONS.OPEN_COMMAND.COOLDOWN");

        FACTIONS_PROMOTE_USAGE = language.getString("FACTIONS.PROMOTE_COMMAND.USAGE");
        FACTIONS_PROMOTE_PROMOTED = language.getString("FACTIONS.PROMOTE_COMMAND.PROMOTED");
        FACTIONS_PROMOTE_MAX_PROMOTE = language.getString("FACTIONS.PROMOTE_COMMAND.MAX_PROMOTE");
        FACTIONS_CANNOT_PROMOTE_SELF = language.getString("FACTIONS.PROMOTE_COMMAND.CANNOT_PROMOTE_SELF");

        FACTIONS_RALLY_SET = language.getString("FACTIONS.RALLY_COMMAND.RALLY_SET");

        FACTIONS_REMOVE_RALLY_NOT_SET = language.getString("FACTIONS.REMOVE_RALLY_COMMAND.RALLY_NOT_SET");
        FACTIONS_REMOVE_RALLY_REMOVED = language.getString("FACTIONS.REMOVE_RALLY_COMMAND.RALLY_REMOVED");

        FACTIONS_REMOVE_CLAIM_NO_CLAIM = language.getString("FACTIONS.REMOVE_CLAIM_COMMAND.NO_CLAIM");
        FACTIONS_REMOVE_CLAIM_REMOVED_SENDER = language.getString("FACTIONS.REMOVE_CLAIM_COMMAND.REMOVED_SENDER");
        FACTIONS_REMOVE_CLAIM_REMOVED_FACTION = language.getString("FACTIONS.REMOVE_CLAIM_COMMAND.REMOVED_FACTION");

        FACTIONS_REMOVE_USAGE = language.getString("FACTIONS.REMOVE_COMMAND.USAGE");
        FACTIONS_REMOVE_CANNOT_REMOVE_THIS_TYPE = language.getString("FACTIONS.REMOVE_COMMAND.CANNOT_REMOVE_THIS_TYPE");
        FACTIONS_REMOVED_SENDER = language.getString("FACTIONS.REMOVE_COMMAND.REMOVED_SENDER");
        FACTIONS_REMOVED_FACTION = language.getString("FACTIONS.REMOVE_COMMAND.REMOVED_FACTION");

        FACTIONS_REMOVE_DTR_USAGE = language.getString("FACTIONS.REMOVE_DTR_COMMAND.USAGE");
        FACTIONS_REMOVE_DTR_CHANGED_SENDER = language.getString("FACTIONS.REMOVE_DTR_COMMAND.CHANGED_SENDER");
        FACTIONS_REMOVE_DTR_CHANGED_FACTION = language.getString("FACTIONS.REMOVE_DTR_COMMAND.CHANGED_FACTION");

        FACTIONS_RENAME_USAGE = language.getString("FACTIONS.RENAME_COMMAND.USAGE");
        FACTIONS_RENAMED = language.getString("FACTIONS.RENAME_COMMAND.RENAMED");
        FACTIONS_RENAME_SAME_NAME = language.getString("FACTIONS.RENAME_COMMAND.SAME_NAME");
        FACTIONS_RENAME_COOLDOWN = language.getString("FACTIONS.RENAME_COMMAND.COOLDOWN");

        FACTIONS_SAFEZONE_USAGE = language.getString("FACTIONS.SAFEZONE_COMMAND.USAGE");
        FACTIONS_SAFEZONE_NOT_SYSTEM_FACTION = language.getString("FACTIONS.SAFEZONE_COMMAND.NOT_SYSTEM_FACTION");
        FACTIONS_SAFEZONE_CHANGED = language.getString("FACTIONS.SAFEZONE_COMMAND.CHANGED");

        FACTIONS_SAVED = language.getString("FACTIONS.SAVE_COMMAND.SAVED");

        FACTIONS_SET_BALANCE_USAGE = language.getString("FACTIONS.SET_BALANCE_COMMAND.USAGE");
        FACTIONS_SET_BALANCE_CHANGED_SENDER = language.getString("FACTIONS.SET_BALANCE_COMMAND.CHANGED_SENDER");
        FACTIONS_SET_BALANCE_CHANGED_FACTION = language.getString("FACTIONS.SET_BALANCE_COMMAND.CHANGED_FACTION");

        FACTIONS_SET_DTR_USAGE = language.getString("FACTIONS.SET_DTR_COMMAND.USAGE");
        FACTIONS_SET_DTR_CHANGED_SENDER = language.getString("FACTIONS.SET_DTR_COMMAND.CHANGED_SENDER");
        FACTIONS_SET_DTR_CHANGED_FACTION = language.getString("FACTIONS.SET_DTR_COMMAND.CHANGED_FACTION");

        FACTIONS_SHOW_HOVER_TEXT = language.getString("FACTIONS.SHOW_COMMAND.HOVER_TEXT");
        FACTIONS_SHOW_NAME_FORMAT = language.getString("FACTIONS.SHOW_COMMAND.NAME_FORMAT");
        FACTIONS_PLAYER_FACTION_SHOW = language.getStringList("FACTIONS.SHOW_COMMAND.PLAYER_FACTION_MESSAGE");
        FACTIONS_SYSTEM_CLAIM_FORMAT = language.getString("FACTIONS.SHOW_COMMAND.SYSTEM_CLAIM_FORMAT");
        FACTIONS_SYSTEM_FACTION_SHOW = language.getStringList("FACTIONS.SHOW_COMMAND.SYSTEM_FACTION_MESSAGE");

        FACTIONS_STUCK_ALREADY_TELEPORTING = language.getString("FACTIONS.STUCK_COMMAND.CANNOT_TELEPORT.ALREADY_TELEPORTING");
        FACTIONS_STUCK_ONLY_IN_OVERWORLD = language.getString("FACTIONS.STUCK_COMMAND.CANNOT_TELEPORT.ONLY_IN_OVERWORLD");
        FACTIONS_STUCK_ONLY_FROM_ENEMY_CLAIMS = language.getString("FACTIONS.STUCK_COMMAND.CANNOT_TELEPORT.ONLY_FROM_ENEMY_CLAIMS");
        FACTIONS_STUCK_IN_OWN_CLAIM = language.getString("FACTIONS.STUCK_COMMAND.CANNOT_TELEPORT.IN_OWN_CLAIM");
        FACTIONS_STUCK_TASK_STARTED = language.getString("FACTIONS.STUCK_COMMAND.TASK_STARTED");
        FACTIONS_STUCK_TELEPORTED = language.getString("FACTIONS.STUCK_COMMAND.TELEPORTED");
        FACTIONS_STUCK_CANCELLED_MOVED = language.getString("FACTIONS.STUCK_COMMAND.TELEPORT_CANCELLED.MOVED");
        FACTIONS_STUCK_CANCELLED_DAMAGED = language.getString("FACTIONS.STUCK_COMMAND.TELEPORT_CANCELLED.DAMAGED");

        FACTIONS_SET_HOME_NOT_IN_OWN_CLAIM = language.getString("FACTIONS.SET_HOME_COMMAND.NOT_IN_OWN_CLAIM");
        FACTIONS_SET_HOME_HOME_SET = language.getString("FACTIONS.SET_HOME_COMMAND.HOME_SET");

        FACTIONS_SET_FREEZE_USAGE = language.getString("FACTIONS.SET_FREEZE_COMMAND.USAGE");
        FACTIONS_SET_FREEZE_CHANGED_SENDER = language.getString("FACTIONS.SET_FREEZE_COMMAND.CHANGED_SENDER");
        FACTIONS_SET_FREEZE_CHANGED_FACTION = language.getString("FACTIONS.SET_FREEZE_COMMAND.CHANGED_FACTION");

        FACTIONS_SET_LIVES_USAGE = language.getString("FACTIONS.SET_LIVES_COMMAND.USAGE");
        FACTIONS_SET_LIVES_CHANGED_SENDER = language.getString("FACTIONS.SET_LIVES_COMMAND.CHANGED_SENDER");
        FACTIONS_SET_LIVES_CHANGED_FACTION = language.getString("FACTIONS.SET_LIVES_COMMAND.CHANGED_FACTION");

        FACTIONS_SET_KILLS_USAGE = language.getString("FACTIONS.SET_KILLS_COMMAND.USAGE");
        FACTIONS_SET_KILLS_CHANGED_SENDER = language.getString("FACTIONS.SET_KILLS_COMMAND.CHANGED_SENDER");
        FACTIONS_SET_KILLS_CHANGED_FACTION = language.getString("FACTIONS.SET_KILLS_COMMAND.CHANGED_FACTION");

        FACTIONS_SET_POINTS_USAGE = language.getString("FACTIONS.SET_POINTS_COMMAND.USAGE");
        FACTIONS_SET_POINTS_CHANGED_SENDER = language.getString("FACTIONS.SET_POINTS_COMMAND.CHANGED_SENDER");
        FACTIONS_SET_POINTS_CHANGED_FACTION = language.getString("FACTIONS.SET_POINTS_COMMAND.CHANGED_FACTION");

        FACTIONS_SYSTEM_CREATE_USAGE = language.getString("FACTIONS.SYSTEM_CREATE_COMMAND.USAGE");
        FACTIONS_SYSTEM_CREATED = language.getString("FACTIONS.SYSTEM_CREATE_COMMAND.CREATED");

        FACTIONS_SYSTEM_LIST_FACTION_FORMAT = language.getString("FACTIONS.SYSTEM_LIST_COMMAND.FACTION_FORMAT");
        FACTIONS_SYSTEM_LIST_HEADER = language.getStringList("FACTIONS.SYSTEM_LIST_COMMAND.HEADER");
        FACTIONS_SYSTEM_LIST_FOOTER = language.getString("FACTIONS.SYSTEM_LIST_COMMAND.FOOTER");

        FACTIONS_SYSTEM_RENAME_USAGE = language.getString("FACTIONS.SYSTEM_RENAME_COMMAND.USAGE");
        FACTIONS_SYSTEM_RENAMED = language.getString("FACTIONS.SYSTEM_RENAME_COMMAND.RENAMED");
        FACTIONS_SYSTEM_RENAME_NOT_SYSTEM_FACTION = language.getString("FACTIONS.SYSTEM_RENAME_COMMAND.NOT_SYSTEM_FACTION");
        FACTIONS_SYSTEM_RENAME_CANNOT_RENAME_THIS_TYPE = language.getString("FACTIONS.SYSTEM_RENAME_COMMAND.CANNOT_RENAME_THIS_TYPE");
        FACTIONS_SYSTEM_RENAME_SAME_NAME = language.getString("FACTIONS.SYSTEM_RENAME_COMMAND.SAME_NAME");

        FACTIONS_THAW_USAGE = language.getString("FACTIONS.THAW_COMMAND.USAGE");
        FACTIONS_THAW_CHANGED_SENDER = language.getString("FACTIONS.THAW_COMMAND.CHANGED_SENDER");
        FACTIONS_THAW_CHANGED_FACTION = language.getString("FACTIONS.THAW_COMMAND.CHANGED_FACTION");

        FACTIONS_TP_HERE_USAGE = language.getString("FACTIONS.TP_HERE_COMMAND.USAGE");
        FACTIONS_TP_HERE_TELEPORTED_SENDER = language.getString("FACTIONS.TP_HERE_COMMAND.TELEPORTED_SENDER");
        FACTIONS_TP_HERE_TELEPORTED_FACTION = language.getString("FACTIONS.TP_HERE_COMMAND.TELEPORTED_FACTION");

        FACTIONS_UNALLY_USAGE = language.getString("FACTIONS.UNALLY_COMMAND.USAGE");
        FACTIONS_UNALLY_NOT_ALLIES = language.getString("FACTIONS.UNALLY_COMMAND.NOT_ALLIES");
        FACTIONS_UNALLY_REMOVED = language.getString("FACTIONS.UNALLY_COMMAND.REMOVED");

        FACTIONS_UNCLAIM_ALL_NO_CLAIMS = language.getString("FACTIONS.UNCLAIM_ALL_COMMAND.NO_CLAIMS");
        FACTIONS_UNCLAIMED_ALL = language.getString("FACTIONS.UNCLAIM_ALL_COMMAND.UNCLAIMED_ALL");
        FACTIONS_UNCLAIM_ALL_HOME_TELEPORT_CANCELLED = language.getString("FACTIONS.UNCLAIM_ALL_COMMAND.HOME_TELEPORT_CANCELLED");
        FACTIONS_CANNOT_UNCLAIM_ALL_WHILE_REGENERATING = language.getString("FACTIONS.UNCLAIM_ALL_COMMAND.CANNOT_UNCLAIM_ALL_WHILE_REGENERATING");

        FACTIONS_UNCLAIM_NO_CLAIMS = language.getString("FACTIONS.UNCLAIM_COMMAND.NO_CLAIMS");
        FACTIONS_UNCLAIM_NOT_OWNER = language.getString("FACTIONS.UNCLAIM_COMMAND.NOT_OWNER");
        FACTIONS_UNCLAIMED = language.getString("FACTIONS.UNCLAIM_COMMAND.UNCLAIMED");
        FACTIONS_UNCLAIM_CLAIM_MONEY_REFUNDED = language.getString("FACTIONS.UNCLAIM_COMMAND.CLAIM_MONEY_REFUNDED");
        FACTIONS_UNCLAIM_HOME_TELEPORT_CANCELLED = language.getString("FACTIONS.UNCLAIM_COMMAND.HOME_TELEPORT_CANCELLED");
        FACTIONS_CANNOT_UNCLAIM_WHILE_REGENERATING = language.getString("FACTIONS.UNCLAIM_COMMAND.CANNOT_UNCLAIM_WHILE_REGENERATING");

        FACTIONS_UNFOCUS_NOT_FOCUSING = language.getString("FACTIONS.UNFOCUS_COMMAND.NOT_FOCUSING");
        FACTIONS_UNFOCUS_UNFOCUSED = language.getString("FACTIONS.UNFOCUS_COMMAND.UNFOCUSED");
        FACTIONS_UNFOCUS_FACTION_DOESNT_EXIST = language.getString("FACTIONS.UNFOCUS_COMMAND.FACTION_DOESNT_EXIST");

        FACTIONS_UNINVITE_USAGE = language.getString("FACTIONS.UNINVITE_COMMAND.USAGE");
        FACTIONS_UNINVITE_UNINVITED = language.getString("FACTIONS.UNINVITE_COMMAND.UNINVITED");
        FACTIONS_UNINVITE_ALL = language.getString("FACTIONS.UNINVITE_COMMAND.UNINVITE_ALL");
        FACTIONS_UNINVITE_NOT_INVITED = language.getString("FACTIONS.UNINVITE_COMMAND.NOT_INVITED");

        FACTIONS_WITHDRAW_USAGE = language.getString("FACTIONS.WITHDRAW_COMMAND.USAGE");
        FACTIONS_WITHDRAW_WITHDRAWN = language.getString("FACTIONS.WITHDRAW_COMMAND.WITHDRAWN");
        FACTIONS_WITHDRAW_NOT_ENOUGH_MONEY = language.getString("FACTIONS.WITHDRAW_COMMAND.NOT_ENOUGH_MONEY");

        CONQUEST_COMMAND_USAGE_PLAYER = language.getStringList("CONQUEST.COMMAND_USAGE_PLAYER");
        CONQUEST_COMMAND_USAGE_ADMIN = language.getStringList("CONQUEST.COMMAND_USAGE_ADMIN");

        CONQUEST_CAPPED = language.getStringList("CONQUEST.CAPPED");
        CONQUEST_STARTED_CAPPING = language.getString("CONQUEST.STARTED_CAPPING");
        CONQUEST_STOPPED_CAPPING = language.getString("CONQUEST.STOPPED_CAPPING");

        CONQUEST_EXCEPTION_NOT_RUNNING = language.getString("CONQUEST.EXCEPTION.NOT_RUNNING");
        CONQUEST_EXCEPTION_INVALID_ZONE = language.getString("CONQUEST.EXCEPTION.INVALID_ZONE");

        CONQUEST_AREA_MAKE_A_SELECTION = language.getString("CONQUEST.AREA_COMMAND.MAKE_A_SELECTION");
        CONQUEST_AREA_SET_BOTH_POSITIONS = language.getString("CONQUEST.AREA_COMMAND.SET_BOTH_POSITIONS");
        CONQUEST_AREA_CLAIM_OVERLAPPING = language.getString("CONQUEST.AREA_COMMAND.CLAIM_OVERLAPPING");
        CONQUEST_AREA_CREATED = language.getString("CONQUEST.AREA_COMMAND.CREATED");

        CONQUEST_INFO_NOT_SETUP = language.getString("CONQUEST.INFO_COMMAND.NOT_SETUP");
        CONQUEST_INFO_MESSAGE = language.getStringList("CONQUEST.INFO_COMMAND.MESSAGE");

        CONQUEST_SET_CAPZONE_USAGE = language.getString("CONQUEST.SET_CAPZONE_COMMAND.USAGE");
        CONQUEST_SET_CAPZONE_MAKE_A_SELECTION = language.getString("CONQUEST.SET_CAPZONE_COMMAND.MAKE_A_SELECTION");
        CONQUEST_SET_CAPZONE_SET_BOTH_POSITIONS = language.getString("CONQUEST.SET_CAPZONE_COMMAND.SET_BOTH_POSITIONS");
        CONQUEST_SET_CAPZONE_CREATED = language.getString("CONQUEST.SET_CAPZONE_COMMAND.CREATED");

        CONQUEST_SET_POINTS_USAGE = language.getString("CONQUEST.SET_POINTS_COMMAND.USAGE");
        CONQUEST_SET_POINTS_INVALID_FACTION = language.getString("CONQUEST.SET_POINTS_COMMAND.INVALID_FACTION");
        CONQUEST_SET_POINTS_CHANGED = language.getString("CONQUEST.SET_POINTS_COMMAND.CHANGED");

        CONQUEST_START_ALREADY_RUNNING = language.getString("CONQUEST.START_COMMAND.ALREADY_RUNNING");
        CONQUEST_START_NOT_SETUP = language.getString("CONQUEST.START_COMMAND.NOT_SETUP");
        CONQUEST_START_STARTED = language.getStringList("CONQUEST.START_COMMAND.STARTED");

        CONQUEST_STOP_STOPPED = language.getString("CONQUEST.STOP_COMMAND.STOPPED");

        CONQUEST_TELEPORT_USAGE = language.getString("CONQUEST.TELEPORT_COMMAND.USAGE");
        CONQUEST_TELEPORT_ZONE_NOT_SET = language.getString("CONQUEST.TELEPORT_COMMAND.ZONE_NOT_SET");

        DTC_COMMAND_USAGE_PLAYER = language.getStringList("DTC.COMMAND_USAGE_PLAYER");
        DTC_COMMAND_USAGE_ADMIN = language.getStringList("DTC.COMMAND_USAGE_ADMIN");

        DTC_DESTROYED = language.getString("DTC.DESTROYED");
        DTC_HEALTH_MESSAGE = language.getString("DTC.HEALTH_MESSAGE");

        DTC_EXCEPTION_ALREADY_RUNNING = language.getString("DTC.EXCEPTION.ALREADY_RUNNING");
        DTC_EXCEPTION_NOT_RUNNING = language.getString("DTC.EXCEPTION.NOT_RUNNING");

        DTC_AREA_MAKE_A_SELECTION = language.getString("DTC.AREA_COMMAND.MAKE_A_SELECTION");
        DTC_AREA_SET_BOTH_POSITIONS = language.getString("DTC.AREA_COMMAND.SET_BOTH_POSITIONS");
        DTC_AREA_CLAIM_OVERLAPPING = language.getString("DTC.AREA_COMMAND.CLAIM_OVERLAPPING");
        DTC_AREA_CREATED = language.getString("DTC.AREA_COMMAND.CREATED");

        DTC_INFO_NOT_SETUP = language.getString("DTC.INFO_COMMAND.NOT_SETUP");
        DTC_INFO_MESSAGE = language.getStringList("DTC.INFO_COMMAND.MESSAGE");

        DTC_SET_MUST_BE_OBSIDIAN = language.getString("DTC.SET_COMMAND.MUST_BE_OBSIDIAN");
        DTC_SET_CORE_SET = language.getString("DTC.SET_COMMAND.CORE_SET");

        DTC_SET_HEALTH_USAGE = language.getString("DTC.SET_HEALTH_COMMAND.USAGE");
        DTC_SET_HEALTH_CHANGED = language.getString("DTC.SET_HEALTH_COMMAND.CHANGED");

        DTC_START_NOT_SETUP = language.getString("DTC.START_COMMAND.NOT_SETUP");
        DTC_START_STARTED = language.getStringList("DTC.START_COMMAND.STARTED");

        DTC_STOP_STOPPED = language.getString("DTC.STOP_COMMAND.STOPPED");

        DTC_TELEPORT_CORE_NOT_SET = language.getString("DTC.TELEPORT_COMMAND.CORE_NOT_SET");

        KOTH_COMMAND_HEADER = language.getString("KOTH.COMMAND_HEADER");
        KOTH_COMMAND_FOOTER = language.getString("KOTH.COMMAND_FOOTER");
        KOTH_COMMAND_USAGE_PLAYER = language.getStringList("KOTH.COMMAND_USAGE_PLAYER");
        KOTH_COMMAND_USAGE_ADMIN = language.getStringList("KOTH.COMMAND_USAGE_ADMIN");
        KOTH_CAPPED_NO_FACTION = language.getStringList("KOTH.CAPPED_NO_FACTION");
        KOTH_CAPPED_WITH_FACTION = language.getStringList("KOTH.CAPPED_WITH_FACTION");
        KOTH_KNOCKED = language.getString("KOTH.KNOCKED");
        KOTH_YOU_STARTED_CAPPING = language.getString("KOTH.YOU_STARTED_CAPPING");
        KOTH_SOMEONE_STARTED_CAPPING = language.getString("KOTH.SOMEONE_STARTED_CAPPING");
        KOTH_NO_ONE_CAPPING = language.getString("KOTH.NO_ONE_CAPPING");
        KOTH_YOU_ARE_CAPPING = language.getString("KOTH.YOU_ARE_CAPPING");
        KOTH_SOMEONE_IS_CAPPING = language.getString("KOTH.SOMEONE_IS_CAPPING");

        KOTH_EXCEPTION_DOESNT_EXIST = language.getString("KOTH.EXCEPTION.DOESNT_EXIST");
        KOTH_EXCEPTION_NOT_RUNNING = language.getString("KOTH.EXCEPTION.NOT_RUNNING");
        KOTH_EXCEPTION_MAX_RUNNING_KOTHS_AMOUNT_REACHED = language.getString("KOTH.EXCEPTION.MAX_RUNNING_KOTHS_AMOUNT_REACHED");

        KOTH_AREA_USAGE = language.getString("KOTH.AREA_COMMAND.USAGE");
        KOTH_AREA_MAKE_A_SELECTION = language.getString("KOTH.AREA_COMMAND.MAKE_A_SELECTION");
        KOTH_AREA_SET_BOTH_POSITIONS = language.getString("KOTH.AREA_COMMAND.SET_BOTH_POSITIONS");
        KOTH_AREA_CLAIM_OVERLAPPING = language.getString("KOTH.AREA_COMMAND.CLAIM_OVERLAPPING");
        KOTH_AREA_CREATED = language.getString("KOTH.AREA_COMMAND.CREATED");

        KOTH_CREATE_USAGE = language.getString("KOTH.CREATE_COMMAND.USAGE");
        KOTH_CREATE_ALREADY_EXISTS = language.getString("KOTH.CREATE_COMMAND.ALREADY_EXISTS");
        KOTH_CREATE_MAKE_A_SELECTION = language.getString("KOTH.CREATE_COMMAND.MAKE_A_SELECTION");
        KOTH_CREATE_SET_BOTH_POSITIONS = language.getString("KOTH.CREATE_COMMAND.SET_BOTH_POSITIONS");
        KOTH_CREATE_CREATED = language.getString("KOTH.CREATE_COMMAND.CREATED");

        KOTH_LIST_COMMAND_TITLE = language.getString("KOTH.LIST_COMMAND.TITLE");
        KOTH_LIST_COMMAND_FORMAT = language.getString("KOTH.LIST_COMMAND.FORMAT");
        KOTH_LIST_NO_KOTHS = language.getString("KOTH.LIST_COMMAND.NO_KOTHS");

        KOTH_LOOT_USAGE = language.getString("KOTH.LOOT_COMMAND.USAGE");

        KOTH_REMOVE_USAGE = language.getString("KOTH.REMOVE_COMMAND.USAGE");
        KOTH_REMOVE_REMOVED = language.getString("KOTH.REMOVE_COMMAND.REMOVED");

        KOTH_START_USAGE = language.getString("KOTH.START_COMMAND.USAGE");
        KOTH_START_ALREADY_RUNNING = language.getString("KOTH.START_COMMAND.ALREADY_RUNNING");
        KOTH_START_STARTED = language.getStringList("KOTH.START_COMMAND.STARTED");

        KOTH_STOP_USAGE = language.getString("KOTH.STOP_COMMAND.USAGE");
        KOTH_STOP_STOPPED = language.getString("KOTH.STOP_COMMAND.STOPPED");

        KOTH_STOP_ALL_STOPPED_ALL = language.getString("KOTH.STOP_ALL_COMMAND.STOPPED_ALL");
        KOTH_STOP_ALL_NO_RUNNING_KOTHS = language.getString("KOTH.STOP_ALL_COMMAND.NO_RUNNING_KOTHS");

        KOTH_SET_TIME_USAGE = language.getString("KOTH.SET_TIME_COMMAND.USAGE");
        KOTH_SET_TIME_CHANGED = language.getString("KOTH.SET_TIME_COMMAND.CHANGED");

        KOTH_STARTING_TIME_USAGE = language.getString("KOTH.STARTING_TIME_COMMAND.USAGE");
        KOTH_STARTING_TIME_CHANGED = language.getString("KOTH.STARTING_TIME_COMMAND.CHANGED");

        KOTH_END_USAGE = language.getString("KOTH.END_COMMAND.USAGE");
        KOTH_END_ENDED = language.getString("KOTH.END_COMMAND.ENDED");

        KOTH_FACTION_POINTS_USAGE = language.getString("KOTH.FACTION_POINTS_COMMAND.USAGE");
        KOTH_FACTION_POINTS_CHANGED = language.getString("KOTH.FACTION_POINTS_COMMAND.CHANGED");

        KOTH_TELEPORT_USAGE = language.getString("KOTH.TELEPORT_COMMAND.USAGE");

        KING_COMMAND_USAGE_ADMIN = language.getStringList("KILL_THE_KING.COMMAND_USAGE_ADMIN");
        KING_KING_SLAIN = language.getStringList("KILL_THE_KING.KING_SLAIN");
        KING_KINGS_LOCATION = language.getString("KILL_THE_KING.KING_LOCATION");

        KING_EXCEPTION_ALREADY_RUNNING = language.getString("KILL_THE_KING.EXCEPTION.ALREADY_RUNNING");
        KING_EXCEPTION_NOT_RUNNING = language.getString("KILL_THE_KING.EXCEPTION.NOT_RUNNING");
        KING_EXCEPTION_ITEM_DROP_DENY = language.getString("KILL_THE_KING.EXCEPTION.ITEM_DROP_DENY");

        KING_START_USAGE = language.getString("KILL_THE_KING.START_COMMAND.USAGE");
        KING_START_STARTED = language.getString("KILL_THE_KING.START_COMMAND.STARTED");
        KING_STOP_STOPPED = language.getString("KILL_THE_KING.STOP_COMMAND.STOPPED");

        MOUNTAIN_COMMAND_HEADER = language.getString("MOUNTAIN.COMMAND_HEADER");
        MOUNTAIN_COMMAND_FOOTER = language.getString("MOUNTAIN.COMMAND_FOOTER");
        MOUNTAIN_COMMAND_USAGE_ADMIN = language.getStringList("MOUNTAIN.COMMAND_USAGE_ADMIN");
        MOUNTAIN_RESPAWNED = language.getStringList("MOUNTAIN.RESPAWNED");
        MOUNTAIN_NEXT_RESPAWN = language.getString("MOUNTAIN.NEXT_RESPAWN");

        MOUNTAIN_EXCEPTION_DOESNT_EXISTS = language.getString("MOUNTAIN.EXCEPTION.DOESNT_EXISTS");
        MOUNTAIN_EXCEPTION_INCORRECT_TYPE = language.getString("MOUNTAIN.EXCEPTION.INCORRECT_TYPE");

        MOUNTAIN_CREATE_USAGE = language.getString("MOUNTAIN.CREATE_COMMAND.USAGE");
        MOUNTAIN_CREATE_MAKE_A_SELECTION = language.getString("MOUNTAIN.CREATE_COMMAND.MAKE_A_SELECTION");
        MOUNTAIN_CREATE_SET_BOTH_POSITIONS = language.getString("MOUNTAIN.CREATE_COMMAND.SET_BOTH_POSITIONS");
        MOUNTAIN_CREATE_CLAIM_OVERLAPPING = language.getString("MOUNTAIN.CREATE_COMMAND.CLAIM_OVERLAPPING");
        MOUNTAIN_CREATE_CREATED = language.getString("MOUNTAIN.CREATE_COMMAND.CREATED");

        MOUNTAIN_LIST_TITLE = language.getString("MOUNTAIN.LIST_COMMAND.TITLE");
        MOUNTAIN_LIST_ADMIN_FORMAT = language.getString("MOUNTAIN.LIST_COMMAND.ADMIN_FORMAT");
        MOUNTAIN_LIST_PLAYER_FORMAT = language.getString("MOUNTAIN.LIST_COMMAND.PLAYER_FORMAT");
        MOUNTAIN_LIST_NO_MOUNTAINS = language.getString("MOUNTAIN.LIST_COMMAND.NO_MOUNTAINS");

        MOUNTAIN_REMOVE_USAGE = language.getString("MOUNTAIN.REMOVE_COMMAND.USAGE");
        MOUNTAIN_REMOVE_REMOVED = language.getString("MOUNTAIN.REMOVE_COMMAND.REMOVED");

        MOUNTAIN_RESPAWN_USAGE = language.getString("MOUNTAIN.RESPAWN_COMMAND.USAGE");
        MOUNTAIN_RESPAWN_RESPAWNED = language.getString("MOUNTAIN.RESPAWN_COMMAND.RESPAWNED");

        MOUNTAIN_TELEPORT_USAGE = language.getString("MOUNTAIN.TELEPORT_COMMAND.USAGE");

        MOUNTAIN_UPDATE_USAGE = language.getString("MOUNTAIN.UPDATE_COMMAND.USAGE");
        MOUNTAIN_UPDATE_UPDATED = language.getString("MOUNTAIN.UPDATE_COMMAND.UPDATED");

        ENDER_DRAGON_COMMAND_USAGE_PLAYER = language.getStringList("ENDER_DRAGON.COMMAND_USAGE_PLAYER");
        ENDER_DRAGON_COMMAND_USAGE_ADMIN = language.getStringList("ENDER_DRAGON.COMMAND_USAGE_ADMIN");

        ENDER_DRAGON_ENDED = language.getStringList("ENDER_DRAGON.ENDED");
        ENDER_DRAGON_DAMAGER_FORMAT = language.getString("ENDER_DRAGON.DAMAGER_FORMAT");

        ENDER_DRAGON_EXCEPTION_NOT_RUNNING = language.getString("ENDER_DRAGON.EXCEPTION.NOT_RUNNING");
        ENDER_DRAGON_EXCEPTION_SPAWN_NOT_SET = language.getString("ENDER_DRAGON.EXCEPTION.SPAWN_NOT_SET");

        ENDER_DRAGON_SPAWN_NOT_SET = language.getString("ENDER_DRAGON.INFO_COMMAND.SPAWN_NOT_SET");
        ENDER_DRAGON_INFO_MESSAGE = language.getStringList("ENDER_DRAGON.INFO_COMMAND.MESSAGE");

        ENDER_DRAGON_START_ALREADY_RUNNING = language.getString("ENDER_DRAGON.START_COMMAND.ALREADY_RUNNING");
        ENDER_DRAGON_START_STARTED = language.getStringList("ENDER_DRAGON.START_COMMAND.STARTED");

        ENDER_DRAGON_STOP_STOPPED = language.getString("ENDER_DRAGON.STOP_COMMAND.STOPPED");

        ENDER_DRAGON_SET_HEALTH_USAGE = language.getString("ENDER_DRAGON.SET_HEALTH_COMMAND.USAGE");
        ENDER_DRAGON_SET_HEALTH_CHANGED = language.getString("ENDER_DRAGON.SET_HEALTH_COMMAND.CHANGED");

        ENDER_DRAGON_SET_SPAWN_NOT_IN_END = language.getString("ENDER_DRAGON.SET_SPAWN_COMMAND.NOT_IN_END");
        ENDER_DRAGON_SET_SPAWN_SET = language.getString("ENDER_DRAGON.SET_SPAWN_COMMAND.SPAWN_SET");

        LOOT_COMMAND_HEADER = language.getString("LOOT.COMMAND_HEADER");
        LOOT_COMMAND_FOOTER = language.getString("LOOT.COMMAND_FOOTER");

        LOOT_COMMAND_USAGE_PLAYER = language.getStringList("LOOT.COMMAND_USAGE_PLAYER");
        LOOT_COMMAND_USAGE_ADMIN = language.getStringList("LOOT.COMMAND_USAGE_ADMIN");

        LOOT_EXCEPTION_DOESNT_EXIST = language.getString("LOOT.EXCEPTION.DOESNT_EXIST");

        LOOT_CLEAR_USAGE = language.getString("LOOT.CLEAR_COMMAND.USAGE");
        LOOT_CLEAR_CLEARED = language.getString("LOOT.CLEAR_COMMAND.CLEARED");

        LOOT_EDIT_USAGE = language.getString("LOOT.EDIT_COMMAND.USAGE");
        LOOT_EDIT_EDITED = language.getString("LOOT.EDIT_COMMAND.EDITED");

        LOOT_LIST_TITLE = language.getString("LOOT.LIST_COMMAND.TITLE");
        LOOT_LIST_FORMAT = language.getString("LOOT.LIST_COMMAND.FORMAT");

        LOOT_SET_AMOUNT_USAGE = language.getString("LOOT.SET_AMOUNT_COMMAND.USAGE");
        LOOT_SET_AMOUNT_CHANGED = language.getString("LOOT.SET_AMOUNT_COMMAND.CHANGED");

        LOOT_VIEW_USAGE = language.getString("LOOT.VIEW_COMMAND.USAGE");

        SCHEDULE_COMMAND_HEADER = language.getString("SCHEDULE.COMMAND_HEADER");
        SCHEDULE_COMMAND_FOOTER = language.getString("SCHEDULE.COMMAND_FOOTER");

        SCHEDULE_COMMAND_USAGE_ADMIN = language.getStringList("SCHEDULE.COMMAND_USAGE_ADMIN");

        SCHEDULE_UPCOMING_EVENTS_FORMAT = language.getString("SCHEDULE.UPCOMING_EVENTS_FORMAT");

        SCHEDULE_CLEAR_NO_SCHEDULES = language.getString("SCHEDULE.CLEAR_COMMAND.NO_SCHEDULES");
        SCHEDULE_CLEAR_CLEARED = language.getString("SCHEDULE.CLEAR_COMMAND.CLEARED");

        SCHEDULE_CREATE_USAGE = language.getString("SCHEDULE.CREATE_COMMAND.USAGE");
        SCHEDULE_CREATE_CREATED = language.getString("SCHEDULE.CREATE_COMMAND.CREATED");
        SCHEDULE_CREATE_EVENT_DOESNT_EXIST = language.getString("SCHEDULE.CREATE_COMMAND.EVENT_DOESNT_EXIST");
        SCHEDULE_CREATE_INCORRECT_DAY_FORMAT = language.getString("SCHEDULE.CREATE_COMMAND.INCORRECT_DAY_FORMAT");
        SCHEDULE_CREATE_INCORRECT_TIME_FORMAT = language.getString("SCHEDULE.CREATE_COMMAND.INCORRECT_TIME_FORMAT");

        SCHEDULE_DELETE_USAGE = language.getString("SCHEDULE.DELETE_COMMAND.USAGE");
        SCHEDULE_DELETE_DOESNT_EXIST = language.getString("SCHEDULE.DELETE_COMMAND.DOESNT_EXIST");
        SCHEDULE_DELETE_DELETED = language.getString("SCHEDULE.DELETE_COMMAND.DELETED");

        SCHEDULE_LIST_NO_SCHEDULES = language.getString("SCHEDULE.LIST_COMMAND.NO_SCHEDULES");
        SCHEDULE_LIST_CURRENT_TIME = language.getString("SCHEDULE.LIST_COMMAND.CURRENT_TIME");
        SCHEDULE_LIST_DAY_FORMAT = language.getString("SCHEDULE.LIST_COMMAND.DAY_FORMAT");
        SCHEDULE_LIST_TITLE = language.getString("SCHEDULE.LIST_COMMAND.TITLE");
        SCHEDULE_LIST_PLAYER_FORMAT = language.getString("SCHEDULE.LIST_COMMAND.PLAYER_FORMAT");
        SCHEDULE_LIST_ADMIN_FORMAT = language.getString("SCHEDULE.LIST_COMMAND.ADMIN_FORMAT");

        DISABLED_COMMANDS_COLON_DISABLED = language.getString("BLOCKED_COMMANDS.COLON_DISABLED");
        DISABLED_COMMANDS_MESSAGE = language.getString("BLOCKED_COMMANDS.DISABLED_COMMAND_MESSAGE");

        BACK_TELEPORTED = language.getString("BACK_COMMAND.TELEPORTED");
        BACK_NO_LOCATION = language.getString("BACK_COMMAND.NO_LOCATION");

        BOOK_DISENCHANT = language.getString("BOOK_DISENCHANT");

        BORDER_REACHED_BORDER = language.getString("BORDER.REACHED_BORDER");
        BORDER_BUCKET_FILL_DENY = language.getString("BORDER.BUCKET_FILL_DENY");
        BORDER_BUCKET_EMPTY_DENY = language.getString("BORDER.BUCKET_EMPTY_DENY");
        BORDER_BLOCK_PLACE_DENY = language.getString("BORDER.BLOCK_PLACE_DENY");
        BORDER_BLOCK_BREAK_DENY = language.getString("BORDER.BLOCK_BREAK_DENY");
        BORDER_TELEPORT_DENY = language.getString("BORDER.TELEPORT_DENY");

        BOTTLE_USAGE = language.getString("BOTTLE_COMMAND.USAGE");
        BOTTLE_NOT_ENOUGH_LEVELS = language.getString("BOTTLE_COMMAND.NOT_ENOUGH_LEVELS");

        BROADCAST_PREFIX = language.getString("BROADCAST_COMMAND.PREFIX");
        BROADCAST_USAGE = language.getString("BROADCAST_COMMAND.USAGE");

        BROADCAST_RAW_USAGE = language.getString("BROADCAST_RAW_COMMAND.USAGE");

        CHAT_MUTED = language.getString("CHAT_CONTROL.CHAT_MUTED");
        CHAT_UNMUTED = language.getString("CHAT_CONTROL.CHAT_UNMUTED");
        CHAT_DELAY_BROADCAST = language.getString("CHAT_CONTROL.CHAT_DELAY_BROADCAST");
        CHAT_CLEAR_BROADCAST = language.getString("CHAT_CONTROL.CHAT_CLEAR_BROADCAST");
        CHAT_EVENT_MUTED_MESSAGE = language.getString("CHAT_CONTROL.CHATEVENT_MUTED_MESSAGE");
        CHAT_COOLDOWN_MESSAGE = language.getString("CHAT_CONTROL.COOLDOWN_MESSAGE");
        CHAT_FOUNDORE_ENABLED = language.getString("CHAT_CONTROL.FOUNDORE_ENABLED");
        CHAT_FOUNDORE_DISABLED = language.getString("CHAT_CONTROL.FOUNDORE_DISABLED");
        CHAT_USAGE = language.getStringList("CHAT_CONTROL.USAGE");

        CLEARINVENTORY_MESSAGE_SELF = language.getString("CLEARINVENTORY_COMMAND.MESSAGE_SELF");
        CLEARINVENTORY_MESSAGE_OTHERS = language.getString("CLEARINVENTORY_COMMAND.MESSAGE_OTHERS");

        COMBAT_TAG_COMMAND_DENY = language.getString("COMBAT_TAG.COMMAND_DENY");
        COMBAT_TAG_EXPIRED = language.getString("COMBAT_TAG.TAG_EXPIRED");
        COMBAT_TAG_END_PORTAL_TELEPORT_DENY = language.getString("COMBAT_TAG.END_PORTAL_TELEPORT_DENY");
        COMBAT_TAG_ENDERCHEST_DENY = language.getString("COMBAT_TAG.ENDERCHEST_DENY");
        COMBAT_TAG_BLOCK_BREAK_DENY = language.getString("COMBAT_TAG.BLOCK_BREAK_DENY");
        COMBAT_TAG_BLOCK_PLACE_DENY = language.getString("COMBAT_TAG.BLOCK_PLACE_DENY");

        COPY_INVENTORY_USAGE = language.getString("COPY_INVENTORY_COMMAND.USAGE");
        COPY_INVENTORY_COPIED = language.getString("COPY_INVENTORY_COMMAND.COPIED");

        CROWBAR_COMMAND_USAGE = language.getString("CROWBAR.COMMAND_USAGE");
        CROWBAR_ZERO_USAGES_SPAWNERS = language.getString("CROWBAR.ZERO_USAGES_SPAWNERS");
        CROWBAR_ZERO_USAGES_PORTALS = language.getString("CROWBAR.ZERO_USAGES_PORTALS");
        CROWBAR_DENY_USAGE_NETHER = language.getString("CROWBAR.DENY_USAGE_NETHER");
        CROWBAR_DENY_USAGE_END = language.getString("CROWBAR.DENY_USAGE_END");
        CROWBAR_DENY_USAGE_WARZONE = language.getString("CROWBAR.DENY_USAGE_WARZONE");
        CROWBAR_GAVE = language.getString("CROWBAR.GAVE");
        CROWBAR_RECEIVED = language.getString("CROWBAR.RECEIVED");

        CUSTOM_TIMER_USAGE = language.getStringList("CUSTOM_TIMER_COMMAND.USAGE");
        CUSTOM_TIMER_LIST_HEADER = language.getStringList("CUSTOM_TIMER_COMMAND.LIST_COMMAND.HEADER");
        CUSTOM_TIMER_LIST_FOOTER = language.getStringList("CUSTOM_TIMER_COMMAND.LIST_COMMAND.FOOTER");
        CUSTOM_TIMER_LIST_FORMAT = language.getString("CUSTOM_TIMER_COMMAND.LIST_COMMAND.TIMER_FORMAT");
        CUSTOM_TIMER_NOT_RUNNING = language.getString("CUSTOM_TIMER_COMMAND.TIMER_NOT_RUNNING");
        CUSTOM_TIMER_STARTED = language.getString("CUSTOM_TIMER_COMMAND.STARTED");
        CUSTOM_TIMER_STOPPED = language.getString("CUSTOM_TIMER_COMMAND.STOPPED");

        DEATHBAN_BAN_MESSAGE = language.getString("DEATHBAN.BAN_MESSAGE");
        DEATHBAN_JOIN_AGAIN_FOR_REVIVE = language.getString("DEATHBAN.JOIN_AGAIN_FOR_REVIVE");
        DEATHBAN_PLAYER_NOT_DEATHBANNED = language.getString("DEATHBAN.PLAYER_NOT_DEATHBANNED");
        DEATHBAN_REVIVED_PLAYER = language.getString("DEATHBAN.REVIVED_PLAYER");
        DEATHBAN_EOTW_MESSAGE = language.getString("DEATHBAN.EOTW_MESSAGE");
        DEATHBAN_COMMAND_USAGE = language.getStringList("DEATHBAN.COMMAND_USAGE");
        DEATHBAN_COMMAND_CHECK = language.getStringList("DEATHBAN.COMMAND_CHECK");

        DEATHMESSAGE_PLAYER_NAME_FORMAT = language.getString("DEATHMESSAGE.PLAYER_NAME_FORMAT");
        DEATHMESSAGE_KILLER_NAME_FORMAT = language.getString("DEATHMESSAGE.KILLER_NAME_FORMAT");
        DEATHMESSAGE_REASON_BLOCK_EXPLOSION = language.getString("DEATHMESSAGE.REASON_BLOCK_EXPLOSION");
        DEATHMESSAGE_REASON_CONTACT = language.getString("DEATHMESSAGE.REASON_CONTACT");
        DEATHMESSAGE_REASON_CUSTOM = language.getString("DEATHMESSAGE.REASON_CUSTOM");
        DEATHMESSAGE_REASON_COMBATLOGGER_KILLER = language.getString("DEATHMESSAGE.REASON_COMBATLOGGER_KILLER");
        DEATHMESSAGE_REASON_COMBATLOGGER = language.getString("DEATHMESSAGE.REASON_COMBATLOGGER");
        DEATHMESSAGE_REASON_DROWNING = language.getString("DEATHMESSAGE.REASON_DROWNING");
        DEATHMESSAGE_REASON_ENTITY_ATTACK_ENTITY = language.getString("DEATHMESSAGE.REASON_ENTITY_ATTACK_ENTITY");
        DEATHMESSAGE_REASON_ENTITY_ATTACK_PLAYER_ITEM = language.getString("DEATHMESSAGE.REASON_ENTITY_ATTACK_PLAYER_ITEM");
        DEATHMESSAGE_REASON_ENTITY_ATTACK_PLAYER_NO_ITEM = language.getString("DEATHMESSAGE.REASON_ENTITY_ATTACK_PLAYER_NO_ITEM");
        DEATHMESSAGE_REASON_ENTITY_EXPLOSION = language.getString("DEATHMESSAGE.REASON_ENTITY_EXPLOSION");
        DEATHMESSAGE_REASON_FALL = language.getString("DEATHMESSAGE.REASON_FALL");
        DEATHMESSAGE_REASON_FALL_KILLER = language.getString("DEATHMESSAGE.REASON_FALL_KILLER");
        DEATHMESSAGE_REASON_FALLING_BLOCK = language.getString("DEATHMESSAGE.REASON_FALLING_BLOCK");
        DEATHMESSAGE_REASON_FIRE = language.getString("DEATHMESSAGE.REASON_FIRE");
        DEATHMESSAGE_REASON_FIRE_TICK = language.getString("DEATHMESSAGE.REASON_FIRE_TICK");
        DEATHMESSAGE_REASON_LAVA = language.getString("DEATHMESSAGE.REASON_LAVA");
        DEATHMESSAGE_REASON_LIGHTNING = language.getString("DEATHMESSAGE.REASON_LIGHTNING");
        DEATHMESSAGE_REASON_MAGIC = language.getString("DEATHMESSAGE.REASON_MAGIC");
        DEATHMESSAGE_REASON_MELTING = language.getString("DEATHMESSAGE.REASON_MELTING");
        DEATHMESSAGE_REASON_POISON = language.getString("DEATHMESSAGE.REASON_POISON");
        DEATHMESSAGE_REASON_PROJECTILE_ENTITY = language.getString("DEATHMESSAGE.REASON_PROJECTILE_ENTITY");
        DEATHMESSAGE_REASON_PROJECTILE_PLAYER_ITEM = language.getString("DEATHMESSAGE.REASON_PROJECTILE_PLAYER_ITEM");
        DEATHMESSAGE_REASON_PROJECTILE_PLAYER_NO_ITEM = language.getString("DEATHMESSAGE.REASON_PROJECTILE_PLAYER_NO_ITEM");
        DEATHMESSAGE_REASON_STARVATION = language.getString("DEATHMESSAGE.REASON_STARVATION");
        DEATHMESSAGE_REASON_SUFFOCATION = language.getString("DEATHMESSAGE.REASON_SUFFOCATION");
        DEATHMESSAGE_REASON_SUICIDE = language.getString("DEATHMESSAGE.REASON_SUICIDE");
        DEATHMESSAGE_REASON_THORNS = language.getString("DEATHMESSAGE.REASON_THORNS");
        DEATHMESSAGE_REASON_VOID = language.getString("DEATHMESSAGE.REASON_VOID");
        DEATHMESSAGE_REASON_VOID_KILLER = language.getString("DEATHMESSAGE.REASON_VOID_KILLER");
        DEATHMESSAGE_REASON_WITHER = language.getString("DEATHMESSAGE.REASON_WITHER");

        DEL_WARP_USAGE = language.getString("DEL_WARP_COMMAND.USAGE");
        DEL_WARP_DELETED = language.getString("DEL_WARP_COMMAND.DELETED");
        DEL_WARP_DOESNT_EXIST = language.getString("DEL_WARP_COMMAND.DOESNT_EXIST");

        ECONOMY_BALANCE_SELF = language.getString("ECONOMY.BALANCE_SELF");
        ECONOMY_BALANCE_OTHERS = language.getString("ECONOMY.BALANCE_OTHERS");
        ECONOMY_PAY_USAGE = language.getString("ECONOMY.PAY_USAGE");
        ECONOMY_BALANCE_CHANGED = language.getString("ECONOMY.BALANCE_CHANGED");
        ECONOMY_BALANCE_CHANGED_STAFF = language.getString("ECONOMY.BALANCE_CHANGED_STAFF");
        ECONOMY_CANNOT_SEND_TO_YOURSELF = language.getString("ECONOMY.CANNOT_SEND_TO_YOURSELF");
        TRANSACTION_INVALID_AMOUNT = language.getString("ECONOMY.TRANSACTION_INVALID_AMOUNT");
        TRANSACTION_MAX_AMOUNT = language.getString("ECONOMY.TRANSACTION_MAX_AMOUNT");
        TRANSACTION_MIN_AMOUNT = language.getString("ECONOMY.TRANSACTION_MIN_AMOUNT");
        TRANSACTION_SUCCESS_SELF = language.getString("ECONOMY.TRANSACTION_SUCCESS_SELF");
        TRANSACTION_SUCCESS_OTHERS = language.getString("ECONOMY.TRANSACTION_SUCCESS_OTHERS");
        ECONOMY_ADMIN_COMMAND_USAGE = language.getStringList("ECONOMY.ADMIN_COMMAND_USAGE");

        ECONOMY_SIGNS_INVALID_MATERIAL = language.getString("ECONOMY_SIGNS.INVALID_MATERIAL");
        ECONOMY_SIGNS_INVALID_AMOUNT = language.getString("ECONOMY_SIGNS.INVALID_AMOUNT");
        ECONOMY_SIGNS_INVALID_PRICE = language.getString("ECONOMY_SIGNS.INVALID_PRICE");
        ECONOMY_SIGNS_CANNOT_AFFORD = language.getString("ECONOMY_SIGNS.CANNOT_AFFORD");
        ECONOMY_SIGNS_NOT_CARRYING = language.getString("ECONOMY_SIGNS.NOT_CARRYING");
        ECONOMY_SIGNS_NO_SPACE = language.getString("ECONOMY_SIGNS.NO_SPACE");
        ECONOMY_SIGNS_BOUGHT = language.getString("ECONOMY_SIGNS.BOUGHT");
        ECONOMY_SIGNS_SOLD = language.getString("ECONOMY_SIGNS.SOLD");

        ENCHANT_USAGE = language.getString("ENCHANT_COMMAND.USAGE");
        ENCHANT_DOESNT_EXIST = language.getString("ENCHANT_COMMAND.DOESNT_EXIST");
        ENCHANT_NON_OP_LIMIT = language.getString("ENCHANT_COMMAND.NON_OP_LIMIT");
        ENCHANT_ENCHANTED = language.getString("ENCHANT_COMMAND.ENCHANTED");
        ENCHANT_ENCHANT_REMOVED = language.getString("ENCHANT_COMMAND.ENCHANT_REMOVED");

        ENDERPEARL_DENY_MESSAGE = language.getString("ENDERPEARL.DENY_MESSAGE");
        ENDERPEARL_DENY_REFUNDED  = language.getString("ENDERPEARL.DENY_REFUNDED");
        ENDERPEARL_COOLDOWN_EXPIRED = language.getString("ENDERPEARL.COOLDOWN_EXPIRED");
        ENDERPEARL_GLITCH_DENY = language.getString("ENDERPEARL.GLITCH_DENY");

        ENDPORTAL_SELECTOR_ADDED_TO_INVENTORY = language.getString("ENDPORTAL.SELECTOR_ADDED_TO_INVENTORY");
        ENDPORTAL_NOT_END_PORTAL_FRAME = language.getString("ENDPORTAL.NOT_END_PORTAL_FRAME");
        ENDPORTAL_SELECTION_TOO_BIG_OR_SMALL = language.getString("ENDPORTAL.SELECTION_TOO_BIG_OR_SMALL");
        ENDPORTAL_WRONG_ELEVATION = language.getString("ENDPORTAL.WRONG_ELEVATION");

        EOTW_NO_EOTW_KOTH = language.getString("EOTW.NO_EOTW_KOTH");
        EOTW_NOT_RUNNING = language.getString("EOTW.NOT_RUNNING");
        EOTW_ALREADY_RUNNING = language.getString("EOTW.ALREADY_RUNNING");
        EOTW_TIMER_STARTED = language.getString("EOTW.TIMER_STARTED");
        EOTW_BROADCAST_START = language.getString("EOTW.BROADCAST_START");
        EOTW_BROADCAST_STOP = language.getString("EOTW.BROADCAST_STOP");
        EOTW_PVP_TIMER_DISABLED = language.getString("EOTW.PVP_TIMER_DISABLED");
        EOTW_COMMAND_USAGE = language.getStringList("EOTW.COMMAND_USAGE");

        EXIT_DOESNT_EXIST = language.getString("EXIT_COMMAND.EXIT_DOESNT_EXIST");
        EXIT_TELEPORTED = language.getString("EXIT_COMMAND.TELEPORTED");

        EXPERIENCE_ADDED = language.getString("EXPERIENCE_COMMAND.ADDED");
        EXPERIENCE_REMOVED = language.getString("EXPERIENCE_COMMAND.REMOVED");
        EXPERIENCE_SET = language.getString("EXPERIENCE_COMMAND.SET");
        EXPERIENCE_USAGE = language.getStringList("EXPERIENCE_COMMAND.USAGE");

        FEED_MESSAGE_SELF = language.getString("FEED_COMMAND.MESSAGE_SELF");
        FEED_MESSAGE_OTHERS = language.getString("FEED_COMMAND.MESSAGE_OTHERS");

        FILTER_ADDED = language.getString("FILTER_COMMAND.ADDED");
        FILTER_REMOVED = language.getString("FILTER_COMMAND.REMOVED");
        FILTER_CLEARED = language.getString("FILTER_COMMAND.CLEARED");
        FILTER_EMPTY = language.getString("FILTER_COMMAND.EMPTY");
        FILTER_TOGGLED_ON = language.getString("FILTER_COMMAND.TOGGLED_ON");
        FILTER_TOGGLED_OFF = language.getString("FILTER_COMMAND.TOGGLED_OFF");
        FILTER_ALREADY_FILTERED = language.getString("FILTER_COMMAND.ALREADY_FILTERED");
        FILTER_NOT_FILTERED = language.getString("FILTER_COMMAND.NOT_FILTERED");
        FILTER_LIST_MESSAGE = language.getStringList("FILTER_COMMAND.LIST_MESSAGE");
        FILTER_USAGE = language.getStringList("FILTER_COMMAND.USAGE");

        FILL_BOTTLE_NO_GLASS_BOTTLE = language.getString("FILL_BOTTLE_COMMAND.NO_GLASS_BOTTLE");
        FILL_BOTTLE_CHEST_NOT_FOUND = language.getString("FILL_BOTTLE_COMMAND.CHEST_NOT_FOUND");
        FILL_BOTTLE_ALREADY_FULL = language.getString("FILL_BOTTLE_COMMAND.ALREADY_FULL");
        FILL_BOTTLE_FILLED = language.getString("FILL_BOTTLE_COMMAND.FILLED");

        FLY_SELF_ENABLED = language.getString("FLY_COMMAND.MESSAGE_SELF_ENABLED");
        FLY_SELF_DISABLED = language.getString("FLY_COMMAND.MESSAGE_SELF_DISABLED");
        FLY_OTHERS_ENABLED = language.getString("FLY_COMMAND.MESSAGE_OTHERS_ENABLED");
        FLY_OTHERS_DISABLED = language.getString("FLY_COMMAND.MESSAGE_OTHERS_DISABLED");

        FOCUS_USAGE = language.getString("FOCUS_COMMAND.USAGE");
        FOCUS_ALREADY_FOCUSING = language.getString("FOCUS_COMMAND.ALREADY_FOCUSING");
        FOCUS_CANNOT_FOCUS = language.getString("FOCUS_COMMAND.CANNOT_FOCUS");
        FOCUS_FOCUSED = language.getStringList("FOCUS_COMMAND.FOCUSED");

        FOUND_ORE_MESSAGE = language.getString("FOUND_ORE.MESSAGE");

        FREEZE_USAGE = language.getString("FREEZE_COMMAND.USAGE");
        FREEZE_DISABLED_COMMAND_MESSAGE = language.getString("FREEZE_COMMAND.DISABLED_COMMAND_MESSAGE");

        FREEZE_FREEZED_STAFF_MESSAGE = language.getString("FREEZE_COMMAND.FREEZED_STAFF_MESSAGE");
        FREEZE_UNFREEZED_STAFF_MESSAGE = language.getString("FREEZE_COMMAND.UNFREEZED_STAFF_MESSAGE");
        FREEZE_FREEZED_ALL = language.getString("FREEZE_COMMAND.FREEZE_ALL");
        FREEZE_UNFREEZED_ALL = language.getString("FREEZE_COMMAND.UNFREEZE_ALL");
        FREEZE_UNFREEZED_MESSAGE_TARGET = language.getString("FREEZE_COMMAND.UNFREEZED_MESSAGE_TARGET");
        FREEZE_PVP_DENY_MESSAGE_DAMAGER = language.getString("FREEZE_COMMAND.PVP_DENY_MESSAGE_DAMAGER");
        FREEZE_PVP_DENY_MESSAGE_VICTIM = language.getString("FREEZE_COMMAND.PVP_DENY_MESSAGE_VICTIM");
        FREEZE_QUIT_WHEN_FROZEN = language.getString("FREEZE_COMMAND.QUIT_WHEN_FROZEN");
        FREEZE_CAN_NOT_FREEZE_PLAYER = language.getString("FREEZE_COMMAND.CAN_NOT_FREEZE_PLAYER");
        FROZEN_MESSAGE = language.getStringList("FREEZE_COMMAND.MESSAGE");

        GAMEMODE_USAGE = language.getString("GAMEMODE_COMMAND.USAGE");
        GAMEMODE_INVALID_GAMEMODE = language.getString("GAMEMODE_COMMAND.INVALID_GAMEMODE");
        GAMEMODE_MESSAGE_SELF = language.getString("GAMEMODE_COMMAND.MESSAGE_SELF");
        GAMEMODE_MESSAGE_OTHERS = language.getString("GAMEMODE_COMMAND.MESSAGE_OTHERS");

        GIVE_USAGE = language.getString("GIVE_COMMAND.USAGE");
        GIVE_INVALID_ITEM = language.getString("GIVE_COMMAND.INVALID_ITEM");
        GIVE_GIVEN_SENDER = language.getString("GIVE_COMMAND.GIVEN_SENDER");
        GIVE_GIVEN_OTHERS = language.getString("GIVE_COMMAND.GIVEN_OTHERS");
        GIVE_INVENTORY_FULL = language.getString("GIVE_COMMAND.INVENTORY_FULL");

        GOD_SELF_ENABLED = language.getString("GOD_COMMAND.MESSAGE_SELF_ENABLED");
        GOD_SELF_DISABLED = language.getString("GOD_COMMAND.MESSAGE_SELF_DISABLED");
        GOD_OTHERS_ENABLED = language.getString("GOD_COMMAND.MESSAGE_OTHERS_ENABLED");
        GOD_OTHERS_DISABLED = language.getString("GOD_COMMAND.MESSAGE_OTHERS_DISABLED");

        GOLDEN_APPLE_NO_COOLDOWNS = language.getString("GOLDEN_APPLE.NO_COOLDOWNS");
        NORMAL_APPLE_COOLDOWN_STARTED = language.getString("GOLDEN_APPLE.NORMAL.COOLDOWN_STARTED_MESSAGE");
        NORMAL_APPLE_COOLDOWN_DENY = language.getString("GOLDEN_APPLE.NORMAL.COOLDOWN_DENY_MESSAGE");
        NORMAL_APPLE_COOLDOWN_EXPIRED = language.getString("GOLDEN_APPLE.NORMAL.COOLDOWN_EXPIRED");
        NORMAL_APPLE_COMMAND_MESSAGE = language.getString("GOLDEN_APPLE.NORMAL.COMMAND_MESSAGE");
        ENCHANTED_APPLE_COOLDOWN_STARTED = language.getString("GOLDEN_APPLE.ENCHANTED.COOLDOWN_STARTED_MESSAGE");
        ENCHANTED_APPLE_COOLDOWN_DENY = language.getString("GOLDEN_APPLE.ENCHANTED.COOLDOWN_DENY_MESSAGE");
        ENCHANTED_APPLE_COOLDOWN_EXPIRED = language.getString("GOLDEN_APPLE.ENCHANTED.COOLDOWN_EXPIRED");
        ENCHANTED_APPLE_COMMAND_MESSAGE = language.getString("GOLDEN_APPLE.ENCHANTED.COMMAND_MESSAGE");

        HEAD_DROP_CLICK_MESSAGE = language.getString("HEAD_DROP.CLICK_MESSAGE");

        HEAL_MESSAGE_SELF = language.getString("HEAL_COMMAND.MESSAGE_SELF");
        HEAL_MESSAGE_OTHERS = language.getString("HEAL_COMMAND.MESSAGE_OTHERS");

        HIDE_STAFF_HIDDEN = language.getString("HIDE_STAFF_COMMAND.HIDDEN");
        HIDE_STAFF_SHOWN = language.getString("HIDE_STAFF_COMMAND.SHOWN");

        IGNORE_USAGE = language.getString("IGNORE_COMMAND.USAGE");
        IGNORE_ENABLED = language.getString("IGNORE_COMMAND.ENABLED");
        IGNORE_DISABLED = language.getString("IGNORE_COMMAND.DISABLED");
        IGNORE_CANNOT_IGNORE_SELF = language.getString("IGNORE_COMMAND.CANNOT_IGNORE_SELF");

        INVENTORY_INSPECT_USAGE = language.getString("INVENTORY_INSPECT_COMMAND.USAGE");

        INVENTORY_RESTORE_USAGE = language.getString("INVENTORY_RESTORE_COMMAND.USAGE");
        INVENTORY_RESTORE_NOTHING_TO_RESTORE = language.getString("INVENTORY_RESTORE_COMMAND.NOTHING_TO_RESTORE");
        INVENTORY_RESTORE_RESTORED_SENDER = language.getString("INVENTORY_RESTORE_COMMAND.RESTORED_SENDER");
        INVENTORY_RESTORE_RESTORED_PLAYER = language.getString("INVENTORY_RESTORE_COMMAND.RESTORED_PLAYER");

        INVSEE_USAGE = language.getString("INVSEE_COMMAND.USAGE");

        ITEM_USAGE = language.getString("ITEM_COMMAND.USAGE");
        ITEM_INVALID_ITEM = language.getString("ITEM_COMMAND.INVALID_ITEM");
        ITEM_RECEIVED = language.getString("ITEM_COMMAND.RECEIVED");
        ITEM_INVENTORY_FULL = language.getString("ITEM_COMMAND.INVENTORY_FULL");

        JOIN_FULL_SERVER_MESSAGE = language.getString("JOIN_FULL_SERVER_MESSAGE");

        KICKALL_USAGE = language.getString("KICKALL_COMMAND.USAGE");
        KICKALL_STAFF_MESSAGE = language.getString("KICKALL_COMMAND.STAFF_MESSAGE");

        KILLALL_USAGE = language.getString("KILLALL_COMMAND.USAGE");
        KILLALL_KILLED = language.getString("KILLALL_COMMAND.KILLED");
        KILLALL_COUNT = language.getString("KILLALL_COMMAND.COUNT");

        KITMAP_KILLSTREAK_ON_DEATH = language.getString("KITMAP_KILLSTREAK.ON_DEATH");
        KITMAP_KILLSTREAK_MESSAGE = language.getString("KITMAP_KILLSTREAK.MESSAGE");

        KITMAP_DENY_ITEM_DROP = language.getString("KITMAP_DENY_ITEM_DROP");

        KILL_USAGE = language.getString("KILL_COMMAND.USAGE");
        KILL_MESSAGE_SELF = language.getString("KILL_COMMAND.MESSAGE_SELF");
        KILL_MESSAGE_OTHERS = language.getString("KILL_COMMAND.MESSAGE_OTHERS");

        KITS_COMMAND_USAGE_PLAYER = language.getStringList("KITS.COMMAND_USAGE_PLAYER");
        KITS_COMMAND_USAGE_ADMIN = language.getStringList("KITS.COMMAND_USAGE_ADMIN");

        KITS_APPLIED_OTHERS = language.getString("KITS.APPLIED_OTHERS");
        KITS_EQUIPPED = language.getString("KITS.EQUIPPED");

        KITS_EXCEPTION_ONE_TIME_ONLY = language.getString("KITS.EXCEPTION.ONE_TIME_ONLY");
        KITS_EXCEPTION_COOLDOWN = language.getString("KITS.EXCEPTION.COOLDOWN");
        KITS_EXCEPTION_NO_PERMISSION = language.getString("KITS.EXCEPTION.NO_PERMISSION");
        KITS_EXCEPTION_ALREADY_EXISTS = language.getString("KITS.EXCEPTION.ALREADY_EXISTS");
        KITS_EXCEPTION_DOESNT_EXISTS = language.getString("KITS.EXCEPTION.DOESNT_EXISTS");
        KITS_EXCEPTION_FULL_INVENTORY = language.getString("KITS.EXCEPTION.FULL_INVENTORY");
        KITS_EXCEPTION_KITMAP_ONLY_IN_SAFEZONE = language.getString("KITS.EXCEPTION.KITMAP_ONLY_IN_SAFEZONE");

        KITS_CREATE_USAGE = language.getString("KITS.CREATE_COMMAND.USAGE");
        KITS_CREATE_CREATED = language.getString("KITS.CREATE_COMMAND.CREATED");
        KITS_CREATE_INVALID_KIT_TYPE = language.getString("KITS.CREATE_COMMAND.INVALID_KIT_TYPE");

        KITS_EDIT_USAGE = language.getString("KITS.EDIT_COMMAND.USAGE");
        KITS_EDIT_EDITED = language.getString("KITS.EDIT_COMMAND.EDITED");

        KITS_GIVE_USAGE = language.getString("KITS.GIVE_COMMAND.USAGE");

        KITS_LIST_NO_KITS = language.getString("KITS.LIST_COMMAND.NO_KITS");
        KITS_LIST_FORMAT = language.getString("KITS.LIST_COMMAND.FORMAT");

        KITS_REMOVE_USAGE = language.getString("KITS.REMOVE_COMMAND.USAGE");
        KITS_REMOVE_REMOVED = language.getString("KITS.REMOVE_COMMAND.REMOVED");
        KITS_REMOVE_CANNOT_REMOVE_SPECIAL_EVENT_KIT = language.getString("KITS.REMOVE_COMMAND.CANNOT_REMOVE_SPECIAL_EVENT_KIT");

        KITS_SET_DELAY_USAGE = language.getString("KITS.SET_DELAY_COMMAND.USAGE");
        KITS_SET_DELAY_CHANGED = language.getString("KITS.SET_DELAY_COMMAND.CHANGED");

        LAG_COMMAND_WORLD_FORMAT = language.getString("LAG_COMMAND.WORLD_FORMAT");
        LAG_COMMAND_MESSAGE = language.getStringList("LAG_COMMAND.MESSAGE");

        LAST_DEATHS_USAGE = language.getString("LAST_DEATHS_COMMAND.USAGE");
        LAST_DEATHS_HEADER_FORMAT = language.getString("LAST_DEATHS_COMMAND.HEADER_FORMAT");
        LAST_DEATHS_DEATH_MESSAGE_FORMAT = language.getString("LAST_DEATHS_COMMAND.DEATH_MESSAGE_FORMAT");
        LAST_DEATHS_NO_DEATHS_YET = language.getString("LAST_DEATHS_COMMAND.NO_DEATHS_YET");

        LAST_KILLS_COMMAND_USAGE = language.getString("LAST_KILLS_COMMAND.USAGE");
        LAST_KILLS_COMMAND_HEADER_FORMAT = language.getString("LAST_KILLS_COMMAND.HEADER_FORMAT");
        LAST_KILLS_COMMAND_KILL_MESSAGE_FORMAT = language.getString("LAST_KILLS_COMMAND.KILL_MESSAGE_FORMAT");
        LAST_KILLS_COMMAND_NO_KILLS_YET = language.getString("LAST_KILLS_COMMAND.NO_KILLS_YET");

        LFF_COMMAND_COOLDOWN = language.getString("LFF_COMMAND.COOLDOWN");
        LFF_COMMAND_COOLDOWN_EXPIRED = language.getString("LFF_COMMAND.COOLDOWN_EXPIRED");
        LFF_COMMAND_ALREADY_IN_FACTION = language.getString("LFF_COMMAND.ALREADY_IN_FACTION");
        LFF_COMMAND_MESSAGE = language.getStringList("LFF_COMMAND.MESSAGE");

        LIST_COMMAND = language.getStringList("LIST_COMMAND");

        LIVES_CHECK_SELF = language.getString("LIVES_COMMAND.CHECK_SELF");
        LIVES_CHECK_OTHERS = language.getString("LIVES_COMMAND.CHECK_OTHERS");
        LIVES_PLAYER_NOT_DEATHBANNED = language.getString("LIVES_COMMAND.PLAYER_NOT_DEATHBANNED");
        LIVES_SUCCESSFULLY_REVIVED_PLAYER = language.getString("LIVES_COMMAND.SUCCESSFULLY_REVIVED_PLAYER");
        LIVES_SUCCESSFULLY_SENT_LIVES = language.getString("LIVES_COMMAND.SUCCESSFULLY_SENT_LIVES");
        LIVES_SUCCESSFULLY_RECEIVED_LIVES = language.getString("LIVES_COMMAND.SUCCESSFULLY_RECEIVED_LIVES");
        LIVES_CAN_NOT_SEND_LIVES_TO_YOURSELF = language.getString("LIVES_COMMAND.CAN_NOT_SEND_LIVES_TO_YOURSELF");
        LIVES_ADD_USAGE = language.getString("LIVES_COMMAND.LIVES_ADD_USAGE");
        LIVES_ADDED = language.getString("LIVES_COMMAND.LIVES_ADDED");
        LIVES_ADD_RECEIVED = language.getString("LIVES_COMMAND.LIVES_ADD_RECEIVED");
        LIVES_SET_USAGE = language.getString("LIVES_COMMAND.LIVES_SET_USAGE");
        LIVES_SET = language.getString("LIVES_COMMAND.LIVES_SET");
        LIVES_SET_RECEIVED = language.getString("LIVES_COMMAND.LIVES_SET_RECEIVED");
        LIVES_ZERO_LIVES = language.getString("LIVES_COMMAND.ZERO_LIVES");
        LIVES_NOT_ENOUGH_LIVES = language.getString("LIVES_COMMAND.NOT_ENOUGH_LIVES");
        LIVES_USAGE = language.getStringList("LIVES_COMMAND.USAGE");

        LOGOUT_KICK_MESSAGE = language.getString("LOGOUT.KICK_MESSAGE");
        LOGOUT_TELEPORT_CANCELLED_MOVED = language.getString("LOGOUT.TELEPORT_CANCELLED_MOVED");
        LOGOUT_TELEPORT_CANCELLED_DAMAGED = language.getString("LOGOUT.TELEPORT_CANCELLED_DAMAGED");
        LOGOUT_ALREADY_RUNNING = language.getString("LOGOUT.ALREADY_RUNNING");
        LOGOUT_START_MESSAGE = language.getString("LOGOUT.START_MESSAGE");

        MAPKIT_EDIT_MESSAGE = language.getString("MAPKIT_EDIT_MESSAGE");

        MESSAGE_USAGE = language.getString("MESSAGE_COMMAND.USAGE");
        MESSAGE_SEND_FORMAT = language.getString("MESSAGE_COMMAND.SEND_FORMAT");
        MESSAGE_RECEIVE_FORMAT = language.getString("MESSAGE_COMMAND.RECEIVE_FORMAT");
        MESSAGE_MESSAGES_DISABLED = language.getString("MESSAGE_COMMAND.MESSAGES_DISABLED");

        MINECART_ELEVATOR_DENY_COMBAT = language.getString("MINECART_ELEVATOR.DENY_COMBAT");
        MINECART_ELEVATOR_OWN_CLAIM_ONLY = language.getString("MINECART_ELEVATOR.OWN_CLAIM_ONLY");
        MINECART_ELEVATOR_NO_VALID_LOCATION = language.getString("MINECART_ELEBATOR.NO_VALID_LOCATION");

        MINER_EFFECT_HEADER = language.getString("MINER_EFFECT_COMMAND.HEADER");
        MINER_EFFECT_FOOTER = language.getString("MINER_EFFECT_COMMAND.FOOTER");
        MINER_EFFECT_TITLE = language.getString("MINER_EFFECT_COMMAND.TITLE");
        MINER_EFFECT_FORMAT = language.getStringList("MINER_EFFECT_COMMAND.FORMAT");

        MORE_COMMAND_MESSAGE = language.getString("MORE_COMMAND_MESSAGE");

        NETHER_PORTAL_TRAP_FIX = language.getString("NETHER_PORTAL_TRAP_FIX");

        NOTES_COMMAND_MESSAGE = language.getString("NOTES_COMMAND.MESSAGE");
        NOTES_COMMAND_NO_NOTES = language.getString("NOTES_COMMAND.NO_NOTES");
        NOTES_COMMAND_NOTE_ADDED = language.getString("NOTES_COMMAND.NOTE_ADDED");
        NOTES_COMMAND_NOTE_REMOVED = language.getString("NOTES_COMMAND.NOTE_REMOVED");
        NOTES_COMMAND_NOTE_DOESNT_EXIST = language.getString("NOTES_COMMAND.NOTE_DOESNT_EXIST");
        NOTES_COMMAND_FORMAT = language.getString("NOTES_COMMAND.FORMAT");
        NOTES_COMMAND_USAGE = language.getStringList("NOTES_COMMAND.USAGE");

        PING_MESSAGE_SELF = language.getString("PING_COMMAND.MESSAGE_SELF");
        PING_MESSAGE_OTHERS = language.getString("PING_COMMAND.MESSAGE_OTHERS");

        PLAYTIME_COMMAND_USAGE = language.getString("PLAYTIME_COMMAND.USAGE");
        PLAYTIME_COMMAND_MESSAGE = language.getString("PLAYTIME_COMMAND.MESSAGE");

        PURGE_STARTED = language.getString("PURGE.STARTED");
        PURGE_ENDED = language.getString("PURGE.ENDED");
        PURGE_NOT_RUNNING = language.getString("PURGE.NOT_RUNNING");
        PURGE_ALREADY_RUNNING = language.getString("PURGE.ALREADY_RUNNING");
        PURGE_BROADCAST_START = language.getString("PURGE.BROADCAST_START");
        PURGE_BROADCAST_STOP = language.getString("PURGE.BROADCAST_STOP");
        PURGE_COMMAND_USAGE = language.getStringList("PURGE.COMMAND_USAGE");

        PVP_CLASSES_ACTIVATED = language.getString("PVP_CLASSES.ACTIVATED");
        PVP_CLASSES_DEACTIVATED = language.getString("PVP_CLASSES.DEACTIVATED");
        PVP_CLASSES_WARMING_UP = language.getString("PVP_CLASSES.WARMING_UP");
        PVP_CLASSES_WARMUP_CANCELLED = language.getString("PVP_CLASSES.WARMUP_CANCELLED");

        PVP_COMMAND_PROTECTION_DISABLED = language.getString("PVP_COMMAND.PROTECTION_DISABLED");
        PVP_COMMAND_NOT_ACTIVE = language.getString("PVP_COMMAND.NOT_ACTIVE");
        PVP_COMMAND_TIME_STATUS = language.getString("PVP_COMMAND.TIME_STATUS");
        PVP_COMMAND_USAGE = language.getStringList("PVP_COMMAND.USAGE");

        PVP_PROT_DENY_TELEPORT = language.getString("PVP_PROTECTION.DENY_TELEPORT");
        PVP_PROT_PROTECTION_EXPIRED = language.getString("PVP_PROTECTION.PROTECTION_EXPIRED");
        PVP_PROT_PVP_DENY_ATTACKER = language.getString("PVP_PROTECTION.PVP_DENY_ATTACKER");
        PVP_PROT_PVP_DENY_VICTIM = language.getString("PVP_PROTECTION.PVP_DENY_VICTIM");
        PVP_PROT_ITEM_DENY_MESSAGE = language.getString("PVP_PROTECTION.ITEM_DENY_MESSAGE");
        PVP_PROT_END_PORTAL_TELEPORT_DENY = language.getString("PVP_PROTECTION.END_PORTAL_TELEPORT_DENY");
        PVP_PROT_TELEPORT_DUE_TO_CLAIM = language.getString("PVP_PROTECTION.TELEPORT_DUE_TO_CLAIM");

        RANK_REVIVE_USAGE = language.getString("RANK_REVIVE_COMMAND.USAGE");
        RANK_REVIVE_NO_PERMISSION = language.getString("RANK_REVIVE_COMMAND.NO_PERMISSION");
        RANK_REVIVE_NOT_DEATHBANNED = language.getString("RANK_REVIVE_COMMAND.NOT_DEATHBANNED");
        RANK_REVIVE_COOLDOWN_EXPIRED = language.getString("RANK_REVIVE_COMMAND.COOLDOWN_EXPIRED");
        RANK_REVIVE_COOLDOWN_ACTIVE = language.getString("RANK_REVIVE_COMMAND.COOLDOWN_ACTIVE");
        RANK_REVIVE_BROADCAST_MESSAGE = language.getString("RANK_REVIVE_COMMAND.BROADCAST_MESSAGE");

        REBOOT_USAGE = language.getStringList("REBOOT_COMMAND.USAGE");
        REBOOT_NOT_RUNNING = language.getString("REBOOT_COMMAND.NOT_RUNNING");
        REBOOT_ALREADY_RUNNING = language.getString("REBOOT_COMMAND.ALREADY_RUNNING");
        REBOOT_PLAYER_KICK_MESSAGE = language.getString("REBOOT_COMMAND.PLAYER_KICK_MESSAGE");
        REBOOT_BROADCAST = language.getStringList("REBOOT_COMMAND.BROADCAST");
        REBOOT_CANCELED = language.getStringList("REBOOT_COMMAND.CANCELED");

        SALE_STARTED = language.getString("SALE_COMMAND.STARTED");
        SALE_STOPPED = language.getString("SALE_COMMAND.STOPPED");
        SALE_FINISHED = language.getString("SALE_COMMAND.FINISHED");
        SALE_EXCEPTION_ALREADY_RUNNING = language.getString("SALE_COMMAND.EXCEPTION.ALREADY_RUNNING");
        SALE_EXCEPTION_NOT_RUNNING = language.getString("SALE_COMMAND.EXCEPTION.NOT_RUNNING");
        SALE_USAGE = language.getStringList("SALE_COMMAND.USAGE");

        SALVAGE_NOT_SALVAGEABLE = language.getString("SALVAGE_COMMAND.NOT_SALVAGEABLE");
        SALVAGE_SALVAGED = language.getString("SALVAGE_COMMAND.SALVAGED");
        SALVAGE_INGREDIENT_FORMAT = language.getString("SALVAGE_COMMAND.INGREDIENT_FORMAT");

        POTION_LIMITER_DENY_MESSAGE = language.getString("POTION_LIMITER_DENY_MESSAGE");

        RECLAIM_ALREADY_USED = language.getString("RECLAIM_COMMAND.ALREADY_USED");
        RECLAIM_BROADCAST_MESSAGE = language.getString("RECLAIM_COMMAND.BROADCAST_MESSAGE");
        RECLAIM_NO_PERMISSION = language.getString("RECLAIM_COMMAND.NO_PERMISSION");

        RENAME_USAGE = language.getString("RENAME_COMMAND.USAGE");
        RENAME_MAX_LENGTH_EXCEEDED = language.getString("RENAME_COMMAND.MAX_LENGTH_EXCEEDED");
        RENAME_BLACKLISTED_WORD = language.getString("RENAME_COMMAND.BLACKLISTED_WORD");
        RENAME_RENAMED = language.getString("RENAME_COMMAND.RENAMED");

        REPLY_USAGE = language.getString("REPLY_COMMAND.USAGE");
        REPLY_NOBODY_TO_REPLY = language.getString("REPLY_COMMAND.NOBODY_TO_REPLY");

        REPAIR_USAGE = language.getString("REPAIR_COMMAND.USAGE");
        REPAIR_NOT_REPAIRABLE = language.getString("REPAIR_COMMAND.NOT_REPAIRABLE");
        REPAIR_REPAIRED_ITEM = language.getString("REPAIR_COMMAND.REPAIRED_ITEM");
        REPAIR_REPAIRED_ALL = language.getString("REPAIR_COMMAND.REPAIRED_ALL");

        REPORT_USAGE = language.getString("REPORT_COMMAND.USAGE");
        REPORT_REPORTED = language.getString("REPORT_COMMAND.REPORTED");
        REPORT_COOLDOWN = language.getString("REPORT_COMMAND.COOLDOWN");
        REPORT_COOLDOWN_EXPIRED = language.getString("REPORT_COMMAND.COOLDOWN_EXPIRED");
        REPORT_FORMAT = language.getStringList("REPORT_COMMAND.FORMAT");

        REQUEST_USAGE = language.getString("REQUEST_COMMAND.USAGE");
        REQUEST_REQUESTED = language.getString("REQUEST_COMMAND.REQUESTED");
        REQUEST_COOLDOWN = language.getString("REQUEST_COMMAND.COOLDOWN");
        REQUEST_COOLDOWN_EXPIRED = language.getString("REQUEST_COMMAND.COOLDOWN_EXPIRED");
        REQUEST_FORMAT = language.getStringList("REQUEST_COMMAND.FORMAT");

        SEEN_USAGE = language.getString("SEEN_COMMAND.USAGE");
        SEEN_ONLINE = language.getString("SEEN_COMMAND.ONLINE");
        SEEN_MESSAGE = language.getString("SEEN_COMMAND.MESSAGE");

        SELECTION_LOCATION_ONE_SET = language.getString("SELECTION.LOCATION_ONE_SET");
        SELECTION_LOCATION_TWO_SET = language.getString("SELECTION.LOCATION_TWO_SET");

        SET_EXIT_USAGE = language.getStringList("SET_EXIT_COMMAND.USAGE");
        SET_EXIT_EXIT_SET = language.getString("SET_EXIT_COMMAND.EXIT_SET");

        SET_RECLAIM_USAGE = language.getString("SET_RECLAIM_COMMAND.USAGE");
        SET_RECLAIM_INVALID_BOOLEAN = language.getString("SET_RECLAIM_COMMAND.INVALID_BOOLEAN");
        SET_RECLAIM_SET_USED_MESSAGE = language.getString("SET_RECLAIM_COMMAND.SET_USED_MESSAGE");
        SET_RECLAIM_SET_NOT_USED_MESSAGE = language.getString("SET_RECLAIM_COMMAND.SET_NOT_USED_MESSAGE");

        SET_SLOTS_USAGE = language.getString("SET_SLOTS_COMMAND.USAGE");
        SET_SLOTS_MESSAGE = language.getStringList("SET_SLOTS_COMMAND.MESSAGE");

        SET_SPAWN_USAGE = language.getStringList("SET_SPAWN_COMMAND.USAGE");
        SET_SPAWN_SPAWN_SET = language.getString("SET_SPAWN_COMMAND.SPAWN_SET");
        SET_SPAWN_NOT_IN_WORLD = language.getString("SET_SPAWN_COMMAND.NOT_IN_WORLD");

        SET_WARP_USAGE = language.getString("SET_WARP_COMMAND.USAGE");
        SET_WARP_CREATED = language.getString("SET_WARP_COMMAND.CREATED");
        SET_WARP_ALREADY_EXIST = language.getString("SET_WARP_COMMAND.ALREADY_EXIST");

        SIGN_ELEVATOR_CREATED = language.getString("SIGN_ELEVATOR.CREATED");
        SIGN_ELEVATOR_DESTROYED = language.getString("SIGN_ELEVATOR.DESTROYED");
        SIGN_ELEVATOR_NO_VALID_LOCATION = language.getString("SIGN_ELEVATOR.NO_VALID_LOCATION");

        SOCIAL_SPY_ENABLED = language.getString("SOCIAL_SPY_COMMAND.ENABLED");
        SOCIAL_SPY_DISABLED = language.getString("SOCIAL_SPY_COMMAND.DISABLED");
        SOCIAL_SPY_MESSAGE_FORMAT = language.getString("SOCIAL_SPY_COMMAND.MESSAGE_FORMAT");

        SOTW_STARTED = language.getString("SOTW.STARTED");
        SOTW_ENDED = language.getString("SOTW.ENDED");
        SOTW_NOT_RUNNING = language.getString("SOTW.NOT_RUNNING");
        SOTW_ALREADY_RUNNING = language.getString("SOTW.ALREADY_RUNNING");
        SOTW_ENABLED = language.getString("SOTW.ENABLED");
        SOTW_ALREADY_ENABLED = language.getString("SOTW.ALREADY_ENABLED");
        SOTW_VOID_FIX = language.getString("SOTW.VOID_FIX");
        SOTW_TIME_STATUS = language.getString("SOTW.TIME_STATUS");
        SOTW_HIDE_PLAYERS_ENABLED = language.getString("SOTW.HIDE_PLAYERS_ENABLED");
        SOTW_HIDE_PLAYERS_DISABLED = language.getString("SOTW.HIDE_PLAYERS_DISABLED");
        SOTW_ADMIN_COMMAND_USAGE = language.getStringList("SOTW.ADMIN_COMMAND_USAGE");
        SOTW_PLAYER_COMMAND_USAGE = language.getStringList("SOTW.PLAYER_COMMAND_USAGE");

        SPAWN_CREDITS_PLAYER_USAGE = language.getStringList("SPAWN_CREDITS_COMMAND.PLAYER_USAGE");
        SPAWN_CREDITS_ADMIN_USAGE = language.getStringList("SPAWN_CREDITS_COMMAND.ADMIN_USAGE");
        SPAWN_CREDITS_AMOUNT_CHANGED = language.getString("SPAWN_CREDITS_COMMAND.AMOUNT_CHANGED");
        SPAWN_CREDITS_AMOUNT_CHANGED_STAFF = language.getString("SPAWN_CREDITS_COMMAND.AMOUNT_CHANGED_STAFF");
        SPAWN_CREDITS_AMOUNT_CHECK_SELF = language.getString("SPAWN_CREDITS_COMMAND.AMOUNT_CHECK_SELF");
        SPAWN_CREDITS_AMOUNT_CHECK_OTHERS = language.getString("SPAWN_CREDITS_COMMAND.AMOUNT_CHECK_OTHERS");

        SPAWN_CREDITS_NOT_ENOUGH_CREDITS = language.getString("SPAWN_CREDITS.NOT_ENOUGH_CREDITS");
        SPAWN_CREDITS_TELEPORTED = language.getString("SPAWN_CREDITS.TELEPORTED");

        SPAWN_DOESNT_EXIST = language.getString("SPAWN_COMMAND.SPAWN_DOESNT_EXIST");
        SPAWN_ALREADY_TELEPORTING = language.getString("SPAWN_COMMAND.ALREADY_TELEPORTING");
        SPAWN_TELEPORTED = language.getString("SPAWN_COMMAND.TELEPORTED");
        SPAWN_TELEPORT_STARTED = language.getString("SPAWN_COMMAND.TELEPORT_STARTED");
        SPAWN_TELEPORTED_KITMAP = language.getString("SPAWN_COMMAND.TELEPORTED_KITMAP");
        SPAWN_TELEPORT_CANCELLED_DAMAGE = language.getString("SPAWN_COMMAND.TELEPORT_CANCELLED.DAMAGE");
        SPAWN_TELEPORT_CANCELLED_MOVED = language.getString("SPAWN_COMMAND.TELEPORT_CANCELLED.MOVED");

        SPAWNER_USAGE = language.getString("SPAWNER_COMMAND.USAGE");
        SPAWNER_MUST_LOOK_AT_SPAWNER = language.getString("SPAWNER_COMMAND.MUST_LOOK_AT_SPAWNER");
        SPAWNER_UPDATED_SPAWNER = language.getString("SPAWNER_COMMAND.UPDATED_SPAWNER");

        SPEED_USAGE = language.getString("SPEED_COMMAND.USAGE");
        SPEED_LIMIT_REACHED = language.getString("SPEED_COMMAND.LIMIT_REACHED");
        SPEED_SPEED_CHANGED = language.getString("SPEED_COMMAND.SPEED_CHANGED");

        STAFF_CHAT_ENABLED = language.getString("STAFF_CHAT.ENABLED");
        STAFF_CHAT_DISABLED = language.getString("STAFF_CHAT.DISABLED");
        STAFF_CHAT_FORMAT = language.getString("STAFF_CHAT.FORMAT");

        STAFF_MODE_ENABLED = language.getString("STAFF_MODE.ENABLED");
        STAFF_MODE_DISABLED = language.getString("STAFF_MODE.DISABLED");
        STAFF_MODE_ENABLED_OTHERS = language.getString("STAFF_MODE.ENABLED_OTHERS");
        STAFF_MODE_DISABLED_OTHERS = language.getString("STAFF_MODE.DISABLED_OTHERS");
        STAFF_MODE_RANDOM_TELEPORT_MESSAGE = language.getString("STAFF_MODE.RANDOM_TELEPORT_MESSAGE");
        STAFF_MODE_RANDOM_TELEPORT_NO_PLAYER_MESSAGE = language.getString("STAFF_MODE.RANDOM_TELEPORT_NO_PLAYER_MESSAGE");
        STAFF_MODE_INVENTORY_INSPECT_MESSAGE = language.getString("STAFF_MODE.INVENTORY_INSPECT_MESSAGE");
        STAFF_MODE_DAMAGE_DENY = language.getString("STAFF_MODE.DAMAGE_DENY");
        STAFF_MODE_INTERACT_DENY = language.getString("STAFF_MODE.INTERACT_DENY");
        STAFF_MODE_PLACE_DENY = language.getString("STAFF_MODE.PLACE_DENY");
        STAFF_MODE_BREAK_DENY = language.getString("STAFF_MODE.BREAK_DENY");

        STAFF_SCOREBOARD_ENABLED = language.getString("STAFF_SCOREBOARD_COMMAND.ENABLED");
        STAFF_SCOREBOARD_DISABLED = language.getString("STAFF_SCOREBOARD_COMMAND.DISABLED");

        SUBCLAIMS_SUCCESSFULLY_CREATED = language.getString("SUBCLAIMS.SUCCESSFULLY_CREATED");
        SUBCLAIMS_CAN_NOT_DESTROY_SIGN = language.getString("SUBCLAIMS.CAN_NOT_DESTROY_SIGN");
        SUBCLAIMS_CAN_NOT_DESTROY_SUBCLAIM = language.getString("SUBCLAIMS.CAN_NOT_DESTROY_SUBCLAIM");
        SUBCLAIMS_CAN_NOT_OPEN = language.getString("SUBCLAIMS.CAN_NOT_OPEN");
        SUBCLAIMS_NOT_IN_OWN_CLAIM = language.getString("SUBCLAIMS.NOT_IN_OWN_CLAIM");
        SUBCLAIMS_ALREADY_EXISTS = language.getString("SUBCLAIMS.ALREADY_EXISTS");
        SUBCLAIMS_MUST_BE_LEADER = language.getString("SUBCLAIMS.MUST_BE_LEADER");
        SUBCLAIMS_MUST_BE_CO_LEADER = language.getString("SUBCLAIMS.MUST_BE_CO_LEADER");
        SUBCLAIMS_MUST_BE_CAPTAIN = language.getString("SUBCLAIMS.MUST_BE_CAPTAIN");

        TELEPORT_USAGE = language.getString("TELEPORT_COMMAND.USAGE");
        TELEPORT_MESSAGE = language.getString("TELEPORT_COMMAND.MESSAGE");

        TELEPORT_ALL_MESSAGE = language.getString("TELEPORTALL_COMMAND.MESSAGE");

        TELEPORT_HERE_USAGE = language.getString("TELEPORTHERE_COMMAND.USAGE");
        TELEPORT_HERE_MESSAGE = language.getString("TELEPORTHERE_COMMAND.MESSAGE");

        TELEPORT_POSITION_USAGE = language.getString("TELEPORTPOSITION_COMMAND.USAGE");
        TELEPORT_POSITION_MESSAGE = language.getString("TELEPORTPOSITION_COMMAND.MESSAGE");

        TELL_LOCATION_MESSAGE = language.getString("TELL_LOCATION_COMMAND.MESSAGE");
        TELL_LOCATION_NOT_IN_FACTION = language.getString("TELL_LOCATION_COMMAND.NOT_IN_FACTION");

        TIME_MESSAGE_DAY = language.getString("TIME_COMMANDS.MESSAGE_DAY");
        TIME_MESSAGE_NIGHT = language.getString("TIME_COMMANDS.MESSAGE_NIGHT");

        TIMER_USAGE = language.getString("TIMER_COMMAND.USAGE");
        TIMER_NOT_CHANGEABLE = language.getString("TIMER_COMMAND.TIMER_NOT_CHANGEABLE");
        TIMER_CHANGED = language.getString("TIMER_COMMAND.CHANGED");
        TIMER_CHANGED_SENDER = language.getString("TIMER_COMMAND.CHANGED_SENDER");

        TOGGLE_COBBLE_TOGGLED_ON = language.getString("TOGGLE_COBBLE_COMMAND.TOGGLED_ON");
        TOGGLE_COBBLE_TOGGLED_OFF = language.getString("TOGGLE_COBBLE_COMMAND.TOGGLED_OFF");

        TOGGLE_DEATHMESSAGES_TOGGLED_ON = language.getString("TOGGLE_DEATHMESSAGES_COMMAND.TOGGLED_ON");
        TOGGLE_DEATHMESSAGES_TOGGLED_OFF = language.getString("TOGGLE_DEATHMESSAGES_COMMAND.TOGGLED_OFF");

        TOGGLE_FOUND_ORE_TOGGLED_ON = language.getString("TOGGLE_FOUND_ORE_COMMAND.TOGGLED_ON");
        TOGGLE_FOUND_ORE_TOGGLED_OFF = language.getString("TOGGLE_FOUND_ORE_COMMAND.TOGGLED_OFF");

        TOGGLE_LIGHTNING_TOGGLED_ON = language.getString("TOGGLE_LIGHTNING_COMMAND.TOGGLED_ON");
        TOGGLE_LIGHTNING_TOGGLED_OFF = language.getString("TOGGLE_LIGHTNING_COMMAND.TOGGLED_OFF");

        TOGGLE_MESSAGES_TOGGLED_ON = language.getString("TOGGLE_MESSAGES_COMMAND.TOGGLED_ON");
        TOGGLE_MESSAGES_TOGGLED_OFF = language.getString("TOGGLE_MESSAGES_COMMAND.TOGGLED_OFF");

        TOGGLE_PUBLIC_CHAT_TOGGLED_ON = language.getString("TOGGLE_PUBLIC_CHAT_COMMAND.TOGGLED_ON");
        TOGGLE_PUBLIC_CHAT_TOGGLED_OFF = language.getString("TOGGLE_PUBLIC_CHAT_COMMAND.TOGGLED_OFF");

        TOGGLE_SCOREBOARD_TOGGLED_ON = language.getString("TOGGLE_SCOREBOARD_COMMAND.TOGGLED_ON");
        TOGGLE_SCOREBOARD_TOGGLED_OFF = language.getString("TOGGLE_SCOREBOARD_COMMAND.TOGGLED_OFF");

        TOGGLE_SOUNDS_TOGGLED_ON = language.getString("TOGGLE_SOUNDS_COMMAND.TOGGLED_ON");
        TOGGLE_SOUNDS_TOGGLED_OFF = language.getString("TOGGLE_SOUNDS_COMMAND.TOGGLED_OFF");

        TOP_COMMAND_MESSAGE = language.getString("TOP_COMMAND_MESSAGE");

        UNFOCUS_NOT_FOCUSING = language.getString("UNFOCUS_COMMAND.NOT_FOCUSING");
        UNFOCUS_UNFOCUSED = language.getString("UNFOCUS_COMMAND.UNFOCUSED");

        UNREPAIRABLE_ITEM_MESSAGE = language.getString("UNREPAIRABLE_ITEM_MESSAGE");

        COORDS_MESSAGE = language.getStringList("COORDS_MESSAGE");
        HELP_MESSAGE = language.getStringList("HELP_MESSAGE");
        SUBCLAIM_MESSAGE = language.getStringList("SUBCLAIM_MESSAGE");

        VANISH_ENABLED = language.getString("VANISH.ENABLED");
        VANISH_DISABLED = language.getString("VANISH.DISABLED");
        VANISH_ENABLED_OTHERS = language.getString("VANISH.ENABLED_OTHERS");
        VANISH_DISABLED_OTHERS = language.getString("VANISH.DISABLED_OTHERS");
        VANISH_ENABLED_STAFF_BROADCAST = language.getString("VANISH.ENABLED_STAFF_BROADCAST");
        VANISH_DISABLED_STAFF_BROADCAST = language.getString("VANISH.DISABLED_STAFF_BROADCAST");
        VANISH_BUILD_ENABLED = language.getString("VANISH.BUILD_ENABLED");
        VANISH_BUILD_DISABLED = language.getString("VANISH.BUILD_DISABLED");
        VANISH_CHEST_MESSAGE = language.getString("VANISH.CHEST_MESSAGE");
        VANISH_DAMAGE_DENY = language.getString("VANISH.DAMAGE_DENY");
        VANISH_INTERACT_DENY = language.getString("VANISH.INTERACT_DENY");
        VANISH_PLACE_DENY = language.getString("VANISH.PLACE_DENY");
        VANISH_BREAK_DENY = language.getString("VANISH.BREAK_DENY");

        VIEW_DISTANCE_ALREADY_RUNNING = language.getString("VIEW_DISTANCE_COMMAND.ALREADY_RUNNING");
        VIEW_DISTANCE_FINISHED = language.getString("VIEW_DISTANCE_COMMAND.FINISHED");
        VIEW_DISTANCE_STARTED = language.getStringList("VIEW_DISTANCE_COMMAND.STARTED");
        VIEW_DISTANCE_USAGE = language.getStringList("VIEW_DISTANCE_COMMAND.USAGE");

        WARP_LIST = language.getString("WARP_COMMAND.LIST");
        WARP_TELEPORTED = language.getString("WARP_COMMAND.TELEPORTED");
        WARP_DOESNT_EXIST = language.getString("WARP_COMMAND.DOESNT_EXIST");

        WORLD_COMMAND_USAGE = language.getString("WORLD_COMMAND.USAGE");
        WORLD_DOESNT_EXIST = language.getString("WORLD_COMMAND.WORLD_DOESNT_EXIST");
    }
}
