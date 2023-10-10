package me.qiooip.lazarus.lunarclient;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.BukkitApollo;
import com.lunarclient.apollo.module.staffmod.StaffMod;
import com.lunarclient.apollo.module.staffmod.StaffModModule;
import com.lunarclient.apollo.player.ApolloPlayer;
import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.factions.event.PlayerLeaveFactionEvent;
import me.qiooip.lazarus.lunarclient.cooldown.CooldownManager;
import me.qiooip.lazarus.lunarclient.task.TeamViewTask;
import me.qiooip.lazarus.lunarclient.waypoint.WaypointManager;
import me.qiooip.lazarus.staffmode.event.StaffModeToggleEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
public class LunarClientManager implements Listener {

    private CooldownManager cooldownManager;
    private WaypointManager waypointManager;
    private TeamViewTask teamViewTask;

    private final StaffModModule staffModModule;

    public LunarClientManager() {
        if(Config.LUNAR_CLIENT_API_COOLDOWNS_ENABLED) {
            this.cooldownManager = new CooldownManager();
        }

        if(Config.LUNAR_CLIENT_API_FORCED_WAYPOINTS_ENABLED) {
            this.waypointManager = new WaypointManager();
        }

        if(Config.LUNAR_CLIENT_API_TEAM_VIEW_ENABLED) {
            this.teamViewTask = new TeamViewTask();
        }

        this.staffModModule = Apollo.getModuleManager().getModule(StaffModModule.class);

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
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

    public boolean isOnLunarClient(UUID uuid) {
        return Apollo.getPlayerManager().hasSupport(uuid);
    }

    public boolean isOnLunarClient(Player player) {
        return this.isOnLunarClient(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerLeaveFaction(PlayerLeaveFactionEvent event) {
        if(this.teamViewTask != null) {
            this.teamViewTask.resetPlayerTeamView(event.getFactionPlayer().getUuid());
        }
    }

    @EventHandler
    public void onStaffModeToggle(StaffModeToggleEvent event) {
        if(!Config.LUNAR_CLIENT_API_STAFF_MODULES_ENABLED) return;

        Player player = event.getPlayer();
        Consumer<ApolloPlayer> consumer;

        if(event.isEnable()) {
            consumer = this.staffModModule::enableAllaStaffMods;
        } else {
            consumer = this.staffModModule::disableAllStaffMods;
        }

        BukkitApollo.runForPlayer(player.getUniqueId(), consumer);
    }
}
