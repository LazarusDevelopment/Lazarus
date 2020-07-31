package me.qiooip.lazarus.timer.scoreboard;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.type.ScoreboardTimer;
import me.qiooip.lazarus.timer.type.SystemTimer;

import java.util.concurrent.ScheduledExecutorService;

public class KeySaleTimer extends SystemTimer implements ScoreboardTimer {

    public KeySaleTimer(ScheduledExecutorService executor) {
        super(executor, "KeySaleTimer", 0, true);

        this.setExpiryMessage(Language.SALE_FINISHED.replace("<type>", "Key"));
    }

    @Override
    public String getPlaceholder() {
        return Config.KEYSALE_PLACEHOLDER;
    }

    @Override
    public String getScoreboardEntry() {
        return this.getDynamicTimeLeft();
    }
}
