package me.qiooip.lazarus.tab.module.impl;

import lombok.AllArgsConstructor;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.games.koth.RunningKoth;
import me.qiooip.lazarus.games.schedule.NextKothSchedule;
import me.qiooip.lazarus.games.schedule.ScheduleManager;
import me.qiooip.lazarus.tab.PlayerTab;
import me.qiooip.lazarus.tab.module.TabModule;
import me.qiooip.lazarus.utils.ServerUtils;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class NextKothModule extends TabModule {

    private List<Supplier<String>> noneScheduled;
    private List<NextKothApplyFunction<NextKothSchedule>> scheduled;
    private List<NextKothApplyFunction<RunningKoth>> runningKoth;

    private int oldLinesCount;

    public NextKothModule() {
        super("NEXT_KOTH_MODULE");
    }

    @Override
    public void loadAdditionalData(ConfigurationSection section) {
        this.noneScheduled = this.loadNoneScheduledFunctions(section);
        this.scheduled = this.loadScheduledFunctions(section);
        this.runningKoth = this.loadRunningKothFunctions(section);
    }

    private List<Supplier<String>> loadNoneScheduledFunctions(ConfigurationSection section) {
        List<String> lines = section.getStringList("NONE_SCHEDULED");
        List<Supplier<String>> functions = new ArrayList<>();

        for(String line : lines) {
            functions.add(() -> line);
        }

        return functions;
    }

    private List<NextKothApplyFunction<NextKothSchedule>> loadScheduledFunctions(ConfigurationSection section) {
        List<String> lines = section.getStringList("SCHEDULED");
        List<NextKothApplyFunction<NextKothSchedule>> functions = new ArrayList<>();

        for(String line : lines) {
            if(line.contains("<startTime>")) {
                functions.add(new NextKothApplyFunction<>(
                    schedule -> StringUtils.formatMillis(schedule.getStartTime() - System.currentTimeMillis()),
                    ServerUtils.parsePlaceholderFunction(line, "<startTime>"))
                );
            } else if(line.contains("<kothName>")) {
                functions.add(new NextKothApplyFunction<>(
                    schedule -> schedule.getKothName(),
                    ServerUtils.parsePlaceholderFunction(line, "<kothName>"))
                );
            } else {
                functions.add(new NextKothApplyFunction<>(s -> "", s -> line));
            }
        }

        return functions;
    }

    private List<NextKothApplyFunction<RunningKoth>> loadRunningKothFunctions(ConfigurationSection section) {
        List<String> lines = section.getStringList("RUNNING_KOTH");
        List<NextKothApplyFunction<RunningKoth>> functions = new ArrayList<>();

        for(String line : lines) {
            if(line.contains("<capTime>")) {
                functions.add(new NextKothApplyFunction<>(
                    koth -> koth.getCapzone().getTimeLeft(),
                    ServerUtils.parsePlaceholderFunction(line, "<capTime>"))
                );
            } else if(line.contains("<location>")) {
                functions.add(new NextKothApplyFunction<>(
                    koth -> StringUtils.getLocationName(koth.getCapzone().getCuboid().getCenter()),
                    ServerUtils.parsePlaceholderFunction(line, "<location>"))
                );
            } else if(line.contains("<kothName>")) {
                functions.add(new NextKothApplyFunction<>(
                    koth -> koth.getKothData().getName(),
                    ServerUtils.parsePlaceholderFunction(line, "<kothName>"))
                );
            } else {
                functions.add(new NextKothApplyFunction<>(s -> "", s -> line));
            }
        }

        return functions;
    }

    @Override
    public void apply(PlayerTab tab) {
        int linesCount = 0;
        RunningKoth koth = Lazarus.getInstance().getKothManager().getFirstRunningKoth();

        if(koth != null) {
            for(int i = 0; i < this.runningKoth.size(); i++) {
                tab.set(this.startSlot + i, this.runningKoth.get(i).applyLine(koth));
                linesCount++;
            }
        } else {
            ScheduleManager manager = Lazarus.getInstance().getScheduleManager();
            NextKothSchedule nextKoth = manager.getNextKothSchedule();

            if(nextKoth == null) {
                for(int i = 0; i < this.noneScheduled.size(); i++) {
                    tab.set(this.startSlot + i, this.noneScheduled.get(i).get());
                    linesCount++;
                }
            } else {
                for(int i = 0; i < this.scheduled.size(); i++) {
                    tab.set(this.startSlot + i, this.scheduled.get(i).applyLine(nextKoth));
                    linesCount++;
                }
            }
        }

        if(linesCount < this.oldLinesCount) {
            for(int i = 0; i < this.oldLinesCount - linesCount; i++) {
                tab.set(this.startSlot + linesCount + i, "");
            }
        }

        this.oldLinesCount = linesCount;
    }

    @AllArgsConstructor
    private static class NextKothApplyFunction<T> {

        private final Function<T, String> parameterSupplier;
        private final Function<String, String> lineFunction;

        public String applyLine(T parameter) {
            return this.lineFunction.apply(this.parameterSupplier.apply(parameter));
        }
    }
}
