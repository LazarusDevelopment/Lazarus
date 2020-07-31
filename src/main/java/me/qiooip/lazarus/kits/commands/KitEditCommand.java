package me.qiooip.lazarus.kits.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.kits.kit.KitData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitEditCommand extends SubCommand {

	KitEditCommand() {
		super("edit", "lazarus.kits.edit", true);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		
		if(args.length == 0) {
            player.sendMessage(Language.KIT_PREFIX + Language.KITS_EDIT_USAGE);
            return;
		}
			
		KitData kit = Lazarus.getInstance().getKitsManager().getKit(args[0]);
			
		if(kit == null) {
		    sender.sendMessage(Language.KIT_PREFIX + Language.KITS_EXCEPTION_DOESNT_EXISTS.replace("<kit>", args[0]));
		    return;
		}
				
		Lazarus.getInstance().getKitsManager().editKit(player, kit);
	}
}
