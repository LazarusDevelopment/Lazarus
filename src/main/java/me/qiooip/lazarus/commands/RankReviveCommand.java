package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankReviveCommand extends BaseCommand {

    public RankReviveCommand() {
        super("rankrevive", true);

        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.PREFIX + Language.RANK_REVIVE_USAGE);
            return;
        }

        Player player = (Player) sender;

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if(!this.checkOfflinePlayer(sender, target, args[0])) return;

        Lazarus.getInstance().getRankReviveHandler().performCommand(player, target);
    }
}
