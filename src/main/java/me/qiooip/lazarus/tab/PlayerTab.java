package me.qiooip.lazarus.tab;

import org.bukkit.entity.Player;

public interface PlayerTab {

    Player getPlayer();

    void unregister();

    void set(int index, String line);
}
