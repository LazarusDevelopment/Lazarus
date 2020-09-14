package me.qiooip.lazarus.lunarclient;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.event.LCPlayerRegisterEvent;
import com.lunarclient.bukkitapi.event.LCPlayerUnregisterEvent;
import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.lunarclient.waypoint.WaypointManager;
import me.qiooip.lazarus.staffmode.event.StaffModeToggleEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class LunarClientManager implements Listener {

    @Getter private static LunarClientManager instance;

    private WaypointManager waypointManager;
    private final Set<UUID> players;

    public LunarClientManager() {
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
    public void onPlayerRegisterLCEvent(LCPlayerRegisterEvent event) {
        Player player = event.getPlayer();
        this.players.add(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerUnregisterLC(LCPlayerUnregisterEvent event) {
        Player player = event.getPlayer();
        this.players.remove(player.getUniqueId());
    }

    @EventHandler
    public void onStaffModeToggle(StaffModeToggleEvent event) {
        if(!Config.LUNAR_CLIENT_API_STAFF_MODULES_ENABLED) return;

        Player player = event.getPlayer();

        if(event.isEnable()) {
            LunarClientAPI.getInstance().giveAllStaffModules(player);
        } else {
            LunarClientAPI.getInstance().disableAllStaffModules(player);
        }
    }
}
