package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.command.CommandSender;

public class SetLivesCommand extends SubCommand {

    public SetLivesCommand() {
        super("setlives", "lazarus.factions.setlives");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_SET_LIVES_USAGE);
            return;
        }

        PlayerFaction faction = FactionsManager.getInstance().searchForFaction(args[0]);

        if(faction == null) {
            sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_FACTION_DOESNT_EXIST.replace("<argument>", args[0]));
            return;
        }

        if(!this.checkNumber(sender, args[1])) return;
        int amount = Math.abs(Integer.parseInt(args[1]));

        faction.setLives(amount);

        sender.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_SET_LIVES_CHANGED_SENDER
        .replace("<faction>", faction.getName()).replace("<amount>", String.valueOf(amount)));

        faction.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_SET_LIVES_CHANGED_FACTION
        .replace("<player>", sender.getName()).replace("<amount>", String.valueOf(amount)));
    }
}
