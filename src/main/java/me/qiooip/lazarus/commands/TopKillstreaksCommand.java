package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.leaderboard.LeaderboardHandler;
import me.qiooip.lazarus.handlers.leaderboard.type.PlayerLeaderboardType;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class TopKillstreaksCommand extends BaseCommand {

    public TopKillstreaksCommand() {
        super("topkillstreak", Collections.singletonList("topkillstreaks"), "lazarus.topkillstreak");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!Config.KITMAP_MODE_ENABLED) {
            sender.sendMessage(Language.PREFIX + Language.LEADERBOARDS_KITMAP_MODE_ONLY);
            return;
        }

        LeaderboardHandler handler = Lazarus.getInstance().getLeaderboardHandler();
        handler.sendLeaderboardMessage(sender, PlayerLeaderboardType.HIGHEST_KILLSTREAK);
    }
}
