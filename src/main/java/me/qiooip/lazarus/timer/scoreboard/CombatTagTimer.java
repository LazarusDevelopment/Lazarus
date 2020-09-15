package me.qiooip.lazarus.timer.scoreboard;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.glass.GlassManager.GlassType;
import me.qiooip.lazarus.lunarclient.cooldown.CooldownType;
import me.qiooip.lazarus.timer.type.PlayerTimer;
import me.qiooip.lazarus.timer.type.ScoreboardTimer;
import me.qiooip.lazarus.utils.StringUtils.FormatType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;

public class CombatTagTimer extends PlayerTimer implements ScoreboardTimer {

    public CombatTagTimer(ScheduledExecutorService executor) {
        super(executor, "CombatTagTimer", Config.COMBAT_TAG_DURATION);

        this.setExpiryMessage(Language.PREFIX + Language.COMBAT_TAG_EXPIRED);
        this.setFormat(FormatType.MILLIS_TO_MINUTES);
        this.setLunarCooldownType(CooldownType.COMBAT_TAG);
    }

    @Override
    public void activate(Player player) {
        this.activate(player, this.delay);
    }

    @Override
    public void activate(Player player, int delay) {
        this.activate(player.getUniqueId(), delay);
    }

    @Override
    public void activate(UUID uuid, int delay) {
        super.cancel(uuid);

        super.activate(uuid, delay, () -> {
            Player player = Bukkit.getPlayer(uuid);
            if(player == null) return;

            Lazarus.getInstance().getGlassManager().clearGlassVisuals(player, GlassType.SPAWN_WALL);
        });
    }

    @Override
    public void cancel(Player player) {
        super.cancel(player);
        Lazarus.getInstance().getGlassManager().clearGlassVisuals(player, GlassType.SPAWN_WALL);
    }

    @Override
    public String getPlaceholder() {
        return Config.COMBAT_TAG_PLACEHOLDER;
    }

    @Override
    public String getScoreboardEntry(Player player) {
        return this.getTimeLeft(player);
    }
}
