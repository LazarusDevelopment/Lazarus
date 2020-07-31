package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionPlayer;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.enums.Role;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class ForceLeaderCommand extends SubCommand {

    public ForceLeaderCommand() {
        super("forceleader", "lazarus.factions.forceleader");

        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FORCE_LEADER_USAGE);
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if(!this.checkOfflinePlayer(sender, target, args[0])) return;

        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(target.getUniqueId());

        if(faction == null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NOT_IN_FACTION.replace("<player>", target.getName()));
            return;
        }

        FactionPlayer targetPlayer = faction.getMember(target);

        if(targetPlayer.getRole() == Role.LEADER) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FORCE_LEADER_ALREADY_LEADER.replace("<player>", target.getName()));
            return;
        }

        faction.getLeader().setRole(Role.CO_LEADER);
        targetPlayer.setRole(Role.LEADER);

        if(Config.TAB_ENABLED) {
            Lazarus.getInstance().getTabManager().updateFactionPlayerList(faction);
        }

        sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FORCE_LEADER_CHANGED_SENDER
        .replace("<player>", target.getName()).replace("<faction>", faction.getName()));

        faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FORCE_LEADER_CHANGED_FACTION
        .replace("<sender>", sender.getName()).replace("<player>", target.getName()));
    }
}
