package me.qiooip.lazarus.tab.task;

import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.tab.PlayerTab;

public interface TabUpdater extends Runnable {

    void cancel();

    void initialSet(PlayerTab tab);

    void updateFactionPlayerList(PlayerTab tab, PlayerFaction faction);

    void clearFactionPlayerList(PlayerTab tab);
}
