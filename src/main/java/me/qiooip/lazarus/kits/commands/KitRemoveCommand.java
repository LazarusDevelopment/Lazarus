package me.qiooip.lazarus.kits.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.kits.kit.KitData;
import me.qiooip.lazarus.kits.kit.KitType;
import org.bukkit.command.CommandSender;

public class KitRemoveCommand extends SubCommand {

	KitRemoveCommand() {
		super("remove", "lazarus.kits.remove");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length == 0) {
            sender.sendMessage(Language.KIT_PREFIX + Language.KITS_REMOVE_USAGE);
			return;
		}
			
		KitData kit = Lazarus.getInstance().getKitsManager().getKit(args[0]);
			
		if(kit == null) {
		    sender.sendMessage(Language.KIT_PREFIX + Language.KITS_EXCEPTION_DOESNT_EXISTS.replace("<kit>", args[0]));
		    return;
		}

		if(kit.getType() == KitType.SPECIAL) {
			sender.sendMessage(Language.KIT_PREFIX + Language.KITS_REMOVE_CANNOT_REMOVE_SPECIAL_EVENT_KIT);
			return;
		}

		Lazarus.getInstance().getKitsManager().removeKit(kit);
		sender.sendMessage(Language.KIT_PREFIX + Language.KITS_REMOVE_REMOVED.replace("<kit>", kit.getName()));
	}
}
