package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;

public class ForceRenameCommand extends SubCommand {

    public ForceRenameCommand() {
        super("forcerename", "lazarus.factions.forcerename");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FORCE_RENAME_USAGE);
            return;
        }

        PlayerFaction faction = FactionsManager.getInstance().searchForFaction(args[0]);

        if(faction == null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_DOESNT_EXIST.replace("<argument>", args[0]));
            return;
        }

        if(faction.getName().equalsIgnoreCase(args[1])) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FORCE_RENAME_SAME_NAME.replace("<name>", args[1]));
            return;
        }

        if(Config.FACTION_NAME_DISALLOWED_NAMES.contains(args[1].toLowerCase())) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_BLOCKED_FACTION_NAME);
            return;
        }

        if(args[1].length() < Config.FACTION_NAME_MINIMUM_LENGTH) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NAME_TOO_SHORT);
            return;
        }

        if(args[1].length() > Config.FACTION_NAME_MAXIMUM_LENGTH) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NAME_TOO_BIG);
            return;
        }

        if(!StringUtils.isAlphaNumeric(args[1])) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_NAME_NOT_ALPHANUMERIC);
            return;
        }

        if(FactionsManager.getInstance().getFactionByName(args[1]) != null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_ALREADY_EXISTS.replace("<name>", args[1]));
            return;
        }

        String oldName = faction.getName();
        if(!faction.setName(sender, args[1], true)) return;


        Messages.sendMessage(Language.FACTIONS_FORCE_RENAMED.replace("<name>", oldName).replace("<newName>", args[1]));
    }
}
