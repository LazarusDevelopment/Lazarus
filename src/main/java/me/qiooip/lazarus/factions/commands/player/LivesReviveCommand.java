package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.enums.Role;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class LivesReviveCommand extends SubCommand {

    public LivesReviveCommand() {
        super("livesrevive", Collections.singletonList("revive"), true);

        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_LIVES_REVIVE_USAGE);
            return;
        }

        Player player = (Player) sender;
        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);

        if(faction == null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NOT_IN_FACTION_SELF);
            return;
        }

        if(!faction.getMember(player).getRole().isAtLeast(Role.CAPTAIN)) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NO_PERMISSION.replace("<role>", (Role.CAPTAIN).getName()));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if(!this.checkOfflinePlayer(sender, target, args[0])) return;

        if(faction.getMember(target.getUniqueId()) == null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NOT_IN_FACTION_OTHERS.replace("<player>", target.getName()));
            return;
        }

        if(!Lazarus.getInstance().getDeathbanManager().isDeathBanned(target.getUniqueId())) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_LIVES_REVIVE_NOT_DEATHBANNED.replace("<target>", target.getName()));
            return;
        }

        if(faction.getLives() <= 0) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_LIVES_REVIVE_NOT_ENOUGH_LIVES);
            return;
        }

        faction.setLives(faction.getLives() - 1);
        Lazarus.getInstance().getDeathbanManager().revivePlayer(target.getUniqueId());

        faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_LIVES_REVIVED
        .replace("<player>", player.getName()).replace("<target>", target.getName()));
    }
}
