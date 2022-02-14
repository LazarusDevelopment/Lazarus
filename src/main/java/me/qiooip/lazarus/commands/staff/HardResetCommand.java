package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.timer.TimerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.ArrayList;
import java.util.List;

public class HardResetCommand extends BaseCommand {

    private final String[] TAB_COMPLETIONS = new String[] {
        "playerfactions", "factionpoints", "userdata", "timers", "deathbans", "leaderboards", "all"
    };

    public HardResetCommand() {
        super("hardreset", "lazarus.hardreset");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(Language.PREFIX + Language.COMMANDS_FOR_CONSOLE_ONLY);
            return;
        }

        if(args.length == 0) {
            Language.HARD_RESET_COMMAND_USAGE.forEach(sender::sendMessage);
            return;
        }

        switch(args[0].toLowerCase()) {
            case "playerfactions": {
                Lazarus.getInstance().log("&4===&c=============================================&4===");
                ClaimManager.getInstance().deleteAllPlayerFactionClaims();
                FactionsManager.getInstance().deleteAllPlayerFactions();
                Lazarus.getInstance().log("&4===&c=============================================&4===");
                return;
            }
            case "factionpoints": {
                Lazarus.getInstance().log("&4===&c=============================================&4===");
                FactionsManager.getInstance().resetPlayerFactionPoints();
                Lazarus.getInstance().log("&4===&c=============================================&4===");
                return;
            }
            case "userdata": {
                Lazarus.getInstance().log("&4===&c=============================================&4===");
                Lazarus.getInstance().getUserdataManager().deleteAllUserdata();
                Lazarus.getInstance().log("&4===&c=============================================&4===");
                return;
            }
            case "timers": {
                Lazarus.getInstance().log("&4===&c=============================================&4===");
                TimerManager.getInstance().deleteAllTimers();
                Lazarus.getInstance().log("&4===&c=============================================&4===");
                return;
            }
            case "deathbans": {
                Lazarus.getInstance().log("&4===&c=============================================&4===");
                Lazarus.getInstance().getDeathbanManager().deleteAllDeathbans(true);
                Lazarus.getInstance().log("&4===&c=============================================&4===");
                return;
            }
            case "leaderboards": {
                Lazarus.getInstance().log("&4===&c=============================================&4===");
                Lazarus.getInstance().getLeaderboardHandler().deleteAllLeaderboards();
                Lazarus.getInstance().log("&4===&c=============================================&4===");
                return;
            }
            case "all": {
                Lazarus.getInstance().log("&4===&c=============================================&4===");
                ClaimManager.getInstance().deleteAllPlayerFactionClaims();
                FactionsManager.getInstance().deleteAllPlayerFactions();
                Lazarus.getInstance().getUserdataManager().deleteAllUserdata();
                Lazarus.getInstance().getDeathbanManager().deleteAllDeathbans(true);
                Lazarus.getInstance().getLeaderboardHandler().deleteAllLeaderboards();
                TimerManager.getInstance().deleteAllTimers();
                Lazarus.getInstance().log("&4===&c=============================================&4===");
                return;
            }
            default: {
                Language.HARD_RESET_COMMAND_USAGE.forEach(sender::sendMessage);
            }
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) {
        if(args.length != 1 || !(sender instanceof ConsoleCommandSender)) return null;

        List<String> completions = new ArrayList<>();

        for(String resetType : TAB_COMPLETIONS) {
            if(!resetType.startsWith(args[0].toLowerCase())) continue;

            completions.add(resetType);
        }

        return completions;
    }
}
