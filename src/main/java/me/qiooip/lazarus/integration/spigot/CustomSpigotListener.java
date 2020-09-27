package me.qiooip.lazarus.integration.spigot;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.utils.PlayerUtils;
import net.minecraft.server.v1_7_R4.TileEntityBrewingStand;
import net.minecraft.server.v1_7_R4.TileEntityFurnace;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EnderpearlLandEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CustomSpigotListener implements Listener {

    private final Map<UUID, Location> locations;
    private final Set<Material> materials;
    private final BlockFace[] faces;

    public CustomSpigotListener() {
        this.locations = new HashMap<>();

        this.materials = EnumSet.of(Material.FENCE_GATE, Material.FENCE, Material.IRON_FENCE,
            Material.NETHER_FENCE, Material.THIN_GLASS, Material.STAINED_GLASS_PANE,
            Material.IRON_DOOR_BLOCK, Material.WOODEN_DOOR);

        this.faces = new BlockFace[] { BlockFace.SOUTH, BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST, BlockFace.SELF };

        TileEntityBrewingStand.brewSpeedMultiplier = Config.BREWING_SPEED_MULTIPLIER;
        TileEntityFurnace.cookSpeedMultiplier = Config.FURNACE_SPEED_MULTIPLIER;

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());

        if(!Config.STRENGTH_NERF_ENABLED) return;

        try {
            Class<?> clazz = Class.forName("gg.custom.CustomSpigotConfig");
            this.setStrengthFields(clazz);
        } catch(ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private void setStrengthFields(Class<?> clazz) throws ReflectiveOperationException {
        Field strength1 = clazz.getDeclaredField("strength_1_nerf");
        Field strength2 = clazz.getDeclaredField("strength_2_nerf");

        strength1.set(clazz, Config.STRENGTH_1_NERF_PERCENTAGE / 100.00);
        strength2.set(clazz, Config.STRENGTH_2_NERF_PERCENTAGE / 100.00);
    }

    /*
        Ported from EnderPearlHandler.java
     */

    @EventHandler
    public void onEnderpearlLand(EnderpearlLandEvent event) {
        if(!Config.ENDERPEARL_GLITCH_FIX_ENABLED) return;
        if(event.getReason() != EnderpearlLandEvent.Reason.BLOCK) return;

        Block block = event.getEntity().getWorld().getBlockAt(event.getEntity().getLocation());
        Player player = (Player) event.getEntity().getShooter();
        boolean cancel = false;

        if(block.getType() == Material.AIR && block.getRelative(BlockFace.UP).getType() != Material.AIR && block.getRelative(BlockFace.DOWN).getType() != Material.AIR) {
            cancel = true;
        } else if(this.materials.contains(block.getType())) {
            if(player.getLocation().getBlock().getLocation().distance(block.getLocation()) < 1.5) {
                cancel = true;
            } else if(block.getRelative(BlockFace.UP).getType() != Material.AIR && block.getRelative(BlockFace.UP).getType().isSolid()) {
                cancel = true;
            }
        }

        if(cancel) {
            event.setCancelled(true);

            TimerManager.getInstance().getEnderPearlTimer().cancel(player);
            player.setMetadata("enderpearlRefund", PlayerUtils.TRUE_METADATA_VALUE);

            player.sendMessage(Language.PREFIX + Language.ENDERPEARL_GLITCH_DENY);
            return;
        }

        this.locations.put(player.getUniqueId(), event.getEntity().getLocation());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if(!Config.ENDERPEARL_GLITCH_FIX_ENABLED) return;
        if(event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;

        Player player = event.getPlayer();
        Location location = event.getTo().clone();
        Block block = location.getBlock();

        Location pearl = this.locations.getOrDefault(player.getUniqueId(), null);
        this.locations.remove(player.getUniqueId());

        if(block.getType() == Material.LADDER || block.getType() == Material.VINE) {
            Block up = block.getRelative(BlockFace.UP);
            boolean teleport = false;

            if(up.getType() == Material.TRAP_DOOR) {
                teleport = true;
            } else if(up.getType() == Material.LADDER || up.getType() == Material.VINE) {
                if(up.getRelative(BlockFace.UP).getType() != Material.TRAP_DOOR) return;

                teleport = true;
            }

            if(teleport) {
                location.setX(location.getBlockX() + 0.5);
                location.setY(location.getY() - 1);
                location.setZ(location.getBlockZ() + 0.5);

                event.setTo(location);
            }

            return;
        }

        if(block.getType().name().endsWith("STEP") || block.getType().name().endsWith("STAIRS")) {
            if(block.getRelative(BlockFace.UP).getType() != Material.AIR) return;

            location.setX(location.getBlockX() + 0.5);
            location.setY(location.getY() + 0.5);
            location.setZ(location.getBlockZ() + 0.5);

            event.setTo(location);
            return;
        }

        if(this.materials.contains(block.getType())) {
            if(pearl == null) return;

            pearl.setPitch(0);
            location.add(pearl.getDirection().multiply(-1).normalize());

            event.setTo(location);
        } else {
            boolean check = false;

            for(BlockFace face : faces) {
                if(block.getRelative(face).getType() == Material.AIR) continue;

                check = true;
            }

            if(block.getType() == Material.AIR && block.getRelative(BlockFace.UP).getType() != Material.AIR && block.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
                location.setY(location.getBlockY() - 1);

                if(!check) event.setTo(location);
            }

            if(check) {
                location.setX(location.getBlockX() + 0.5);
                location.setZ(location.getBlockZ() + 0.5);

                event.setTo(location);
            }
        }
    }
}
