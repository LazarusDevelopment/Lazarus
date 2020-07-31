package me.qiooip.lazarus.games.koth.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.koth.KothData;
import me.qiooip.lazarus.games.koth.RunningKoth;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.StringUtils.FormatType;
import org.bukkit.command.CommandSender;

public class KothSetTimeCommand extends SubCommand {

    KothSetTimeCommand() {
        super("settime", "lazarus.koth.settime");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_SET_TIME_USAGE);
            return;
        }

        KothData koth = Lazarus.getInstance().getKothManager().getKoth(args[0]);

        if(koth == null) {
            sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_EXCEPTION_DOESNT_EXIST.replace("<koth>", args[0]));
            return;
        }

        RunningKoth runningKoth = Lazarus.getInstance().getKothManager().getRunningKoth(args[0]);

        if(runningKoth == null) {
            sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_EXCEPTION_NOT_RUNNING.replace("<koth>", koth.getName()));
            return;
        }

        int duration = StringUtils.parseSeconds(args[1]);

        if(duration == -1) {
            sender.sendMessage(Language.PREFIX + Language.COMMANDS_INVALID_DURATION);
            return;
        }

        runningKoth.changeCapTime(duration);

        Messages.sendMessage(Language.KOTH_PREFIX + Language.KOTH_SET_TIME_CHANGED
            .replace("<koth>", koth.getName())
            .replace("<time>", StringUtils.formatTime(duration, FormatType.SECONDS_TO_MINUTES)));
    }
}
