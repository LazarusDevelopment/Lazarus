package me.qiooip.lazarus.handlers.holograms;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.utils.FileUtils;
import me.qiooip.lazarus.utils.GsonUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HologramHandler extends Handler implements Listener {

    private final List<Hologram> holograms;

    public HologramHandler() {
        this.holograms = this.loadHolograms();
    }

    private File getHologramsFile() {
        return FileUtils.getOrCreateFile(Config.HOLOGRAMS_DIR, "holograms.json");
    }

    private List<Hologram> loadHolograms() {
        String content = FileUtils.readWholeFile(this.getHologramsFile());

        if(content == null) {
            return new ArrayList<>();
        } else {
            return Lazarus.getInstance().getGson().fromJson(content, GsonUtils.HOLOGRAMS_TYPE);
        }
    }

    private void saveHolograms() {
        if(this.holograms == null) return;

        FileUtils.writeString(this.getHologramsFile(), Lazarus
            .getInstance().getGson().toJson(this.holograms, GsonUtils.HOLOGRAMS_TYPE));
    }

    public void createHologram(Player player, HologramType type) {

    }

    public void deleteHologram(Player player, int hologramId) {

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

    }
}
