package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldCommand extends BaseCommand {

    public WorldCommand() {
        super("world", "lazarus.world", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(args.length == 0) {
            player.sendMessage(Language.PREFIX + Language.WORLD_COMMAND_USAGE);
            return;
        }

        if(StringUtils.isInteger(args[0])) {
            int worldId = Math.abs(Integer.parseInt(args[0]));

            if(worldId > Bukkit.getWorlds().size() - 1) {
                player.sendMessage(Language.PREFIX + Language.WORLD_DOESNT_EXIST.replace("<name>", args[0]));
                return;
            }

            Location location = player.getLocation().clone();
            location.setWorld(Bukkit.getWorlds().get(worldId));

            player.teleport(location);
            return;
        }

        World world = Bukkit.getWorld(args[0]);

        if(world == null) {
            player.sendMessage(Language.PREFIX + Language.WORLD_DOESNT_EXIST.replace("<name>", args[0]));
            return;
        }

        Location location = player.getLocation().clone();
        location.setWorld(world);

        player.teleport(location);
    }
}
