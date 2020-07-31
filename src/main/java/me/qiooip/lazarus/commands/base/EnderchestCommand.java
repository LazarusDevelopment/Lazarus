package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class EnderchestCommand extends BaseCommand {

    public EnderchestCommand() {
        super("enderchest", Collections.singletonList("ec"), "lazarus.enderchest", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(Config.COMBAT_TAG_DISABLE_ENDERCHESTS && TimerManager.getInstance().getCombatTagTimer().isActive(player)) {
            player.sendMessage(Language.PREFIX + Language.COMBAT_TAG_ENDERCHEST_DENY);
            return;
        }

        if(args.length == 0) {
            player.openInventory(player.getEnderChest());
            return;
        }

        if(!this.checkPermission(sender, "lazarus.enderchest.others")) return;

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        player.openInventory(target.getEnderChest());
    }
}
