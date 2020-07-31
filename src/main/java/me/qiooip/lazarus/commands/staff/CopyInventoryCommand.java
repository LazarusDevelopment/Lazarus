package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class CopyInventoryCommand extends BaseCommand {

    public CopyInventoryCommand() {
        super("copyinv", Collections.singletonList("cpi"), "lazarus.copyinv", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.PREFIX + Language.COPY_INVENTORY_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        Player player = (Player) sender;

        player.getInventory().setContents(target.getInventory().getContents());
        player.getInventory().setArmorContents(target.getInventory().getArmorContents());

        sender.sendMessage(Language.PREFIX + Language.COPY_INVENTORY_COPIED
            .replace("<player>", target.getName()));
    }
}
