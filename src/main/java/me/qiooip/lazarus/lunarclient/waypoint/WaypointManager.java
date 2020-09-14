package me.qiooip.lazarus.lunarclient.waypoint;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.event.LCPlayerRegisterEvent;
import com.lunarclient.bukkitapi.event.LCPlayerUnregisterEvent;
import com.lunarclient.bukkitapi.nethandler.client.LCPacketServerRule;
import com.lunarclient.bukkitapi.nethandler.client.obj.ServerRule;
import com.lunarclient.bukkitapi.object.LCWaypoint;
import com.lunarclient.bukkitapi.object.MinimapStatus;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.event.FactionPlayerFocusedEvent;
import me.qiooip.lazarus.factions.event.FactionSetHomeEvent;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.games.conquest.ConquestManager;
import me.qiooip.lazarus.games.conquest.RunningConquest;
import me.qiooip.lazarus.games.conquest.event.ConquestStartEvent;
import me.qiooip.lazarus.games.conquest.event.ConquestStopEvent;
import me.qiooip.lazarus.games.dtc.DtcManager;
import me.qiooip.lazarus.games.dtc.event.DtcStartEvent;
import me.qiooip.lazarus.games.dtc.event.DtcStopEvent;
import me.qiooip.lazarus.games.koth.KothData;
import me.qiooip.lazarus.games.koth.event.KothStartEvent;
import me.qiooip.lazarus.games.koth.event.KothStopEvent;
import me.qiooip.lazarus.handlers.event.SpawnSetEvent;
import me.qiooip.lazarus.lunarclient.LunarClientManager;
import me.qiooip.lazarus.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WaypointManager implements Listener {

    private final Map<PlayerWaypointType, LCWaypoint> globalWaypoints;
    private final Table<UUID, PlayerWaypointType, LCWaypoint> playerWaypoints;
    private final Map<String, LCWaypoint> kothWaypoints;

    private final Map<PlayerWaypointType, LunarClientWaypoint> waypoints;
    private final PlayerWaypointType[] waypointTypes;

    public WaypointManager() {
        this.globalWaypoints = new HashMap<>();
        this.playerWaypoints = HashBasedTable.create();
        this.kothWaypoints = new HashMap<>();

        this.waypoints = new HashMap<>();
        this.waypointTypes = PlayerWaypointType.values();

        this.setupWaypoints();

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.globalWaypoints.clear();
        this.playerWaypoints.clear();
        this.kothWaypoints.clear();

        this.waypoints.clear();
    }

    private void setupWaypoints() {
        ConfigurationSection section = Lazarus.getInstance().getConfig().getConfigurationSection("FORCED_WAYPOINTS");

        section.getKeys(false).forEach(waypointName -> {
            LunarClientWaypoint lunarClientWaypoint = new LunarClientWaypoint();
            lunarClientWaypoint.setName(Color.translate(section.getString(waypointName + ".NAME")));
            lunarClientWaypoint.setColor(section.getString(waypointName + ".COLOR"));

            PlayerWaypointType type = PlayerWaypointType.valueOf(waypointName);
            this.waypoints.put(type, lunarClientWaypoint);
        });

        for(PlayerWaypointType type : waypointTypes) {
            this.updateGlobalWaypoints(type, false);
        }
    }

    @EventHandler
    public void onFactionSetHome(FactionSetHomeEvent event) {
        for(Player player : event.getFaction().getOnlinePlayers()) {
            this.updateWaypoint(player, PlayerWaypointType.FACTION_HOME);
        }
    }

    @EventHandler
    public void onFactionPlayerFocused(FactionPlayerFocusedEvent event) {
        for(Player player : event.getFaction().getOnlinePlayers()) {
            this.updateWaypoint(player, PlayerWaypointType.FOCUSED_FACTION_HOME);
        }
    }

    @EventHandler
    public void onSpawnSet(SpawnSetEvent event) {
        this.updateGlobalWaypoints(PlayerWaypointType.SPAWN, true);
    }

    @EventHandler
    public void onKoTHStart(KothStartEvent event) {
        this.updateKoTHWaypoint(event.getKoth().getKothData(), true);
    }

    @EventHandler
    public void onKoTHStop(KothStopEvent event) {
        this.updateKoTHWaypoint(event.getKoth(), false);
    }

    @EventHandler
    public void onConquestStart(ConquestStartEvent event) {
        this.updateGlobalWaypoints(PlayerWaypointType.CONQUEST_RED,true);
        this.updateGlobalWaypoints(PlayerWaypointType.CONQUEST_BLUE,true);
        this.updateGlobalWaypoints(PlayerWaypointType.CONQUEST_GREEN,true);
        this.updateGlobalWaypoints(PlayerWaypointType.CONQUEST_YELLOW,true);
    }

    @EventHandler
    public void onConquestStop(ConquestStopEvent event) {
        this.updateGlobalWaypoints(PlayerWaypointType.CONQUEST_RED, true);
        this.updateGlobalWaypoints(PlayerWaypointType.CONQUEST_BLUE,true);
        this.updateGlobalWaypoints(PlayerWaypointType.CONQUEST_GREEN,true);
        this.updateGlobalWaypoints(PlayerWaypointType.CONQUEST_YELLOW,true);
    }

    @EventHandler
    public void onDtcStart(DtcStartEvent event) {
        this.updateGlobalWaypoints(PlayerWaypointType.DTC,true);
    }

    @EventHandler
    public void onDtcStop(DtcStopEvent event) {
        this.updateGlobalWaypoints(PlayerWaypointType.DTC, true);
    }

    @EventHandler
    public void onPlayerRegisterLCEvent(LCPlayerRegisterEvent event) {
        Player player = event.getPlayer();

        LunarClientAPI.getInstance().sendPacket(player, new LCPacketServerRule(ServerRule.SERVER_HANDLES_WAYPOINTS, true));
        LunarClientAPI.getInstance().setMinimapStatus(player, MinimapStatus.NEUTRAL);

        this.registerPlayerWaypoints(player);
    }

    @EventHandler
    public void onPlayerUnregisterLC(LCPlayerUnregisterEvent event) {
        Player player = event.getPlayer();

        if(this.playerWaypoints.containsRow(player.getUniqueId())) {
            for(LCWaypoint waypoint : this.playerWaypoints.values()) {
                this.removeWaypoint(player, waypoint);
            }

            this.playerWaypoints.row(player.getUniqueId()).clear();
        }

        for(LCWaypoint waypoint : this.kothWaypoints.values()) {
            this.removeWaypoint(player, waypoint);
        }
    }

    private void registerPlayerWaypoints(Player player) {
        for(PlayerWaypointType type : this.waypointTypes) {
            this.updateWaypoint(player, type);
        }

        for(LCWaypoint waypoint : this.kothWaypoints.values()) {
            this.addWaypoint(player, waypoint);
        }
    }

    private void updateKoTHWaypoint(KothData data, boolean add) {
        String name = data.getName();

        if(add) {
            LCWaypoint waypoint = this.waypoints.get(PlayerWaypointType.KOTH)
                .createWaypoint(data.getCuboid().getCenterWithMinY(), name);

            for(UUID uuid : LunarClientManager.getInstance().getPlayers()) {
                this.addWaypoint(Bukkit.getPlayer(uuid), waypoint);
            }

            this.kothWaypoints.put(name, waypoint);
        } else if(this.kothWaypoints.containsKey(name)) {
            LCWaypoint waypoint = this.kothWaypoints.remove(name);

            for(UUID uuid : LunarClientManager.getInstance().getPlayers()) {
                this.removeWaypoint(Bukkit.getPlayer(uuid), waypoint);
            }
        }
    }

    private void updateWaypoint(Player player, PlayerWaypointType type) {
        LCWaypoint lcWaypoint = this.playerWaypoints.remove(player.getUniqueId(), type);

        if(lcWaypoint != null) {
            this.removeWaypoint(player, lcWaypoint);
        }

        LCWaypoint waypoint = null;
        LunarClientWaypoint typeWaypoint = this.waypoints.get(type);
        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);

        switch(type) {
            case SPAWN:
            case DTC:
            case END_EXIT:
            case CONQUEST_RED:
            case CONQUEST_BLUE:
            case CONQUEST_GREEN:
            case CONQUEST_YELLOW: {
                waypoint = this.globalWaypoints.get(type);
                break;
            }
            case FACTION_HOME: {
                if(faction != null && faction.getHome() != null) {
                    waypoint = typeWaypoint.createWaypoint(faction.getHome());
                }

                break;
            }
            case FOCUSED_FACTION_HOME: {
                if(faction != null && faction.getFocused() != null) {
                    PlayerFaction focusedFaction = FactionsManager.getInstance().getPlayerFaction(faction.getFocused());

                    if(focusedFaction != null && focusedFaction.getHome() != null) {
                        waypoint = typeWaypoint.createWaypoint(focusedFaction.getHome());
                    }
                }

                break;
            }
        }

        if(waypoint != null) {
            this.addWaypoint(player, waypoint);
            this.playerWaypoints.put(player.getUniqueId(), type, waypoint);
        }
    }

    private void updateGlobalWaypoints(PlayerWaypointType type, boolean update) {
        if(update) {
            if(this.globalWaypoints.containsKey(type)) {
                for(UUID uuid : LunarClientManager.getInstance().getPlayers()) {
                    this.removeWaypoint(Bukkit.getPlayer(uuid), this.globalWaypoints.get(type));
                }
            }

            this.globalWaypoints.remove(type);
        }

        switch(type) {
            case SPAWN: {
                Config.WORLD_SPAWNS.values().forEach(location -> {
                    if(location == null) return;
                    this.globalWaypoints.put(type, this.waypoints.get(type).createWaypoint(location));
                });
            }
            case CONQUEST_RED:
            case CONQUEST_BLUE:
            case CONQUEST_GREEN:
            case CONQUEST_YELLOW: {
                ConquestManager conquestManager = Lazarus.getInstance().getConquestManager();

                if(conquestManager.isActive()) {
                    RunningConquest runningConquest = conquestManager.getRunningConquest();

                    this.globalWaypoints.put(type, this.waypoints.get(type).createWaypoint(runningConquest
                        .getCapzones().get(type.getConquestZone()).getCuboid().getCenterWithMinY()));
                }

                break;
            }
            case DTC: {
                DtcManager dtcManager = Lazarus.getInstance().getDtcManager();

                if(dtcManager.isActive()) {
                    this.globalWaypoints.put(type, this.waypoints.get(type).createWaypoint(dtcManager.getDtcData().getLocation()));
                }

                break;
            }
            case END_EXIT: {
                Location endExit = Config.WORLD_EXITS.get(Environment.THE_END);

                if(endExit != null) {
                    this.globalWaypoints.put(type, this.waypoints.get(type).createWaypoint(endExit));
                }
            }
        }

        if(update) {
            for(UUID uuid : LunarClientManager.getInstance().getPlayers()) {
                for(PlayerWaypointType pwt : this.globalWaypoints.keySet()) {
                    this.updateWaypoint(Bukkit.getPlayer(uuid), pwt);
                }
            }
        }
    }

    private void addWaypoint(Player player, LCWaypoint waypoint) {
        LunarClientAPI.getInstance().sendWaypoint(player, waypoint);
    }

    private void removeWaypoint(Player player, LCWaypoint waypoint) {
        LunarClientAPI.getInstance().removeWaypoint(player, waypoint);
    }
}
