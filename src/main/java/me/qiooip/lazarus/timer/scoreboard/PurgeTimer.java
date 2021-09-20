package me.qiooip.lazarus.timer.scoreboard;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.type.ScoreboardTimer;
import me.qiooip.lazarus.timer.type.SystemTimer;
import me.qiooip.lazarus.utils.Messages;

import java.util.concurrent.ScheduledExecutorService;

public class PurgeTimer extends SystemTimer implements ScoreboardTimer {

    private PurgeTimerType type;

    public PurgeTimer(ScheduledExecutorService executor) {
        super(executor, "PurgeTimer", 0, true);
    }

    public void activate(PurgeTimerType type, int delay, int purgeDuration) {
        this.type = type;

        if(type == PurgeTimerType.PURGE_TIMER) {
            super.activate(delay, () -> Messages.sendMessage(Language.PURGE_ENDED));
        } else {
            super.activate(delay, () -> Lazarus.getInstance().getPurgeHandler().startPurge(purgeDuration));
        }
    }

    public boolean isActive(PurgeTimerType type) {
        return this.type == type && super.isActive();
    }

    @Override
    public String getPlaceholder() {
        return this.type == PurgeTimerType.INITIAL_DELAY
            ? Config.PURGE_START_PLACEHOLDER : Config.PURGE_PLACEHOLDER;
    }

    @Override
    public String getScoreboardEntry() {
        return this.getDynamicTimeLeft();
    }

    public enum PurgeTimerType {
        INITIAL_DELAY, PURGE_TIMER
    }
}
