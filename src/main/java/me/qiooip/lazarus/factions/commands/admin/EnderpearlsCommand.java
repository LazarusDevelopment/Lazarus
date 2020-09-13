package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.SystemFaction;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class EnderpearlsCommand extends SubCommand {

    public EnderpearlsCommand() {
        super("enderpearls", Arrays.asList("enderpearl", "ep"), "lazarus.factions.enderpearls");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_ENDERPEARLS_USAGE);
            return;
        }

        Faction faction = FactionsManager.getInstance().getFactionByName(args[0]);

        if(faction == null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_DOESNT_EXIST.replace("<argument>", args[0]));
            return;
        }

        if(!(faction instanceof SystemFaction)) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_ENDERPEARLS_NOT_SYSTEM_FACTION);
            return;
        }

        SystemFaction systemFaction = (SystemFaction) faction;
        systemFaction.setEnderpearls(!systemFaction.isEnderpearls());

        sender.sendMessage(Language.FACTION_PREFIX + (systemFaction.isEnderpearls()
            ? Language.FACTIONS_ENDERPEARLS_ENABLED
            : Language.FACTIONS_ENDERPEARLS_DISABLED)
        .replace("<faction>", systemFaction.getDisplayName(sender)));
    }
}
