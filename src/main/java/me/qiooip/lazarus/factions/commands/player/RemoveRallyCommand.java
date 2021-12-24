package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.event.FactionRallyEvent;
import me.qiooip.lazarus.factions.event.FactionRallyEvent.RallyEventType;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveRallyCommand extends SubCommand {

    public RemoveRallyCommand() {
        super("removerally", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);

        if(faction == null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NOT_IN_FACTION_SELF);
            return;
        }

        if(faction.getRallyLocation() == null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_REMOVE_RALLY_NOT_SET);
            return;
        }

        faction.setRallyLocation(null);

        faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_REMOVE_RALLY_REMOVED
            .replace("<player>", player.getName()));

        new FactionRallyEvent(player.getUniqueId(), null, faction, RallyEventType.REMOVE);
    }
}
