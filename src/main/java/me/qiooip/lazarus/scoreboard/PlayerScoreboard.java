package me.qiooip.lazarus.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public interface PlayerScoreboard  {

    void unregister();

    void clear();
    void update();
    void add(String value, String time);
    void addLine(ChatColor color);
    void addConquest(String prefix, String value, String suffix);
    void addLinesAndFooter();

    boolean isEmpty();
    void setUpdate(boolean value);

    void updateTabRelations(Iterable<? extends Player> players);
    void updateRelation(Player player);
}
