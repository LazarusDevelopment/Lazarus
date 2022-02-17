package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.userdata.Userdata;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ToggleCobbleCommand extends BaseCommand {

    public ToggleCobbleCommand() {
        super("togglecobble", Arrays.asList("tcobble", "cobble"), "lazarus.cobble", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        Userdata data = Lazarus.getInstance().getUserdataManager().getUserdata(player);
        data.getSettings().setCobble(!data.getSettings().isCobble());

        player.sendMessage(Language.PREFIX + (data.getSettings().isCobble()
            ? Language.TOGGLE_COBBLE_TOGGLED_ON : Language.TOGGLE_COBBLE_TOGGLED_OFF));
    }
}
