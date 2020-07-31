package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class BroadcastCommand extends BaseCommand {

    public BroadcastCommand() {
        super("broadcast", Collections.singletonList("bc"), "lazarus.broadcast");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.PREFIX + Language.BROADCAST_USAGE);
            return;
        }

        String message = Color.translate(StringUtils.joinArray(args, " ", 1));
        Messages.sendMessage(Language.BROADCAST_PREFIX + message);
    }
}
