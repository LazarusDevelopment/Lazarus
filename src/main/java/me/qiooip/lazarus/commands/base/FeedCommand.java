package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class FeedCommand extends BaseCommand {

    public FeedCommand() {
        super("feed", Collections.singletonList("eat"), "lazarus.feed");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            if(!this.checkConsoleSender(sender)) return;

            Player player = (Player) sender;
            player.setFoodLevel(20);
            player.sendMessage(Language.PREFIX + Language.FEED_MESSAGE_SELF);
            return;
        }

        if(!this.checkPermission(sender, "lazarus.feed.others")) return;

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        target.setFoodLevel(20);

        target.sendMessage(Language.PREFIX + Language.FEED_MESSAGE_SELF);
        sender.sendMessage(Language.PREFIX + Language.FEED_MESSAGE_OTHERS.replace("<player>", target.getName()));
    }
}
