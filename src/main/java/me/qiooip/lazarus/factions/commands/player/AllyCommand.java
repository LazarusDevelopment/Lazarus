package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.enums.Role;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AllyCommand extends SubCommand {

    public AllyCommand() {
        super("ally", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(Config.FACTION_MAX_ALLIES <= 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_ALLIES_DISABLED);
            return;
        }

        if(args.length == 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_ALLY_USAGE);
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

        if(faction == targetFaction) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_ALLY_CANNOT_ALLY_SELF);
            return;
        }

        if(faction.getAllies().size() >= Config.FACTION_MAX_ALLIES) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_ALLY_MAX_ALLIES_SELF);
            return;
        }

        if(targetFaction.getAllies().size() >= Config.FACTION_MAX_ALLIES) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_ALLY_MAX_ALLIES_OTHERS.replace("<faction>", targetFaction.getName()));
            return;
        }

        if(faction.isAlly(targetFaction)) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_ALLY_ALREADY_ALLIES.replace("<faction>", targetFaction.getName()));
            return;
        }

        if(targetFaction.getAllyInvitations().contains(faction.getId())) {
            if(!FactionsManager.getInstance().acceptAllyRequest(faction, targetFaction)) return;

            faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_ALLY_ACCEPTED.replace("<faction>", targetFaction.getName()));
            targetFaction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_ALLY_ACCEPTED.replace("<faction>", faction.getName()));
            return;
        }

        if(faction.getAllyInvitations().contains(targetFaction.getId())) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_ALLY_REQUEST_ALREADY_SENT.replace("<faction>", targetFaction.getName()));
            return;
        }

        faction.getAllyInvitations().add(targetFaction.getId());

        faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_ALLY_REQUESTED_SELF.replace("<faction>", targetFaction.getName()));
        targetFaction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_ALLY_REQUESTED_OTHERS.replace("<faction>", faction.getName()));
    }
}
