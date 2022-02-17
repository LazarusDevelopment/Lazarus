package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.scoreboard.AppleTimer;
import me.qiooip.lazarus.timer.scoreboard.GAppleTimer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class GoldenAppleCommand extends BaseCommand {

    public GoldenAppleCommand() {
        super("goldenapple", Collections.singletonList("gapple"), "lazarus.goldenapple", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        AppleTimer appleTimer = TimerManager.getInstance().getAppleTimer();
        GAppleTimer gAppleTimer = TimerManager.getInstance().getGAppleTimer();

        if(!appleTimer.isActive(player) && !gAppleTimer.isActive(player)) {
            player.sendMessage(Language.PREFIX + Language.GOLDEN_APPLE_NO_COOLDOWNS);
            return;
        }

        if(appleTimer.isActive(player)) {
            player.sendMessage(Language.PREFIX + Language.NORMAL_APPLE_COMMAND_MESSAGE
                .replace("<time>", appleTimer.getTimeLeft(player)));
        }

        if(gAppleTimer.isActive(player)) {
            player.sendMessage(Language.PREFIX + Language.ENCHANTED_APPLE_COMMAND_MESSAGE
                .replace("<time>", gAppleTimer.getDynamicTimeLeft(player)));
        }
    }
}
