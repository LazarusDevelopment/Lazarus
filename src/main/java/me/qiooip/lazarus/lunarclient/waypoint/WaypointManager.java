package me.qiooip.lazarus.lunarclient.waypoint;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.event.LCPlayerRegisterEvent;
import com.lunarclient.bukkitapi.event.LCPlayerUnregisterEvent;
import com.lunarclient.bukkitapi.object.LCWaypoint;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.event.FactionDisbandEvent;
import me.qiooip.lazarus.factions.event.FactionFocusedEvent;
import me.qiooip.lazarus.factions.event.FactionRallyEvent;
import me.qiooip.lazarus.factions.event.FactionSetHomeEvent;
import me.qiooip.lazarus.factions.event.FactionUnfocusedEvent;
import me.qiooip.lazarus.factions.event.PlayerJoinFactionEvent;
import me.qiooip.lazarus.factions.event.PlayerLeaveFactionEvent;
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
import me.qiooip.lazarus.handlers.event.ExitSetEvent;
import me.qiooip.lazarus.handlers.event.SpawnSetEvent;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.Tasks;
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

    private final Map<PlayerWaypointType, LunarClientWaypoint> waypoints;

    private final Map<PlayerWaypointType, LCWaypoint> globalWaypoints;
    private final Map<String, LCWaypoint> kothWaypoints;

    private final Table<UUID, PlayerWaypointType, LCWaypoint> playerWaypoints;
    private final PlayerWaypointType[] waypointTypes;

    public WaypointManager() {
        this.waypoints = new HashMap<>();

        this.globalWaypoints = new HashMap<>();
        this.kothWaypoints = new HashMap<>();

        this.playerWaypoints = HashBasedTable.create();

        this.waypointTypes = PlayerWaypointType.values();
        this.setupWaypoints();

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.waypoints.clear();

        this.globalWaypoints.clear();
        this.kothWaypoints.clear();

        this.playerWaypoints.clear();
    }

    private void setupWaypoints() {
        ConfigurationSection section = Lazarus.getInstance().getConfig().getSection("FORCED_WAYPOINTS");

        section.getKeys(false).forEach(waypointName -> {
            LunarClientWaypoint lunarClientWaypoint = new LunarClientWaypoint();
            lunarClientWaypoint.setName(Color.translate(section.getString(waypointName + ".NAME")));
            lunarClientWaypoint.setRgbColor(section.getString(waypointName + ".COLOR"));
            lunarClientWaypoint.setForced(section.getBoolean(waypointName + ".FORCED"));

            this.waypoints.put(PlayerWaypointType.valueOf(waypointName), lunarClientWaypoint);
        });

        for(PlayerWaypointType type : this.waypointTypes) {
            this.updateGlobalWaypoints(type, false);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionDisband(FactionDisbandEvent event) {
        if(!(event.getFaction() instanceof PlayerFaction)) return;
        PlayerFaction faction = (PlayerFaction) event.getFaction();

        for(Player player : faction.getOnlinePlayers()) {
            this.updatePlayerFactionChange(player);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoinFaction(PlayerJoinFactionEvent event) {
        this.updatePlayerFactionChange(event.getFactionPlayer().getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLeaveFaction(PlayerLeaveFactionEvent event) {
        this.updatePlayerFactionChange(event.getFactionPlayer().getPlayer());
    }

    @EventHandler
    public void onFactionPlayerFocused(FactionFocusedEvent event) {
        for(Player player : event.getFaction().getOnlinePlayers()) {
            this.updateWaypoint(player, PlayerWaypointType.FOCUSED_FACTION_HOME);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionPlayerUnfocused(FactionUnfocusedEvent event) {
        Tasks.sync(() -> {
            for(Player player : event.getFaction().getOnlinePlayers()) {
                this.updateWaypoint(player, PlayerWaypointType.FOCUSED_FACTION_HOME);
            }
        });
    }

    @EventHandler
    public void onFactionSetHome(FactionSetHomeEvent event) {
        for(Player player : event.getFaction().getOnlinePlayers()) {
            this.updateWaypoint(player, PlayerWaypointType.FACTION_HOME);
        }
    }

    @EventHandler
    public void onFactionRally(FactionRallyEvent event) {
        for(Player player : event.getFaction().getOnlinePlayers()) {
            this.updateWaypoint(player, PlayerWaypointType.FACTION_RALLY);
        }
    }

    @EventHandler
    public void onSpawnSet(SpawnSetEvent event) {
        PlayerWaypointType type = this.getTypeByEnvironment(event.getEnvironment());
        this.updateGlobalWaypoints(type, true);
    }

    @EventHandler
    public void onExitSet(ExitSetEvent event) {
        this.updateGlobalWaypoints(PlayerWaypointType.END_EXIT, true);
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
        this.updateConquestWaypoints();
    }

    @EventHandler
    public void onConquestStop(ConquestStopEvent event) {
        this.updateConquestWaypoints();
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
        this.registerPlayerWaypoints(event.getPlayer());
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

    private void updatePlayerFactionChange(Player player) {
        this.updateWaypoint(player, PlayerWaypointType.FACTION_RALLY);
        this.updateWaypoint(player, PlayerWaypointType.FACTION_HOME);
        this.updateWaypoint(player, PlayerWaypointType.FOCUSED_FACTION_HOME);
    }

    private void updateConquestWaypoints() {
        this.updateGlobalWaypoints(PlayerWaypointType.CONQUEST_RED, true);
        this.updateGlobalWaypoints(PlayerWaypointType.CONQUEST_BLUE, true);
        this.updateGlobalWaypoints(PlayerWaypointType.CONQUEST_GREEN, true);
        this.updateGlobalWaypoints(PlayerWaypointType.CONQUEST_YELLOW, true);
    }

    private void registerPlayerWaypoints(Player player) {
        for(PlayerWaypointType type : this.waypointTypes) {
            this.updateWaypoint(player, type);
        }

        for(LCWaypoint waypoint : this.kothWaypoints.values()) {
            this.addWaypoint(player, waypoint);
        }
    }

    private Environment getEnvironmentByType(PlayerWaypointType type) {
        switch(type) {
            case NETHER_SPAWN: return Environment.NETHER;
            case END_SPAWN: return Environment.THE_END;
            default: return Environment.NORMAL;
        }
    }

    private PlayerWaypointType getTypeByEnvironment(Environment environment) {
        switch(environment) {
            case NETHER: return PlayerWaypointType.NETHER_SPAWN;
            case THE_END: return PlayerWaypointType.END_SPAWN;
            default: return PlayerWaypointType.SPAWN;
        }
    }

    private void addGlobalWaypoint(PlayerWaypointType type, Location location) {
        this.globalWaypoints.put(type, this.waypoints.get(type).createWaypoint(location));
    }

    private void updateKoTHWaypoint(KothData data, boolean add) {
        if(!this.waypoints.containsKey(PlayerWaypointType.KOTH)) return;

        String name = data.getName();

        if(add) {
            LCWaypoint waypoint = this.waypoints.get(PlayerWaypointType.KOTH)
                .createWaypoint(data.getCuboid().getCenterWithMinY(), name);

            for(UUID uuid : Lazarus.getInstance().getLunarClientManager().getPlayers()) {
                this.addWaypoint(Bukkit.getPlayer(uuid), waypoint);
            }

            this.kothWaypoints.put(name, waypoint);
            return;
        }

        LCWaypoint waypoint = this.kothWaypoints.remove(name);
        if(waypoint == null) return;

        for(UUID uuid : Lazarus.getInstance().getLunarClientManager().getPlayers()) {
            this.removeWaypoint(Bukkit.getPlayer(uuid), waypoint);
        }
    }

    private void updateGlobalWaypoints(PlayerWaypointType type, boolean update) {
        if(!this.waypoints.containsKey(type)) return;

        if(update) {
            if(this.globalWaypoints.containsKey(type)) {
                for(UUID uuid : Lazarus.getInstance().getLunarClientManager().getPlayers()) {
                    this.removeWaypoint(Bukkit.getPlayer(uuid), this.globalWaypoints.get(type));
                }
            }

            this.globalWaypoints.remove(type);
        }

        switch(type) {
            case SPAWN:
            case NETHER_SPAWN:
            case END_SPAWN: {
                Location spawn = Config.WORLD_SPAWNS.get(this.getEnvironmentByType(type));

                if(spawn != null) {
                    this.addGlobalWaypoint(type, spawn);
                }

                break;
            }
            case CONQUEST_RED:
            case CONQUEST_BLUE:
            case CONQUEST_GREEN:
            case CONQUEST_YELLOW: {
                ConquestManager conquestManager = Lazarus.getInstance().getConquestManager();

                if(conquestManager.isActive()) {
                    RunningConquest runningConquest = conquestManager.getRunningConquest();

                    this.addGlobalWaypoint(type, runningConquest.getCapzones().
                    get(type.getConquestZone()).getCuboid().getCenterWithMinY());
                }

                break;
            }
            case DTC: {
                DtcManager dtcManager = Lazarus.getInstance().getDtcManager();

                if(dtcManager.isActive()) {
                    this.addGlobalWaypoint(type, dtcManager.getDtcData().getLocation());
                }

                break;
            }
            case END_EXIT: {
                Location endExit = Config.WORLD_EXITS.get(Environment.THE_END);

                if(endExit != null) {
                    this.addGlobalWaypoint(type, endExit);
                }
            }
        }

        if(update) {
            for(UUID uuid : Lazarus.getInstance().getLunarClientManager().getPlayers()) {
                for(PlayerWaypointType pwt : this.globalWaypoints.keySet()) {
                    this.updateWaypoint(Bukkit.getPlayer(uuid), pwt);
                }
            }
        }
    }

    private void updateWaypoint(Player player, PlayerWaypointType type) {
        if(player == null) return;

        LCWaypoint lcWaypoint = this.playerWaypoints.remove(player.getUniqueId(), type);

        if(lcWaypoint != null) {
            this.removeWaypoint(player, lcWaypoint);
        }

        if(!this.waypoints.containsKey(type)) return;

        LCWaypoint waypoint = null;
        LunarClientWaypoint typeWaypoint = this.waypoints.get(type);
        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);

        switch(type) {
            case SPAWN:
            case NETHER_SPAWN:
            case END_SPAWN:
            case DTC:
            case END_EXIT:
            case CONQUEST_RED:
            case CONQUEST_BLUE:
            case CONQUEST_GREEN:
            case CONQUEST_YELLOW: {
                waypoint = this.globalWaypoints.get(type);
                break;
            }
            case FACTION_RALLY: {
                if(faction != null && faction.getRallyLocation() != null) {
                    waypoint = typeWaypoint.createWaypoint(faction.getRallyLocation());
                }

                break;
            }
            case FACTION_HOME: {
                if(faction != null && faction.getHome() != null) {
                    waypoint = typeWaypoint.createWaypoint(faction.getHome());
                }

                break;
            }
            case FOCUSED_FACTION_HOME: {
                if(faction != null && faction.getFocusedFaction() != null) {
                    PlayerFaction focusedFaction = faction.getFocusedAsFaction();

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

    private void addWaypoint(Player player, LCWaypoint waypoint) {
        if(player == null) return;
        LunarClientAPI.getInstance().sendWaypoint(player, waypoint);
    }

    private void removeWaypoint(Player player, LCWaypoint waypoint) {
        if(player == null) return;
        LunarClientAPI.getInstance().removeWaypoint(player, waypoint);
    }
}
