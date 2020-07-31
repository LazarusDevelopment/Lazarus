package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MapkitCommand extends BaseCommand {

	public MapkitCommand() {
		super("mapkit", "lazarus.mapkit", true);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;

		if(args.length == 1 && args[0].equalsIgnoreCase("edit")) {
			if(!player.hasPermission("lazarus.mapkit.edit")) {
				player.sendMessage(Language.PREFIX + Language.COMMANDS_NO_PERMISSION);
				return;
			}

			Lazarus.getInstance().getMapkitHandler().setEditingMapkit(player);
		}

        player.openInventory(Lazarus.getInstance().getMapkitHandler().getMapkitInv());
	}
}
