package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.enums.Role;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RenameCommand extends SubCommand {

    public RenameCommand() {
        super("rename", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_RENAME_USAGE);
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

        if(faction.getName().equalsIgnoreCase(args[0])) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_RENAME_SAME_NAME.replace("<name>", args[0]));
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

        String oldName = faction.getName();
        if(!faction.setName(sender, args[0], false)) return;

        Messages.sendMessage(Language.FACTIONS_RENAMED.replace("<name>", oldName).replace("<newName>", args[0]));
    }
}
