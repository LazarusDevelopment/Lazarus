package me.qiooip.lazarus.games.schedule.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.schedule.ScheduleData;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class ScheduleDeleteCommand extends SubCommand {

    ScheduleDeleteCommand() {
        super("delete", Collections.singletonList("remove"), "lazarus.schedule.delete");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.SCHEDULE_PREFIX + Language.SCHEDULE_DELETE_USAGE);
            return;
        }

        if(!this.checkNumber(sender, args[0])) return;

        int scheduleId = Integer.parseInt(args[0]);
        ScheduleData schedule = Lazarus.getInstance().getScheduleManager().getScheduleById(scheduleId);

        if(schedule == null) {
            sender.sendMessage(Language.SCHEDULE_PREFIX + Language.SCHEDULE_DELETE_DOESNT_EXIST.replace("<id>", args[0]));
            return;
        }

        Lazarus.getInstance().getScheduleManager().removeSchedule(schedule, true);

        sender.sendMessage(Language.SCHEDULE_PREFIX + Language.SCHEDULE_DELETE_DELETED
            .replace("<id>", String.valueOf(schedule.getId())));
    }
}
