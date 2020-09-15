package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class InventoryRestoreCommand extends BaseCommand {

    public InventoryRestoreCommand() {
        super("inventoryrestore", Arrays.asList("invrestore", "lastinv", "invrollback"), "lazarus.invrestore", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.PREFIX + Language.INVENTORY_RESTORE_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        Lazarus.getInstance().getInventoryRestoreManager().openRestoreInventory((Player) sender, target);
    }
}
