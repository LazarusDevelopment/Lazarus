package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionPlayer;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class ForcePromoteCommand extends SubCommand {

    public ForcePromoteCommand() {
        super("forcepromote", "lazarus.factions.forcepromote");

        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FORCE_PROMOTE_USAGE);
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

        if(targetPlayer.getRole().getPromote() == null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FORCE_PROMOTE_MAX_PROMOTE);
            return;
        }

        targetPlayer.setRole(targetPlayer.getRole().getPromote());

        if(Config.TAB_ENABLED) {
            Lazarus.getInstance().getTabManager().updateFactionPlayerList(faction);
        }

        sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FORCE_PROMOTED_SENDER.replace("<player>",
        target.getName()).replace("<role>", targetPlayer.getRole().getName()));

        faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FORCE_PROMOTED_FACTION.replace("<player>",
        target.getName()).replace("<role>", targetPlayer.getRole().getName()));
    }
}
