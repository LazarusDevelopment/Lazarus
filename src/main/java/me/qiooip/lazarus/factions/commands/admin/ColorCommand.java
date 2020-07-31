package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.SystemFaction;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ColorCommand extends SubCommand {

    public ColorCommand() {
        super("color", "lazarus.factions.color");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_COLOR_USAGE);
            return;
        }

        Faction faction = FactionsManager.getInstance().getFactionByName(args[0]);

        if(faction == null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_DOESNT_EXIST.replace("<argument>", args[0]));
            return;
        }

        if(!(faction instanceof SystemFaction)) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_COLOR_NOT_SYSTEM_FACTION);
            return;
        }

        ChatColor color;

        try {
            if(args[1].length() == 1) {
                color = ChatColor.getByChar(args[1].toLowerCase());
            } else if(args[1].length() == 2 && args[1].startsWith("&")) {
                color = ChatColor.getByChar(args[1].replace("&", "").toLowerCase());
            } else {
                color = ChatColor.valueOf(args[1].toUpperCase());
            }
        } catch(IllegalArgumentException e) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_COLOR_DOESNT_EXIST.replace("<color>", args[1]));
            return;
        }

        SystemFaction systemFaction = (SystemFaction) faction;
        systemFaction.setColor(color);

        sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_COLOR_CHANGED
            .replace("<faction>", faction.getDisplayName(sender))
            .replace("<color>", color + color.name()));
    }
}
