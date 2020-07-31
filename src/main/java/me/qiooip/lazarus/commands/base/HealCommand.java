package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand extends BaseCommand {

    public HealCommand() {
        super("heal", "lazarus.heal");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            if(!this.checkConsoleSender(sender)) return;

            Player player = (Player) sender;
            player.setHealth(player.getMaxHealth());
            player.sendMessage(Language.PREFIX + Language.HEAL_MESSAGE_SELF);
            return;
        }

        if(!this.checkPermission(sender, "lazarus.heal.others")) return;

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        target.setHealth(target.getMaxHealth());

        target.sendMessage(Language.PREFIX + Language.HEAL_MESSAGE_SELF);
        sender.sendMessage(Language.PREFIX + Language.HEAL_MESSAGE_OTHERS.replace("<player>", target.getName()));
    }
}
