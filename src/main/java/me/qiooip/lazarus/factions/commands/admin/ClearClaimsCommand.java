package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.event.FactionClaimChangeEvent.ClaimChangeReason;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.command.CommandSender;

public class ClearClaimsCommand extends SubCommand {

    public ClearClaimsCommand() {
        super("clearclaims", "lazarus.factions.clearclaims");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLEAR_CLAIMS_USAGE);
            return;
        }

        Faction faction = FactionsManager.getInstance().getAnyFaction(args[0]);

        if(faction == null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_DOESNT_EXIST.replace("<argument>", args[0]));
            return;
        }

        if(!ClaimManager.getInstance().removeAllClaims(faction, ClaimChangeReason.UNCLAIM_ALL)) return;

        sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLEAR_CLAIMS_CLEARED_SENDER.replace("<faction>", faction.getDisplayName(sender)));

        if(!(faction instanceof PlayerFaction)) return;

        PlayerFaction playerFaction = (PlayerFaction) faction;
        playerFaction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLEAR_CLAIMS_CLEARED_FACTION.replace("<player>", sender.getName()));

        playerFaction.setHome(null);
        FactionsManager.getInstance().checkHomeTeleports(playerFaction, Language.FACTIONS_CLEAR_CLAIMS_HOME_TELEPORT_CANCELLED);
    }
}
