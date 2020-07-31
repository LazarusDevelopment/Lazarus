package me.qiooip.lazarus.factions.commands.player;

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
import org.bukkit.entity.Player;

public class DemoteCommand extends SubCommand {

    public DemoteCommand() {
        super("demote", true);

        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_DEMOTE_USAGE);
            return;
        }

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

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if(!this.checkOfflinePlayer(sender, target, args[0])) return;

        if(player == target) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CANNOT_DEMOTE_SELF);
            return;
        }

        FactionPlayer targetPlayer = faction.getMember(target);

        if(targetPlayer == null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NOT_IN_FACTION_OTHERS.replace("<player>", target.getName()));
            return;
        }

        if(targetPlayer.getRole().getDemote() == null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_DEMOTE_MIN_DEMOTE);
            return;
        }

        targetPlayer.setRole(targetPlayer.getRole().getDemote());

        if(Config.TAB_ENABLED) {
            Lazarus.getInstance().getTabManager().updateFactionPlayerList(faction);
        }

        faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_DEMOTE_DEMOTED.replace("<player>",
        target.getName()).replace("<role>", targetPlayer.getRole().getName()));
    }
}
