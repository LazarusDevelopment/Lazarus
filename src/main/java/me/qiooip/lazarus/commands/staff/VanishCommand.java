package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class VanishCommand extends BaseCommand {

    public VanishCommand() {
        super("vanish", Collections.singletonList("v"), "lazarus.vanish");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            if(!this.checkConsoleSender(sender)) return;
            Lazarus.getInstance().getVanishManager().toggleVanish((Player) sender);
            return;
        }

        if(args[0].equalsIgnoreCase("build")) {
            if(!this.checkConsoleSender(sender)) return;
            Lazarus.getInstance().getVanishManager().toggleVanishBuild((Player) sender);
            return;
        }

        if(!this.checkPermission(sender, "lazarus.vanish.others")) return;

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        Lazarus.getInstance().getVanishManager().toggleVanish(target);

        this.sendChangedMessage(sender, target, Lazarus.getInstance().getVanishManager().isVanished(target)
        ? Language.VANISH_ENABLED_OTHERS : Language.VANISH_DISABLED_OTHERS);
    }

    private void sendChangedMessage(CommandSender sender, Player target, String message) {
        Messages.sendMessage(Language.PREFIX + message
            .replace("<player>", sender.getName())
            .replace("<target>", target.getName()), "lazarus.staff");
    }
}
