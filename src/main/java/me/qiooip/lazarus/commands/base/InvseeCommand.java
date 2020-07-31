package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvseeCommand extends BaseCommand {

    public InvseeCommand() {
        super("invsee", "lazarus.invsee", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.PREFIX + Language.INVSEE_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        Player player = (Player) sender;
        Lazarus.getInstance().getInventoryHandler().openInvseeInventory(player, target);
    }
}
