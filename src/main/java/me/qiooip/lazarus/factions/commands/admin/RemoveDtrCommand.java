package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class RemoveDtrCommand extends SubCommand {

    public RemoveDtrCommand() {
        super("removedtr", Collections.singletonList("dtrremove"), "lazarus.factions.dtr");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_REMOVE_DTR_USAGE);
            return;
        }

        PlayerFaction faction = FactionsManager.getInstance().searchForFaction(args[0]);

        if(faction == null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_DOESNT_EXIST.replace("<argument>", args[0]));
            return;
        }

        if(!StringUtils.isDouble(args[1])) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.COMMANDS_INVALID_NUMBER);
            return;
        }

        faction.setDtr(faction.getDtr() - Double.parseDouble(args[1]));

        sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_REMOVE_DTR_CHANGED_SENDER
            .replace("<faction>", faction.getName()).replace("<value>", faction.getDtrString()));

        faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_REMOVE_DTR_CHANGED_FACTION
            .replace("<player>", sender.getName()).replace("<dtr>", faction.getDtrString()));
    }
}