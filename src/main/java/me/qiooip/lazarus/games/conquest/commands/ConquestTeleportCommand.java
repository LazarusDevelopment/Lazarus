package me.qiooip.lazarus.games.conquest.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.Cuboid;
import me.qiooip.lazarus.games.conquest.ZoneType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class ConquestTeleportCommand extends SubCommand {

    ConquestTeleportCommand() {
        super("teleport", Collections.singletonList("tp"), "lazarus.conquest.teleport", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.CONQUEST_PREFIX + Language.CONQUEST_TELEPORT_USAGE);
            return;
        }

        ZoneType type;

        try {
            type = ZoneType.valueOf(args[0].toUpperCase());
        } catch(IllegalArgumentException e) {
            sender.sendMessage(Language.CONQUEST_PREFIX + Language.CONQUEST_EXCEPTION_INVALID_ZONE);
            return;
        }

        Cuboid cuboid = Lazarus.getInstance().getConquestManager().getConquest().getCuboid(type);

        if(cuboid == null) {
            sender.sendMessage(Language.CONQUEST_PREFIX + Language.CONQUEST_TELEPORT_ZONE_NOT_SET
            .replace("<zone>", type.getName()));
            return;
        }

        Player player = (Player) sender;
        player.teleport(cuboid.getCenter());
    }
}
