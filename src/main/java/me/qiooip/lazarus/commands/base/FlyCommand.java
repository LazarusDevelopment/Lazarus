package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand extends BaseCommand {

    public FlyCommand() {
        super("fly", "lazarus.fly");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            if(!this.checkConsoleSender(sender)) return;

            Player player = (Player) sender;
            player.setAllowFlight(!player.getAllowFlight());

            player.sendMessage(Language.PREFIX + (player.getAllowFlight()
                ? Language.FLY_SELF_ENABLED : Language.FLY_SELF_DISABLED));
            return;
        }

        if(!this.checkPermission(sender, "lazarus.fly.others")) return;

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        target.setAllowFlight(!target.getAllowFlight());

        target.sendMessage(Language.PREFIX + (target.getAllowFlight()
            ? Language.FLY_SELF_ENABLED : Language.FLY_SELF_DISABLED));

        sender.sendMessage(Language.PREFIX + (target.getAllowFlight()
            ? Language.FLY_OTHERS_ENABLED
            : Language.FLY_OTHERS_DISABLED).replace("<player>", target.getName()));
    }
}
