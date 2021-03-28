package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class JoinCommand extends SubCommand {

    public JoinCommand() {
        super("join", Collections.singletonList("accept"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_JOIN_USAGE);
            return;
        }

        Player player = (Player) sender;

        if(FactionsManager.getInstance().getPlayerFaction(player) != null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_ALREADY_IN_FACTION_SELF);
            return;
        }

        PlayerFaction faction = FactionsManager.getInstance().searchForFaction(args[0]);

        if(faction == null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_DOESNT_EXIST.replace("<argument>", args[0]));
            return;
        }

        if(!faction.getPlayerInvitations().contains(player.getName()) && !faction.isOpen()) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NOT_INVITED.replace("<name>", faction.getName()));
            return;
        }

        if(faction.getMembers().size() >= Config.FACTION_PLAYER_LIMIT) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_JOIN_FACTION_FULL.replace("<faction>", faction.getName()));
            return;
        }

        if(!Config.FACTION_JOIN_WHILE_FROZEN && faction.isFrozen()) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CANNOT_JOIN_WHILE_REGENERATING.replace("<faction>", faction.getName()));
            return;
        }

        if(!FactionsManager.getInstance().joinFaction(player, faction)) return;

        faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_JOINED.replace("<player>", player.getName()));
    }
}
