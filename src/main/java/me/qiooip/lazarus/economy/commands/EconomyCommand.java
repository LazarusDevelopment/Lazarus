package me.qiooip.lazarus.economy.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
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

        switch(args[0].toLowerCase()) {
            case "give":
            case "add": {
                this.modifyBalance(sender, Bukkit.getOfflinePlayer(args[1]), args[1], 1, args[2]);
                return;
            }
            case "remove": {
                this.modifyBalance(sender, Bukkit.getOfflinePlayer(args[1]), args[1], 2, args[2]);
                return;
            }
            case "set": {
                this.modifyBalance(sender, Bukkit.getOfflinePlayer(args[1]), args[1], 3, args[2]);
                return;
            }
            default: Language.ECONOMY_ADMIN_COMMAND_USAGE.forEach(sender::sendMessage);
        }
    }

    private void modifyBalance(CommandSender sender, OfflinePlayer target, String name, int action, String amount) {
        if(!this.checkOfflinePlayer(sender, target, name)) return;
        if(!this.checkNumber(sender, amount)) return;

        int balance = Lazarus.getInstance().getEconomyManager().getBalance(target);
        int newAmount = Math.abs(Integer.parseInt(amount));

        switch(action) {
            case 1: {
                Lazarus.getInstance().getEconomyManager().setBalance(target, Math.min(Config.MAX_BALANCE, balance + newAmount));
                break;
            }
            case 2: {
                Lazarus.getInstance().getEconomyManager().setBalance(target, Math.max(0, balance - newAmount));
                break;
            }
            case 3: {
                Lazarus.getInstance().getEconomyManager().setBalance(target, Math.min(Config.MAX_BALANCE, newAmount));
            }
        }

        sender.sendMessage(Language.PREFIX + Language.ECONOMY_BALANCE_CHANGED_STAFF.replace("<player>", target
        .getName()).replace("<sender>", sender.getName()).replace("<amount>", String.valueOf(Lazarus
        .getInstance().getEconomyManager().getBalance(target))));

        if(target.isOnline()) {
            target.getPlayer().sendMessage(Language.PREFIX + Language.ECONOMY_BALANCE_CHANGED.replace("<sender>", sender.getName())
            .replace("<amount>", String.valueOf(Lazarus.getInstance().getEconomyManager().getBalance(target))));
        }
    }
}
