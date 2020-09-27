package me.qiooip.lazarus.timer.scoreboard;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.event.LazarusKickEvent;
import me.qiooip.lazarus.handlers.event.LazarusKickEvent.KickType;
import me.qiooip.lazarus.lunarclient.cooldown.CooldownType;
import me.qiooip.lazarus.timer.type.PlayerTimer;
import me.qiooip.lazarus.timer.type.ScoreboardTimer;
import me.qiooip.lazarus.utils.PlayerUtils;
import me.qiooip.lazarus.utils.StringUtils.FormatType;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.entity.Player;

import java.util.concurrent.ScheduledExecutorService;

public class LogoutTimer extends PlayerTimer implements ScoreboardTimer {

    public LogoutTimer(ScheduledExecutorService executor) {
        super(executor, "LogoutTimer", Config.LOGOUT_DELAY);

        this.setFormat(FormatType.MILLIS_TO_SECONDS);
        this.setLunarCooldownType(CooldownType.LOGOUT);
    }

    @Override
    public void activate(Player player) {
        super.activate(player, () -> Tasks.sync(() -> {
            player.setMetadata("logout", PlayerUtils.TRUE_METADATA_VALUE);

            LazarusKickEvent event = new LazarusKickEvent(player, KickType.LOGOUT, Language.LOGOUT_KICK_MESSAGE);

            if(!event.isCancelled()) {
                player.kickPlayer(Language.LOGOUT_KICK_MESSAGE);
            }
        }));
    }

    @Override
    public String getPlaceholder() {
        return Config.LOGOUT_PLACEHOLDER;
    }

    @Override
    public String getScoreboardEntry(Player player) {
        return this.getTimeLeft(player) + "s";
    }
}
