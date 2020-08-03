package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.KothFaction;
import me.qiooip.lazarus.factions.type.SystemFaction;
import me.qiooip.lazarus.utils.Color;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ColorCommand extends SubCommand {

    public ColorCommand() {
        super("color", "lazarus.factions.color");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_COLOR_USAGE);
            return;
        }

        Faction faction = FactionsManager.getInstance().getFactionByName(args[0]);

        if(faction == null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_DOESNT_EXIST.replace("<argument>", args[0]));
            return;
        }

        if(!(faction instanceof SystemFaction)) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_COLOR_NOT_SYSTEM_FACTION);
            return;
        }

        String color = args[1].toLowerCase();

        if(!this.isColorStringValid(color)) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_COLOR_DOESNT_EXIST.replace("<color>", args[1]));
            return;
        }

        String oldColoredName = faction.getDisplayName(sender);

        SystemFaction systemFaction = (SystemFaction) faction;
        systemFaction.setColor(Color.translate(color));

        if(systemFaction instanceof KothFaction) {
            Lazarus.getInstance().getKothManager().getKoth(systemFaction.getName()).setupKothColor();
        }

        sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_COLOR_CHANGED
            .replace("<faction>", oldColoredName)
            .replace("<color>", faction.getDisplayName(sender)));
    }

    private boolean isColorStringValid(String value) {
        if(value.length() % 2 != 0) return false;

        String[] colorParts = new String[value.length() / 2];

        int count = 0;

        for(int i = 0; i < value.length(); i += 2) {
            colorParts[count] = value.substring(i, i + 2);
            count++;
        }

        for(String colorPart : colorParts) {
            if(colorPart.charAt(0) != '&') return false;
            if(ChatColor.getByChar(colorPart.charAt(1)) == null) return false;
        }

        return true;
    }
}
