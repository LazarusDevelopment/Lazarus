package me.qiooip.lazarus.lunarclient;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.event.LCPlayerRegisterEvent;
import com.lunarclient.bukkitapi.event.LCPlayerUnregisterEvent;
import com.lunarclient.bukkitapi.nethandler.client.LCPacketModSettings;
import com.lunarclient.bukkitapi.nethandler.client.LCPacketServerRule;
import com.lunarclient.bukkitapi.nethandler.client.obj.ModSettings;
import com.lunarclient.bukkitapi.nethandler.client.obj.ServerRule;
import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.lunarclient.cooldown.CooldownManager;
import me.qiooip.lazarus.lunarclient.task.TeamViewTask;
import me.qiooip.lazarus.lunarclient.waypoint.WaypointManager;
import me.qiooip.lazarus.staffmode.event.StaffModeToggleEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class LunarClientManager implements Listener {

    private CooldownManager cooldownManager;
    private WaypointManager waypointManager;
    private TeamViewTask teamViewTask;

    private final Set<UUID> players;
    private final Set<LCPacketServerRule> serverRules;
    private final LCPacketModSettings modSettings;

    public LunarClientManager() {
        this.players = new HashSet<>();
        this.serverRules = new HashSet<>();

        if(Config.LUNAR_CLIENT_API_COOLDOWNS_ENABLED) {
            this.cooldownManager = new CooldownManager();
        }

        if(Config.LUNAR_CLIENT_API_FORCED_WAYPOINTS_ENABLED) {
            this.waypointManager = new WaypointManager();
        }

        if(Config.LUNAR_CLIENT_API_TEAM_VIEW_ENABLED) {
            this.teamViewTask = new TeamViewTask();
        }

        this.setupServerRules();
        this.modSettings = this.setupModSettings();

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.players.clear();

        if(this.cooldownManager != null) {
            this.cooldownManager.disable();
        }

        if(this.waypointManager != null) {
            this.waypointManager.disable();
        }

        if(this.teamViewTask != null) {
            this.teamViewTask.cancel();
        }
    }

    private void setupServerRules() {
        ConfigurationSection section = Lazarus.getInstance().getConfig().getSection("SERVER_RULES");

        section.getKeys(false).forEach(key -> {
            ServerRule rule;

            try {
                rule = ServerRule.valueOf(key);
            } catch (IllegalArgumentException e) {
                return;
            }

            LCPacketServerRule serverRule;
            Object value = section.get(key);

            if(value instanceof String) {
                serverRule = new LCPacketServerRule(rule, section.getString(key));
            } else {
                serverRule = new LCPacketServerRule(rule, section.getBoolean(key));
            }

            this.serverRules.add(serverRule);
        });
    }

    private LCPacketModSettings setupModSettings() {
        ModSettings settings = new ModSettings();

        for(String id : Config.LUNAR_CLIENT_FORCE_DISABLED_MODS) {
            settings.addModSetting(id, new ModSettings.ModSetting(false, new HashMap<>()));
        }

        return new LCPacketModSettings(settings);
    }

    public boolean isOnLunarClient(UUID uuid) {
        return this.players.contains(uuid);
    }

    public boolean isOnLunarClient(Player player) {
        return this.isOnLunarClient(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerRegisterLC(LCPlayerRegisterEvent event) {
        Player player = event.getPlayer();
        this.players.add(player.getUniqueId());

        this.sendRules(player);

        if(!Config.LUNAR_CLIENT_FORCE_DISABLED_MODS.isEmpty()) {
            LunarClientAPI.getInstance().sendPacket(player, this.modSettings);
        }
    }

    @EventHandler
    public void onPlayerUnregisterLC(LCPlayerUnregisterEvent event) {
        this.players.remove(event.getPlayer().getUniqueId());
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

    private void sendRules(Player player) {
        for(LCPacketServerRule packet : this.serverRules) {
            LunarClientAPI.getInstance().sendPacket(player, packet);
        }
    }
}
