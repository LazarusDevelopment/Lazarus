package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MessageCommand extends BaseCommand {

    public MessageCommand() {
        super("message", Arrays.asList("msg", "m", "tell"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length <= 1) {
            sender.sendMessage(Language.PREFIX + Language.MESSAGE_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        Player player = (Player) sender;

        if(!player.hasPermission("lazarus.staff") && Lazarus.getInstance().getVanishManager().isVanished(target)) {
            player.sendMessage(Language.PREFIX + Language.COMMANDS_PLAYER_NOT_ONLINE.replace("<player>", target.getName()));
            return;
        }

        Lazarus.getInstance().getMessagingHandler().sendMessage(player, target, StringUtils.joinArray(args, " ", 2));
    }
}
