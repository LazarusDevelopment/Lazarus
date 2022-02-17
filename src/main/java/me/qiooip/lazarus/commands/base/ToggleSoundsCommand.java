package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.userdata.Userdata;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class ToggleSoundsCommand extends BaseCommand {

    public ToggleSoundsCommand() {
        super("togglesounds", Collections.singletonList("tsounds"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        Userdata data = Lazarus.getInstance().getUserdataManager().getUserdata(player);
        data.getSettings().setSounds(!data.getSettings().isSounds());

        player.sendMessage(Language.PREFIX + (data.getSettings().isSounds()
            ? Language.TOGGLE_SOUNDS_TOGGLED_ON : Language.TOGGLE_SOUNDS_TOGGLED_OFF));
    }
}
