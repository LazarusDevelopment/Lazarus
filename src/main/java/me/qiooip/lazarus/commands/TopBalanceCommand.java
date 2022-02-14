package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.handlers.leaderboard.LeaderboardHandler;
import me.qiooip.lazarus.handlers.leaderboard.type.PlayerLeaderboardType;
import org.bukkit.command.CommandSender;

public class TopBalanceCommand extends BaseCommand {

    public TopBalanceCommand() {
        super("topbalance", "lazarus.topbalance");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        LeaderboardHandler handler = Lazarus.getInstance().getLeaderboardHandler();
        handler.sendLeaderboardMessage(sender, PlayerLeaderboardType.BALANCE);
    }
}
