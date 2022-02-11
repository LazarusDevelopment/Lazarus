package me.qiooip.lazarus.integration.vault;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.economy.EconomyManager.TransactionResult;
import me.qiooip.lazarus.userdata.Userdata;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class Economy_Lazarus implements Economy {

    @Override
    public boolean isEnabled() {
        return Lazarus.getInstance().isEnabled();
    }

    @Override
    public String getName() {
        return "Lazarus";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(double balance) {
        return String.valueOf(balance);
    }

    @Override
    public String currencyNamePlural() {
        return "";
    }

    @Override
    public String currencyNameSingular() {
        return "";
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return Lazarus.getInstance().getUserdataManager().getUserdata(offlinePlayer) != null;
    }

    @Override
    public boolean hasAccount(String playerName) {
        return this.hasAccount(Bukkit.getOfflinePlayer(playerName));
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String worldName) {
        return this.hasAccount(offlinePlayer);
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return this.hasAccount(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        Userdata userdata = Lazarus.getInstance().getUserdataManager().getUserdata(offlinePlayer);
        return userdata == null ? 0 : userdata.getBalance();
    }

    @Override
    public double getBalance(String playerName) {
        return this.getBalance(Bukkit.getOfflinePlayer(playerName));
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String worldName) {
        return this.getBalance(offlinePlayer);
    }

    @Override
    public double getBalance(String playerName, String worldName) {
        return this.getBalance(playerName);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        return this.getBalance(offlinePlayer) >= amount;
    }

    @Override
    public boolean has(String playerName, double amount) {
        return this.getBalance(playerName) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return this.has(offlinePlayer, amount);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return this.has(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        Userdata userdata = Lazarus.getInstance().getUserdataManager().getUserdata(offlinePlayer);

        if(userdata == null) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "User " + offlinePlayer.getName() + " never played on this server!");
        }

        TransactionResult result = this.withdrawBalance(userdata, (int) amount);

        if(result == TransactionResult.INVALID_AMOUNT) {
            return new EconomyResponse(0, userdata.getBalance(), ResponseType.FAILURE, "Cannot withdraw negative funds");
        } else if(result == TransactionResult.MIN_BALANCE) {
            return new EconomyResponse(0, userdata.getBalance(), ResponseType.FAILURE, "Insufficient funds");
        } else {
            return new EconomyResponse(amount, userdata.getBalance(), ResponseType.SUCCESS, "");
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return this.withdrawPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return this.withdrawPlayer(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return this.withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        Userdata userdata = Lazarus.getInstance().getUserdataManager().getUserdata(offlinePlayer);

        if(userdata == null) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "User " + offlinePlayer.getName() + " never played on this server!");
        }

        TransactionResult result = this.depositBalance(userdata, (int) amount);

        if(result == TransactionResult.INVALID_AMOUNT) {
            return new EconomyResponse(0, userdata.getBalance(), ResponseType.FAILURE, "Cannot deposit negative funds");
        } else if(result == TransactionResult.MAX_BALANCE) {
            return new EconomyResponse(0, userdata.getBalance(), ResponseType.FAILURE, "User exceeded balance limit");
        } else {
            return new EconomyResponse(amount, userdata.getBalance(), ResponseType.SUCCESS, "");
        }
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return this.depositPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return this.depositPlayer(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return this.depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Lazarus does not support bank accounts!");
    }

    @Override
    public EconomyResponse createBank(String name, String playerName) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Lazarus does not support bank accounts!");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Lazarus does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Lazarus does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Lazarus does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Lazarus does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Lazarus does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Lazarus does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Lazarus does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Lazarus does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Lazarus does not support bank accounts!");
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<>();
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        throw new UnsupportedOperationException("This function is not supported!");
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        throw new UnsupportedOperationException("This function is not supported!");
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String worldName) {
        throw new UnsupportedOperationException("This function is not supported!");
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        throw new UnsupportedOperationException("This function is not supported!");
    }

    private TransactionResult depositBalance(Userdata userdata, int amount) {
        if(amount <= 0) {
            return TransactionResult.INVALID_AMOUNT;
        }

        if(userdata.getBalance() + amount > Config.MAX_BALANCE) {
            return TransactionResult.MAX_BALANCE;
        }

        userdata.changeBalance(userdata.getBalance() + amount);
        return TransactionResult.SUCCESS;
    }

    private TransactionResult withdrawBalance(Userdata userdata, int amount) {
        if(amount <= 0) {
            return TransactionResult.INVALID_AMOUNT;
        }

        if(userdata.getBalance() < amount) {
            return TransactionResult.MIN_BALANCE;
        }

        userdata.changeBalance(userdata.getBalance() - amount);
        return TransactionResult.SUCCESS;
    }
}
