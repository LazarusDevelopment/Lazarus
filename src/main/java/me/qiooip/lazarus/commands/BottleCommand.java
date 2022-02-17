package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.PlayerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BottleCommand extends BaseCommand {

	public BottleCommand() {
		super("bottle", "lazarus.bottle", true);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		
		if(args.length == 0) {
			player.sendMessage(Language.PREFIX + Language.BOTTLE_USAGE);
			return;
		}
			
		if(!this.checkNumber(player, args[0])) return;

		int level = Math.abs(Integer.parseInt(args[0]));
		ItemStack bottle = Lazarus.getInstance().getBottleHandler().getBottle(player, level);

		if(bottle == null) {
			player.sendMessage(Language.PREFIX + Language.BOTTLE_NOT_ENOUGH_LEVELS
				.replace("<level>", String.valueOf(level)));
			return;
		}

		player.setLevel(player.getLevel() - level);
		PlayerUtils.addToInventoryOrDropToFloor(player, bottle);
	}
}
