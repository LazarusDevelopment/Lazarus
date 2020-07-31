package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class DeathbanCommand extends BaseCommand {

    public DeathbanCommand() {
        super("deathban", "lazarus.deathban");

        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            Language.DEATHBAN_COMMAND_USAGE.forEach(sender::sendMessage);
            return;
        }

        if(args[0].equalsIgnoreCase("check")) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            if(!this.checkOfflinePlayer(sender, target, args[1])) return;

            Lazarus.getInstance().getDeathbanManager().checkStatus(sender, target);
            return;
        }

        if(args[0].equalsIgnoreCase("revive")) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            if(!this.checkOfflinePlayer(sender, target, args[1])) return;

            Lazarus.getInstance().getDeathbanManager().revivePlayer(sender, target);
            return;
        }

        Language.DEATHBAN_COMMAND_USAGE.forEach(sender::sendMessage);
    }
}
