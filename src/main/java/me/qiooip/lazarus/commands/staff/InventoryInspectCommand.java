package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class InventoryInspectCommand extends BaseCommand {

    public InventoryInspectCommand() {
        super("inventoryinspect", Arrays.asList("invinspect", "invi", "ii"), "lazarus.invinspect", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.PREFIX + Language.INVENTORY_INSPECT_USAGE);
            return;
        }

        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[0]);

        if(this.checkPlayer(player, target, args[0])) {
            Lazarus.getInstance().getStaffModeManager().inventoryInspect(player, target);
        }
    }
}
