package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.type.SystemFaction;
import me.qiooip.lazarus.selection.Selection;
import me.qiooip.lazarus.selection.SelectionType;
import me.qiooip.lazarus.utils.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimForCommand extends SubCommand {

    public ClaimForCommand() {
        super("claimfor", "lazarus.factions.claimfor", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_FOR_USAGE);
            return;
        }

        Faction faction = FactionsManager.getInstance().getAnyFaction(args[0]);

        if(faction == null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_DOESNT_EXIST.replace("<argument>", args[0]));
            return;
        }

        if(!(faction instanceof SystemFaction)) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_FOR_NOT_SYSTEM_FACTION);
            return;
        }

        Player player = (Player) sender;
        Selection selection = Lazarus.getInstance().getSelectionManager().getSelection(player);

        if(selection == null || selection.getType() != SelectionType.SYSTEM_CLAIM) {
            Lazarus.getInstance().getSelectionManager().toggleSelectionProcess(player, SelectionType.SYSTEM_CLAIM, null);
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_FOR_MAKE_A_SELECTION);
            return;
        }

        if(!selection.areBothPositionsSet()) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_FOR_SET_BOTH_POSITIONS);
            return;
        }

        if(!ClaimManager.getInstance().getClaimsInSelection(selection.getPosOne(), selection.getPosTwo()).isEmpty()) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_FOR_CLAIM_OVERLAPPING);
            return;
        }

        if(!ClaimManager.getInstance().addClaim(selection.toClaim(faction))) return;
        Lazarus.getInstance().getSelectionManager().removeSelectionProcess(player);

        Messages.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_FOR_CLAIM_CLAIMED
            .replace("<player>", player.getName())
            .replace("<faction>", faction.getDisplayName(sender)), "lazarus.staff");
    }
}
