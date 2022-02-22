package me.qiooip.lazarus.utils;

import com.google.gson.reflect.TypeToken;
import me.qiooip.lazarus.deathban.Deathban;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionPlayer;
import me.qiooip.lazarus.factions.claim.Claim;
import me.qiooip.lazarus.factions.type.ConquestFaction;
import me.qiooip.lazarus.factions.type.DtcFaction;
import me.qiooip.lazarus.factions.type.KothFaction;
import me.qiooip.lazarus.factions.type.MountainFaction;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.factions.type.RoadFaction;
import me.qiooip.lazarus.factions.type.SpawnFaction;
import me.qiooip.lazarus.factions.type.SystemFaction;
import me.qiooip.lazarus.games.koth.KothData;
import me.qiooip.lazarus.games.loot.LootData;
import me.qiooip.lazarus.games.mountain.MountainData;
import me.qiooip.lazarus.games.schedule.ScheduleData;
import me.qiooip.lazarus.hologram.Hologram;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GsonUtils {

    public static final Type CLAIM_TYPE = new TypeToken<List<Claim>>(){}.getType();

    public static final Type DEATHBAN_TYPE = new TypeToken<Map<UUID, Deathban>>(){}.getType();

    public static final Type FACTION_TYPE = new TypeToken<Map<UUID, Faction>>(){}.getType();

    public static final Type KOTH_TYPE = new TypeToken<List<KothData>>(){}.getType();

    public static final Type LOOT_TYPE = new TypeToken<List<LootData>>(){}.getType();

    public static final Type SCHEDULE_TYPE = new TypeToken<List<ScheduleData>>(){}.getType();

    public static final Type MOUNTAIN_TYPE = new TypeToken<List<MountainData>>(){}.getType();

    public static final Type PLAYER_TYPE = new TypeToken<Map<UUID, FactionPlayer>>(){}.getType();

    public static final Type HOLOGRAMS_TYPE = new TypeToken<List<Hologram>>(){}.getType();

    public static Class<?> getFactionClass(String name) {
        switch(name) {
            case "ConquestFaction": return ConquestFaction.class;
            case "DtcFaction": return DtcFaction.class;
            case "KothFaction": return KothFaction.class;
            case "MountainFaction": return MountainFaction.class;
            case "PlayerFaction": return PlayerFaction.class;
            case "RoadFaction": return RoadFaction.class;
            case "SpawnFaction": return SpawnFaction.class;
            case "SystemFaction": return SystemFaction.class;
        }

        throw new RuntimeException("Invalid faction type");
    }

    public static String getFactionType(Class<?> clazz) {
        if(clazz == ConquestFaction.class) {
            return "ConquestFaction";
        } else if(clazz == DtcFaction.class) {
            return "DtcFaction";
        } else if(clazz == KothFaction.class) {
            return "KothFaction";
        } else if(clazz == MountainFaction.class) {
            return "MountainFaction";
        } else if(clazz == PlayerFaction.class) {
            return "PlayerFaction";
        } else if(clazz == RoadFaction.class) {
            return "RoadFaction";
        } else if(clazz == SpawnFaction.class) {
            return "SpawnFaction";
        } else if(clazz == SystemFaction.class) {
            return "SystemFaction";
        }

        throw new RuntimeException("Invalid faction class type");
    }
}
