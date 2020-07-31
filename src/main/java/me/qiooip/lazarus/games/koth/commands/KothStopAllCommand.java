package me.qiooip.lazarus.games.koth.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.Messages;
import org.bukkit.command.CommandSender;

public class KothStopAllCommand extends SubCommand {

    KothStopAllCommand() {
        super("stopall", "lazarus.koth.stopall");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(Lazarus.getInstance().getKothManager().getRunningKoths().isEmpty()) {
            sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_STOP_ALL_NO_RUNNING_KOTHS);
            return;
        }

        Lazarus.getInstance().getKothManager().stopAllKoths();

        Messages.sendMessage(Language.KOTH_PREFIX + Language.KOTH_STOP_ALL_STOPPED_ALL
        .replace("<player>", sender.getName()));
    }
}
