package me.qiooip.lazarus.kits.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.kits.kit.KitData;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitGiveCommand extends SubCommand {

	KitGiveCommand() {
		super("give", "lazarus.kits.give");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length < 2) {
            sender.sendMessage(Language.KIT_PREFIX + Language.KITS_GIVE_USAGE);
            return;
		}

		Player target = Bukkit.getPlayer(args[0]);
		if(!this.checkPlayer(sender, target, args[0])) return;
				
		KitData kit = Lazarus.getInstance().getKitsManager().getKit(args[1]);
				
		if(kit == null) {
		    sender.sendMessage(Language.KIT_PREFIX + Language.KITS_EXCEPTION_DOESNT_EXISTS.replace("<kit>", args[1]));
		    return;
		}

		Lazarus.getInstance().getKitsManager().giveKitWithCommand(sender, target, kit);
	}
}
