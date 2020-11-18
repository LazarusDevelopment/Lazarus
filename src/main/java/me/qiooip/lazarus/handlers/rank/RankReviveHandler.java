package me.qiooip.lazarus.handlers.rank;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.cooldown.RankReviveTimer;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.Messages;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RankReviveHandler extends Handler {

    private final List<RankReviveData> rankRevives;

    public RankReviveHandler() {
        this.rankRevives = new ArrayList<>();

        this.loadRankReviveData();
    }

    @Override
    public void disable() {
        this.rankRevives.clear();
    }

    private void loadRankReviveData() {
        ConfigurationSection section = Lazarus.getInstance().getConfig()
            .getSection("RANK_REVIVE_COMMAND");

        section.getKeys(false).forEach(key -> {
            RankReviveData rankRevive = new RankReviveData();

            rankRevive.setRankName(Color.translate(section.getString(key + ".RANK_NAME", "")));
            rankRevive.setPermission(section.getString(key + ".PERMISSION"));
            rankRevive.setCooldown(section.getInt(key + ".COOLDOWN"));

            this.rankRevives.add(rankRevive);
        });
    }

    private boolean hasRankRevivePermission(Player player) {
        return this.rankRevives.stream().anyMatch(rankRevive -> player.hasPermission(rankRevive.getPermission()));
    }

    private RankReviveData getRankReviveData(Player player) {
        return this.rankRevives.stream().filter(rankRevive -> player.hasPermission(rankRevive.getPermission()))
        .findFirst().orElse(null);
    }

    public void performCommand(Player player, OfflinePlayer target) {
        if(!this.hasRankRevivePermission(player)) {
            player.sendMessage(Language.PREFIX + Language.RANK_REVIVE_NO_PERMISSION);
            return;
        }

        RankReviveTimer timer = TimerManager.getInstance().getRankReviveTimer();

        if(timer.isActive(player)) {
            player.sendMessage(Language.PREFIX + Language.RANK_REVIVE_COOLDOWN_ACTIVE
            .replace("<seconds>", timer.getDynamicTimeLeft(player)));
            return;
        }

        if(!Lazarus.getInstance().getDeathbanManager().isDeathBanned(target.getUniqueId())) {
            player.sendMessage(Language.PREFIX + Language.RANK_REVIVE_NOT_DEATHBANNED
            .replace("<player>", target.getName()));
            return;
        }

        RankReviveData rankReviveData = this.getRankReviveData(player);

        Lazarus.getInstance().getDeathbanManager().revivePlayer(target.getUniqueId());
        timer.activate(player, rankReviveData.getCooldown() * 60);

        Messages.sendMessage(Language.RANK_REVIVE_BROADCAST_MESSAGE
            .replace("<rankName>", rankReviveData.getRankName())
            .replace("<player>", player.getName())
            .replace("<target>", target.getName()));
    }

    @Getter
    @Setter
    private static class RankReviveData {

        private String rankName;
        private String permission;
        private int cooldown;
    }
}
