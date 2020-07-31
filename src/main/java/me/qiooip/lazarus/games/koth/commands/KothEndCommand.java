package me.qiooip.lazarus.games.koth.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.koth.KothData;
import me.qiooip.lazarus.games.koth.RunningKoth;
import me.qiooip.lazarus.utils.Messages;
import org.bukkit.command.CommandSender;

public class KothEndCommand extends SubCommand {

    KothEndCommand() {
        super("end", "lazarus.koth.end");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_END_USAGE);
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

        runningKoth.getCapzone().setTime(0);
        Messages.sendMessage(Language.KOTH_PREFIX + Language.KOTH_END_ENDED.replace("<koth>", koth.getName()));
    }
}
