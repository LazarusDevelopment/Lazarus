package me.qiooip.lazarus.lunarclient;

import com.moonsworth.client.api.LunarClientAPI;
import com.moonsworth.client.api.event.PlayerRegisterLCEvent;
import com.moonsworth.client.api.event.PlayerUnregisterLCEvent;
import com.moonsworth.client.api.object.LCWaypoint;
import com.moonsworth.client.api.object.MinimapStatus;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.utils.ManagerEnabler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class LunarClientManager implements Listener, ManagerEnabler {

    private Set<LCWaypoint> defaultWaypoints;
    private Map<UUID, Set<LCWaypoint>> playerWaypoints;

    public LunarClientManager() {
        if(!Config.LUNAR_CLIENT_API_ENABLED) return;

        Plugin api = Bukkit.getPluginManager().getPlugin("LunarClientAPI");
        if(api == null || !api.isEnabled() || !api.getDescription().getMain().equals("com.moonsworth.client.api.LunarClientAPI")) return;

        if(Config.LUNAR_CLIENT_API_FORCED_WAYPOINTS_ENABLED) {
            this.registerDefaultWaypoints();
        }

        this.defaultWaypoints = new HashSet<>();
        this.playerWaypoints = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.defaultWaypoints.clear();
        this.playerWaypoints.clear();
    }

    @EventHandler
    public void onPlayerRegisterLCEvent(PlayerRegisterLCEvent event) {
        Player player = event.getPlayer();

        if(Config.LUNAR_CLIENT_API_FORCED_WAYPOINTS_ENABLED) {
            LunarClientAPI.getInstance().setMinimapStatus(player, MinimapStatus.NEUTRAL);

            for(LCWaypoint waypoint : this.defaultWaypoints) {
                this.addWaypoint(player, waypoint);
            }

            this.registerPlayerWaypoints(player);
        }
    }

    @EventHandler
    public void onPlayerUnregisterLC(PlayerUnregisterLCEvent event) {
        Player player = event.getPlayer();

        if(Config.LUNAR_CLIENT_API_FORCED_WAYPOINTS_ENABLED) {
            for(LCWaypoint waypoint : this.defaultWaypoints) {
                this.removeWaypoint(player, waypoint);
            }

            if(this.playerWaypoints.containsKey(player.getUniqueId())) {
                for(LCWaypoint waypoint : this.playerWaypoints.get(player.getUniqueId())) {
                    this.removeWaypoint(player, waypoint);
                }

                this.playerWaypoints.remove(player.getUniqueId());
            }
        }
    }

    private void registerPlayerWaypoints(Player player) {
        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);
        Set<LCWaypoint> waypoints = new HashSet<>();

        if(faction != null) {
            if(faction.getHome() != null) {
                waypoints.add(this.createWaypoint("Home", faction.getHome(), Config.LUNAR_CLIENT_API_HOME_COLOR));
            }

            if(faction.getFocused() != null) {
                PlayerFaction focusedFaction = FactionsManager.getInstance().getPlayerFaction(faction.getFocused());
                if(focusedFaction != null && focusedFaction.getHome() != null) {
                    waypoints.add(this.createWaypoint("Focused Team Home", focusedFaction.getHome(), Config.LUNAR_CLIENT_API_FOCUSED_FACTION_HOME_COLOR));
                }
            }

            for(LCWaypoint waypoint : waypoints) {
                this.addWaypoint(player, waypoint);
            }
        }

        this.playerWaypoints.put(player.getUniqueId(), waypoints);
    }

    private void registerDefaultWaypoints() {
        Config.WORLD_SPAWNS.values().forEach(location ->
                this.defaultWaypoints.add(this.createWaypoint("Spawn", location, Config.LUNAR_CLIENT_API_SPAWN_COLOR)));
    }

    private LCWaypoint createWaypoint(String name, Location location, String color) {
        return new LCWaypoint(name, location, Color.decode(color).getRGB(), true);
    }

    private void addWaypoint(Player player, LCWaypoint waypoint) {
        LunarClientAPI.getInstance().sendWaypoint(player, waypoint);
    }

    private void removeWaypoint(Player player, LCWaypoint waypoint) {
        LunarClientAPI.getInstance().removeWaypoint(player, waypoint);
    }
}
