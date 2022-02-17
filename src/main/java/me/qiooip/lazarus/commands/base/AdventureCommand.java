package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class AdventureCommand extends BaseCommand {

    public AdventureCommand() {
        super("adventure", Collections.singletonList("gma"), "lazarus.gamemode");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            if(!this.checkConsoleSender(sender)) return;

            Player player = (Player) sender;
            player.setGameMode(GameMode.ADVENTURE);
            player.sendMessage(Language.PREFIX + Language.GAMEMODE_MESSAGE_SELF.replace("<gamemode>", "adventure"));
            return;
        }

        if(!this.checkPermission(sender, "lazarus.gamemode.others")) return;

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        target.setGameMode(GameMode.ADVENTURE);

        target.sendMessage(Language.PREFIX + Language.GAMEMODE_MESSAGE_SELF
            .replace("<gamemode>", "adventure"));

        sender.sendMessage(Language.PREFIX + Language.GAMEMODE_MESSAGE_OTHERS
            .replace("<player>", target.getName())
            .replace("<gamemode>", "adventure"));
    }
}
