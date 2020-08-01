package me.qiooip.lazarus.timer.type;

import org.bukkit.entity.Player;

public interface ScoreboardTimer {

    default String getPlaceholder() { return ""; }

    default String getPlaceholder(Player player) { return ""; }

    default String getScoreboardEntry() { return ""; }

    default String getScoreboardEntry(Player player) { return ""; }
}
