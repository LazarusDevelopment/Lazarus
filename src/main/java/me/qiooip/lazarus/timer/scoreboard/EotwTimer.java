package me.qiooip.lazarus.timer.scoreboard;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.timer.type.ScoreboardTimer;
import me.qiooip.lazarus.timer.type.SystemTimer;
import me.qiooip.lazarus.utils.StringUtils.FormatType;
import org.bukkit.command.CommandSender;

import java.util.concurrent.ScheduledExecutorService;

public class EotwTimer extends SystemTimer implements ScoreboardTimer {

    public EotwTimer(ScheduledExecutorService executor) {
        super(executor, "EotwTimer", 0, true);

        this.setFormat(FormatType.MILLIS_TO_MINUTES);
    }

    public void activate(CommandSender sender, int delay) {
        super.activate(delay, () -> Lazarus.getInstance().getEotwHandler().startEotw(delay));
    }

    @Override
    public String getPlaceholder() {
        return Config.EOTW_START_PLACEHOLDER;
    }

    @Override
    public String getScoreboardEntry() {
        return this.getTimeLeft();
    }
}
