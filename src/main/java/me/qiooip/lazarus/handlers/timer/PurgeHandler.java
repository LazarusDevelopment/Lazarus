package me.qiooip.lazarus.handlers.timer;

import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.scoreboard.PurgeTimer.PurgeTimerType;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;

public class PurgeHandler extends Handler {

    public boolean isActive() {
        return TimerManager.getInstance().getPurgeTimer().isActive(PurgeTimerType.PURGE_TIMER);
    }

    private boolean isPreTaskOrActive() {
        return TimerManager.getInstance().getPurgeTimer().isActive();
    }

    public void startPrePurgeTask(CommandSender sender, int initialDelay, int purgeDuration) {
        if(this.isPreTaskOrActive()) {
            sender.sendMessage(Language.PREFIX + Language.PURGE_ALREADY_RUNNING);
            return;
        }

        TimerManager.getInstance().getPurgeTimer()
            .activate(PurgeTimerType.INITIAL_DELAY, initialDelay, purgeDuration);

        Messages.sendMessage(Language.PURGE_STARTED
            .replace("<time>", StringUtils.formatDurationWords(initialDelay * 1000L)));
    }

    public void startPurge(int delay) {
        TimerManager.getInstance().getPurgeTimer().activate(PurgeTimerType.PURGE_TIMER, delay, -1);

        Messages.sendMessage(Language.PURGE_BROADCAST_START
            .replace("<time>", StringUtils.formatDurationWords(delay * 1000L)));
    }

    public void stopPurge(CommandSender sender) {
        if(!this.isPreTaskOrActive()) {
            sender.sendMessage(Language.PREFIX + Language.PURGE_NOT_RUNNING);
            return;
        }

        TimerManager.getInstance().getPurgeTimer().cancel();
        Messages.sendMessage(Language.PURGE_BROADCAST_STOP);
    }
}
