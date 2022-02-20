package me.qiooip.lazarus.hologram.task;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.hologram.Hologram;
import me.qiooip.lazarus.hologram.HologramHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class HologramRenderTask extends BukkitRunnable {

    private static final int DISTANCE = 48 * 48;
    private final HologramHandler handler;

    public HologramRenderTask(HologramHandler handler) {
        this.handler = handler;
        this.runTaskTimerAsynchronously(Lazarus.getInstance(), 0L, 40L);
    }

    private void checkHologramDistance(Player player, Hologram hologram, int distance) {
        UUID playerUuid = player.getUniqueId();
        Set<UUID> viewers = hologram.getViewers();

        if(distance > DISTANCE && viewers.contains(playerUuid)) {
            viewers.remove(playerUuid);
            hologram.removeHologram(player);
        } else if(!viewers.contains(playerUuid)) {
            viewers.add(playerUuid);
            hologram.sendHologram(player);
        }
    }

    @Override
    public void run() {
        Collection<Hologram> holograms = this.handler.getHolograms().values();
        if(holograms.isEmpty()) return;

        for(Hologram hologram : holograms) {
            Location hologramLocation = hologram.getLocation();

            Set<UUID> viewers = hologram.getViewers();
            viewers.removeIf(uuid -> Bukkit.getPlayer(uuid) == null);

            for(Player player : Bukkit.getOnlinePlayers()) {
                if(!hologram.isInSameWorld(player)) continue;

                int distance = (int) Math.round(hologramLocation.distanceSquared(player.getLocation()));
                this.checkHologramDistance(player, hologram, distance);
            }
        }
    }
}
