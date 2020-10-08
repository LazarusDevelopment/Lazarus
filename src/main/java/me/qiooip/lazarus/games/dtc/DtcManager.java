package me.qiooip.lazarus.games.dtc;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.dtc.event.DtcStartEvent;
import me.qiooip.lazarus.games.dtc.event.DtcStopEvent;
import me.qiooip.lazarus.utils.ManagerEnabler;
import me.qiooip.lazarus.utils.FileUtils;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.File;

public class DtcManager implements Listener, ManagerEnabler {

    private final File dtcFile;

    @Getter private DtcData dtcData;
    @Getter private boolean active;

    public DtcManager() {
        this.dtcFile = FileUtils.getOrCreateFile(Config.GAMES_DIR, "dtc.json");

        this.loadDtc();
    }

    public void disable() {
        this.saveDtc();
        if(this.isActive()) this.stopDtc();
    }

    private void loadDtc() {
        String content = FileUtils.readWholeFile(this.dtcFile);

        if(content == null) {
            this.dtcData = new DtcData();
            return;
        }

        this.dtcData = Lazarus.getInstance().getGson().fromJson(content, DtcData.class);
    }

    private void saveDtc() {
        if(this.dtcData == null) return;

        FileUtils.writeString(this.dtcFile, Lazarus.getInstance().getGson()
            .toJson(this.dtcData, DtcData.class));
    }

    public void startDtc(CommandSender sender, int breaks) {
        if(this.dtcData.getLocation() == null) {
            sender.sendMessage(Language.DTC_PREFIX + Language.DTC_START_NOT_SETUP);
            return;
        }

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());

        this.active = true;

        this.dtcData.getLocation().getBlock().setType(Material.OBSIDIAN);
        this.dtcData.setBreaksLeft(breaks);
        this.dtcData.setStartTime(System.currentTimeMillis());

        new DtcStartEvent(this.dtcData);

        Language.DTC_START_STARTED.forEach(line -> Messages.sendMessage(line
        .replace("<location>", StringUtils.getLocationNameWithWorld(dtcData
        .getLocation())).replace("<amount>", String.valueOf(breaks))));
    }

    public void stopDtc() {
        this.active = false;
        this.dtcData.getLocation().getBlock().setType(Material.AIR);

        new DtcStopEvent();

        HandlerList.unregisterAll(this);
    }

    public int getBreaksLeft() {
        return this.dtcData.getBreaksLeft();
    }

    private void handleWin(Player winner) {
        long duration = System.currentTimeMillis() - this.dtcData.getStartTime();

        Messages.sendMessage(Language.DTC_PREFIX + Language.DTC_DESTROYED
        .replace("<player>", winner.getName()).replace("<time>", DurationFormatUtils
        .formatDurationWords(duration, true, true)));

        Lazarus.getInstance().getLootManager().getLootByName("DTC").handleRewards(winner);
        this.stopDtc();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        if(!event.getBlock().getLocation().equals(this.dtcData.getLocation())) return;

        event.setCancelled(true);

        if(this.dtcData.decreaseBreaks() <= 0) {
            this.handleWin(event.getPlayer());
            return;
        }

        int dtcBreakInterval = Config.DTC_BREAK_MESSAGE_INTERVAL;

        if(dtcBreakInterval != 0 && this.dtcData.getBreaksLeft() % dtcBreakInterval == 0) {
            Messages.sendMessage(Language.DTC_PREFIX + Language.DTC_HEALTH_MESSAGE
            .replace("<amount>", String.valueOf(this.dtcData.getBreaksLeft())));
        }
    }
}
