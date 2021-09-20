package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlaytimeCommand extends BaseCommand {

    public PlaytimeCommand() {
        super("playtime", "lazarus.playtime");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.PREFIX + Language.PLAYTIME_COMMAND_USAGE);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        long playTime = target.getStatistic(Statistic.PLAY_ONE_TICK);

        sender.sendMessage(Language.PREFIX + Language.PLAYTIME_COMMAND_MESSAGE
            .replace("<time>", StringUtils.formatDurationWords(playTime * 50))
            .replace("<player>", target.getName()));
    }
}
