package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.handlers.chat.ChatHandler;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.cooldown.CooldownTimer;
import me.qiooip.lazarus.utils.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LffCommand extends BaseCommand {

    public LffCommand() {
        super("lff", "lazarus.lff", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(FactionsManager.getInstance().getPlayerFaction(player) != null) {
            player.sendMessage(Language.PREFIX + Language.LFF_COMMAND_ALREADY_IN_FACTION);
            return;
        }

        CooldownTimer timer = TimerManager.getInstance().getCooldownTimer();

        if(timer.isActive(player, "LFF")) {
            player.sendMessage(Language.PREFIX + Language.LFF_COMMAND_COOLDOWN
                .replace("<seconds>", timer.getTimeLeft(player, "LFF")));
            return;
        }

        String rankPrefix = ChatHandler.getInstance().getPrefix(player);

        Language.LFF_COMMAND_MESSAGE.forEach(line -> Messages.sendMessage(line
            .replace("<player>", player.getName())
            .replace("<rankPrefix>", rankPrefix)));

        timer.activate(player, "LFF", Config.LFF_COMMAND_COOLDOWN,
            Language.PREFIX + Language.LFF_COMMAND_COOLDOWN_EXPIRED);
    }
}
