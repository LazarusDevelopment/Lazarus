package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.handlers.leaderboard.LeaderboardHandler;
import me.qiooip.lazarus.handlers.leaderboard.LeaderboardType;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class TopKillstreaksCommand extends BaseCommand {

    public TopKillstreaksCommand() {
        super("topkillstreaks", Collections.singletonList("topkillstreak"), "lazarus.topkillstreaks");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        LeaderboardHandler handler = Lazarus.getInstance().getLeaderboardHandler();
        handler.sendLeaderboardMessage(sender, LeaderboardType.HIGHEST_KILLSTREAK);
    }
}
