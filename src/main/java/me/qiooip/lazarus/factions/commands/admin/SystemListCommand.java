package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.SystemFaction;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SystemListCommand extends SubCommand {

    public SystemListCommand() {
        super("systemlist", Collections.singletonList("listsystem"), "lazarus.factions.systemlist");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        List<Faction> factions = FactionsManager.getInstance().getFactions()
            .values().stream().filter(faction -> faction instanceof SystemFaction)
            .sorted(Comparator.comparing(faction -> faction.getClass().getSimpleName()))
            .collect(Collectors.toList());

        Language.FACTIONS_SYSTEM_LIST_HEADER.forEach(line -> sender.sendMessage(line
            .replace("<count>", String.valueOf(factions.size()))));

        for(int i = 0; i < factions.size(); i++) {
            SystemFaction faction = (SystemFaction) factions.get(i);

            sender.sendMessage(Language.FACTIONS_SYSTEM_LIST_FACTION_FORMAT
                .replace("<number>", String.valueOf(i + 1))
                .replace("<name>", faction.getName(sender)));
        }

        sender.sendMessage(Language.FACTIONS_SYSTEM_LIST_FOOTER);
    }
}
