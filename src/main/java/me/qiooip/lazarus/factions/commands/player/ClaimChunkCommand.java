package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.Claim;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.enums.Role;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Chunk;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimChunkCommand extends SubCommand {

    public ClaimChunkCommand() {
        super("claimchunk", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);

        if(faction == null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NOT_IN_FACTION_SELF);
            return;
        }

        if(!faction.getMember(player).getRole().isAtLeast(Role.CO_LEADER)) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NO_PERMISSION.replace("<role>", Role.CO_LEADER.getName()));
            return;
        }

        int claimCount = faction.getClaims().size();

        if(claimCount >= Config.FACTION_MAX_CLAIMS) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_MAX_CLAIMS_EXCEEDED);
            return;
        }

        if(claimCount >= faction.getMembers().size() * Config.FACTION_CLAIMS_PER_PLAYER) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_MAX_CLAIMS_EXCEEDED);
            return;
        }

        if(player.getWorld().getEnvironment() != Environment.NORMAL) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_CAN_CLAIM_ONLY_IN_OVERWORLD);
            return;
        }

        Chunk chunk = player.getLocation().getChunk();

        int x1 = chunk.getX() * 16;
        int x2 = x1 + 15;
        int z1 = chunk.getZ() * 16;
        int z2 = z1 + 15;

        Claim claim = new Claim(faction, chunk.getWorld(), x1, x2, z1, z2);

        if(!ClaimManager.getInstance().isSelectionConnected(faction, claim)) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_CLAIM_NOT_CONNECTED);
            return;
        }

        if(!ClaimManager.getInstance().canClaimSelection(faction, claim, player, true)) return;

        int price = ClaimManager.getInstance().getClaimPrice(faction, claim.sizeX() * claim.sizeZ());

        if(faction.getBalance() < price) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_NOT_ENOUGH_MONEY);
            return;
        }

        if(!ClaimManager.getInstance().addClaim(claim)) return;
        faction.removeBalance(price);

        faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CLAIM_CLAIM_CLAIMED
            .replace("<location1>", StringUtils.getLocationNameWithoutY(claim.getMinimumPoint()))
            .replace("<location2>", StringUtils.getLocationNameWithoutY(claim.getMaximumPoint()))
            .replace("<player>", player.getName()));
    }
}
