package me.qiooip.lazarus.timer.abilities;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.type.PlayerTimer;
import me.qiooip.lazarus.timer.type.ScoreboardTimer;
import me.qiooip.lazarus.utils.StringUtils.FormatType;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;

public class GlobalAbilitiesTimer extends PlayerTimer implements ScoreboardTimer {

    public GlobalAbilitiesTimer(ScheduledExecutorService executor) {
        super(executor, "GlobalAbilitiesTimer", Config.ABILITIES_GLOBAL_COOLDOWN_DURATION);

        this.setExpiryMessage(Language.PREFIX + Language.ABILITIES_GLOBAL_COOLDOWN_EXPIRED);
        this.setFormat(FormatType.MILLIS_TO_SECONDS);
    }

    @Override
    public void activate(Player player) {
        this.activate(player.getUniqueId());
    }

    @Override
    public void activate(UUID uuid) {
        if(!Config.ABILITIES_GLOBAL_COOLDOWN_ENABLED) return;
        super.activate(uuid);
    }

    @Override
    public String getTimeLeft(Player player) {
        return super.getTimeLeft(player) + 's';
    }

    @Override
    public String getPlaceholder() {
        return Config.ABILITIES_GLOBAL_COOLDOWN_PLACEHOLDER;
    }

    @Override
    public String getScoreboardEntry(Player player) {
        return this.getTimeLeft(player);
    }
}
