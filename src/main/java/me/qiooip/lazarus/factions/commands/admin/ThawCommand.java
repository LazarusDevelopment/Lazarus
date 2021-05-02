package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.timer.TimerManager;
import org.bukkit.command.CommandSender;

public class ThawCommand extends SubCommand {

    public ThawCommand() {
        super("thaw", "lazarus.factions.thaw");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 1) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_THAW_USAGE);
            return;
        }

        PlayerFaction faction = FactionsManager.getInstance().searchForFaction(args[0]);

        if(faction == null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_DOESNT_EXIST.replace("<argument>", args[0]));
            return;
        }

        TimerManager.getInstance().getFactionFreezeTimer().cancel(faction);
        TimerManager.getInstance().getDtrRegenTimer().addFaction(faction, faction.getDtr());

        sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_THAW_CHANGED_SENDER
            .replace("<faction>", faction.getName()));

        faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_THAW_CHANGED_FACTION
            .replace("<player>", sender.getName()));
    }
}
