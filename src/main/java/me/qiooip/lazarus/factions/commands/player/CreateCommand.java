package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class CreateCommand extends SubCommand {

    public CreateCommand() {
        super("create", Collections.singletonList("new"), true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CREATE_USAGE);
            return;
        }

        if(Config.FACTION_NAME_DISALLOWED_NAMES.contains(args[0].toLowerCase())) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_BLOCKED_FACTION_NAME);
            return;
        }

        if(args[0].length() < Config.FACTION_NAME_MINIMUM_LENGTH) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NAME_TOO_SHORT);
            return;
        }

        if(args[0].length() > Config.FACTION_NAME_MAXIMUM_LENGTH) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NAME_TOO_BIG);
            return;
        }

        if(!StringUtils.isAlphaNumeric(args[0])) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NAME_NOT_ALPHANUMERIC);
            return;
        }

        if(FactionsManager.getInstance().getFactionByName(args[0]) != null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_ALREADY_EXISTS.replace("<name>", args[0]));
            return;
        }

        Player player = (Player) sender;

        if(FactionsManager.getInstance().getPlayerFaction(player) != null) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_ALREADY_IN_FACTION_SELF);
            return;
        }

        if(!FactionsManager.getInstance().createPlayerFaction(args[0], player)) return;

        Messages.sendMessage(Language.FACTIONS_CREATED.replace("<player>", player.getName()).replace("<name>", args[0]));
    }
}
