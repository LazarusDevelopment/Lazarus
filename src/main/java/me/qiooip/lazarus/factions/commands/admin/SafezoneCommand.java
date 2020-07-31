package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.SystemFaction;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SafezoneCommand extends SubCommand {

    public SafezoneCommand() {
        super("safezone", "lazarus.factions.safezone");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_SAFEZONE_USAGE);
            return;
        }

        Faction faction = FactionsManager.getInstance().getFactionByName(args[0]);

        if(faction == null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_DOESNT_EXIST.replace("<argument>", args[0]));
            return;
        }

        if(!(faction instanceof SystemFaction)) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_SAFEZONE_NOT_SYSTEM_FACTION);
            return;
        }

        SystemFaction systemFaction = (SystemFaction) faction;
        systemFaction.setSafezone(!systemFaction.isSafezone());

        ChatColor color = systemFaction.isSafezone() ? ChatColor.GREEN : ChatColor.RED;

        sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_SAFEZONE_CHANGED
            .replace("<faction>", faction.getDisplayName(sender))
            .replace("<safezone>", color.toString() + systemFaction.isSafezone()));
    }
}
