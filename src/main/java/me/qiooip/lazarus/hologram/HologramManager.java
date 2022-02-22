package me.qiooip.lazarus.hologram;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.hologram.task.HologramRenderTask;
import me.qiooip.lazarus.hologram.type.HologramType;
import me.qiooip.lazarus.utils.FileUtils;
import me.qiooip.lazarus.utils.GsonUtils;
import me.qiooip.lazarus.utils.ManagerEnabler;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class HologramManager implements ManagerEnabler, Listener {

    private HologramRenderTask renderTask;
    private List<Hologram> holograms;

    public HologramManager() {
        Tasks.syncLater(() -> this.loadHolograms(), 20L);
        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

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
            this.holograms = new ArrayList<>();
            return;
        }

        this.holograms = Lazarus.getInstance().getGson().fromJson(content, GsonUtils.HOLOGRAMS_TYPE);
        this.holograms.forEach(Hologram::updateHologramLines);

        this.renderTask = new HologramRenderTask(this);
    }

    private void saveHolograms() {
        if(this.holograms == null) return;

        FileUtils.writeString(this.getHologramsFile(), Lazarus
            .getInstance().getGson().toJson(this.holograms, GsonUtils.HOLOGRAMS_TYPE));
    }

    public Hologram getHologramById(int id) {
        return this.holograms.stream().filter(hologram -> hologram.getId() == id).findFirst().orElse(null);
    }

    public void removeHologramById(int id) {
        this.holograms.removeIf(hologram -> hologram.getId() == id);
    }

    public void createHologram(Player player, HologramType type) {
        Hologram hologram;
    }

    public void deleteHologram(CommandSender sender, int hologramId) {
        Hologram hologram = this.getByCommandParam(sender, hologramId);
        if(hologram == null) return;

        this.removeHologramById(hologramId);
        hologram.forEachViewer(hologram::removeHologram);

        for(Hologram remaining : this.holograms) {
            if(remaining.getId() > hologramId) {
                remaining.decrementId();
            }
        }

        sender.sendMessage(Language.HOLOGRAMS_PREFIX + Language.HOLOGRAMS_DELETE_DELETED
            .replace("<id>", String.valueOf(hologramId)));
    }

    public void teleportHologram(Player player, int hologramId) {
        Hologram hologram = this.getByCommandParam(player, hologramId);
        if(hologram == null) return;

        hologram.teleportHologram(player);
        player.sendMessage(Language.HOLOGRAMS_PREFIX + Language.HOLOGRAMS_TELEPORT_TELEPORTED);
    }

    private Hologram getByCommandParam(CommandSender sender, int hologramId) {
        Hologram hologram = this.getHologramById(hologramId);

        if(hologram != null) {
            return hologram;
        }

        sender.sendMessage(Language.HOLOGRAMS_PREFIX + Language.HOLOGRAMS_EXCEPTIONS_DOESNT_EXIST
            .replace("<id>", String.valueOf(hologramId)));

        return null;
    }

    public void listAllHolograms(CommandSender sender) {
        if(this.holograms.isEmpty()) {
            sender.sendMessage(Language.HOLOGRAMS_PREFIX + Language.HOLOGRAMS_LIST_NO_HOLOGRAMS);
            return;
        }

        sender.sendMessage(Language.HOLOGRAMS_COMMAND_HEADER);
        sender.sendMessage(Language.HOLOGRAMS_LIST_TITLE);

        sender.sendMessage("");

        List<Hologram> sorted = this.holograms.stream()
            .sorted(Comparator.comparing(Hologram::getId))
            .collect(Collectors.toList());

        for(Hologram hologram : sorted) {
            String location = StringUtils.getLocationNameWithWorld(hologram.getLocation());

            sender.sendMessage(Language.HOLOGRAMS_LIST_FORMAT
                .replace("<id>", String.valueOf(hologram.getId()))
                .replace("<location>", location));
        }

        sender.sendMessage(Language.HOLOGRAMS_COMMAND_FOOTER);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        for(Hologram hologram : this.holograms) {
            hologram.getViewers().remove(player.getUniqueId());
        }
    }
}
