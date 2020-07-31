package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class LivesCommand extends BaseCommand {

    public LivesCommand() {
        super("lives");

        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 1) {
            switch(args[0].toLowerCase()) {
                case "check": {
                    Lazarus.getInstance().getDeathbanManager().sendLivesCount(sender);
                    return;
                }
                case "add": {
                    if(!this.checkPermission(sender, "lazarus.lives.add")) return;
                    sender.sendMessage(Language.PREFIX + Language.LIVES_ADD_USAGE);
                    return;
                }
                case "set": {
                    if(!this.checkPermission(sender, "lazarus.lives.set")) return;
                    sender.sendMessage(Language.PREFIX + Language.LIVES_SET_USAGE);
                    return;
                }
                default: {
                    Language.LIVES_USAGE.forEach(sender::sendMessage);
                    return;
                }
            }
        }

        if(args.length == 2) {
            switch(args[0].toLowerCase()) {
                case "check": {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    if(!this.checkOfflinePlayer(sender, target, args[1])) return;

                    Lazarus.getInstance().getDeathbanManager().sendLivesCount(sender, target);
                    return;
                }
                case "revive": {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    if(!this.checkOfflinePlayer(sender, target, args[1])) return;

                    Lazarus.getInstance().getDeathbanManager().reviveUsingLives(sender, target);
                    return;
                }
                case "add": {
                    if(!this.checkPermission(sender, "lazarus.lives.add")) return;
                    sender.sendMessage(Language.PREFIX + Language.LIVES_ADD_USAGE);
                    return;
                }
                case "set": {
                    if(!this.checkPermission(sender, "lazarus.lives.set")) return;
                    sender.sendMessage(Language.PREFIX + Language.LIVES_SET_USAGE);
                    return;
                }
                default: {
                    Language.LIVES_USAGE.forEach(sender::sendMessage);
                    return;
                }
            }
        }

        if(args.length == 3) {
            switch(args[0].toLowerCase()) {
                case "send": {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    if(!this.checkOfflinePlayer(sender, target, args[1])) return;

                    Lazarus.getInstance().getDeathbanManager().sendLives(sender, target, args[2]);
                    return;
                }
                case "add": {
                    if(!this.checkPermission(sender, "lazarus.lives.add")) return;

                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    if(!this.checkOfflinePlayer(sender, target, args[1])) return;

                    Lazarus.getInstance().getDeathbanManager().changeLives(sender, target, args[2], true);
                    return;
                }
                case "set": {
                    if(!this.checkPermission(sender, "lazarus.lives.set")) return;

                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    if(!this.checkOfflinePlayer(sender, target, args[1])) return;

                    Lazarus.getInstance().getDeathbanManager().changeLives(sender, target, args[2], false);
                    return;
                }
                default: {
                    Language.LIVES_USAGE.forEach(sender::sendMessage);
                    return;
                }
            }
        }

        Language.LIVES_USAGE.forEach(sender::sendMessage);
    }
}
