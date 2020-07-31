package me.qiooip.lazarus.games.schedule.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import org.bukkit.command.CommandSender;

public class ScheduleListCommand extends SubCommand {

    ScheduleListCommand() {
        super("list");

        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Lazarus.getInstance().getScheduleManager().listAllSchedules(sender);
    }
}
