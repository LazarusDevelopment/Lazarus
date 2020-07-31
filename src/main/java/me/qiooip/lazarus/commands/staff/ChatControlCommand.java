package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;

public class ChatControlCommand extends BaseCommand {

    public ChatControlCommand() {
        super("chat", "lazarus.chatcontrol");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            Language.CHAT_USAGE.forEach(sender::sendMessage);
            return;
        }

        if(args.length == 1) {
            switch(args[0].toLowerCase()) {
                case "mute": {
                    Lazarus.getInstance().getChatControlHandler().toggleChat(sender);
                    return;
                }
                case "clear": {
                    Lazarus.getInstance().getChatControlHandler().clearChat(sender);
                    return;
                }
                default: {
                    Language.CHAT_USAGE.forEach(sender::sendMessage);
                    return;
                }
            }
        }

        switch(args[0].toLowerCase()) {
            case "delay": {
                Lazarus.getInstance().getChatControlHandler().setDelay(sender, args[1]);
                return;
            }
            case "toggle": {
                if(args[1].equalsIgnoreCase("foundore")) {
                    Lazarus.getInstance().getChatControlHandler().toggleFoundOreMessages(sender);
                    return;
                }

                Language.CHAT_USAGE.forEach(sender::sendMessage);
                return;
            }
            default: Language.CHAT_USAGE.forEach(sender::sendMessage);
        }
    }
}
