package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.enums.Role;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand extends SubCommand {

    public SetHomeCommand() {
        super("sethome", true);
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
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NO_PERMISSION.replace("<role>", Role.getName(Role.CO_LEADER)));
            return;
        }

        if(ClaimManager.getInstance().getFactionAt(player.getLocation()) != faction) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_SET_HOME_NOT_IN_OWN_CLAIM);
            return;
        }

        faction.setHome(player.getLocation());

        faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_SET_HOME_HOME_SET
            .replace("<x>", String.valueOf(player.getLocation().getBlockX()))
            .replace("<z>", String.valueOf(player.getLocation().getBlockZ()))
            .replace("<player>", player.getName()));
    }
}
