package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.leaderboard.type.LeaderboardType;
import me.qiooip.lazarus.handlers.leaderboard.type.PlayerLeaderboardType;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardCommand extends BaseCommand {

    public LeaderboardCommand() {
        super("leaderboard", Collections.singletonList("lb"), "lazarus.leaderboard");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 1) {
            sender.sendMessage(Language.PREFIX + Language.LEADERBOARDS_COMMAND_USAGE);
            return;
        }

        LeaderboardType type = PlayerLeaderboardType.getByName(args[0]);

        if(type == null) {
            sender.sendMessage(Language.PREFIX + Language.LEADERBOARDS_TYPE_DOESNT_EXIST
                .replace("<type>", args[0]));
            return;
        }

        if(type == PlayerLeaderboardType.HIGHEST_KILLSTREAK && !Config.KITMAP_MODE_ENABLED) {
            sender.sendMessage(Language.PREFIX + Language.LEADERBOARDS_KITMAP_MODE_ONLY);
            return;
        }

        Lazarus.getInstance().getLeaderboardHandler().sendLeaderboardMessage(sender, type);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        if(args.length != 1 || !sender.hasPermission("lazarus.leaderboard")) {
            return super.tabComplete(sender, alias, args);
        }

        List<String> completions = new ArrayList<>();

        for(PlayerLeaderboardType type : PlayerLeaderboardType.values()) {
            String param = type.getParamName();
            if(!param.startsWith(args[0].toUpperCase())) continue;

            completions.add(param.toLowerCase());
        }

        return completions;
    }
}
