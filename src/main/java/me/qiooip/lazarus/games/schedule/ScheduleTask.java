package me.qiooip.lazarus.games.schedule;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.koth.KothData;
import me.qiooip.lazarus.games.koth.KothManager;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Getter
class ScheduleTask extends BukkitRunnable {

    private final ScheduleManager manager;
    private DayOfWeek day;
    private List<ScheduleData> daySchedules;

    ScheduleTask(ScheduleManager manager) {
        this.manager = manager;

        this.day = LocalDateTime.now(Config.TIMEZONE.toZoneId()).getDayOfWeek();
        this.daySchedules = manager.getSchedulesByDay(this.day);

        this.runTaskTimerAsynchronously(Lazarus.getInstance(), 0L, 20L);
    }

    @Override
    public void run() {
        LocalDateTime date = LocalDateTime.now(Config.TIMEZONE.toZoneId());
        if(date.getSecond() != 0) return;

        DayOfWeek day = date.getDayOfWeek();

        if(this.day != day) {
            this.day = day;
            this.daySchedules = Lazarus.getInstance().getScheduleManager().getSchedulesByDay(day);
        }

        this.daySchedules.forEach(schedule -> {
            LocalDateTime time = schedule.getTime();
            if(time.getHour() != date.getHour() || time.getMinute() != date.getMinute()) return;

            Tasks.sync(() -> this.startScheduledEvent(schedule.getName()));
        });
    }

    private void startScheduledEvent(String scheduleName) {
        Lazarus.getInstance().getScheduleManager().cacheNextEventSchedule();

        if(scheduleName.equalsIgnoreCase("Conquest")) {
            Lazarus.getInstance().getConquestManager().startConquest(Bukkit.getConsoleSender());
            return;
        }

        if(scheduleName.equalsIgnoreCase("DTC")) {
            Lazarus.getInstance().getDtcManager().startDtc(Bukkit.getConsoleSender(), Config.DTC_CORE_BREAKS);
            return;
        }

        if(scheduleName.equalsIgnoreCase("EnderDragon")) {
            Lazarus.getInstance().getEnderDragonManager().startEnderDragon(Bukkit.getConsoleSender());
            return;
        }

        this.startKoth(scheduleName);
    }

    private void startKoth(String scheduleName) {
        KothManager manager = Lazarus.getInstance().getKothManager();

        if(manager.isMaxRunningKothsReached()) {
            Lazarus.getInstance().log(Language.KOTH_PREFIX + Language.KOTH_EXCEPTION_MAX_RUNNING_KOTHS_AMOUNT_REACHED);
            return;
        }

        KothData koth = manager.getKoth(scheduleName);

        if(koth != null) {
            manager.startKoth(koth);
        } else {
            this.manager.removeScheduleByName(scheduleName);
        }
    }
}
