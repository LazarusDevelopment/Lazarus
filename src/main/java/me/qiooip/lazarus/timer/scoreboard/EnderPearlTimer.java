package me.qiooip.lazarus.timer.scoreboard;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.lunarclient.cooldown.CooldownType;
import me.qiooip.lazarus.timer.type.PlayerTimer;
import me.qiooip.lazarus.timer.type.ScoreboardTimer;
import me.qiooip.lazarus.utils.StringUtils.FormatType;
import org.bukkit.entity.Player;

import java.util.concurrent.ScheduledExecutorService;

public class EnderPearlTimer extends PlayerTimer implements ScoreboardTimer {

    public EnderPearlTimer(ScheduledExecutorService executor) {
        super(executor, "EnderPearlTimer", Config.ENDER_PEARL_COOLDOWN_TIME);

        this.setExpiryMessage(Language.PREFIX + Language.ENDERPEARL_COOLDOWN_EXPIRED);
        this.setFormat(FormatType.MILLIS_TO_SECONDS);
        this.setLunarCooldownType(CooldownType.ENDERPEARL);
    }

    @Override
    public String getPlaceholder() {
        return Config.ENDERPEARL_PLACEHOLDER;
    }

    @Override
    public String getScoreboardEntry(Player player) {
        return this.getTimeLeft(player) + "s";
    }
}
