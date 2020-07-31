package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.SystemType;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class SystemCreateCommand extends SubCommand {

    public SystemCreateCommand() {
        super("systemcreate", Collections.singletonList("createsystem"), "lazarus.factions.systemcreate");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_SYSTEM_CREATE_USAGE);
            return;
        }

        if(FactionsManager.getInstance().getFactionByName(args[0]) != null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_ALREADY_EXISTS.replace("<name>", args[0]));
            return;
        }

        if(FactionsManager.getInstance().createSystemFaction(args[0], SystemType.DEFAULT, sender) == null) return;

        sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_SYSTEM_CREATED.replace("<name>", args[0]));
    }
}
