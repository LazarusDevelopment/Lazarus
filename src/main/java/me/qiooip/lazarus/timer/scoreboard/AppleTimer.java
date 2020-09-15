package me.qiooip.lazarus.timer.scoreboard;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.lunarclient.cooldown.CooldownType;
import me.qiooip.lazarus.timer.type.PlayerTimer;
import me.qiooip.lazarus.timer.type.ScoreboardTimer;
import me.qiooip.lazarus.utils.StringUtils.FormatType;
import org.bukkit.entity.Player;

import java.util.concurrent.ScheduledExecutorService;

public class AppleTimer extends PlayerTimer implements ScoreboardTimer {

    public AppleTimer(ScheduledExecutorService executor) {
        super(executor, "AppleTimer", Config.NORMAL_GOLDEN_APPLE_COOLDOWN, true);

        this.setExpiryMessage(Language.PREFIX + Language.NORMAL_APPLE_COOLDOWN_EXPIRED);
        this.setFormat(FormatType.MILLIS_TO_MINUTES);
        this.setLunarCooldownType(CooldownType.APPLE);
    }

    @Override
    public String getPlaceholder() {
        return Config.NORMAL_GOLDEN_APPLE_PLACEHOLDER;
    }

    @Override
    public String getScoreboardEntry(Player player) {
        return this.getTimeLeft(player);
    }
}
