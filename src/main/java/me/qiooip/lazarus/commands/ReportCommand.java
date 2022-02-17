package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.cooldown.CooldownTimer;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand extends BaseCommand {

    public ReportCommand() {
        super("report", "lazarus.report", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(Language.PREFIX + Language.REPORT_USAGE);
            return;
        }

        Player player = (Player) sender;

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        CooldownTimer timer = TimerManager.getInstance().getCooldownTimer();

        if(timer.isActive(player, "REPORT")) {
            player.sendMessage(Language.PREFIX + Language.REPORT_COOLDOWN
                .replace("<seconds>", timer.getTimeLeft(player, "REPORT")));
            return;
        }

        String reason = StringUtils.joinArray(args, " ", 2);

        Language.REPORT_FORMAT.forEach(line ->
            Messages.sendMessage(line
                .replace("<reporter>", player.getName())
                .replace("<suspect>", target.getName())
                .replace("<reason>", reason), "lazarus.report.receive")
        );

        player.sendMessage(Language.PREFIX + Language.REPORT_REPORTED
            .replace("<player>", target.getName()));

        timer.activate(player, "REPORT", Config.REPORT_COOLDOWN,
            Language.PREFIX + Language.REPORT_COOLDOWN_EXPIRED);
    }
}
