package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KillCommand extends BaseCommand {

    public KillCommand() {
        super("kill", "lazarus.kill");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.PREFIX + Language.KILL_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        target.setHealth(0);

        target.sendMessage(Language.PREFIX + Language.KILL_MESSAGE_SELF
            .replace("<player>", sender.getName()));

        sender.sendMessage(Language.PREFIX + Language.KILL_MESSAGE_OTHERS
            .replace("<player>", target.getName()));
    }
}
