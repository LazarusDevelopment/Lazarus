package me.qiooip.lazarus.games.dtc.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.selection.Selection;
import me.qiooip.lazarus.selection.SelectionType;
import me.qiooip.lazarus.utils.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DtcAreaCommand extends SubCommand {

    DtcAreaCommand() {
        super("area", "lazarus.dtc.area", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Selection selection = Lazarus.getInstance().getSelectionManager().getSelection(player);

        if(selection == null || selection.getType() != SelectionType.SYSTEM_CLAIM) {
            Lazarus.getInstance().getSelectionManager().toggleSelectionProcess(player, SelectionType.SYSTEM_CLAIM, null);
            player.sendMessage(Language.DTC_PREFIX + Language.DTC_AREA_MAKE_A_SELECTION);
            return;
        }

        if(!selection.areBothPositionsSet()) {
            player.sendMessage(Language.DTC_PREFIX + Language.DTC_AREA_SET_BOTH_POSITIONS);
            return;
        }

        if(!ClaimManager.getInstance().getClaimsInSelection(selection.getPosOne(), selection.getPosTwo()).isEmpty()) {
            player.sendMessage(Language.DTC_PREFIX + Language.DTC_AREA_CLAIM_OVERLAPPING);
            return;
        }

        Faction faction = FactionsManager.getInstance().getFactionByName("DTC");

        if(!ClaimManager.getInstance().addClaim(selection.toClaim(faction))) return;
        Lazarus.getInstance().getSelectionManager().removeSelectionProcess(player);

        Messages.sendMessage(Language.DTC_PREFIX + Language.DTC_AREA_CREATED
        .replace("<player>", player.getName()), "lazarus.staff");
    }
}
