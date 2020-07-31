package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CrowbarCommand extends BaseCommand {

    public CrowbarCommand() {
        super("crowbar", "lazarus.crowbar");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2 || !args[0].equalsIgnoreCase("give")) {
            sender.sendMessage(Language.PREFIX + Language.CROWBAR_COMMAND_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if(!this.checkPlayer(sender, target, args[1])) return;

        target.getInventory().addItem(Lazarus.getInstance().getCrowbarHandler().getNewCrowbar());
        target.updateInventory();

        sender.sendMessage(Language.PREFIX + Language.CROWBAR_GAVE.replace("<player>", target.getName()));
        target.sendMessage(Language.PREFIX + Language.CROWBAR_RECEIVED);
    }
}
