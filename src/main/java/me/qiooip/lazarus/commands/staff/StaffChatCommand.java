package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class StaffChatCommand extends BaseCommand {

    public StaffChatCommand() {
        super("staffchat", Arrays.asList("sc", "staffc", "schat"), "lazarus.staffchat", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(args.length == 0) {
            Lazarus.getInstance().getStaffChatHandler().toggleStaffChat(player);
            return;
        }

        String message = StringUtils.joinArray(args, " ", 1);

        Messages.sendMessage(Language.STAFF_CHAT_FORMAT
            .replace("<player>", player.getName())
            .replace("<message>", message), "lazarus.staffchat");
    }
}
