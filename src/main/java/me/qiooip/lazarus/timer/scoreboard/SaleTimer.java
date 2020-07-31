package me.qiooip.lazarus.timer.scoreboard;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.type.ScoreboardTimer;
import me.qiooip.lazarus.timer.type.SystemTimer;

import java.util.concurrent.ScheduledExecutorService;

public class SaleTimer extends SystemTimer implements ScoreboardTimer {

    public SaleTimer(ScheduledExecutorService executor) {
        super(executor, "SaleTimer", 0, true);

        this.setExpiryMessage(Language.SALE_FINISHED.replace("<type>", "Store"));
    }

    @Override
    public String getPlaceholder() {
        return Config.SALE_PLACEHOLDER;
    }

    @Override
    public String getScoreboardEntry() {
        return this.getDynamicTimeLeft();
    }
}
