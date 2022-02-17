package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.scoreboard.SotwTimer;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SotwCommand extends BaseCommand {

	public SotwCommand() {
		super("sotw");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length == 0) {
			(sender.hasPermission("lazarus.sotw")
				? Language.SOTW_ADMIN_COMMAND_USAGE
				: Language.SOTW_PLAYER_COMMAND_USAGE).forEach(sender::sendMessage);
			return;
		}

		if(args.length == 1) {
			switch(args[0].toLowerCase()) {
				case "start": {
					if(!this.checkPermission(sender, "lazarus.sotw")) return;
					Lazarus.getInstance().getSotwHandler().startSotwTimer(sender, Config.SOTW_DEFAULT_TIME * 60);
					return;
				}
				case "stop": {
					if(!this.checkPermission(sender, "lazarus.sotw")) return;
					Lazarus.getInstance().getSotwHandler().stopSotwTimer(sender);
					return;
				}
				case "hideplayers": {
					if(!this.checkPermission(sender, "lazarus.sotw")) return;
					Tasks.async(() -> Lazarus.getInstance().getSotwHandler().toggleSotwInvisibility(sender, null));
					return;
				}
				case "enable": {
					if(!this.checkConsoleSender(sender) || !this.checkPermission(sender, "lazarus.sotw.enable")) return;
					Lazarus.getInstance().getSotwHandler().enableSotwForPlayer((Player) sender);
					return;
				}
				case "time": {
					SotwTimer sotwTimer = TimerManager.getInstance().getSotwTimer();

					if(!sotwTimer.isActive()) {
						sender.sendMessage(Language.PREFIX + Language.SOTW_NOT_RUNNING);
						return;
					}

					sender.sendMessage(Language.PREFIX + Language.SOTW_TIME_STATUS
						.replace("<time>", sotwTimer.getDynamicTimeLeft()));
					return;
				}
				default: {
					(sender.hasPermission("lazarus.sotw")
						? Language.SOTW_ADMIN_COMMAND_USAGE
						: Language.SOTW_PLAYER_COMMAND_USAGE).forEach(sender::sendMessage);
				}
			}
		} else {
			if(!this.checkPermission(sender, "lazarus.sotw")) return;

			if(args[0].equalsIgnoreCase("start")) {
				int time = StringUtils.parseSeconds(args[1]);

				if(time == -1) {
					sender.sendMessage(Language.KIT_PREFIX + Language.COMMANDS_INVALID_DURATION);
					return;
				}

				Lazarus.getInstance().getSotwHandler().startSotwTimer(sender, time);
			} else {
				Language.SOTW_ADMIN_COMMAND_USAGE.forEach(sender::sendMessage);
			}
		}
	}
}
