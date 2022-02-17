package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.event.LazarusKickEvent;
import me.qiooip.lazarus.handlers.event.LazarusKickEvent.KickType;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class KickallCommand extends BaseCommand {

    public KickallCommand() {
        super("kickall", "lazarus.kickall");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.PREFIX + Language.KICKALL_USAGE);
            return;
        }

        String reason = Color.translate("&c" + StringUtils.joinArray(args, " ", 1));
        Lazarus.getInstance().getCombatLoggerHandler().setKickAll(true);

        Bukkit.getOnlinePlayers().stream().filter(player -> !player.hasPermission("lazarus.staff")).forEach(player -> {
            LazarusKickEvent event = new LazarusKickEvent(player, KickType.KICKALL, reason);

            if(!event.isCancelled()) {
                player.kickPlayer(reason);
            }
        });

        Lazarus.getInstance().getCombatLoggerHandler().setKickAll(false);

        Messages.sendMessage(Language.PREFIX + Language.KICKALL_STAFF_MESSAGE
            .replace("<player>", sender.getName())
            .replace("<reason>", reason));
    }
}
