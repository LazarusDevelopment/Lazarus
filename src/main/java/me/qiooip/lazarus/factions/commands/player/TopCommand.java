package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.handlers.leaderboard.entry.UuidCacheEntry;
import me.qiooip.lazarus.handlers.leaderboard.type.FactionLeaderboardType;
import me.qiooip.lazarus.handlers.leaderboard.type.LeaderboardType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.NavigableSet;

public class TopCommand extends SubCommand {

    public TopCommand() {
        super("top");

        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 1) {
            sender.sendMessage(Language.PREFIX + Language.LEADERBOARDS_FACTION_COMMAND_USAGE);
            return;
        }

        LeaderboardType type = FactionLeaderboardType.getByName(args[0]);

        if(type == null) {
            sender.sendMessage(Language.PREFIX + Language.LEADERBOARDS_TYPE_DOESNT_EXIST
                .replace("<type>", args[0]));
            return;
        }

        NavigableSet<UuidCacheEntry<Integer>> leaderboard = type.getLeaderboard();

        if(leaderboard.isEmpty()) {
            sender.sendMessage(Language.PREFIX + Language.LEADERBOARDS_NO_LEADERBOARDS);
            return;
        }

        Player player = (Player) sender;
        String title = type.getTitle();
        String lineFormat = type.getLineFormat();

        int index = 1;

        sender.sendMessage(Language.LEADERBOARDS_COMMAND_HEADER);
        sender.sendMessage(title);

        for(UuidCacheEntry<Integer> entry : leaderboard) {
            Faction faction = FactionsManager.getInstance().getFactionByUuid(entry.getKey());

            String factionName = faction.getName(sender);
            String hoverText = Language.FACTIONS_SHOW_HOVER_TEXT.replace("<faction>", factionName);

            ComponentBuilder message = new ComponentBuilder(lineFormat
                .replace("<number>", String.valueOf(index))
                .replace("<faction>", factionName)
                .replace("<value>", String.valueOf(entry.getValue())))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f show " + faction.getName()));

            player.spigot().sendMessage(message.create());
            index++;
        }

        sender.sendMessage(Language.LEADERBOARDS_COMMAND_FOOTER);
    }
}
