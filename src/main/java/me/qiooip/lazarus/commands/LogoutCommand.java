package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.scoreboard.LogoutTimer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LogoutCommand extends BaseCommand {

	public LogoutCommand() {
		super("logout", "lazarus.logout", true);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		LogoutTimer logoutTimer = TimerManager.getInstance().getLogoutTimer();

		if(logoutTimer.isActive(player)) {
			player.sendMessage(Language.PREFIX + Language.LOGOUT_ALREADY_RUNNING);
			return;
		}

		logoutTimer.activate(player);

		player.sendMessage(Language.PREFIX + Language.LOGOUT_START_MESSAGE
			.replace("<seconds>", String.valueOf(Config.LOGOUT_DELAY)));
	}
}
