package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.userdata.Userdata;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class ToggleChatCommand extends BaseCommand {

    public ToggleChatCommand() {
        super("togglechat", Collections.singletonList("tchat"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        Userdata data = Lazarus.getInstance().getUserdataManager().getUserdata(player);
        data.getSettings().setPublicChat(!data.getSettings().isPublicChat());

        player.sendMessage(Language.PREFIX + (data.getSettings().isPublicChat() ?
            Language.TOGGLE_PUBLIC_CHAT_TOGGLED_ON : Language.TOGGLE_PUBLIC_CHAT_TOGGLED_OFF));
    }
}
