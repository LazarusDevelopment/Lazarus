package me.qiooip.lazarus.tab.task;

import me.qiooip.lazarus.tab.PlayerTab;
import me.qiooip.lazarus.tab.module.TabModule;

public interface TabUpdater extends Runnable {

    void cancel();

    void initialSet(PlayerTab tab);

    void registerTabModule(TabModule module);
}
