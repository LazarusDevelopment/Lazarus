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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.IntStream;

public class SignElevatorHandler extends Handler implements Listener {

	private final Set<Material> materials;

	public SignElevatorHandler() {
		this.materials = EnumSet.of(Material.AIR, Material.WALL_SIGN, Material.SIGN_POST);
	}

	@Override
	public void disable() {
		this.materials.clear();
	}

	private boolean isUpSign(Sign sign) {
		return IntStream.range(0, 4).allMatch(i -> sign.getLine(i).equalsIgnoreCase(Config.SIGN_ELEVATOR_ELEVATOR_UP.get(i)));
	}

	private boolean isDownSign(Sign sign) {
		return IntStream.range(0, 4).allMatch(i -> sign.getLine(i).equalsIgnoreCase(Config.SIGN_ELEVATOR_ELEVATOR_DOWN.get(i)));
	}

    private void findTeleportLocation(Player player, Location location, boolean up) {
		if(up) {
			location.add(0, 1, 0);

			for(int i = location.getBlockY(); i < location.getWorld().getMaxHeight(); i++, location.add(0, 1, 0)) {
				Material plusOne = location.getBlock().getRelative(BlockFace.UP).getType();
				Material plusTwo = location.getBlock().getRelative(BlockFace.UP, 2).getType();

				if(!this.materials.contains(plusOne) || !this.materials.contains(plusTwo)) continue;
				if(!this.isSolidGround(location.getBlock().getType())) continue;

				player.teleport(location.add(0.5, 1, 0.5).setDirection(player.getLocation().getDirection()));
				return;
			}
		} else {
			for(int i = location.getBlockY(); i > 0; i--, location.subtract(0, 1, 0)) {
				Material minusOne = location.getBlock().getRelative(BlockFace.DOWN).getType();
				Material minusTwo = location.getBlock().getRelative(BlockFace.DOWN, 2).getType();

				if(!this.materials.contains(minusOne) || !this.materials.contains(minusTwo)) continue;
				if(!this.isSolidGround(location.getBlock().getRelative(BlockFace.DOWN, 3).getType())) continue;

				player.teleport(location.add(0.5, -2, 0.5).setDirection(player.getLocation().getDirection()));
				return;
			}
		}

		player.sendMessage(Language.PREFIX + Language.SIGN_ELEVATOR_NO_VALID_LOCATION);
	}

	private boolean isSolidGround(Material material) {
		return material.isSolid() && material != Material.SIGN_POST && material != Material.WALL_SIGN;
	}
	
	private void teleport(Player player, Location location, boolean up) {
		PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFaction(player);
		Faction factionAt = ClaimManager.getInstance().getFactionAt(location);

		if(Config.SIGN_ELEVATOR_OWN_CLAIM_ONLY && playerFaction != factionAt) {
			player.sendMessage(Language.PREFIX + Language.MINECART_ELEVATOR_OWN_CLAIM_ONLY);
			return;
		}

		CombatTagTimer timer = TimerManager.getInstance().getCombatTagTimer();

		if(Config.SIGN_ELEVATOR_DISABLED_IN_COMBAT && timer.isActive(player)) {
			player.sendMessage(Language.PREFIX + Language.MINECART_ELEVATOR_DENY_COMBAT);
			return;
		}

		this.findTeleportLocation(player, location, up);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onSignChange(SignChangeEvent event) {
		if(!Config.SIGN_ELEVATOR_ENABLED) return;

		String[] lines = event.getLines();
		if(!lines[0].equalsIgnoreCase("[elevator]") && !lines[0].equalsIgnoreCase("[e]")) return;

		Player player = event.getPlayer();

		if(lines[1].equalsIgnoreCase("up")) {
			IntStream.range(0, 4).forEach(i -> event.setLine(i, Config.SIGN_ELEVATOR_ELEVATOR_UP.get(i)));
			player.sendMessage(Language.PREFIX + Language.SIGN_ELEVATOR_CREATED);
			return;
		}

		if(lines[1].equalsIgnoreCase("down")) {
			IntStream.range(0, 4).forEach(i -> event.setLine(i, Config.SIGN_ELEVATOR_ELEVATOR_DOWN.get(i)));
			player.sendMessage(Language.PREFIX + Language.SIGN_ELEVATOR_CREATED);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		if(!Config.SIGN_ELEVATOR_ENABLED) return;
		if(event.getBlock().getType() != Material.SIGN_POST && event.getBlock().getType() != Material.WALL_SIGN) return;
		
		Sign sign = (Sign) event.getBlock().getState();
		if(!this.isUpSign(sign) && !this.isDownSign(sign)) return;

        event.getPlayer().sendMessage(Language.PREFIX + Language.SIGN_ELEVATOR_DESTROYED);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(!Config.SIGN_ELEVATOR_ENABLED || event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if(event.getClickedBlock().getType() != Material.SIGN_POST && event.getClickedBlock().getType() != Material.WALL_SIGN) return;

		Sign sign = (Sign) event.getClickedBlock().getState();

		if(this.isUpSign(sign)) {
			this.teleport(event.getPlayer(), sign.getLocation().clone(), true);
            event.setUseInteractedBlock(Result.DENY);
		} else if(this.isDownSign(sign)) {
			this.teleport(event.getPlayer(), sign.getLocation().clone(), false);
            event.setUseInteractedBlock(Result.DENY);
		}
	}
}
