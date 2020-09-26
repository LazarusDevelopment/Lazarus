package me.qiooip.lazarus.timer.scoreboard;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.lunarclient.cooldown.CooldownType;
import me.qiooip.lazarus.timer.type.PlayerTimer;
import me.qiooip.lazarus.timer.type.ScoreboardTimer;
import me.qiooip.lazarus.utils.LocationUtils;
import me.qiooip.lazarus.utils.StringUtils.FormatType;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.concurrent.ScheduledExecutorService;

public class StuckTimer extends PlayerTimer implements ScoreboardTimer {

    public StuckTimer(ScheduledExecutorService executor) {
        super(executor, "StuckTimer", Config.FACTION_STUCK_WARMUP);

        this.setExpiryMessage(Language.FACTION_PREFIX + Language.FACTIONS_STUCK_TELEPORTED);
        this.setFormat(FormatType.MILLIS_TO_MINUTES);
        this.setLunarCooldownType(CooldownType.STUCK);
    }

    public void activate(Player player, Location location) {
        super.activate(player, () -> Tasks.sync(() -> LocationUtils.teleportWithChunkLoad(player, location)));
    }

    @Override
    public void cancel(Player player) {
        super.cancel(player);
        FactionsManager.getInstance().removeStuckInitialLocation(player);
    }

    @Override
    public String getPlaceholder() {
        return Config.STUCK_PLACEHOLDER;
    }

    @Override
    public String getScoreboardEntry(Player player) {
        return this.getTimeLeft(player);
    }
}
