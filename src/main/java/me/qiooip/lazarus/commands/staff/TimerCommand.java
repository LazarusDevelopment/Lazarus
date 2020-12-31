package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.scoreboard.PvpProtTimer;
import me.qiooip.lazarus.timer.type.PlayerTimer;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TimerCommand extends BaseCommand {

    private static final String[] TIMER_NAMES = new String[] { "APPLE", "COMBAT", "ENDERPEARL", "GAPPLE", "PVPPROT" };

    public TimerCommand() {
        super("timer", "lazarus.timer");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 3) {
            sender.sendMessage(Language.PREFIX + Language.TIMER_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        PlayerTimer timer = StringUtils.getPlayerTimerByName(args[1]);

        if(timer == null) {
            sender.sendMessage(Language.PREFIX + Language.TIMER_NOT_CHANGEABLE);
            return;
        }

        int duration = StringUtils.parseSeconds(args[2]);

        if(duration == -1) {
            sender.sendMessage(Language.PREFIX + Language.COMMANDS_INVALID_DURATION);
            return;
        }

        if(timer.isActive(target)) {
            timer.cancel(target);
        }

        if(duration > 0) {
            if(timer instanceof PvpProtTimer) {
                ((PvpProtTimer) timer).activate(target, duration, target.getLocation());
            } else {
                timer.activate(target, duration);
            }
        }

        sender.sendMessage(Language.PREFIX + Language.TIMER_CHANGED_SENDER
            .replace("<player>", target.getName())
            .replace("<timer>", args[1].toLowerCase())
            .replace("<time>", args[2].toLowerCase()));

        if(!Language.TIMER_CHANGED.isEmpty()) {
            target.sendMessage(Language.PREFIX + Language.TIMER_CHANGED
                .replace("<timer>", args[1].toLowerCase())
                .replace("<time>", args[2].toLowerCase())
                .replace("<sender>", sender.getName()));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        if(args.length != 2 || !sender.hasPermission("lazarus.timer")) {
            return super.tabComplete(sender, alias, args);
        }

        List<String> completions = new ArrayList<>();

        for(String timerName : TIMER_NAMES) {
            if(!timerName.startsWith(args[1].toUpperCase())) continue;

            completions.add(timerName);
        }

        return completions;
    }
}
