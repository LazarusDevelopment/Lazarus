package me.qiooip.lazarus.games.schedule.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import org.bukkit.command.CommandSender;

public class ScheduleClearCommand extends SubCommand {

    ScheduleClearCommand() {
        super("clear", "lazarus.schedule.clear");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Lazarus.getInstance().getScheduleManager().clearSchedule(sender);
    }
}
