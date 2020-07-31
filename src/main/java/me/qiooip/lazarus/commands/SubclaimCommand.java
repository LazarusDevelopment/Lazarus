package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;

public class SubclaimCommand extends BaseCommand {

    public SubclaimCommand() {
        super("subclaim", "lazarus.subclaim");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Language.SUBCLAIM_MESSAGE.forEach(sender::sendMessage);
    }
}
