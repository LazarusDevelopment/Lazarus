package me.qiooip.lazarus.games.mountain.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.games.mountain.MountainType;
import me.qiooip.lazarus.selection.Selection;
import me.qiooip.lazarus.selection.SelectionType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class MountainCreateCommand extends SubCommand {

    MountainCreateCommand() {
        super("create", Collections.singletonList("new"), "lazarus.mountain.create", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.MOUNTAIN_PREFIX + Language.MOUNTAIN_CREATE_USAGE);
            return;
        }

        MountainType type;
        try {
            type = MountainType.valueOf(args[0].toUpperCase());
        } catch(IllegalArgumentException e) {
            sender.sendMessage(Language.MOUNTAIN_PREFIX + Language.MOUNTAIN_EXCEPTION_INCORRECT_TYPE);
            return;
        }

        Player player = (Player) sender;
        Selection selection = Lazarus.getInstance().getSelectionManager().getSelection(player);

        if(selection == null || selection.getType() != SelectionType.SYSTEM_CLAIM) {
            Lazarus.getInstance().getSelectionManager().toggleSelectionProcess(player, SelectionType.SYSTEM_CLAIM, null);
            player.sendMessage(Language.MOUNTAIN_PREFIX + Language.MOUNTAIN_CREATE_MAKE_A_SELECTION);
            return;
        }

        if(!selection.areBothPositionsSet()) {
            player.sendMessage(Language.MOUNTAIN_PREFIX + Language.MOUNTAIN_CREATE_SET_BOTH_POSITIONS);
            return;
        }

        if(!ClaimManager.getInstance().getClaimsInSelection(selection.getPosOne(), selection.getPosTwo()).isEmpty()) {
            player.sendMessage(Language.MOUNTAIN_PREFIX + Language.MOUNTAIN_CREATE_CLAIM_OVERLAPPING);
            return;
        }

        Faction faction = type == MountainType.GLOWSTONE ? FactionsManager.getInstance()
        .getFactionByName("Glowstone") : FactionsManager.getInstance().getFactionByName("Ore");

        if(!ClaimManager.getInstance().addClaim(selection.toClaim(faction))) return;

        int id = Lazarus.getInstance().getMountainManager().createMountain(type, faction.getId(), selection.toCuboid());

        Lazarus.getInstance().getSelectionManager().removeSelectionProcess(player);

        sender.sendMessage(Language.MOUNTAIN_PREFIX + Language.MOUNTAIN_CREATE_CREATED
            .replace("<id>", String.valueOf(id)));
    }
}
