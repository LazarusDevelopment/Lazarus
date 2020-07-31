package me.qiooip.lazarus.games.loot.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.loot.LootData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LootViewCommand extends SubCommand {

    LootViewCommand() {
        super("view", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.LOOT_PREFIX + Language.LOOT_VIEW_USAGE);
            return;
        }

        LootData loot = Lazarus.getInstance().getLootManager().getLootByName(args[0]);

        if(loot == null) {
            sender.sendMessage(Language.LOOT_PREFIX + Language.LOOT_EXCEPTION_DOESNT_EXIST.replace("<loot>", args[0]));
            return;
        }

        ((Player) sender).openInventory(loot.getInventory());
    }
}
