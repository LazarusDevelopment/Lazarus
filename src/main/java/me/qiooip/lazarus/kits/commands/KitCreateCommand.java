package me.qiooip.lazarus.kits.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.kits.kit.KitData;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCreateCommand extends SubCommand {

	KitCreateCommand() {
		super("create", "lazarus.kits.create", true);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		
		if(args.length < 2) {
            player.sendMessage(Language.KIT_PREFIX + Language.KITS_CREATE_USAGE);
		    return;
		}
			
		KitData kit = Lazarus.getInstance().getKitsManager().getKit(args[1]);

		if(kit != null) {
            player.sendMessage(Language.KIT_PREFIX + Language.KITS_EXCEPTION_ALREADY_EXISTS.replace("<kit>", kit.getName()));
            return;
		}

		int delay = StringUtils.parseSeconds(args[1]);

		if(delay == -1) {
			sender.sendMessage(Language.KIT_PREFIX + Language.COMMANDS_INVALID_DURATION);
			return;
		}

		Lazarus.getInstance().getKitsManager().createKit(args[0], delay);

		player.sendMessage(Language.KIT_PREFIX + Language.KITS_CREATE_CREATED.replace("<kit>", args[0]));
	}
}
