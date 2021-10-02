package me.qiooip.lazarus.timer.scoreboard;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.scoreboard.ScoreboardManager;
import me.qiooip.lazarus.timer.type.ScoreboardTimer;
import me.qiooip.lazarus.timer.type.SystemTimer;
import me.qiooip.lazarus.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.ScheduledExecutorService;

public class SotwTimer extends SystemTimer implements ScoreboardTimer {

    public SotwTimer(ScheduledExecutorService executor) {
        super(executor, "SotwTimer", Config.SOTW_DEFAULT_TIME * 60, true);
    }

    @Override
    public void activate(int delay) {
        super.activate(delay, () -> {
            Lazarus.getInstance().getSotwHandler().showSotwInvisiblePlayers();
            ScoreboardManager.getInstance().updateTabRelations(Bukkit.getOnlinePlayers(), false);

            Messages.sendMessage(Language.SOTW_ENDED);
        });
    }

    @Override
    public String getPlaceholder(Player player) {
        if(Lazarus.getInstance().getSotwHandler().isPlayerSotwEnabled(player)) {
            return Config.SOTW_ENABLE_PLACEHOLDER;
        } else {
            return Config.SOTW_PLACEHOLDER;
        }
    }

    @Override
    public String getScoreboardEntry(Player player) {
        return this.getDynamicTimeLeft();
    }
}
