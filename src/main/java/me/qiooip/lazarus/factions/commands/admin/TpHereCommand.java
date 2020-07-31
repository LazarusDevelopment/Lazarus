package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class TpHereCommand extends SubCommand {

    public TpHereCommand() {
        super("tphere", Collections.singletonList("teleporthere"), "lazarus.factions.tphere", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 1) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_TP_HERE_USAGE);
            return;
        }

        PlayerFaction faction = FactionsManager.getInstance().searchForFaction(args[0]);

        if(faction == null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_DOESNT_EXIST.replace("<argument>", args[0]));
            return;
        }

        Player player = (Player) sender;

        faction.getOnlinePlayers().forEach(online -> {
            if(!online.teleport(player)) return;

            online.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_TP_HERE_TELEPORTED_FACTION
                .replace("<player>", player.getName()));
        });

        player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_TP_HERE_TELEPORTED_SENDER
            .replace("<faction>", faction.getName()));
    }
}
