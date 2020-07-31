package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;

public class CoordsCommand extends BaseCommand {

    public CoordsCommand() {
        super("coords", "lazarus.coords");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Language.COORDS_MESSAGE.forEach(sender::sendMessage);
    }
}
