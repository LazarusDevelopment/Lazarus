package me.qiooip.lazarus.games.koth.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.koth.KothData;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;

public class KothStartingTimeCommand extends SubCommand {

    KothStartingTimeCommand() {
        super("startingtime", "lazarus.koth.startingtime");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_STARTING_TIME_USAGE);
            return;
        }

        KothData koth = Lazarus.getInstance().getKothManager().getKoth(args[0]);

        if(koth == null) {
            sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_EXCEPTION_DOESNT_EXIST.replace("<koth>", args[0]));
            return;
        }

        int duration = StringUtils.parseSeconds(args[1]);

        if(duration == -1) {
            sender.sendMessage(Language.PREFIX + Language.COMMANDS_INVALID_DURATION);
            return;
        }

        koth.setCaptime(duration);

        sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_STARTING_TIME_CHANGED
            .replace("<koth>", koth.getName())
            .replace("<time>", StringUtils.formatTime(duration, StringUtils.FormatType.SECONDS_TO_MINUTES)));
    }
}