package me.qiooip.lazarus.timer.cooldown;

import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.type.PlayerTimer;
import me.qiooip.lazarus.utils.StringUtils.FormatType;

import java.util.concurrent.ScheduledExecutorService;

public class RankReviveTimer extends PlayerTimer {

    public RankReviveTimer(ScheduledExecutorService executor) {
        super(executor, "RankReviveTimer", 0, true);

        this.setExpiryMessage(Language.PREFIX + Language.RANK_REVIVE_COOLDOWN_EXPIRED);
        this.setFormat(FormatType.MILLIS_TO_MINUTES);
    }
}
