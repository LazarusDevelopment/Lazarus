package me.qiooip.lazarus.games.loot.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.loot.LootData;
import me.qiooip.lazarus.utils.InventoryUtils;
import org.bukkit.command.CommandSender;

public class LootClearCommand extends SubCommand {

    LootClearCommand() {
        super("clear", "lazarus.loot.clear");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.LOOT_PREFIX + Language.LOOT_CLEAR_USAGE);
            return;
        }

        LootData loot = Lazarus.getInstance().getLootManager().getLootByName(args[0]);

        if(loot == null) {
            sender.sendMessage(Language.LOOT_PREFIX + Language.LOOT_EXCEPTION_DOESNT_EXIST.replace("<loot>", args[0]));
            return;
        }

        loot.getInventory().clear();
        loot.setItems(InventoryUtils.getItemsFromInventory(loot.getInventory()));

        sender.sendMessage(Language.LOOT_PREFIX + Language.LOOT_CLEAR_CLEARED.replace("<loot>", loot.getName()));
    }
}
