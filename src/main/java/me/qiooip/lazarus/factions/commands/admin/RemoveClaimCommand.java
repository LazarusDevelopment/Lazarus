package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.Claim;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveClaimCommand extends SubCommand {

    public RemoveClaimCommand() {
        super("removeclaim", "lazarus.factions.removeclaim", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        Claim claim = ClaimManager.getInstance().getClaimAt(player.getLocation());

        if(claim == null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_REMOVE_CLAIM_NO_CLAIM);
            return;
        }

        if(!ClaimManager.getInstance().removeClaim(claim)) return;

        player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_REMOVE_CLAIM_REMOVED_SENDER
            .replace("<faction>", claim.getOwner().getDisplayName(sender))
            .replace("<location>", StringUtils.getLocationNameWithoutY(claim.getCenter())));

        if(!(claim.getOwner() instanceof PlayerFaction)) return;

        PlayerFaction faction = (PlayerFaction) claim.getOwner();

        faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_REMOVE_CLAIM_REMOVED_FACTION
            .replace("<player>", player.getName())
            .replace("<location>", StringUtils.getLocationNameWithoutY(claim.getCenter())));

        if(faction.getHome() != null && claim.contains(faction.getHome())) {
            faction.setHome(null);
            FactionsManager.getInstance().checkHomeTeleports(faction, Language.FACTIONS_UNCLAIM_HOME_TELEPORT_CANCELLED);
        }
    }
}
