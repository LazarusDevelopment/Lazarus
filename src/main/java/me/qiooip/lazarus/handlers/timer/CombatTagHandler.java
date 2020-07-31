package me.qiooip.lazarus.handlers.timer;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.scoreboard.CombatTagTimer;
import me.qiooip.lazarus.utils.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatTagHandler extends Handler implements Listener {

    private final Map<UUID, Long> messageDelays;

    public CombatTagHandler() {
        this.messageDelays = new HashMap<>();
    }

    @Override
    public void disable() {
        this.messageDelays.clear();
    }

    private void sendDelayedMessage(Player player, String message) {
        if(this.messageDelays.containsKey(player.getUniqueId()) && (this.messageDelays
        .get(player.getUniqueId()) - System.currentTimeMillis() > 0)) return;

        player.sendMessage(message);
        this.messageDelays.put(player.getUniqueId(), System.currentTimeMillis() + 3000L);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        CombatTagTimer timer = TimerManager.getInstance().getCombatTagTimer();

        Player attacker = PlayerUtils.getAttacker(event);
        if(attacker == null || attacker.isDead() || player.isDead()) return;
        if(attacker == player && event.getCause() == DamageCause.FALL) return;

        timer.activate(attacker.getUniqueId());
        timer.activate(player.getUniqueId());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if(!TimerManager.getInstance().getCombatTagTimer().isActive(event.getPlayer())) return;

        if(Config.COMBAT_TAG_DISABLED_COMMANDS.stream().noneMatch(command -> event
        .getMessage().toLowerCase().startsWith(command.toLowerCase()))) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(Language.PREFIX + Language.COMBAT_TAG_COMMAND_DENY);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if(!TimerManager.getInstance().getCombatTagTimer().isActive(event.getPlayer())) return;
        if(Config.COMBAT_TAG_BREAK_BLOCKS) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(Language.PREFIX + Language.COMBAT_TAG_BLOCK_BREAK_DENY);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if(!TimerManager.getInstance().getCombatTagTimer().isActive(event.getPlayer())) return;
        if(Config.COMBAT_TAG_PLACE_BLOCKS) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(Language.PREFIX + Language.COMBAT_TAG_BLOCK_PLACE_DENY);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPortal(PlayerPortalEvent event) {
        if(event.getCause() != TeleportCause.END_PORTAL || !Config.COMBAT_TAG_DISABLE_END_ENTRY) return;
        if(event.getFrom().getWorld().getEnvironment() != Environment.NORMAL) return;
        if(!TimerManager.getInstance().getCombatTagTimer().isActive(event.getPlayer())) return;

        event.setCancelled(true);
        this.sendDelayedMessage(event.getPlayer(), Language.PREFIX + Language.COMBAT_TAG_END_PORTAL_TELEPORT_DENY);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK || !Config.COMBAT_TAG_DISABLE_ENDERCHESTS) return;
        if(event.getClickedBlock().getType() != Material.ENDER_CHEST) return;
        if(!TimerManager.getInstance().getCombatTagTimer().isActive(event.getPlayer())) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(Language.PREFIX + Language.COMBAT_TAG_ENDERCHEST_DENY);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        TimerManager.getInstance().getCombatTagTimer().cancel(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.messageDelays.remove(event.getPlayer().getUniqueId());
    }
}
