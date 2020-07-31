package me.qiooip.lazarus.games.conquest.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.Messages;
import org.bukkit.command.CommandSender;

public class ConquestStopCommand extends SubCommand {

    ConquestStopCommand() {
        super("stop", "lazarus.conquest.stop");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!Lazarus.getInstance().getConquestManager().isActive()) {
            sender.sendMessage(Language.CONQUEST_PREFIX + Language.CONQUEST_EXCEPTION_NOT_RUNNING);
            return;
        }

        Lazarus.getInstance().getConquestManager().stopConquest(false);

        Messages.sendMessage(Language.CONQUEST_PREFIX + Language
        .CONQUEST_STOP_STOPPED.replace("<player>", sender.getName()));
    }
}
