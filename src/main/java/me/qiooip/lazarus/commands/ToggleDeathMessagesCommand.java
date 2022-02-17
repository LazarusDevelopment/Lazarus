package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.userdata.Userdata;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class ToggleDeathMessagesCommand extends BaseCommand {

    public ToggleDeathMessagesCommand() {
        super("toggledeathmessages", Collections.singletonList("tdeathmessages"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        Userdata data = Lazarus.getInstance().getUserdataManager().getUserdata(player);
        data.getSettings().setDeathMessages(!data.getSettings().isDeathMessages());

        player.sendMessage(Language.PREFIX + (data.getSettings().isDeathMessages()
            ? Language.TOGGLE_DEATHMESSAGES_TOGGLED_ON : Language.TOGGLE_DEATHMESSAGES_TOGGLED_OFF));
    }
}
