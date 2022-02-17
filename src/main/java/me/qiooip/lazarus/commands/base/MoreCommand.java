package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MoreCommand extends BaseCommand {

    public MoreCommand() {
        super("more", "lazarus.more", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        ItemStack item = player.getItemInHand();

        if(item == null || item.getType() == Material.AIR) {
            player.sendMessage(Language.PREFIX + Language.ITEMS_NOT_HOLDING);
            return;
        }

        player.getItemInHand().setAmount(64);
        player.sendMessage(Language.PREFIX + Language.MORE_COMMAND_MESSAGE);
    }
}
