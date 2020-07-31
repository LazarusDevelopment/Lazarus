package me.qiooip.lazarus.games.dtc.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.games.loot.LootData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DtcLootCommand extends SubCommand {

    DtcLootCommand() {
        super("loot", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        LootData loot = Lazarus.getInstance().getLootManager().getLootByName("DTC");
        ((Player) sender).openInventory(loot.getInventory());
    }
}
