package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.factions.type.SystemFaction;
import org.bukkit.command.CommandSender;

public class RemoveCommand extends SubCommand {

    public RemoveCommand() {
        super("remove", "lazarus.factions.remove");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_REMOVE_USAGE);
            return;
        }

        Faction faction = FactionsManager.getInstance().getAnyFaction(args[0]);

        if(faction == null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_DOESNT_EXIST.replace("<argument>", args[0]));
            return;
        }

        if(!(faction instanceof PlayerFaction) && faction.getClass() != SystemFaction.class) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_REMOVE_CANNOT_REMOVE_THIS_TYPE);
            return;
        }

        if(!FactionsManager.getInstance().disbandFaction(faction.getId(), sender)) return;

        sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_REMOVED_SENDER.replace("<faction>", faction.getDisplayName(sender)));

        if(faction instanceof PlayerFaction) {
            PlayerFaction playerFaction = (PlayerFaction) faction;
            playerFaction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_REMOVED_FACTION.replace("<player>", sender.getName()));
        }
    }
}
