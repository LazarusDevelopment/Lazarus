package me.qiooip.lazarus.handlers.logger;

import lombok.Setter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatLoggerHandler extends Handler implements Listener {

    @Setter private boolean kickAll;
    private final Map<UUID, CombatLogger> combatLoggers;

    public CombatLoggerHandler() {
        this.combatLoggers = new HashMap<>();
        NmsUtils.getInstance().registerCombatLogger();
    }

    @Override
    public void disable() {
        this.combatLoggers.values().forEach(CombatLogger::removeCombatLogger);
        this.combatLoggers.clear();
    }

    public CombatLogger removeCombatLogger(UUID uuid) {
        return this.combatLoggers.remove(uuid);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.removeMetadata("Logout", Lazarus.getInstance());

        CombatLogger combatLogger = this.removeCombatLogger(player.getUniqueId());
        if(combatLogger == null) return;

        player.setHealth(combatLogger.getCombatLoggerHealth());
        player.teleport(combatLogger.getCombatLoggerLocation());

        combatLogger.handleEffectChanges(player);
        combatLogger.removeCombatLogger();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if(player.getHealth() == 0.0) return;
        if(this.kickAll) return;

        if(player.hasPermission("lazarus.combatlogger.bypass") || player.hasMetadata("logout")) {
            player.removeMetadata("logout", Lazarus.getInstance());
            return;
        }

        if(TimerManager.getInstance().getPvpProtTimer().isActive(player)) return;
        if(Lazarus.getInstance().getSotwHandler().isUnderSotwProtection(player)) return;
        if(Lazarus.getInstance().getStaffModeManager().isInStaffModeOrVanished(player)) return;
        if(ClaimManager.getInstance().getFactionAt(player).isSafezone()) return;

        CombatLogger logger = NmsUtils.getInstance().spawnCombatLogger(player.getWorld(), player);
        this.combatLoggers.put(player.getUniqueId(), logger);
    }
}
