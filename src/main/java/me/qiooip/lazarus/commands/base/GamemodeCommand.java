package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class GamemodeCommand extends BaseCommand {

    public GamemodeCommand() {
        super("gamemode", Collections.singletonList("gm"), "lazarus.gamemode");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length != 1 && args.length != 2) {
            sender.sendMessage(Language.PREFIX + Language.GAMEMODE_USAGE);
            return;
        }

        if(args.length == 1) {

            if(!this.checkConsoleSender(sender)) return;

            Player player = (Player) sender;
            String gamemode = this.getGamemodeName(args[0]);

            if(gamemode == null) {
                player.sendMessage(Language.PREFIX + Language.GAMEMODE_INVALID_GAMEMODE);
                return;
            }

            player.setGameMode(GameMode.valueOf(gamemode));
            player.sendMessage(Language.PREFIX + Language.GAMEMODE_MESSAGE_SELF.replace("<gamemode>", gamemode.toLowerCase()));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if(!this.checkPlayer(sender, target, args[1])) return;

        String gamemode = this.getGamemodeName(args[0]);

        if(gamemode == null) {
            sender.sendMessage(Language.PREFIX + Language.GAMEMODE_INVALID_GAMEMODE);
            return;
        }

        target.setGameMode(GameMode.valueOf(gamemode));

        target.sendMessage(Language.PREFIX + Language.GAMEMODE_MESSAGE_SELF
            .replace("<gamemode>", gamemode.toLowerCase()));

        sender.sendMessage(Language.PREFIX + Language.GAMEMODE_MESSAGE_OTHERS
            .replace("<player>", target.getName())
            .replace("<gamemode>", gamemode.toLowerCase()));
    }

    private String getGamemodeName(String arg) {
        switch(arg.toLowerCase()) {
            case "0":
            case "s":
            case "survival": return "SURVIVAL";
            case "1":
            case "c":
            case "creative": return "CREATIVE";
            case "2":
            case "a":
            case "adventure": return "ADVENTURE";
            case "3":
            case "spectator": {
                if(NmsUtils.getInstance().isSpigot18()) {
                    return "SPECTATOR";
                }
            }
            default: return null;
        }
    }
}
