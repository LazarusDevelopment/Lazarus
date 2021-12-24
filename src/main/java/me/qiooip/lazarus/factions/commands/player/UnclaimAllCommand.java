package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.enums.Role;
import me.qiooip.lazarus.factions.event.FactionClaimChangeEvent.ClaimChangeReason;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnclaimAllCommand extends SubCommand {

    public UnclaimAllCommand() {
        super("unclaimall", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);

        if(faction == null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NOT_IN_FACTION_SELF);
            return;
        }

        if(faction.getMember(player).getRole() != Role.LEADER) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NO_PERMISSION.replace("<role>", Role.LEADER.getName()));
            return;
        }

        if(faction.getClaims().size() == 0) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_UNCLAIM_ALL_NO_CLAIMS);
            return;
        }

        if(!Config.FACTION_UNCLAIM_WHILE_FROZEN && faction.isFrozen()) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CANNOT_UNCLAIM_ALL_WHILE_REGENERATING);
            return;
        }

        if(!ClaimManager.getInstance().removeAllClaims(faction, ClaimChangeReason.UNCLAIM_ALL)) return;

        faction.setHome(null);
        FactionsManager.getInstance().checkHomeTeleports(faction, Language.FACTIONS_UNCLAIM_ALL_HOME_TELEPORT_CANCELLED);

        faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_UNCLAIMED_ALL.replace("<player>", player.getName()));
    }
}
