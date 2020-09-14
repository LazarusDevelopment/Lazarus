package me.qiooip.lazarus.lunarclient;

import com.moonsworth.client.api.event.PlayerRegisterLCEvent;
import com.moonsworth.client.api.event.PlayerUnregisterLCEvent;
import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.lunarclient.waypoint.WaypointManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class LunarClientManager implements Listener {

    @Getter private static LunarClientManager instance;

    private WaypointManager waypointManager;

    private Set<UUID> players;

    public LunarClientManager() {
        Plugin api = Bukkit.getPluginManager().getPlugin("LunarClientAPI");
        if(api == null || !api.isEnabled() || !api.getDescription().getMain().equals("com.moonsworth.client.api.LunarClientAPI")) return;

        instance = this;

        this.players = new HashSet<>();

        if(Config.LUNAR_CLIENT_API_FORCED_WAYPOINTS_ENABLED) {
            this.waypointManager = new WaypointManager();
        }

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.players.clear();

        if(this.waypointManager != null) {
            this.waypointManager.disable();
        }
    }

    @EventHandler
    public void onPlayerRegisterLCEvent(PlayerRegisterLCEvent event) {
        Player player = event.getPlayer();
        this.players.add(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerUnregisterLC(PlayerUnregisterLCEvent event) {
        Player player = event.getPlayer();
        this.players.remove(player.getUniqueId());
    }
}
