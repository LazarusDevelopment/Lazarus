package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.userdata.Userdata;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackCommand extends BaseCommand {

    public BackCommand() {
        super("back", "lazarus.back", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Userdata data = Lazarus.getInstance().getUserdataManager().getUserdata(player);

        if(data.getLastLocation() == null) {
            player.sendMessage(Language.PREFIX + Language.BACK_NO_LOCATION);
            return;
        }

        if(!player.teleport(data.getLastLocation())) return;
        player.sendMessage(Language.PREFIX + Language.BACK_TELEPORTED);
    }
}
