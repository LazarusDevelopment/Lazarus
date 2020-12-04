package me.qiooip.lazarus.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collections;

public interface PlayerScoreboard  {

    void unregister();

    void clear();
    void update();
    void add(String value, String time);
    void addLine(ChatColor color);
    void addEmptyLine(ChatColor color);
    void addConquest(String prefix, String value, String suffix);
    void addLinesAndFooter();

    boolean isEmpty();
    void setUpdate(boolean value);

    void updateTabRelations(Iterable<? extends Player> players, boolean lunarOnly);

    default void updateTabRelations(Iterable<? extends Player> players) {
        this.updateTabRelations(players, false);
    }

    default void updateRelation(Player player) {
        this.updateTabRelations(Collections.singletonList(player), false);
    }

    default void updateRelation(Player player, boolean lunarOnly) {
        this.updateTabRelations(Collections.singletonList(player), lunarOnly);
    }
}
