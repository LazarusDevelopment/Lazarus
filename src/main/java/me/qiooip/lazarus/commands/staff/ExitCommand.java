package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
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

        if(args.length == 0) {
            this.teleportToExit(player, Environment.THE_END, "End");
            return;
        }

        switch(args[0].toLowerCase()) {
            case "nether": {
                this.teleportToExit(player, Environment.NETHER, "Nether");
                return;
            }
            case "end": {
                this.teleportToExit(player, Environment.THE_END, "End");
                return;
            }
            default: this.teleportToExit(player, Environment.THE_END, "End");
        }
    }

    private void teleportToExit(Player player, Environment environment, String world) {
        Location exit = Config.WORLD_EXITS.get(environment);

        if(exit == null) {
            player.sendMessage(Language.PREFIX + Language.EXIT_DOESNT_EXIST.replace("<world>", world));
            return;
        }

        if(!player.teleport(exit)) return;
        player.sendMessage(Language.PREFIX + Language.EXIT_TELEPORTED.replace("<world>", world));
    }
}
