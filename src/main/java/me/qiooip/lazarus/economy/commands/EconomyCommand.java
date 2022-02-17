package me.qiooip.lazarus.economy.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.economy.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class EconomyCommand extends BaseCommand {

    public EconomyCommand() {
        super("economy", Arrays.asList("eco", "econ"), "lazarus.economy");

        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 3) {
            Language.ECONOMY_ADMIN_COMMAND_USAGE.forEach(sender::sendMessage);
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        if(!this.checkOfflinePlayer(sender, target, args[1])) return;

        if(!this.checkNumber(sender, args[2])) return;
        int newAmount = Math.abs(Integer.parseInt(args[2]));

        switch(args[0].toLowerCase()) {
            case "give":
            case "add": {
                this.modifyBalance(sender, target, 1, newAmount);
                return;
            }
            case "remove": {
                this.modifyBalance(sender, target, 2, newAmount);
                return;
            }
            case "set": {
                this.modifyBalance(sender, target, 3, newAmount);
                return;
            }
            default: {
                Language.ECONOMY_ADMIN_COMMAND_USAGE.forEach(sender::sendMessage);
            }
        }
    }

    private void modifyBalance(CommandSender sender, OfflinePlayer target, int action, int newAmount) {
        EconomyManager manager = Lazarus.getInstance().getEconomyManager();
        int balance = manager.getBalance(target);

        switch(action) {
            case 1: {
                manager.setBalance(target, Math.min(Config.MAX_BALANCE, balance + newAmount));
                break;
            }
            case 2: {
                manager.setBalance(target, Math.max(0, balance - newAmount));
                break;
            }
            case 3: {
                manager.setBalance(target, Math.min(Config.MAX_BALANCE, newAmount));
            }
        }

        int newBalance = manager.getBalance(target);

        sender.sendMessage(Language.PREFIX + Language.ECONOMY_BALANCE_CHANGED_STAFF
            .replace("<player>", target.getName())
            .replace("<sender>", sender.getName())
            .replace("<amount>", String.valueOf(newBalance)));

        if(target.isOnline()) {
            target.getPlayer().sendMessage(Language.PREFIX + Language.ECONOMY_BALANCE_CHANGED
                .replace("<sender>", sender.getName())
                .replace("<amount>", String.valueOf(newBalance)));
        }
    }
}
