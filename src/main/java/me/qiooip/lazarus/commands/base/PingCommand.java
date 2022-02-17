package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand extends BaseCommand {

    public PingCommand() {
        super("ping", "lazarus.ping");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            if(!this.checkConsoleSender(sender)) return;

            Player player = (Player) sender;

            player.sendMessage(Language.PREFIX + Language.PING_MESSAGE_SELF.replace("<ping>",
            String.valueOf(NmsUtils.getInstance().getPing(player))));
            return;
        }

        if(!this.checkPermission(sender, "lazarus.ping.others")) return;

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        sender.sendMessage(Language.PREFIX + Language.PING_MESSAGE_OTHERS
            .replace("<player>", target.getName())
            .replace("<ping>", String.valueOf(NmsUtils.getInstance().getPing(target))));
    }
}
