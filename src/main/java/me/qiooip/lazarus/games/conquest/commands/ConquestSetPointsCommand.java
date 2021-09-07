package me.qiooip.lazarus.games.conquest.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.games.conquest.RunningConquest;
import org.bukkit.command.CommandSender;

public class ConquestSetPointsCommand extends SubCommand {

    ConquestSetPointsCommand() {
        super("setpoints", "lazarus.conquest.setpoints");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(Language.CONQUEST_PREFIX + Language.CONQUEST_SET_POINTS_USAGE);
            return;
        }

        RunningConquest conquest = Lazarus.getInstance().getConquestManager().getRunningConquest();

        if(conquest == null) {
            sender.sendMessage(Language.CONQUEST_PREFIX + Language.CONQUEST_EXCEPTION_NOT_RUNNING);
            return;
        }

        PlayerFaction faction = FactionsManager.getInstance().searchForFaction(args[0]);

        if(faction == null) {
            sender.sendMessage(Language.CONQUEST_PREFIX + Language.CONQUEST_SET_POINTS_INVALID_FACTION
                .replace("<argument>", args[0]));
            return;
        }

        if(!this.checkNumber(sender, args[1])) return;

        int amount = Math.min(Integer.parseInt(args[1]), Config.CONQUEST_POINTS_TO_WIN - 1);
        conquest.setPoints(faction, amount);

        sender.sendMessage(Language.CONQUEST_PREFIX + Language.CONQUEST_SET_POINTS_CHANGED
            .replace("<faction>", faction.getName())
            .replace("<amount>", String.valueOf(amount)));
    }
}
