package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ShowCommand extends SubCommand {

    public ShowCommand() {
        super("show", Arrays.asList("who", "i", "info"));

        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            if(!this.checkConsoleSender(sender)) return;

            Player player = (Player) sender;
            PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);

            if(faction == null) {
                player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NOT_IN_FACTION_SELF);
                return;
            }

            faction.showInformation(player);
            return;
        }

        Faction faction = FactionsManager.getInstance().getFactionByName(args[0]);
        PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFaction(args[0]);

        if(playerFaction == null && faction == null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_DOESNT_EXIST.replace("<argument>", args[0]));
            return;
        }

        if(faction != null) faction.showInformation(sender);
        if(playerFaction != null) playerFaction.showInformation(sender);
    }
}
