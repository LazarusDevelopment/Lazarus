package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.scoreboard.TeleportTimer;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends BaseCommand {

    public SpawnCommand() {
        super("spawn", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(!player.hasPermission("lazarus.spawn")) {
            if(!Config.KITMAP_MODE_ENABLED) {
                player.sendMessage(Language.PREFIX + Language.COMMANDS_NO_PERMISSION);
                return;
            }

            Location spawn = Config.WORLD_SPAWNS.get(Environment.NORMAL);

            if(spawn == null) {
                player.sendMessage(Language.PREFIX + Language.SPAWN_DOESNT_EXIST
                .replace("<world>", player.getWorld().getName()));
                return;
            }

            TeleportTimer timer = TimerManager.getInstance().getTeleportTimer();

            if(timer.isActive(player)) {
                player.sendMessage(Language.PREFIX + Language.SPAWN_ALREADY_TELEPORTING);
                return;
            }

            timer.activate(player, spawn);

            player.sendMessage(Language.PREFIX + Language.SPAWN_TELEPORT_STARTED
            .replace("<time>", String.valueOf(Config.KITMAP_SPAWN_TELEPORT_DELAY)));
            return;
        }

        if(args.length == 0) {
            this.teleportToSpawn(player, Environment.NORMAL, "World");
            return;
        }

        switch(args[0].toLowerCase()) {
            case "nether": {
                this.teleportToSpawn(player, Environment.NETHER, "Nether");
                return;
            }
            case "end": {
                this.teleportToSpawn(player, Environment.THE_END, "End");
                return;
            }
            default: this.teleportToSpawn(player, Environment.NORMAL, "World");
        }
    }

    private void teleportToSpawn(Player player, Environment environment, String world) {
        Location spawn = Config.WORLD_SPAWNS.get(environment);

        if(spawn == null) {
            player.sendMessage(Language.PREFIX + Language.SPAWN_DOESNT_EXIST.replace("<world>", world));
            return;
        }

        if(!player.teleport(spawn)) return;
        player.sendMessage(Language.PREFIX + Language.SPAWN_TELEPORTED.replace("<world>", world));
    }
}
