package me.qiooip.lazarus.games.king.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.Messages;
import org.bukkit.command.CommandSender;

public class KillTheKingStopCommand extends SubCommand {

    KillTheKingStopCommand() {
        super("stop", "lazarus.killtheking.stop");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!Lazarus.getInstance().getKillTheKingManager().isActive()) {
            sender.sendMessage(Language.KING_PREFIX + Language.KING_EXCEPTION_NOT_RUNNING);
            return;
        }

        Lazarus.getInstance().getKillTheKingManager().stopKillTheKing(false);
        Messages.sendMessage(Language.KING_PREFIX + Language.KING_STOP_STOPPED.replace("<player>", sender.getName()));
    }
}
