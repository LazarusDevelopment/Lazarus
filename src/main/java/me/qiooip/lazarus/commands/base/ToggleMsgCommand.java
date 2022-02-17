package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.userdata.Userdata;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ToggleMsgCommand extends BaseCommand {

    public ToggleMsgCommand() {
        super("togglemessages", Arrays.asList("togglemsg", "tmsg", "togglepm", "tpm"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        Userdata data = Lazarus.getInstance().getUserdataManager().getUserdata(player);
        data.getSettings().setMessages(!data.getSettings().isMessages());

        player.sendMessage(Language.PREFIX + (data.getSettings().isMessages()
            ? Language.TOGGLE_MESSAGES_TOGGLED_ON : Language.TOGGLE_MESSAGES_TOGGLED_OFF));
    }
}
