package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.scoreboard.PvpProtTimer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvpCommand extends BaseCommand {

    public PvpCommand() {
        super("pvp", "lazarus.pvp", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            Language.PVP_COMMAND_USAGE.forEach(sender::sendMessage);
            return;
        }

        Player player = (Player) sender;

        switch(args[0].toLowerCase()) {
            case "enable": {
                PvpProtTimer timer = TimerManager.getInstance().getPvpProtTimer();

                if(!timer.isActive(player)) {
                    player.sendMessage(Language.PREFIX + Language.PVP_COMMAND_NOT_ACTIVE);
                    return;
                }

                timer.cancel(player);
                player.sendMessage(Language.PREFIX + Language.PVP_COMMAND_PROTECTION_DISABLED);
                return;
            }
            case "time": {
                PvpProtTimer timer = TimerManager.getInstance().getPvpProtTimer();

                if(!timer.isActive(player)) {
                    player.sendMessage(Language.PREFIX + Language.PVP_COMMAND_NOT_ACTIVE);
                    return;
                }

                player.sendMessage(Language.PREFIX + Language.PVP_COMMAND_TIME_STATUS
                    .replace("<time>", timer.getDynamicTimeLeft(player)));
                return;
            }
            default: Language.PVP_COMMAND_USAGE.forEach(player::sendMessage);
        }
    }
}
