package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class HelpCommand extends BaseCommand {

    public HelpCommand() {
        super("help", Collections.singletonList("?"), "lazarus.help");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Language.HELP_MESSAGE.forEach(sender::sendMessage);
    }
}
