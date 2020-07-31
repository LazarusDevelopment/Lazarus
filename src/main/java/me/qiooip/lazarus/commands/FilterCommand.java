package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FilterCommand extends BaseCommand {

    public FilterCommand() {
        super("filter", "lazarus.filter", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(args.length == 0) {
            Language.FILTER_USAGE.forEach(sender::sendMessage);
            return;
        }

        if(args.length == 1) {
            switch(args[0].toLowerCase()) {
                case "list": {
                    Lazarus.getInstance().getFilterHandler().listFilter(player);
                    return;
                }
                case "toggle": {
                    Lazarus.getInstance().getFilterHandler().toggleFilter(player);
                    return;
                }
                case "clear": {
                    Lazarus.getInstance().getFilterHandler().clearFilter(player);
                    return;
                }
                default: {
                    Language.FILTER_USAGE.forEach(sender::sendMessage);
                    return;
                }
            }
        }

        switch(args[0].toLowerCase()) {
            case "add": {
                Lazarus.getInstance().getFilterHandler().addItem(player, args[1]);
                return;
            }
            case "remove": {
                Lazarus.getInstance().getFilterHandler().removeItem(player, args[1]);
                return;
            }
            default: Language.FILTER_USAGE.forEach(sender::sendMessage);
        }
    }
}
