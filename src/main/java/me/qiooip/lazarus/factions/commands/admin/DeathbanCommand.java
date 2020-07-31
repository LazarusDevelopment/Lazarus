package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.SystemFaction;
import org.bukkit.command.CommandSender;

public class DeathbanCommand extends SubCommand {

    public DeathbanCommand() {
        super("deathban", "lazarus.factions.deathban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_DEATHBAN_USAGE);
            return;
        }

        Faction faction = FactionsManager.getInstance().getFactionByName(args[0]);

        if(faction == null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_DOESNT_EXIST.replace("<argument>", args[0]));
            return;
        }

        if(!(faction instanceof SystemFaction)) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_DEATHBAN_NOT_SYSTEM_FACTION);
            return;
        }

        faction.setDeathban(!faction.isDeathban());

        sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_DEATHBAN_CHANGED
            .replace("<faction>", faction.getDisplayName(sender))
            .replace("<deathban>", faction.getDeathbanString()));
    }
}
