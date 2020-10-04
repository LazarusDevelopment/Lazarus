package me.qiooip.lazarus.timer;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.lunarclient.cooldown.CooldownType;
import me.qiooip.lazarus.utils.StringUtils.FormatType;

import java.util.concurrent.ScheduledExecutorService;

@Getter
public abstract class Timer {

    protected final String name;

    protected final ScheduledExecutorService executor;
    protected final int delay;
    protected final boolean persistable;

    @Setter protected FormatType format;
    @Setter protected String expiryMessage;
    @Setter protected CooldownType lunarCooldownType;

    protected Timer(ScheduledExecutorService executor, String name, int delay, boolean persistable) {
        this.name = name;

        this.executor = executor;
        this.delay = delay;
        this.persistable = persistable;
    }

    public abstract void disable();
}
