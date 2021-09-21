package me.qiooip.lazarus.games.schedule.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.commands.manager.SubCommandExecutor;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Arrays;

public class ScheduleCommandExecutor extends SubCommandExecutor {

    public ScheduleCommandExecutor() {
        super("schedule", null);

        this.addSubCommand(new ScheduleClearCommand());
        this.addSubCommand(new ScheduleCreateCommand());
        this.addSubCommand(new ScheduleDeleteCommand());
        this.addSubCommand(new ScheduleListCommand());
        this.addSubCommand(new ScheduleNextCommand());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(args.length == 0) {
            if(sender.hasPermission("lazarus.schedule.admin")) {
                Language.SCHEDULE_COMMAND_USAGE_ADMIN.forEach(sender::sendMessage);
            } else {
                Tasks.async(() -> Lazarus.getInstance().getScheduleManager().listNextSchedules(sender));
            }
            return true;
        }

        SubCommand sub = this.getSubCommand(args[0]);

        if(sub == null) {
            sender.sendMessage(Language.SCHEDULE_PREFIX + Language.COMMANDS_COMMAND_NOT_FOUND
                .replace("<command>", args[0]));
            return true;
        }

        if(sub.isForPlayersOnly() && sender instanceof ConsoleCommandSender) {
            sender.sendMessage(Language.SCHEDULE_PREFIX + Language.COMMANDS_FOR_PLAYERS_ONLY);
            return true;
        }

        if(sub.getPermission() != null && !sender.hasPermission(sub.getPermission())) {
            sender.sendMessage(Language.SCHEDULE_PREFIX + Language.COMMANDS_NO_PERMISSION);
            return true;
        }

        if(sub.isExecuteAsync()) {
            Tasks.async(() -> sub.execute(sender, Arrays.copyOfRange(args, 1, args.length)));
        } else {
            sub.execute(sender, Arrays.copyOfRange(args, 1, args.length));
        }
        return true;
    }
}
