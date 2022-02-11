package me.qiooip.lazarus.economy;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.utils.ManagerEnabler;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class EconomyManager implements ManagerEnabler {

    public int getBalance(OfflinePlayer offlinePlayer) {
        return Lazarus.getInstance().getUserdataManager().getUserdata(offlinePlayer).getBalance();
    }

    public void addBalance(OfflinePlayer player, int amount) {
        this.setBalance(player, this.getBalance(player) + amount);
    }

    public void removeBalance(OfflinePlayer player, int amount) {
        this.setBalance(player, this.getBalance(player) - amount);
    }

    public void setBalance(OfflinePlayer player, int amount) {
        Lazarus.getInstance().getUserdataManager().getUserdata(player).changeBalance(amount);
    }

    public TransactionResult depositBalance(OfflinePlayer player, int amount) {
        if(amount <= 0) {
            return TransactionResult.INVALID_AMOUNT;
        }

        if(this.getBalance(player) + amount > Config.MAX_BALANCE) {
            return TransactionResult.MAX_BALANCE;
        }

        this.addBalance(player, amount);
        return TransactionResult.SUCCESS;
    }

    public TransactionResult withdrawBalance(OfflinePlayer player, int amount) {
        if(amount <= 0) {
            return TransactionResult.INVALID_AMOUNT;
        }

        if(this.getBalance(player) < amount) {
            return TransactionResult.MIN_BALANCE;
        }

        this.removeBalance(player, amount);
        return TransactionResult.SUCCESS;
    }

    public TransactionResult transferBalance(Player player, OfflinePlayer target, int amount) {
        if(amount <= 0) {
            return TransactionResult.INVALID_AMOUNT;
        }

        if(this.getBalance(player) < amount) {
            return TransactionResult.MIN_BALANCE;
        }

        if(this.getBalance(target) + amount > Config.MAX_BALANCE) {
            return TransactionResult.MAX_BALANCE;
        }

        this.removeBalance(player, amount);
        this.addBalance(target, amount);

        return TransactionResult.SUCCESS;
    }

    public enum TransactionResult {
        SUCCESS, MAX_BALANCE, MIN_BALANCE, INVALID_AMOUNT
    }
}
