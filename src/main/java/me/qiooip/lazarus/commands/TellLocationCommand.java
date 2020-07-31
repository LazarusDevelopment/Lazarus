package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TellLocationCommand extends BaseCommand {

    public TellLocationCommand() {
        super("telllocation", Arrays.asList("tellloc", "tl"), "lazarus.telllocation", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);

        if(faction == null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NOT_IN_FACTION_SELF);
            return;
        }

        faction.sendMessage(Language.TELL_LOCATION_MESSAGE
            .replace("<player>", player.getName())
            .replace("<location>", StringUtils.getLocationName(player.getLocation())));
    }
}