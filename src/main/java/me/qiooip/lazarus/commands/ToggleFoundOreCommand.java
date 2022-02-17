package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.userdata.Userdata;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class ToggleFoundOreCommand extends BaseCommand {

    public ToggleFoundOreCommand() {
        super("togglefoundore", Collections.singletonList("tfoundore"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        Userdata data = Lazarus.getInstance().getUserdataManager().getUserdata(player);
        data.getSettings().setFoundOre(!data.getSettings().isFoundOre());

        player.sendMessage(Language.PREFIX + (data.getSettings().isFoundOre()
            ? Language.TOGGLE_FOUND_ORE_TOGGLED_ON : Language.TOGGLE_FOUND_ORE_TOGGLED_OFF));
    }
}
