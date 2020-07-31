package me.qiooip.lazarus.economy.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class BalanceCommand extends BaseCommand {

    public BalanceCommand() {
        super("balance", Arrays.asList("bal", "money"));

        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            if(sender instanceof ConsoleCommandSender) {
                sender.sendMessage(Language.PREFIX + Language.COMMANDS_FOR_PLAYERS_ONLY);
                return;
            }

            Player player = (Player) sender;

            sender.sendMessage(Language.PREFIX + Language.ECONOMY_BALANCE_SELF.replace("<amount>",
            String.valueOf(Lazarus.getInstance().getEconomyManager().getBalance(player))));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if(!this.checkOfflinePlayer(sender, target, args[0])) return;

        sender.sendMessage(Language.PREFIX + Language.ECONOMY_BALANCE_OTHERS.replace("<player>", target.getName())
        .replace("<amount>", String.valueOf(Lazarus.getInstance().getEconomyManager().getBalance(target))));
    }
}
