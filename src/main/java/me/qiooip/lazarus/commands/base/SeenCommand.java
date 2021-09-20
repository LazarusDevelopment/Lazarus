package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class SeenCommand extends BaseCommand {

    public SeenCommand() {
        super("seen", "lazarus.seen");

        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.PREFIX + Language.SEEN_USAGE);
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if(target.isOnline()) {
            sender.sendMessage(Language.PREFIX + Language.SEEN_ONLINE
                .replace("<player>", target.getName())
                .replace("<time>", StringUtils.formatDurationWords(System.currentTimeMillis() - target.getLastPlayed())));
            return;
        }

        if(!target.hasPlayedBefore()) {
            sender.sendMessage(Language.PREFIX + Language.COMMANDS_PLAYER_NOT_FOUND.replace("<player>", args[0]));
            return;
        }

        sender.sendMessage(Language.PREFIX + Language.SEEN_MESSAGE
            .replace("<player>", target.getName())
            .replace("<time>", StringUtils.formatDurationWords(System.currentTimeMillis() - target.getLastPlayed())));
    }
}
