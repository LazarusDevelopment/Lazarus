package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.SystemFaction;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class SystemRenameCommand extends SubCommand {

    public SystemRenameCommand() {
        super("systemrename", Collections.singletonList("renamesystem"), "lazarus.factions.systemrename");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_SYSTEM_RENAME_USAGE);
            return;
        }

        Faction faction = FactionsManager.getInstance().getFactionByName(args[0]);

        if(faction == null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_DOESNT_EXIST.replace("<argument>", args[0]));
            return;
        }

        if(!(faction instanceof SystemFaction)) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_SYSTEM_RENAME_NOT_SYSTEM_FACTION);
            return;
        }

        if(faction.getClass() != SystemFaction.class) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_SYSTEM_RENAME_CANNOT_RENAME_THIS_TYPE);
            return;
        }

        if(faction.getName().equalsIgnoreCase(args[1])) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_SYSTEM_RENAME_SAME_NAME.replace("<name>", args[1]));
            return;
        }

        if(FactionsManager.getInstance().getFactionByName(args[1]) != null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_ALREADY_EXISTS.replace("<name>", args[1]));
            return;
        }

        String oldName = faction.getName(sender);

        if(!faction.setName(sender, args[1], true)) return;

        sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_SYSTEM_RENAMED.replace("<faction>",
        oldName).replace("<name>", ((SystemFaction) faction).getColor() + args[1]));
    }
}
