package me.qiooip.lazarus.hologram.task;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.hologram.Hologram;
import me.qiooip.lazarus.hologram.HologramManager;
import me.qiooip.lazarus.hologram.impl.LeaderboardHologram;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class HologramRenderTask extends BukkitRunnable {

    private static final int DISTANCE = 48 * 48;
    private final HologramManager handler;

    public HologramRenderTask(HologramManager handler) {
        this.handler = handler;
        this.runTaskTimerAsynchronously(Lazarus.getInstance(), 0L, 40L);
    }

    public void renderOrRemoveHolograms(Player player) {
        for(Hologram hologram : this.handler.getHolograms()) {
            this.renderOrRemoveHologram(player, hologram);
        }
    }

    private void renderOrRemoveHologram(Player player, Hologram hologram) {
        if(!hologram.isInSameWorld(player)) return;

        UUID playerUuid = player.getUniqueId();
        Set<UUID> viewers = hologram.getViewers();
        int distance = this.getHologramDistance(player, hologram);

        if(distance > DISTANCE && viewers.contains(playerUuid)) {
            viewers.remove(playerUuid);
            hologram.removeHologram(player);
        } else if(!viewers.contains(playerUuid)) {
            viewers.add(playerUuid);
            hologram.sendHologram(player);
        }
    }

    private int getHologramDistance(Player player, Hologram hologram) {
        return (int) Math.round(hologram.getLocation().distanceSquared(player.getLocation()));
    }

    private void updateLeaderboardHologram(LeaderboardHologram hologram) {
        if(!hologram.getUpdate().get()) return;

        NmsUtils nmsUtils = NmsUtils.getInstance();

        hologram.forEachViewer(viewer -> hologram.getEntries().forEach(entry ->
            nmsUtils.sendHologramMessagePacket(viewer, entry.getEntityId(), entry.getMessage())));

        hologram.getUpdate().set(false);
    }

    @Override
    public void run() {
        List<Hologram> holograms = this.handler.getHolograms();
        if(holograms.isEmpty()) return;

        for(Hologram hologram : holograms) {
            for(Player player : Bukkit.getOnlinePlayers()) {
                this.renderOrRemoveHologram(player, hologram);
            }

            if(hologram instanceof LeaderboardHologram) {
                this.updateLeaderboardHologram((LeaderboardHologram) hologram);
            }
        }
    }
}
