package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExitCommand extends BaseCommand {

    public ExitCommand() {
        super("exit", "lazarus.exit", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        this.teleportToExit(player, args);
    }

    private void teleportToExit(Player player, String[] args) {
        Environment environment = this.getWorldEnvironment(args);
        String worldName = StringUtils.getWorldName(environment);
        Location exit = Config.WORLD_EXITS.get(environment);

        if(exit == null) {
            player.sendMessage(Language.PREFIX + Language.EXIT_DOESNT_EXIST.replace("<world>", worldName));
            return;
        }

        if(player.teleport(exit)) {
            player.sendMessage(Language.PREFIX + Language.EXIT_TELEPORTED.replace("<world>", worldName));
        }
    }

    private Environment getWorldEnvironment(String[] args) {
        if(args.length == 0) {
            return Environment.THE_END;
        }

        if(args[0].equalsIgnoreCase("nether")) {
            return Environment.NETHER;
        } else {
            return Environment.THE_END;
        }
    }
}
