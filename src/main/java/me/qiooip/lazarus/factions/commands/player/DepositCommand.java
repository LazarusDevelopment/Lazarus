package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class DepositCommand extends SubCommand {

    public DepositCommand() {
        super("deposit", Collections.singletonList("d"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_DEPOSIT_USAGE);
            return;
        }

        Player player = (Player) sender;
        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);

        if(faction == null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NOT_IN_FACTION_SELF);
            return;
        }

        int balance = Lazarus.getInstance().getEconomyManager().getBalance(player);
        int amount;

        if(args[0].equalsIgnoreCase("all")) {
            amount = balance;
        } else {
            if(!this.checkNumber(sender, args[0])) return;
            amount = Math.abs(Integer.parseInt(args[0]));
        }

        if(amount > balance) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_DEPOSIT_NOT_ENOUGH_MONEY
            .replace("<amount>", String.valueOf(balance)));
            return;
        }

        if(amount == 0) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_DEPOSIT_CAN_NOT_DEPOSIT_ZERO);
            return;
        }

        Lazarus.getInstance().getEconomyManager().removeBalance(player, amount);
        faction.addBalance(amount);

        faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_DEPOSIT_DEPOSITED
            .replace("<player>", player.getName())
            .replace("<amount>", String.valueOf(amount)));
    }
}
