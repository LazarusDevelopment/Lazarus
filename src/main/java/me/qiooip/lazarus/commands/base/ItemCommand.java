package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class ItemCommand extends BaseCommand {

    public ItemCommand() {
        super("item", Collections.singletonList("i"), "lazarus.item", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.PREFIX + Language.ITEM_USAGE);
            return;
        }

        Player player = (Player) sender;
        ItemStack item = ItemUtils.parseItem(args[0]);

        if(item == null) {
            sender.sendMessage(Language.PREFIX + Language.ITEM_INVALID_ITEM.replace("<item>", args[0]));
            return;
        }

        int amount = 64;

        if(args.length > 1) {
            if(!this.checkNumber(sender, args[1])) return;
            amount = Integer.parseInt(args[1]);
        }

        if(player.getInventory().firstEmpty() == -1) {
            sender.sendMessage(Language.PREFIX + Language.ITEM_INVENTORY_FULL);
            return;
        }

        item.setAmount(amount);

        player.getInventory().addItem(item);
        ItemUtils.updateInventory(player);

        player.sendMessage(Language.PREFIX + Language.ITEM_RECEIVED
            .replace("<item>", ItemUtils.getItemName(item))
            .replace("<amount>", String.valueOf(amount)));
    }
}
