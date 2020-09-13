package me.qiooip.lazarus.timer.abilities;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.type.PlayerTimer;
import me.qiooip.lazarus.timer.type.ScoreboardTimer;
import me.qiooip.lazarus.utils.StringUtils.FormatType;
import org.bukkit.entity.Player;

import java.util.concurrent.ScheduledExecutorService;

public class GlobalAbilitiesTimer extends PlayerTimer implements ScoreboardTimer {

    public GlobalAbilitiesTimer(ScheduledExecutorService executor) {
        super(executor, "GlobalAbilitiesTimer", Config.ABILITIES_GLOBAL_COOLDOWN_DURATION);

        // TODO: change expiry message
        this.setExpiryMessage(Language.PREFIX + Language.NORMAL_APPLE_COOLDOWN_EXPIRED);
        this.setFormat(FormatType.MILLIS_TO_SECONDS);
    }

    @Override
    public String getPlaceholder() {
        return Config.ABILITIES_GLOBAL_COOLDOWN_PLACEHOLDER;
    }

    @Override
    public String getScoreboardEntry(Player player) {
        return this.getTimeLeft(player);
    }
}
