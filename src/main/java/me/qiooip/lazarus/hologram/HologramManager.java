package me.qiooip.lazarus.hologram;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.leaderboard.event.LeaderboardUpdateEvent;
import me.qiooip.lazarus.hologram.impl.LeaderboardHologram;
import me.qiooip.lazarus.hologram.impl.StaticHologram;
import me.qiooip.lazarus.hologram.task.HologramRenderTask;
import me.qiooip.lazarus.hologram.type.LeaderboardHologramType;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.FileUtils;
import me.qiooip.lazarus.utils.GsonUtils;
import me.qiooip.lazarus.utils.ManagerEnabler;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.Tasks;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
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
        Tasks.syncLater(() -> this.enable(), 20L);
    }

    private void enable() {
        this.loadHolograms();
        this.renderTask = new HologramRenderTask(this);

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.saveHolograms();

        if(this.renderTask != null) {
            this.renderTask.cancel();
        }

        if(this.holograms != null && !this.holograms.isEmpty()) {
            this.holograms.forEach(hologram -> hologram.forEachViewer(hologram::removeHologram));
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
        this.holograms.forEach(Hologram::createHologramLines);
    }

    private void saveHolograms() {
        if(this.holograms == null) return;

        FileUtils.writeString(this.getHologramsFile(), Lazarus
            .getInstance().getGson().toJson(this.holograms, GsonUtils.HOLOGRAMS_TYPE));
    }

    public Hologram getHologramById(int id) {
        return this.holograms.stream().filter(hologram -> hologram.getId() == id).findFirst().orElse(null);
    }

    public void addHologram(Hologram hologram) {
        this.holograms.add(hologram);
    }

    public void removeHologramById(int id) {
        this.holograms.removeIf(hologram -> hologram.getId() == id);
    }

    public void createHologram(Player player, String parameter) {
        Hologram hologram = this.createHologramFromParameter(player, parameter);
        if(hologram == null) return;

        hologram.createHologramLines();
        Bukkit.getOnlinePlayers().forEach(hologram::sendHologram);

        this.addHologram(hologram);
        player.sendMessage(Language.HOLOGRAMS_PREFIX + Language.HOLOGRAMS_CREATE_CREATED);
    }

    private Hologram createHologramFromParameter(Player player, String parameter) {
        Location location = player.getEyeLocation();
        int hologramId = this.holograms.size() + 1;

        if(parameter.equalsIgnoreCase("normal")) {
            return new StaticHologram(hologramId, location);
        }

        LeaderboardHologramType type = LeaderboardHologramType.getByName(parameter);

        if(type != null) {
            return new LeaderboardHologram(hologramId, location, type);
        }

        player.sendMessage(Language.HOLOGRAMS_PREFIX + Language
            .HOLOGRAMS_EXCEPTIONS_TYPE_NOT_FOUND.replace("<type>", parameter));
        return null;
    }

    public void deleteHologram(CommandSender sender, int hologramId) {
        Hologram hologram = this.getByCommandParam(sender, hologramId);
        if(hologram == null) return;

        this.removeHologramById(hologramId);
        hologram.forEachViewerAsync(hologram::removeHologram);

        for(Hologram remaining : this.holograms) {
            if(remaining.getId() > hologramId) {
                remaining.decrementId();
            }
        }

        sender.sendMessage(Language.HOLOGRAMS_PREFIX + Language.HOLOGRAMS_DELETE_DELETED
            .replace("<id>", String.valueOf(hologramId)));
    }

    public void teleportToTheHologram(Player player, int hologramId) {
        Hologram hologram = this.getByCommandParam(player, hologramId);
        if(hologram == null) return;

        if(!player.teleport(hologram.getLocation())) return;

        player.sendMessage(Language.HOLOGRAMS_PREFIX + Language.HOLOGRAMS_TELEPORT_TELEPORTED
            .replace("<id>", String.valueOf(hologramId)));
    }

    public void teleportHologram(Player player, int hologramId) {
        Hologram hologram = this.getByCommandParam(player, hologramId);
        if(hologram == null) return;

        hologram.teleportHologram(player);
        player.sendMessage(Language.HOLOGRAMS_PREFIX + Language.HOLOGRAMS_TELEPORT_HERE_TELEPORTED);
    }

    public void addHologramLine(CommandSender sender, int hologramId, String text) {
        StaticHologram staticHologram = this.getStaticHologramByCommandParam(sender, hologramId);
        if(staticHologram == null) return;

        String lineText = text.equalsIgnoreCase("EMPTY") ? "" : Color.translate(text);

        staticHologram.addLine(lineText);
        staticHologram.refreshForViewersAsync();

        sender.sendMessage(Language.HOLOGRAMS_PREFIX + Language
            .HOLOGRAMS_ADD_LINE_LINE_ADDED.replace("<id>", String.valueOf(hologramId)));
    }

    public void insertHologramLine(CommandSender sender, int hologramId, int lineNumber, String text) {
        StaticHologram staticHologram = this.getStaticHologramByCommandParam(sender, hologramId);
        if(staticHologram == null) return;

        int lineIndex = lineNumber - 1;
        String lineText = text.equalsIgnoreCase("EMPTY") ? "" : Color.translate(text);

        if(!staticHologram.addLine(lineIndex, lineText)) {
            sender.sendMessage(Language.HOLOGRAMS_PREFIX + Language.HOLOGRAMS_EXCEPTIONS_CANT_INSERT_LINE
                .replace("<lineIndex>", String.valueOf(lineNumber)));
            return;
        }

        staticHologram.refreshForViewersAsync();

        sender.sendMessage(Language.HOLOGRAMS_PREFIX + Language
            .HOLOGRAMS_INSERT_LINE_INSERTED.replace("<id>", String.valueOf(hologramId)));
    }

    public void updateHologramLine(CommandSender sender, int hologramId, int lineNumber, String text) {
        StaticHologram staticHologram = this.getStaticHologramByCommandParam(sender, hologramId);
        if(staticHologram == null) return;

        int lineIndex = lineNumber - 1;
        String lineText = text.equalsIgnoreCase("EMPTY") ? "" : Color.translate(text);

        if(!staticHologram.updateLine(lineIndex, lineText)) {
            sender.sendMessage(Language.HOLOGRAMS_PREFIX + Language.HOLOGRAMS_EXCEPTIONS_LINE_DOESNT_EXIST
                .replace("<lineIndex>", String.valueOf(lineNumber)));
            return;
        }

        staticHologram.refreshForViewersAsync();

        sender.sendMessage(Language.HOLOGRAMS_PREFIX + Language
            .HOLOGRAMS_UPDATE_LINE_UPDATED.replace("<id>", String.valueOf(hologramId)));
    }

    public void removeHologramLine(CommandSender sender, int hologramId, int lineNumber) {
        StaticHologram staticHologram = this.getStaticHologramByCommandParam(sender, hologramId);
        if(staticHologram == null) return;

        List<Integer> entityIds = staticHologram.getEntries().stream()
            .map(HologramEntry::getEntityId).collect(Collectors.toList());

        if(!staticHologram.removeLine(lineNumber - 1)) {
            sender.sendMessage(Language.HOLOGRAMS_PREFIX + Language.HOLOGRAMS_EXCEPTIONS_LINE_DOESNT_EXIST
                .replace("<lineIndex>", String.valueOf(lineNumber)));
            return;
        }

        staticHologram.createHologramLines();
        NmsUtils nmsUtils = NmsUtils.getInstance();

        staticHologram.forEachViewerAsync(viewer -> {
            entityIds.forEach(id -> nmsUtils.sendHologramDestroyPacket(viewer, id));
            staticHologram.sendHologram(viewer);
        });

        sender.sendMessage(Language.HOLOGRAMS_PREFIX + Language
            .HOLOGRAMS_REMOVE_LINE_REMOVED.replace("<id>", String.valueOf(hologramId)));
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

    private StaticHologram getStaticHologramByCommandParam(CommandSender sender, int hologramId) {
        Hologram hologram = this.getByCommandParam(sender, hologramId);

        if(hologram == null) {
            return null;
        }

        if(!(hologram instanceof StaticHologram)) {
            sender.sendMessage(Language.HOLOGRAMS_PREFIX + Language.HOLOGRAMS_EXCEPTIONS_TYPE_MUST_BE_NORMAL);
            return null;
        }

        return (StaticHologram) hologram;
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

    public void updateLeaderboardHologram(LeaderboardHologramType type) {
        for(Hologram hologram : this.holograms) {
            if(!(hologram instanceof LeaderboardHologram)) continue;

            LeaderboardHologram leaderboardHologram = (LeaderboardHologram) hologram;

            if(leaderboardHologram.getLeaderboardType() == type) {
                leaderboardHologram.updateHologramLines();
            }
        }
    }

    @EventHandler
    public void onLeaderboardUpdate(LeaderboardUpdateEvent event) {
        this.updateLeaderboardHologram(event.getType());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Tasks.async(() -> this.renderTask.renderOrRemoveHolograms(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        for(Hologram hologram : this.holograms) {
            hologram.getViewers().remove(player.getUniqueId());
        }
    }
}
