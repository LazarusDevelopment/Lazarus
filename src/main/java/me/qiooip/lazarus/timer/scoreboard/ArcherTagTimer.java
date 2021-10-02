package me.qiooip.lazarus.timer.scoreboard;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.scoreboard.ScoreboardManager;
import me.qiooip.lazarus.timer.type.PlayerTimer;
import me.qiooip.lazarus.timer.type.ScoreboardTimer;
import me.qiooip.lazarus.utils.StringUtils.FormatType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;

public class ArcherTagTimer extends PlayerTimer implements ScoreboardTimer {

    public ArcherTagTimer(ScheduledExecutorService executor) {
        super(executor, "ArcherTagTimer", Config.ARCHER_TAG_DURATION);

        this.setExpiryMessage(Language.PREFIX + Language.ARCHER_TAG_EXPIRED_MESSAGE);
        this.setFormat(FormatType.MILLIS_TO_SECONDS);
    }

    @Override
    public void activate(Player player) {
        this.activate(player.getUniqueId());
    }

    @Override
    public void activate(UUID uuid) {
        super.cancel(uuid);

        super.activate(uuid, () -> {
            Player player = Bukkit.getPlayer(uuid);

            if(player != null) {
                ScoreboardManager.getInstance().updateTabRelations(player, false);
            }
        });
    }

    @Override
    public String getPlaceholder() {
        return Config.ARCHER_TAG_PLACEHOLDER;
    }

    @Override
    public String getScoreboardEntry(Player player) {
        return this.getTimeLeft(player) + "s";
    }
}
