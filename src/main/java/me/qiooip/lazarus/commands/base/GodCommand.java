package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GodCommand extends BaseCommand {

    public GodCommand() {
        super("god", "lazarus.god");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            if(!this.checkConsoleSender(sender)) return;

            Player player = (Player) sender;
            NmsUtils.getInstance().toggleInvulnerable(player);

            player.sendMessage(Language.PREFIX + (NmsUtils.getInstance().isInvulnerable(player)
            ? Language.GOD_SELF_ENABLED : Language.GOD_SELF_DISABLED));
            return;
        }

        if(!this.checkPermission(sender, "lazarus.god.others")) return;

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        NmsUtils.getInstance().toggleInvulnerable(target);

        target.sendMessage(Language.PREFIX + (NmsUtils.getInstance().isInvulnerable(target)
            ? Language.GOD_SELF_ENABLED : Language.GOD_SELF_DISABLED));

        sender.sendMessage(Language.PREFIX + (NmsUtils.getInstance().isInvulnerable(target)
            ? Language.GOD_OTHERS_ENABLED : Language.GOD_OTHERS_DISABLED).replace("<player>", target.getName()));
    }
}
