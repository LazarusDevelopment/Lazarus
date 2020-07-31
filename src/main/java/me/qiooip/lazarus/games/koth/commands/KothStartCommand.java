package me.qiooip.lazarus.games.koth.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.koth.KothData;
import me.qiooip.lazarus.games.koth.RunningKoth;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;

public class KothStartCommand extends SubCommand {

    KothStartCommand() {
        super("start", "lazarus.koth.start");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_START_USAGE);
            return;
        }

        if(Lazarus.getInstance().getKothManager().isMaxRunningKothsReached()) {
            sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_EXCEPTION_MAX_RUNNING_KOTHS_AMOUNT_REACHED);
            return;
        }

        KothData koth = Lazarus.getInstance().getKothManager().getKoth(args[0]);

        if(koth == null) {
            sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_EXCEPTION_DOESNT_EXIST.replace("<koth>", args[0]));
            return;
        }

        RunningKoth runningKoth = Lazarus.getInstance().getKothManager().getRunningKoth(args[0]);

        if(runningKoth != null) {
            sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_START_ALREADY_RUNNING.replace("<koth>", koth.getName()));
            return;
        }

        int time = args.length == 1 ? koth.getCaptime() : StringUtils.parseSeconds(args[1]);

        if(time == -1) {
            sender.sendMessage(Language.PREFIX + Language.COMMANDS_INVALID_DURATION);
            return;
        }

        Lazarus.getInstance().getKothManager().startKoth(koth, time);
    }
}
