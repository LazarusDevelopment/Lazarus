package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.stream.Stream;

public class RepairCommand extends BaseCommand {

    public RepairCommand() {
        super("repair", Collections.singletonList("fix"), "lazarus.repair", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(args.length < 1) {
            player.sendMessage(Language.PREFIX + Language.REPAIR_USAGE);
            return;
        }

        switch(args[0].toLowerCase()) {
            case "hand": {
                ItemStack item = player.getItemInHand();

                if(item == null || item.getType() == Material.AIR) {
                    player.sendMessage(Language.PREFIX + Language.ITEMS_NOT_HOLDING);
                    return;
                }

                if(!ItemUtils.isRepairable(item.getType())) {
                    player.sendMessage(Language.PREFIX + Language.REPAIR_NOT_REPAIRABLE);
                    return;
                }

                item.setDurability((short) 0);

                player.sendMessage(Language.PREFIX + Language.REPAIR_REPAIRED_ITEM
                    .replace("<item>", ItemUtils.getItemName(item)));
                return;
            }
            case "all": {
                Stream.of(player.getInventory().getContents())
                    .filter(item -> item != null && item.getType() != Material.AIR && ItemUtils.isRepairable(item.getType()))
                    .forEach(item -> item.setDurability((short) 0));

                Stream.of(player.getInventory().getArmorContents())
                    .filter(item -> item != null && item.getType() != Material.AIR)
                    .forEach(item -> item.setDurability((short) 0));

                player.sendMessage(Language.PREFIX + Language.REPAIR_REPAIRED_ALL);
                return;
            }
            default: player.sendMessage(Language.PREFIX + Language.REPAIR_USAGE);
        }
    }
}
