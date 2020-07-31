package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.userdata.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IgnoreCommand extends BaseCommand {

    public IgnoreCommand() {
        super("ignore", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.PREFIX + Language.IGNORE_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        Player player = (Player) sender;

        if(player == target) {
            player.sendMessage(Language.PREFIX + Language.IGNORE_CANNOT_IGNORE_SELF);
            return;
        }

        Userdata data = Lazarus.getInstance().getUserdataManager().getUserdata(player);

        if(data.isIgnoring(target)) {
            data.removeIgnoring(target);
            player.sendMessage(Language.PREFIX + Language.IGNORE_DISABLED.replace("<player>", target.getName()));
        } else {
            data.addIgnoring(target);
            player.sendMessage(Language.PREFIX + Language.IGNORE_ENABLED.replace("<player>", target.getName()));
        }
    }
}
