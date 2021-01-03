package me.qiooip.lazarus.handlers.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class RankAnnouncerHandler extends Handler {

    private BukkitTask announcementTask;
    private final List<RankAnnouncementData> rankAnnouncementData;

    public RankAnnouncerHandler() {
        this.rankAnnouncementData = new ArrayList<>();
        this.loadRankAnnouncementData();

        Tasks.syncLater(this::setupAnnouncementTask, 10L);
    }

    @Override
    public void disable() {
        this.rankAnnouncementData.clear();

        if(this.announcementTask != null) {
            this.announcementTask.cancel();
        }
    }

    private void loadRankAnnouncementData() {
        ConfigurationSection section = Lazarus.getInstance().getConfig()
            .getSection("ONLINE_RANK_ANNOUNCER");

        section.getKeys(false).forEach(key -> {
            String permission = section.getString(key + ".PERMISSION");
            String rankPrefix = Color.translate(section.getString(key + ".RANK_PREFIX"));

            this.rankAnnouncementData.add(new RankAnnouncementData(permission, rankPrefix));
        });
    }

    private void handlePlayerAnnouncementMessage(Player player, Map<String, List<String>> playerNames) {
        this.rankAnnouncementData.forEach(data -> {
            if(player.isOp() || !player.hasPermission(data.getPermission())) return;

            playerNames.get(data.getPermission()).add(data.getRankPrefix() + player.getName());
        });
    }

    private void sendRankAnnouncementMessage() {
        Map<String, List<String>> playerNames = new LinkedHashMap<>();
        this.rankAnnouncementData.forEach(data -> playerNames.put(data.getPermission(), new ArrayList<>()));

        for(Player player : Bukkit.getOnlinePlayers()) {
            this.handlePlayerAnnouncementMessage(player, playerNames);
        }

        StringJoiner joiner = new StringJoiner(Language.ONLINE_RANK_ANNOUNCER_DELIMITER);
        playerNames.values().forEach(list -> list.forEach(joiner::add));

        if(joiner.length() != 0) {
            Language.ONLINE_RANK_ANNOUNCER_MESSAGE.forEach(line ->
                this.sendGlobalMessage(line.replace("<donators>", joiner.toString())));
        }
    }

    private void sendGlobalMessage(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
    }

    private void setupAnnouncementTask() {
        this.announcementTask = Tasks.asyncTimer(() ->
            this.sendRankAnnouncementMessage(), 0L, Config.ONLINE_RANK_ANNOUNCER_INTERVAL * 20L);
    }

    @Getter
    @AllArgsConstructor
    private static class RankAnnouncementData {

        private final String permission;
        private final String rankPrefix;
    }
}
