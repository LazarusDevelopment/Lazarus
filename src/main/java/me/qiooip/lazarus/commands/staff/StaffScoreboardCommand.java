package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.scoreboard.ScoreboardManager;
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
		ScoreboardManager.getInstance().toggleStaffScoreboard(player);
	}
}
