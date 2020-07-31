package me.qiooip.lazarus.games.dragon.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.Messages;
import org.bukkit.command.CommandSender;

public class EnderDragonStopCommand extends SubCommand {

    EnderDragonStopCommand() {
        super("stop", "lazarus.enderdragon.stop");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!Lazarus.getInstance().getEnderDragonManager().isActive()) {
            sender.sendMessage(Language.ENDER_DRAGON_PREFIX + Language.ENDER_DRAGON_EXCEPTION_NOT_RUNNING);
            return;
        }

        Lazarus.getInstance().getEnderDragonManager().stopEnderDragon(true);
        Messages.sendMessage(Language.ENDER_DRAGON_PREFIX + Language.ENDER_DRAGON_STOP_STOPPED);
    }
}
