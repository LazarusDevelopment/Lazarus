package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class ReplyCommand extends BaseCommand {

    public ReplyCommand() {
        super("reply", Collections.singletonList("r"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.PREFIX + Language.REPLY_USAGE);
            return;
        }

        Player player = (Player) sender;

        Lazarus.getInstance().getMessagingHandler().sendReplyMessage(player, StringUtils.joinArray(args, " ", 1));
    }
}
