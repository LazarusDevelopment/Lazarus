package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionPlayer;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.enums.Role;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand extends SubCommand {

    public KickCommand() {
        super("kick", true);

        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_KICK_USAGE);
            return;
        }

        Player player = (Player) sender;
        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);

        if(faction == null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NOT_IN_FACTION_SELF);
            return;
        }

        Role playerRole = faction.getMember(player).getRole();

        if(!playerRole.isAtLeast(Role.CAPTAIN)) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NO_PERMISSION.replace("<role>", Role.CAPTAIN.getName()));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if(!this.checkOfflinePlayer(sender, target, args[0])) return;

        FactionPlayer targetPlayer = faction.getMember(target);

        if(targetPlayer == null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NOT_IN_FACTION_OTHERS.replace("<player>", target.getName()));
            return;
        }

        if((playerRole.ordinal() - targetPlayer.getRole().ordinal()) < 1) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NO_PERMISSION_ROLE.replace("<player>", target.getName()));
            return;
        }

        if(!FactionsManager.getInstance().kickPlayer(target, faction)) return;

        if(target.isOnline()) target.getPlayer().sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_KICKED_SELF.replace("<name>", faction.getName()));
        faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_KICKED_OTHERS.replace("<player>", target.getName()));
    }
}
