package me.qiooip.lazarus.games.koth.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.koth.KothData;
import me.qiooip.lazarus.games.koth.RunningKoth;
import me.qiooip.lazarus.utils.Messages;
import org.bukkit.command.CommandSender;

public class KothStopCommand extends SubCommand {

    KothStopCommand() {
        super("stop", "lazarus.koth.stop");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_STOP_USAGE);
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

        Lazarus.getInstance().getKothManager().stopKoth(runningKoth);

        Messages.sendMessage(Language.KOTH_PREFIX + Language.KOTH_STOP_STOPPED
            .replace("<player>", sender.getName()).replace("<koth>", koth.getName()));
    }
}
