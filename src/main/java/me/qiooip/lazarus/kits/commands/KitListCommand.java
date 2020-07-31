package me.qiooip.lazarus.kits.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import org.bukkit.command.CommandSender;

public class KitListCommand extends SubCommand {

	KitListCommand() {
		super("list", "lazarus.kits.list");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Lazarus.getInstance().getKitsManager().listKits(sender);
	}
}
