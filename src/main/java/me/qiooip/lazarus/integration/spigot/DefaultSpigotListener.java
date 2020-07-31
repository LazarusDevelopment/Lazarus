package me.qiooip.lazarus.integration.spigot;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DefaultSpigotListener implements Listener {

    private final Map<Location, BrewingStand> activeBrewingStands;

    public DefaultSpigotListener() {
        this.activeBrewingStands = new HashMap<>();
        new BrewingUpdateTask();

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    private PotionEffect getPotionEffect(Player player, PotionEffectType type) {
        return player.getActivePotionEffects().stream().filter(potion -> potion.getType().equals(type)).findFirst().orElse(null);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if(event.getFrom().getBlockX() == event.getTo().getBlockX()
            && event.getFrom().getBlockY() == event.getTo().getBlockY()
            && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) return;

        if(event.getPlayer().getWorld().getEnvironment() != Environment.THE_END) return;
        if(event.getTo().getBlock().getType() != Material.STATIONARY_WATER) return;

        Location exit = Config.WORLD_EXITS.get(Environment.THE_END);
        World defaultWorld = Bukkit.getWorlds().get(0);

        event.getPlayer().teleport(exit == null ? defaultWorld.getSpawnLocation() : exit);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player)) return;

        Player damager = (Player) event.getDamager();
        if(!damager.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) return;

        PotionEffect effect = this.getPotionEffect(damager, PotionEffectType.INCREASE_DAMAGE);
        if(effect == null) return;

        int level = effect.getAmplifier() + 1;
        int nerf = level == 1 ? Config.STRENGTH_1_NERF_PERCENTAGE : Config.STRENGTH_2_NERF_PERCENTAGE;

        event.setDamage(10.0D * event.getDamage() / (10.0D + 13.0D * level) + 13.0D * event.getDamage() * level * nerf / 100.0D / (10.0D + 13.0D * level));
    }

    @EventHandler(ignoreCancelled = true)
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        if(Config.FURNACE_SPEED_MULTIPLIER <= 1) return;

        new FurnaceUpdateTask((Furnace) event.getBlock().getState());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();

        if(block.getType() == Material.BREWING_STAND) {
            BrewingStand stand = (BrewingStand) block.getState();
            this.activeBrewingStands.put(stand.getLocation(), stand);
        } else if(block.getType() == Material.FURNACE || block.getType() == Material.BURNING_FURNACE) {
            Furnace furnace = (Furnace) block.getState();
            furnace.setCookTime((short)(furnace.getCookTime() + Config.FURNACE_SPEED_MULTIPLIER));
        }
    }

    private static class FurnaceUpdateTask extends BukkitRunnable {

        private final Furnace furnace;

        FurnaceUpdateTask(Furnace furnace) {
            this.furnace = furnace;
            this.runTaskTimer(Lazarus.getInstance(), 0L, 2L);
        }

        public void run() {
            this.furnace.setCookTime((short)(this.furnace.getCookTime() + Config.FURNACE_SPEED_MULTIPLIER));
            this.furnace.update();

            if(this.furnace.getBurnTime() <= 1) {
                this.cancel();
            }
        }
    }

    private class BrewingUpdateTask extends BukkitRunnable {

        BrewingUpdateTask() {
            this.runTaskTimer(Lazarus.getInstance(), 0L, 2L);
        }

        public void run() {
            if(activeBrewingStands.isEmpty()) return;

            Iterator<BrewingStand> iterator = activeBrewingStands.values().iterator();

            while(iterator.hasNext()) {
                BrewingStand stand = iterator.next();

                if (!stand.getChunk().isLoaded()) {
                    iterator.remove();
                    continue;
                }

                if(stand.getBrewingTime() > 1) {
                    stand.setBrewingTime(Math.max(1, stand.getBrewingTime() - Config.BREWING_SPEED_MULTIPLIER));
                }
            }
        }
    }
}
