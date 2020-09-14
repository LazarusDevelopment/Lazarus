package me.qiooip.lazarus.lunarclient.waypoint;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.moonsworth.client.api.LunarClientAPI;
import com.moonsworth.client.api.event.PlayerRegisterLCEvent;
import com.moonsworth.client.api.event.PlayerUnregisterLCEvent;
import com.moonsworth.client.api.object.LCWaypoint;
import com.moonsworth.client.api.object.MinimapStatus;
import com.moonsworth.client.nethandler.obj.ServerRule;
import com.moonsworth.client.nethandler.server.LCPacketServerRule;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.event.FactionPlayerFocusedEvent;
import me.qiooip.lazarus.factions.event.FactionSetHomeEvent;
import me.qiooip.lazarus.factions.type.ConquestFaction;
import me.qiooip.lazarus.factions.type.PlayerFaction;
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
import org.bukkit.Bukkit;
import org.bukkit.Location;
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

        section.getKeys(false).forEach(waypoint -> {
            LunarClientWaypoint lunarClientWaypoint = new LunarClientWaypoint();
            lunarClientWaypoint.setName(section.getString(waypoint + ".NAME"));
            lunarClientWaypoint.setColor(section.getString(waypoint + ".COLOR"));

            PlayerWaypointType type = PlayerWaypointType.getByName(section.getString(waypoint + ".TYPE"), true);
            this.waypoints.put(type, lunarClientWaypoint);
        });

        for(PlayerWaypointType type : waypointTypes) {
            this.updateGlobalWaypoints(type, false);
        }
    }

    @EventHandler
    public void onFactionSetHome(FactionSetHomeEvent event) {
        for(Player player : event.getFaction().getOnlinePlayers()) {
            this.updateWaypoint(player, PlayerWaypointType.HOME);
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
        this.updateGlobalWaypoints(PlayerWaypointType.CONQUEST,true);
    }

    @EventHandler
    public void onConquestStop(ConquestStopEvent event) {
        this.updateGlobalWaypoints(PlayerWaypointType.CONQUEST, true);
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
    public void onPlayerRegisterLCEvent(PlayerRegisterLCEvent event) {
        Player player = event.getPlayer();

        LunarClientAPI.getInstance().sendPacket(player, new LCPacketServerRule(ServerRule.SERVER_HANDLES_WAYPOINTS, true));
        LunarClientAPI.getInstance().setMinimapStatus(player, MinimapStatus.NEUTRAL);

        this.registerPlayerWaypoints(player);
    }

    @EventHandler
    public void onPlayerUnregisterLC(PlayerUnregisterLCEvent event) {
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
            LCWaypoint waypoint = this.waypoints.get(PlayerWaypointType.KOTH).createWaypoint(data.getCuboid().getCenter(), name);

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
        if(this.playerWaypoints.contains(player.getUniqueId(), type)) {
            this.removeWaypoint(player, this.playerWaypoints.get(player.getUniqueId(), type));
            this.playerWaypoints.remove(player.getUniqueId(), type);
        }

        LCWaypoint waypoint = null;
        LunarClientWaypoint typeWaypoint = this.waypoints.get(type);
        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);

        if(type == PlayerWaypointType.HOME && faction != null && faction.getHome() != null) {
            waypoint = typeWaypoint.createWaypoint(faction.getHome());
        } else if(type == PlayerWaypointType.FOCUSED_FACTION_HOME && faction != null && faction.getFocused() != null) {
            PlayerFaction focusedFaction = FactionsManager.getInstance().getPlayerFaction(faction.getFocused());
            if(focusedFaction != null && focusedFaction.getHome() != null) {
                waypoint = typeWaypoint.createWaypoint(focusedFaction.getHome());
            }
        } else if(type == PlayerWaypointType.SPAWN || type == PlayerWaypointType.CONQUEST || type == PlayerWaypointType.DTC) {
            waypoint = this.globalWaypoints.get(type);
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

        switch (type) {
            case SPAWN:
                Config.WORLD_SPAWNS.forEach((environment, location) -> {
                    if(location == null) return;
                    this.globalWaypoints.put(PlayerWaypointType.SPAWN, this.waypoints.get(PlayerWaypointType.SPAWN).createWaypoint(location));
                });
                break;
            case CONQUEST:
                ConquestFaction conquestFaction = (ConquestFaction) FactionsManager.getInstance().getFactionByName("Conquest");
                if(conquestFaction != null && Lazarus.getInstance().getConquestManager().getRunningConquest() != null) {
                    Location location = conquestFaction.getClaims().get(0).getCenter();
                    this.globalWaypoints.put(PlayerWaypointType.CONQUEST, this.waypoints.get(PlayerWaypointType.CONQUEST).createWaypoint(location));
                }
                break;
            case DTC:
                DtcManager dtc = Lazarus.getInstance().getDtcManager();
                if(dtc.isActive()) {
                    Location location = dtc.getDtcData().getLocation();
                    this.globalWaypoints.put(PlayerWaypointType.DTC, this.waypoints.get(PlayerWaypointType.DTC).createWaypoint(location));
                }
                break;
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
