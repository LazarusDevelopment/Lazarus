package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceJoinCommand extends SubCommand {

    public ForceJoinCommand() {
        super("forcejoin", "lazarus.factions.forcejoin", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FORCE_JOIN_USAGE);
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

        if(!FactionsManager.getInstance().joinFaction(player, faction)) return;

        faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FORCE_JOINED.replace("<player>", player.getName()));
    }
}
