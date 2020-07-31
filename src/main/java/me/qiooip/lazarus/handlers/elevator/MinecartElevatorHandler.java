package me.qiooip.lazarus.handlers.elevator;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.scoreboard.CombatTagTimer;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class MinecartElevatorHandler extends Handler implements Listener {

    private void teleport(Player player, Location location) {
        PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFaction(player);
        Faction factionAt = ClaimManager.getInstance().getFactionAt(location);

        if(Config.MINECART_ELEVATOR_OWN_CLAIM_ONLY && playerFaction != factionAt) {
            player.sendMessage(Language.PREFIX + Language.MINECART_ELEVATOR_OWN_CLAIM_ONLY);
            return;
        }

        CombatTagTimer timer = TimerManager.getInstance().getCombatTagTimer();

        if(Config.MINECART_ELEVATOR_DISABLED_IN_COMBAT && timer.isActive(player)) {
            player.sendMessage(Language.PREFIX + Language.MINECART_ELEVATOR_DENY_COMBAT);
            return;
        }

        location.add(0, 1, 0);

        for(int i = location.getBlockY(); i < location.getWorld().getMaxHeight(); i++, location.add(0, 1, 0)) {
            Material plusOne = location.getBlock().getRelative(BlockFace.UP).getType();
            Material plusTwo = location.getBlock().getRelative(BlockFace.UP, 2).getType();

            if(plusOne != Material.AIR || plusTwo != Material.AIR) continue;
            if(!location.getBlock().getType().isSolid()) continue;

            player.teleport(location.add(0, 1, 0).setDirection(player.getLocation().getDirection()));
            return;
        }

        player.sendMessage(Language.PREFIX + Language.MINECART_ELEVATOR_NO_VALID_LOCATION);
    }

    @EventHandler(ignoreCancelled = true)
    public void onVehicleEnter(VehicleEnterEvent event) {
        if(!Config.MINECART_ELEVATOR_ENABLED) return;

        if(!(event.getVehicle() instanceof Minecart) || !(event.getEntered() instanceof Player)) return;

        Player player = (Player) event.getEntered();
        if(!player.isSneaking()) return;

        Location location = event.getVehicle().getLocation();
        if(location.getBlock().getType() != Material.FENCE_GATE) return;

        event.setCancelled(true);

        location.setY(location.getBlockY());
        this.teleport(player, location.clone());
    }

    @EventHandler(ignoreCancelled = true)
    public void onVehicleExit(VehicleExitEvent event) {
        if(!Config.MINECART_ELEVATOR_ENABLED) return;
        if(!(event.getVehicle() instanceof Minecart) || !(event.getExited() instanceof Player)) return;

        Player player = (Player) event.getExited();

        Location location = event.getVehicle().getLocation();
        if(location.getBlock().getType() != Material.FENCE_GATE) return;

        location.setY(location.getBlockY());
        Tasks.sync(() -> this.teleport(player, location.clone()));
    }
}
