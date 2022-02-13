package me.qiooip.lazarus.tab.module.impl;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.games.koth.RunningKoth;
import me.qiooip.lazarus.games.schedule.ScheduleManager;
import me.qiooip.lazarus.tab.PlayerTab;
import me.qiooip.lazarus.tab.module.TabModule;
import me.qiooip.lazarus.utils.StringUtils;

public class NextKothModule extends TabModule {

    public NextKothModule() {
        super("NEXT_KOTH_MODULE");
    }

    @Override
    public void apply(PlayerTab tab) {
        RunningKoth koth = Lazarus.getInstance().getKothManager().getFirstRunningKoth();

        if(koth != null) {
            tab.set(52, "&5&l" + koth.getKothData().getName());
            tab.set(53, "&7" + koth.getCapzone().getTimeLeft());
            tab.set(54, "&7" + StringUtils.getLocationName(koth.getCapzone().getCuboid().getCenter()));
            return;
        }

        ScheduleManager manager = Lazarus.getInstance().getScheduleManager();

        if(manager.getSchedules().isEmpty()) {
            tab.set(52, "&5&lKoTH");
            tab.set(53, "&7None scheduled");
        }
    }
}
