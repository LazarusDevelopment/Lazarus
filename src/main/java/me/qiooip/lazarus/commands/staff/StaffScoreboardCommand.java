package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class StaffScoreboardCommand extends BaseCommand {

	public StaffScoreboardCommand() {
		super("staffscoreboard", Collections.singletonList("staffsb"), "lazarus.staffscoreboard", true);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		
		Lazarus.getInstance().getScoreboardManager().toggleStaffScoreboard(player);
	}
}
