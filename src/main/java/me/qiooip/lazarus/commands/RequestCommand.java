package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.cooldown.CooldownTimer;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class RequestCommand extends BaseCommand {

    public RequestCommand() {
        super("request", Collections.singletonList("helpop"), "lazarus.request", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.PREFIX + Language.REQUEST_USAGE);
            return;
        }

        Player player = (Player) sender;
        CooldownTimer timer = TimerManager.getInstance().getCooldownTimer();

        if(timer.isActive(player, "REQUEST")) {
            player.sendMessage(Language.PREFIX + Language.REQUEST_COOLDOWN
                .replace("<seconds>", timer.getTimeLeft(player, "REQUEST")));
            return;
        }

        String message = StringUtils.joinArray(args, " ", 1);

        Language.REQUEST_FORMAT.forEach(line ->
            Messages.sendMessage(line
                .replace("<player>", player.getName())
                .replace("<message>", message), "lazarus.request.receive")
        );

        player.sendMessage(Language.PREFIX + Language.REQUEST_REQUESTED);

        timer.activate(player, "REQUEST", Config.REQUEST_COOLDOWN,
            Language.PREFIX + Language.REQUEST_COOLDOWN_EXPIRED);
    }
}
