package me.qiooip.lazarus.games.conquest.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.games.loot.LootData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConquestLootCommand extends SubCommand {

    ConquestLootCommand() {
        super("loot", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        LootData loot = Lazarus.getInstance().getLootManager().getLootByName("Conquest");
        ((Player) sender).openInventory(loot.getInventory());
    }
}
