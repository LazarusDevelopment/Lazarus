package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TeleportPositionCommand extends BaseCommand {

    public TeleportPositionCommand() {
        super("teleportposition", Arrays.asList("teleportpos", "tppos"), "lazarus.teleporposition", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(args.length < 3) {
            player.sendMessage(Language.PREFIX + Language.TELEPORT_POSITION_USAGE);
            return;
        }

        if(!StringUtils.isInteger(args[0]) || !StringUtils.isInteger(args[1]) || !StringUtils.isInteger(args[2])) {
            player.sendMessage(Language.PREFIX + Language.COMMANDS_INVALID_NUMBER);
            return;
        }

        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        int z = Integer.parseInt(args[2]);

        if(Math.abs(x) >= 30000000 || Math.abs(z) >= 30000000) {
            player.sendMessage(Language.PREFIX + Language.COMMANDS_INVALID_NUMBER);
            return;
        }

        Location where = player.getLocation().clone();

        where.setX(x);
        where.setY(y);
        where.setZ(z);

        if(!player.teleport(where.add(0.5, 0, 0.5))) return;

        player.sendMessage(Language.PREFIX + Language.TELEPORT_POSITION_MESSAGE
            .replace("<x>", String.valueOf(x))
            .replace("<y>", String.valueOf(y))
            .replace("<z>", String.valueOf(z)));
    }
}
