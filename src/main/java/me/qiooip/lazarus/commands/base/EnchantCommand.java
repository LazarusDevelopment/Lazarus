package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.item.ItemUtils;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EnchantCommand extends BaseCommand {

    public EnchantCommand() {
        super("enchant", "lazarus.enchant", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(Language.PREFIX + Language.ENCHANT_USAGE);
            return;
        }

        Player player = (Player) sender;
        ItemStack item = player.getItemInHand();

        if(item == null || item.getType() == Material.AIR) {
            player.sendMessage(Language.PREFIX + Language.ITEMS_NOT_HOLDING);
            return;
        }

        if(!this.checkNumber(player, args[1])) return;

        int level = Math.abs(Integer.parseInt(args[1]));

        if (level > 7 && !player.isOp()) {
            player.sendMessage(Language.PREFIX + Language.ENCHANT_NON_OP_LIMIT);
            return;
        }

        Enchantment enchantment = Enchantment.getByName(StringUtils.getEnchantName(args[0].toUpperCase()));

        if(enchantment == null) {
            player.sendMessage(Language.PREFIX + Language.ENCHANT_DOESNT_EXIST.replace("<name>", args[0]));
            return;
        }

        if(level == 0) {
            item.removeEnchantment(enchantment);

            player.sendMessage(Language.PREFIX + Language.ENCHANT_ENCHANT_REMOVED
                .replace("<item>", ItemUtils.getItemName(item))
                .replace("<enchantment>", StringUtils.getEnchantName(enchantment)));
            return;
        }

        item.addUnsafeEnchantment(enchantment, level);
        player.sendMessage(Language.PREFIX + Language.ENCHANT_ENCHANTED.replace("<item>", ItemUtils.getItemName(item)));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) {
        if(args.length != 1 || !sender.hasPermission("lazarus.enchant")) return null;

        List<String> completions = new ArrayList<>();

        for(String enchantmentName : StringUtils.ENCHANTMENT_NAMES) {
            if(!enchantmentName.startsWith(args[0].toUpperCase())) continue;

            completions.add(enchantmentName);
        }

        return completions;
    }
}
