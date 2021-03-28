package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.enums.Role;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.utils.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisbandCommand extends SubCommand {

    public DisbandCommand() {
        super("disband", true);
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
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_MUST_BE_LEADER);
            return;
        }

        if(!Config.FACTION_DISBAND_WHILE_FROZEN && faction.isFrozen()) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CANNOT_DISBAND_WHILE_REGENERATING);
            return;
        }

        if(faction.isRaidable() && !Lazarus.getInstance().getEotwHandler().isActive()) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_DISBAND_RAIDABLE_DENY);
            return;
        }

        if(!FactionsManager.getInstance().disbandFaction(faction.getId(), player)) return;

        Messages.sendMessage(Language.FACTIONS_DISBANDED.replace("<player>", player.getName()).replace("<name>", faction.getName()));
    }
}
