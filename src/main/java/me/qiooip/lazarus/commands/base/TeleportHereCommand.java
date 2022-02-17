package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TeleportHereCommand extends BaseCommand {

    public TeleportHereCommand() {
        super("teleporthere", Arrays.asList("tphere", "s"), "lazarus.teleporthere", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(args.length == 0) {
            player.sendMessage(Language.PREFIX + Language.TELEPORT_HERE_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        if(!target.teleport(player)) return;

        target.sendMessage(Language.PREFIX + Language.TELEPORT_MESSAGE
            .replace("<player>", player.getName()));

        player.sendMessage(Language.PREFIX + Language.TELEPORT_HERE_MESSAGE
            .replace("<player>", target.getName()));
    }
}
