package me.qiooip.lazarus.timer.scoreboard;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.type.PlayerTimer;
import me.qiooip.lazarus.timer.type.ScoreboardTimer;
import me.qiooip.lazarus.utils.LocationUtils;
import me.qiooip.lazarus.utils.StringUtils.FormatType;
import me.qiooip.lazarus.utils.Tasks;
import me.qiooip.lazarus.utils.Tasks.Callable;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.concurrent.ScheduledExecutorService;

public class TeleportTimer extends PlayerTimer implements ScoreboardTimer {

    public TeleportTimer(ScheduledExecutorService executor) {
        super(executor, "TeleportTimer", Config.KITMAP_SPAWN_TELEPORT_DELAY);

        this.setExpiryMessage(Language.PREFIX + Language.SPAWN_TELEPORTED_KITMAP);
        this.setFormat(FormatType.MILLIS_TO_SECONDS);
    }

    public void activate(Player player, int delay, Location location) {
        super.activate(player, delay, () -> Tasks.sync(()
            -> LocationUtils.teleportWithChunkLoad(player, location)));
    }

    public void activate(Player player, int delay, Location location, Callable callable) {
        super.activate(player, delay, () -> Tasks.sync(() -> {
            callable.call();
            LocationUtils.teleportWithChunkLoad(player, location);
        }));
    }

    @Override
    public String getPlaceholder() {
        return Config.TELEPORT_PLACEHOLDER;
    }

    @Override
    public String getScoreboardEntry(Player player) {
        return this.getTimeLeft(player) + "s";
    }
}
