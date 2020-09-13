package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.item.ItemUtils;
import me.qiooip.lazarus.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand extends BaseCommand {

    public GiveCommand() {
        super("give", "lazarus.give");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(Language.PREFIX + Language.GIVE_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        ItemStack item = ItemUtils.parseItem(args[1]);

        if(item == null) {
            sender.sendMessage(Language.PREFIX + Language.GIVE_INVALID_ITEM.replace("<item>", args[1]));
            return;
        }

        int amount = 64;

        if(args.length > 2) {
            if(!this.checkNumber(sender, args[2])) return;
            amount = Integer.parseInt(args[2]);
        }

        boolean drop = false;

        if(target.getInventory().firstEmpty() == -1) {
            if(Config.KITMAP_MODE_ENABLED && Config.KITMAP_KILLSTREAK_ENABLED) {
                drop = !PlayerUtils.removeSplashFromInventory(target.getInventory());
            } else {
                sender.sendMessage(Language.PREFIX + Language.GIVE_INVENTORY_FULL.replace("<player>", target.getName()));
                return;
            }
        }

        item.setAmount(amount);

        if(drop) {
            target.getWorld().dropItemNaturally(target.getLocation(), item);
        } else {
            target.getInventory().addItem(item);
            ItemUtils.updateInventory(target);
        }

        sender.sendMessage(Language.PREFIX + Language.GIVE_GIVEN_SENDER
            .replace("<player>", target.getName())
            .replace("<item>", ItemUtils.getItemName(item))
            .replace("<amount>", String.valueOf(amount)));

        target.sendMessage(Language.PREFIX + Language.GIVE_GIVEN_OTHERS
            .replace("<player>", sender.getName())
            .replace("<item>", ItemUtils.getItemName(item))
            .replace("<amount>", String.valueOf(amount)));
    }
}
