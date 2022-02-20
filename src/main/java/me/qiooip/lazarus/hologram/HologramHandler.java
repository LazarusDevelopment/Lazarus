package me.qiooip.lazarus.hologram;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.hologram.impl.LeaderboardHologram;
import me.qiooip.lazarus.hologram.task.HologramRenderTask;
import me.qiooip.lazarus.hologram.type.HologramType;
import me.qiooip.lazarus.hologram.type.LeaderboardHologramType;
import me.qiooip.lazarus.utils.FileUtils;
import me.qiooip.lazarus.utils.GsonUtils;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
public class HologramHandler extends Handler implements Listener {

    private HologramRenderTask renderTask;
    private Map<Integer, Hologram> holograms;

    public HologramHandler() {
        Tasks.syncLater(() -> this.loadHolograms(), 20L);
    }

    @Override
    public void disable() {
        this.saveHolograms();

        if(this.renderTask != null) {
            this.renderTask.cancel();
        }
    }

    private File getHologramsFile() {
        return FileUtils.getOrCreateFile(Config.HOLOGRAMS_DIR, "holograms.json");
    }

    private void loadHolograms() {
        String content = FileUtils.readWholeFile(this.getHologramsFile());

        if(content == null) {
            this.holograms = new HashMap<>();
            return;
        }

        this.holograms = Lazarus.getInstance().getGson().fromJson(content, GsonUtils.HOLOGRAMS_TYPE);
        this.holograms.values().forEach(Hologram::updateHologramLines);

        this.renderTask = new HologramRenderTask(this);
    }

    private void saveHolograms() {
        if(this.holograms == null) return;

        FileUtils.writeString(this.getHologramsFile(), Lazarus
            .getInstance().getGson().toJson(this.holograms, GsonUtils.HOLOGRAMS_TYPE));
    }

    public Hologram getHologramById(int id) {
        return this.holograms.get(id);
    }

    public Hologram removeHologramById(int id) {
        return this.holograms.remove(id);
    }

    public void createHologram(Player player, HologramType type) {
        Hologram hologram;
    }

    public void deleteHologram(Player player, int hologramId) {
        Hologram hologram = this.removeHologramById(hologramId);

        if(hologram == null) {

            return;
        }

        hologram.forEachViewer(hologram::removeHologram);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Tasks.syncLater(() -> {
            LeaderboardHologram hologram = new LeaderboardHologram(1, player.getEyeLocation(), LeaderboardHologramType.PLAYER_DEATHS);
            hologram.sendHologram(player);
            this.holograms.put(hologram.getId(), hologram);
        }, 100L);
    }
}
