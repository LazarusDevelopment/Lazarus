package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.userdata.Userdata;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class ToggleLightningCommand extends BaseCommand {

    public ToggleLightningCommand() {
        super("togglelightning", Collections.singletonList("tlightning"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        Userdata data = Lazarus.getInstance().getUserdataManager().getUserdata(player);
        data.getSettings().setLightning(!data.getSettings().isLightning());

        player.sendMessage(Language.PREFIX + (data.getSettings().isLightning()
            ? Language.TOGGLE_LIGHTNING_TOGGLED_ON : Language.TOGGLE_LIGHTNING_TOGGLED_OFF));
    }
}
