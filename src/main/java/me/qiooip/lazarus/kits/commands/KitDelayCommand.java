package me.qiooip.lazarus.kits.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.kits.kit.KitData;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;

public class KitDelayCommand extends SubCommand {

	KitDelayCommand() {
		super("delay", "lazarus.kits.delay");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length < 2) {
			sender.sendMessage(Language.KIT_PREFIX + Language.KITS_SET_DELAY_USAGE);
			return;
		}
			
		KitData kit = Lazarus.getInstance().getKitsManager().getKit(args[0]);
			
		if(kit == null) {
			sender.sendMessage(Language.KIT_PREFIX + Language.KITS_EXCEPTION_DOESNT_EXISTS.replace("<kit>", args[0]));
			return;
		}

		Integer delay = StringUtils.tryParseInteger(args[1]);

		if(delay == null || delay != -1) {
			delay = StringUtils.parseSeconds(args[1]);

			if(delay == -1) {
				sender.sendMessage(Language.KIT_PREFIX + Language.COMMANDS_INVALID_DURATION);
				return;
			}
		}

		kit.setDelay(delay);

		sender.sendMessage(Language.KIT_PREFIX + Language.KITS_SET_DELAY_CHANGED.replace("<kit>", args[0])
			.replace("<delay>", StringUtils.formatDurationWords(delay * 1000L)));
	}
}
