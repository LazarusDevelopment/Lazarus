package me.qiooip.lazarus.games.schedule.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.koth.KothData;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScheduleCreateCommand extends SubCommand {

    private final List<String> days;

    ScheduleCreateCommand() {
        super("create", Collections.singletonList("new"), "lazarus.schedule.create");

        this.days = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 3) {
            sender.sendMessage(Language.SCHEDULE_PREFIX + Language.SCHEDULE_CREATE_USAGE);
            return;
        }

        KothData koth = Lazarus.getInstance().getKothManager().getKoth(args[0]);

        if(!args[0].equalsIgnoreCase("Conquest") && !args[0].equalsIgnoreCase("DTC") && !args[0].equalsIgnoreCase("EnderDragon") && koth == null) {
            sender.sendMessage(Language.SCHEDULE_PREFIX + Language.SCHEDULE_CREATE_EVENT_DOESNT_EXIST.replace("<name>", args[0]));
            return;
        }

        String dayName = StringUtils.capitalize(args[1].toLowerCase());

        if(!dayName.equalsIgnoreCase("daily") && !dayName.equalsIgnoreCase("all") && !this.days.contains(dayName)) {
            sender.sendMessage(Language.SCHEDULE_PREFIX + Language.SCHEDULE_CREATE_INCORRECT_DAY_FORMAT);
            return;
        }

        if(!this.isTimeValid(args[2])) {
            sender.sendMessage(Language.SCHEDULE_PREFIX + Language.SCHEDULE_CREATE_INCORRECT_TIME_FORMAT);
            return;
        }

        if(dayName.equalsIgnoreCase("daily") || dayName.equalsIgnoreCase("all")) {
            this.days.forEach(day -> this.createSchedule(sender, args[0], day, args[2]));
        } else {
            this.createSchedule(sender, args[0], args[1], args[2]);
        }
    }

    private void createSchedule(CommandSender sender, String name, String dayName, String time) {
        DayOfWeek day = DayOfWeek.valueOf(dayName.toUpperCase());

        int id = Lazarus.getInstance().getScheduleManager()
            .createSchedule(name, day, this.getCorrectTime(time));

        sender.sendMessage(Language.SCHEDULE_PREFIX + Language
            .SCHEDULE_CREATE_CREATED.replace("<id>", String.valueOf(id)));
    }

    private String getCorrectTime(String time) {
        return time.contains(":") ? time : time + ":00";
    }

    private boolean isTimeValid(String value) {
        if(value.contains(":")) {
            String[] time = value.split(":");
            if(!StringUtils.isInteger(time[0]) || !StringUtils.isInteger(time[1])) return false;

            int hours = Integer.parseInt(time[0]);
            int minutes = Integer.parseInt(time[1]);

            return hours >= 0 && hours < 24 && minutes >= 0 && minutes < 60;
        } else {
            if(!StringUtils.isInteger(value)) return false;

            int hours = Integer.parseInt(value);
            return hours >= 0 && hours < 24;
        }
    }
}
