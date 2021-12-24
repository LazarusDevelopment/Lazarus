package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.enums.Role;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnallyCommand extends SubCommand {

    public UnallyCommand() {
        super("unally", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(Config.FACTION_MAX_ALLIES <= 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_ALLIES_DISABLED);
            return;
        }

        if(args.length == 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_UNALLY_USAGE);
            return;
        }

        Player player = (Player) sender;
        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);

        if(faction == null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NOT_IN_FACTION_SELF);
            return;
        }

        if(!faction.getMember(player).getRole().isAtLeast(Role.CAPTAIN)) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NO_PERMISSION.replace("<role>", Role.CAPTAIN.getName()));
            return;
        }

        PlayerFaction targetFaction = FactionsManager.getInstance().searchForFaction(args[0]);

        if(targetFaction == null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_DOESNT_EXIST.replace("<argument>", args[0]));
            return;
        }

        if(!faction.isAlly(targetFaction)) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_UNALLY_NOT_ALLIES.replace("<faction>", targetFaction.getName(sender)));
            return;
        }

        if(!FactionsManager.getInstance().removeAllyRelation(faction, targetFaction)) return;

        faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_UNALLY_REMOVED.replace("<faction>", targetFaction.getName()));
        targetFaction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_UNALLY_REMOVED.replace("<faction>", faction.getName()));
    }
}
