package me.qiooip.lazarus.timer.scoreboard;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.type.PlayerTimer;
import me.qiooip.lazarus.timer.type.ScoreboardTimer;
import org.bukkit.entity.Player;

import java.util.concurrent.ScheduledExecutorService;

public class GAppleTimer extends PlayerTimer implements ScoreboardTimer {

    public GAppleTimer(ScheduledExecutorService executor) {
        super(executor, "GAppleTimer", Config.ENCHANTED_GOLDEN_APPLE_COOLDOWN, true);

        this.setExpiryMessage(Language.PREFIX + Language.ENCHANTED_APPLE_COOLDOWN_EXPIRED);
    }

    @Override
    public String getPlaceholder() {
        return Config.ENCHANTED_GOLDEN_APPLE_PLACEHOLDER;
    }

    @Override
    public String getScoreboardEntry(Player player) {
        return this.getDynamicTimeLeft(player);
    }
}
