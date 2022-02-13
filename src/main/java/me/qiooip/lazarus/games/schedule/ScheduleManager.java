package me.qiooip.lazarus.games.schedule;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.schedule.event.ScheduleClearEvent;
import me.qiooip.lazarus.games.schedule.event.ScheduleCreateEvent;
import me.qiooip.lazarus.games.schedule.event.ScheduleDeleteEvent;
import me.qiooip.lazarus.utils.FileUtils;
import me.qiooip.lazarus.utils.GsonUtils;
import me.qiooip.lazarus.utils.ManagerEnabler;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ScheduleManager implements ManagerEnabler, Listener {

    private final File scheduleFile;

    private List<ScheduleData> schedules;
    private final Set<DayOfWeek> days;
    private final DateTimeFormatter timeFormatter;

    private ScheduleTask scheduleTask;
    private NextEventSchedule nextEventSchedule;

    public ScheduleManager() {
        this.scheduleFile = FileUtils.getOrCreateFile(Config.GAMES_DIR, "schedule.json");

        this.schedules = new ArrayList<>();
        this.days = EnumSet.allOf(DayOfWeek.class);
        this.timeFormatter = DateTimeFormatter.ofPattern("EEEE, " + Config.DATE_FORMAT, Locale.ENGLISH);

        this.loadSchedules();
        this.cacheNextEventSchedule();

        if(!this.schedules.isEmpty()) {
            this.startScheduleTask();
        }

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.saveSchedules();

        this.schedules.clear();
        this.days.clear();

        if(this.scheduleTask != null) this.cancelScheduleTask();
    }

    private void loadSchedules() {
        String content = FileUtils.readWholeFile(this.scheduleFile);

        if(content == null) {
            this.schedules = new ArrayList<>();
            return;
        }

        this.schedules = Lazarus.getInstance().getGson().fromJson(content, GsonUtils.SCHEDULE_TYPE);
    }

    private void saveSchedules() {
        if(this.schedules == null) return;

        FileUtils.writeString(this.scheduleFile, Lazarus.getInstance().getGson()
            .toJson(this.schedules, GsonUtils.SCHEDULE_TYPE));
    }

    private void startScheduleTask() {
        this.scheduleTask = new ScheduleTask(this);
    }

    private void cancelScheduleTask() {
        this.scheduleTask.cancel();
        this.scheduleTask = null;
    }

    public void cacheNextEventSchedule() {
        LocalDateTime current = this.calculateScheduleOffsets();
        ScheduleData nextEventSchedule = this.schedules.isEmpty() ? null : this.schedules.get(0);

        if(nextEventSchedule == null) {
            this.nextEventSchedule = null;
            return;
        }

        long untilNextEvent = current.until(nextEventSchedule.getTime(), ChronoUnit.MILLIS);

        this.nextEventSchedule = new NextEventSchedule(
            nextEventSchedule.getName(),
            untilNextEvent + System.currentTimeMillis()
        );
    }

    public int createSchedule(String name, DayOfWeek day, String time) {
        int id = this.schedules.size() + 1;

        ScheduleData schedule = new ScheduleData();
        schedule.setId(id);
        schedule.setName(name);
        schedule.setTimeString(time);

        LocalDateTime current = LocalDateTime.now(Config.TIMEZONE.toZoneId());
        String[] timeArray = time.split(":");

        int dayOfMonth = current.getDayOfMonth() + (day.getValue() - current.getDayOfWeek().getValue());

        dayOfMonth = dayOfMonth < 1 ? dayOfMonth + 7 : dayOfMonth > current.getMonth()
            .length(Year.isLeap(current.getYear())) ? dayOfMonth - 7 : dayOfMonth;

        int hours = Integer.parseInt(timeArray[0]);
        int minutes = Integer.parseInt(timeArray[1]);

        schedule.setTime(LocalDateTime.of(current.getYear(), current.getMonth(), dayOfMonth, hours, minutes));

        if(this.scheduleTask == null) {
            this.startScheduleTask();
        }

        this.schedules.add(schedule);
        this.schedules.sort(Comparator.comparing(ScheduleData::getTime));

        if(this.scheduleTask.getDay() == day) {
            this.scheduleTask.getDaySchedules().add(schedule);
        }

        new ScheduleCreateEvent(schedule);
        return id;
    }

    public void removeSchedule(ScheduleData schedule, boolean save) {
        this.schedules.remove(schedule);
        this.schedules.stream().filter(s -> s.getId() > schedule.getId()).forEach(s -> s.setId(s.getId() - 1));

        if(this.scheduleTask.getDay() == schedule.getTime().getDayOfWeek()) {
            this.scheduleTask.getDaySchedules().remove(schedule);
        }

        if(save) {
            this.saveSchedules();
        }

        if(this.schedules.isEmpty()) {
            this.cancelScheduleTask();
        }

        new ScheduleDeleteEvent(schedule);
    }

    public void removeScheduleByName(String name) {
        List<ScheduleData> schedules = new ArrayList<>(this.schedules);

        for(ScheduleData schedule : schedules) {
            if(schedule.getName().equals(name)) {
                this.removeSchedule(schedule, false);
            }
        }

        this.saveSchedules();
    }

    public ScheduleData getScheduleById(int id) {
        return this.schedules.stream().filter(schedule -> schedule.getId() == id).findFirst().orElse(null);
    }

    public List<ScheduleData> getSchedulesByDay(DayOfWeek day) {
        return this.schedules.stream().filter(schedule -> schedule.getTime().getDayOfWeek() == day).collect(Collectors.toList());
    }

    private LocalDateTime calculateScheduleOffsets() {
        LocalDateTime current = LocalDateTime.now(Config.TIMEZONE.toZoneId());

        for(ScheduleData schedule : this.schedules) {
            while(schedule.getTime().isBefore(current)) {
                schedule.setTime(schedule.getTime().plusDays(7));
            }
        }

        this.schedules.sort(Comparator.comparing(ScheduleData::getTime));
        return current;
    }

    public void clearSchedule(CommandSender sender) {
        if(this.schedules.isEmpty()) {
            sender.sendMessage(Language.SCHEDULE_PREFIX + Language.SCHEDULE_CLEAR_NO_SCHEDULES);
            return;
        }

        this.schedules.clear();
        this.saveSchedules();

        this.scheduleTask.cancel();
        this.scheduleTask = null;

        new ScheduleClearEvent();

        Messages.sendMessage(Language.SCHEDULE_PREFIX + Language.SCHEDULE_CLEAR_CLEARED
            .replace("<player>", sender.getName()), "lazarus.staff");
    }

    public void listNextSchedules(CommandSender sender) {
        if(this.schedules.isEmpty()) {
            sender.sendMessage(Language.SCHEDULE_PREFIX + Language.SCHEDULE_LIST_NO_SCHEDULES);
            return;
        }

        LocalDateTime current = this.calculateScheduleOffsets();

        sender.sendMessage(Language.SCHEDULE_COMMAND_HEADER);

        for(int i = 0; i < Math.min(Config.SCHEDULE_LIST_EVENT_AMOUNT, this.schedules.size()); i++) {
            ScheduleData schedule = this.schedules.get(i);

            sender.sendMessage(Language.SCHEDULE_UPCOMING_EVENTS_FORMAT
                .replace("<id>", String.valueOf(i + 1))
                .replace("<event>", schedule.getName())
                .replace("<time>", StringUtils.formatMillis(current.until(schedule.getTime(), ChronoUnit.MILLIS))));
        }

        sender.sendMessage(Language.SCHEDULE_COMMAND_FOOTER);
    }

    public void listAllSchedules(CommandSender sender) {
        if(this.schedules.isEmpty()) {
            sender.sendMessage(Language.SCHEDULE_PREFIX + Language.SCHEDULE_LIST_NO_SCHEDULES);
            return;
        }

        sender.sendMessage(Language.SCHEDULE_COMMAND_HEADER);
        sender.sendMessage(Language.SCHEDULE_LIST_TITLE);

        sender.sendMessage("");

        sender.sendMessage(Language.SCHEDULE_LIST_CURRENT_TIME + LocalDateTime
            .now(Config.TIMEZONE.toZoneId()).format(this.timeFormatter));

        sender.sendMessage("");

        String message = sender.hasPermission("lazarus.schedule.admin")
            ? Language.SCHEDULE_LIST_ADMIN_FORMAT : Language.SCHEDULE_LIST_PLAYER_FORMAT;

        this.days.forEach(day -> {
            List<ScheduleData> schedules = this.schedules.stream().filter(schedule -> schedule
                .getTime().getDayOfWeek() == day).collect(Collectors.toList());

            if(schedules.isEmpty()) return;

            sender.sendMessage(Language.SCHEDULE_LIST_DAY_FORMAT
                .replace("<day>", day.getDisplayName(TextStyle.FULL, Locale.ENGLISH)));

            schedules.forEach(schedule -> sender.sendMessage(message
                .replace("<id>", String.valueOf(schedule.getId()))
                .replace("<event>", schedule.getName())
                .replace("<time>", schedule.getTimeString())));

            sender.sendMessage("");
        });

        sender.sendMessage(Language.SCHEDULE_COMMAND_FOOTER);
    }

    @EventHandler
    public void onScheduleCreate(ScheduleCreateEvent event) {
        this.cacheNextEventSchedule();
    }

    @EventHandler
    public void onScheduleDelete(ScheduleDeleteEvent event) {
        this.cacheNextEventSchedule();
    }

    @EventHandler
    public void onScheduleClear(ScheduleClearEvent event) {
        this.cacheNextEventSchedule();
    }
}
