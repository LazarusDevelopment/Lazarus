package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class FreezeCommand extends BaseCommand {

    public FreezeCommand() {
        super("freeze", Collections.singletonList("ss"), "lazarus.freeze");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.PREFIX + Language.FREEZE_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        if(sender != target && target.hasPermission("lazarus.freeze.bypass")) {
            sender.sendMessage(Language.PREFIX + Language.FREEZE_CAN_NOT_FREEZE_PLAYER
                .replace("<player>", target.getName()));
            return;
        }

        Lazarus.getInstance().getFreezeHandler().toggleFreeze(sender, target);
    }
}
