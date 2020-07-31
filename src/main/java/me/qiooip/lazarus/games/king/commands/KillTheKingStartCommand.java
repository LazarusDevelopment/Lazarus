package me.qiooip.lazarus.games.king.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KillTheKingStartCommand extends SubCommand {

    KillTheKingStartCommand() {
        super("start", "lazarus.killtheking.start");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.KING_PREFIX + Language.KING_START_USAGE);
            return;
        }

        if(Lazarus.getInstance().getKillTheKingManager().isActive()) {
            sender.sendMessage(Language.KING_PREFIX + Language.KING_EXCEPTION_ALREADY_RUNNING);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        Lazarus.getInstance().getKillTheKingManager().startKillTheKing(target);
    }
}
