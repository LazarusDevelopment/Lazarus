package me.qiooip.lazarus.timer.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.scoreboard.PlayerScoreboard;
import me.qiooip.lazarus.timer.Timer;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.StringUtils.FormatType;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CustomTimer extends Timer {

    @Getter private final List<CustomTimerData> customTimers;

    public CustomTimer(ScheduledExecutorService executor) {
        super(executor, "Custom", 0, false);

        this.customTimers = new ArrayList<>();
    }

    @Override
    public void disable() {
        this.customTimers.forEach(CustomTimerData::cancel);
        this.customTimers.clear();
    }

    public void activate(CommandSender sender, String[] args, int duration) {
        StringJoiner joiner = new StringJoiner(" ");

        for(int i = 2; i < args.length - 1; i++) {
            joiner.add(args[i]);
        }

        String placeholder = Color.translate(joiner.toString());

        ScheduledFuture<?> timer = this.scheduleExpiry(args[1], duration);
        CustomTimerData customTimer = new CustomTimerData(args[1], placeholder + " ", timer);

        this.customTimers.add(customTimer);

        String timerName = placeholder.replace(":", "");

        Messages.sendMessage(Language.PREFIX + Language.CUSTOM_TIMER_STARTED
            .replace("<timerName>", timerName)
            .replace("<player>", sender.getName()));
    }

    public boolean cancel(CommandSender sender, String name) {
        CustomTimerData timerData = this.removeByName(name, true);
        if(timerData == null) return false;

        String timerName = Color.translate(timerData.getPlaceholder()).replace(": ", "");

        Messages.sendMessage(Language.PREFIX + Language.CUSTOM_TIMER_STOPPED
            .replace("<timerName>", timerName)
            .replace("<player>", sender.getName()));

        return true;
    }

    private CustomTimerData getByName(String name) {
        return this.customTimers.stream().filter(timer -> timer.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    private CustomTimerData removeByName(String name, boolean cancel) {
        CustomTimerData timerData = this.getByName(name);
        if(timerData == null) return null;

        if(cancel) {
            timerData.cancel();
        }

        this.customTimers.remove(timerData);
        return timerData;
    }

    private ScheduledFuture<?> scheduleExpiry(String name, int delay) {
        return this.executor.schedule(() -> {
            try {
                this.removeByName(name, false);
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }, delay, TimeUnit.SECONDS);
    }

    public void handleScoreboardUpdate(PlayerScoreboard scoreboard) {
        for(CustomTimerData timer : this.customTimers) {
            scoreboard.add(timer.getPlaceholder(), timer.getDynamicTimeLeft());
        }
    }

    public void listActiveTimers(CommandSender sender) {
        Language.CUSTOM_TIMER_LIST_HEADER.forEach(sender::sendMessage);

        for(int i = 0; i < this.customTimers.size(); i++) {
            CustomTimerData timer = this.customTimers.get(i);

            sender.sendMessage(Language.CUSTOM_TIMER_LIST_FORMAT
                .replace("<id>", String.valueOf(i + 1))
                .replace("<timerName>", timer.getName())
                .replace("<placeholder>", Color.translate(timer.getPlaceholder())));
        }

        Language.CUSTOM_TIMER_LIST_FOOTER.forEach(sender::sendMessage);
    }

    @Getter
    @AllArgsConstructor
    public static class CustomTimerData {

        @Setter private String name;
        private final String placeholder;
        private final ScheduledFuture<?> timer;

        public void cancel() {
            this.timer.cancel(true);
        }

        public String getDynamicTimeLeft() {
            long remaining = this.timer.getDelay(TimeUnit.MILLISECONDS);

            if(remaining < 3600000L) {
                return StringUtils.formatTime(remaining, FormatType.MILLIS_TO_MINUTES);
            } else {
                return StringUtils.formatTime(remaining, FormatType.MILLIS_TO_HOURS);
            }
        }
    }
}
