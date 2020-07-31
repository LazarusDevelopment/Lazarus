package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpCommand extends SubCommand {

    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            Language.FACTIONS_HELP_PAGES.get(1).forEach(sender::sendMessage);
            return;
        }

        if(!this.checkNumber(sender, args[0])) return;

        List<String> help = Language.FACTIONS_HELP_PAGES.get(Integer.parseInt(args[0]));

        if(help == null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_HELP_PAGE_NOT_FOUND.replace("<page>", args[0]));
            return;
        }

        help.forEach(sender::sendMessage);
    }
}
