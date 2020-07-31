package me.qiooip.lazarus.economy.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.economy.EconomyManager.TransactionResult;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand extends BaseCommand {

    public PayCommand() {
        super("pay", true);

        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(args.length < 2) {
            player.sendMessage(Language.PREFIX + Language.ECONOMY_PAY_USAGE);
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if(!this.checkOfflinePlayer(sender, target, args[0])) return;

        if(player == target) {
            player.sendMessage(Language.PREFIX + Language.ECONOMY_CANNOT_SEND_TO_YOURSELF);
            return;
        }

        if(!StringUtils.isInteger(args[1]) && !args[1].equalsIgnoreCase("all")) {
            player.sendMessage(Language.PREFIX + Language.COMMANDS_INVALID_NUMBER);
            return;
        }

        int amount = args[1].equalsIgnoreCase("all") ? Lazarus.getInstance()
        .getEconomyManager().getBalance(player) : Integer.parseInt(args[1]);

        TransactionResult result = Lazarus.getInstance().getEconomyManager().transferBalance(player, target, amount);
        this.transactionMessage(player, target, result, String.valueOf(amount));
    }

    private void transactionMessage(Player player, OfflinePlayer target, TransactionResult result, String amount) {
        switch(result) {
            case INVALID_AMOUNT: {
                player.sendMessage(Language.PREFIX + Language.TRANSACTION_INVALID_AMOUNT);
                return;
            }
            case MAX_BALANCE: {
                player.sendMessage(Language.PREFIX + Language.TRANSACTION_MAX_AMOUNT
                .replace("<player>", target.getName()));
                return;
            }
            case MIN_BALANCE: {
                player.sendMessage(Language.PREFIX + Language.TRANSACTION_MIN_AMOUNT);
                return;
            }
            case SUCCESS: {
                player.sendMessage(Language.PREFIX + Language.TRANSACTION_SUCCESS_SELF
                    .replace("<player>", target.getName())
                    .replace("<amount>", amount));

                if(target.isOnline()) {
                    Player targetPlayer = target.getPlayer();

                    targetPlayer.sendMessage(Language.PREFIX + Language.TRANSACTION_SUCCESS_OTHERS
                        .replace("<player>", player.getName())
                        .replace("<amount>", amount));
                }
            }
        }
    }
}
