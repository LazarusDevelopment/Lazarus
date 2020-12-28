package me.qiooip.lazarus.timer.type;

import me.qiooip.lazarus.timer.Timer;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.event.TimerActivateEvent;
import me.qiooip.lazarus.timer.event.TimerCancelEvent;
import me.qiooip.lazarus.timer.event.TimerExpireEvent;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.StringUtils.FormatType;
import me.qiooip.lazarus.utils.Tasks.Callable;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SystemTimer extends Timer {

    protected ScheduledFuture<?> timer;

    protected SystemTimer(ScheduledExecutorService executor, String name, int delay, boolean persistable) {
        super(executor, name, delay, persistable);

        this.loadTimer();
    }

    @Override
    public void disable() {
        this.saveTimer();
        this.cancel();
    }

    protected void loadTimer() {
        if(!this.persistable) return;

        int time = TimerManager.getInstance().getTimersFile().getInt(this.name);
        if(time == 0) return;

        this.activate(time);
    }

    public void saveTimer() {
        if(!this.persistable) return;

        if(!this.isActive()) {
            TimerManager.getInstance().getTimersFile().set(this.name, null);
            return;
        }

        TimerManager.getInstance().getTimersFile().set(this.name, this.getCooldown(TimeUnit.SECONDS));
    }

    public void activate() {
        this.activate(this.delay);
    }

    public void activate(int delay) {
        if(delay <= 0) return;

        TimerActivateEvent event = new TimerActivateEvent(null, this, delay);
        if(event.isCancelled()) return;

        this.timer = this.scheduleExpiry(delay);
    }

    public void activate(int delay, Callable callable) {
        if(delay <= 0) return;

        TimerActivateEvent event = new TimerActivateEvent(null, this, delay);
        if(event.isCancelled()) return;

        this.timer =  this.scheduleExpiry(delay, callable);
    }

    public void cancel() {
        if(!this.isActive()) return;

        new TimerCancelEvent(null, this);

        this.timer.cancel(true);
        this.timer = null;
    }

    public boolean isActive() {
        return this.timer != null;
    }

    public long getCooldown(TimeUnit timeUnit) {
        return this.timer != null ? this.timer.getDelay(timeUnit) : 0L;
    }

    public String getTimeLeft() {
        return StringUtils.formatTime(this.getCooldown(TimeUnit.MILLISECONDS), this.format);
    }

    public String getDynamicTimeLeft() {
        long remaining = this.getCooldown(TimeUnit.MILLISECONDS);

        if(remaining < 3600000L) {
            return StringUtils.formatTime(remaining, FormatType.MILLIS_TO_MINUTES);
        } else {
            return StringUtils.formatTime(remaining, FormatType.MILLIS_TO_HOURS);
        }
    }

    private ScheduledFuture<?> scheduleExpiry(int delay) {
        return this.executor.schedule(() -> {
            try {
                new TimerExpireEvent(null, this);

                this.timer = null;
                if(this.expiryMessage != null) Messages.sendMessage(this.expiryMessage);
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }, delay, TimeUnit.SECONDS);
    }

    private ScheduledFuture<?> scheduleExpiry(int delay, Callable callable) {
        return this.executor.schedule(() -> {
            try {
                new TimerExpireEvent(null, this);

                this.timer = null;
                callable.call();
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }, delay, TimeUnit.SECONDS);
    }
}
