package me.qiooip.lazarus.games.mountain.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.mountain.MountainData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class MountainTeleportCommand extends SubCommand {

    MountainTeleportCommand() {
        super("teleport", Collections.singletonList("tp"), "lazarus.mountain.tp", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.MOUNTAIN_PREFIX + Language.MOUNTAIN_TELEPORT_USAGE);
            return;
        }

        if(!this.checkNumber(sender, args[0])) return;

        MountainData mountain = Lazarus.getInstance().getMountainManager().getMountain(Integer.parseInt(args[0]));

        if(mountain == null) {
            sender.sendMessage(Language.MOUNTAIN_PREFIX + Language.MOUNTAIN_EXCEPTION_DOESNT_EXISTS.replace("<id>", args[0]));
            return;
        }

        Player player = (Player) sender;
        player.teleport(mountain.getCuboid().getCenter());
    }
}
