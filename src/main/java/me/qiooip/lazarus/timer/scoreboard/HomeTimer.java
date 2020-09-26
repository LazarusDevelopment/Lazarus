package me.qiooip.lazarus.timer.scoreboard;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.lunarclient.cooldown.CooldownType;
import me.qiooip.lazarus.timer.type.PlayerTimer;
import me.qiooip.lazarus.timer.type.ScoreboardTimer;
import me.qiooip.lazarus.utils.LocationUtils;
import me.qiooip.lazarus.utils.StringUtils.FormatType;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.concurrent.ScheduledExecutorService;

public class HomeTimer extends PlayerTimer implements ScoreboardTimer {

    public HomeTimer(ScheduledExecutorService executor) {
        super(executor, "HomeTimer", 0);

        this.setExpiryMessage(Language.FACTION_PREFIX + Language.FACTIONS_HOME_TELEPORTED);
        this.setFormat(FormatType.MILLIS_TO_SECONDS);
        this.setLunarCooldownType(CooldownType.HOME);
    }

    public void activate(Player player, int delay, Location location) {
        super.activate(player, delay, () -> Tasks.sync(() -> LocationUtils.teleportWithChunkLoad(player, location)));
    }

    @Override
    public String getPlaceholder() {
        return Config.HOME_PLACEHOLDER;
    }

    @Override
    public String getScoreboardEntry(Player player) {
        return this.getTimeLeft(player) + "s";
    }
}
