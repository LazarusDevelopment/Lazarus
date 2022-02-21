package me.qiooip.lazarus.games.king.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommandExecutor;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KillTheKingCommandExecutor extends SubCommandExecutor {

    private final List<String> PLAYER_COMMAND_USAGE;

    public KillTheKingCommandExecutor() {
        super("killtheking", Arrays.asList("ktk", "king"), null);

        this.setPrefix(Language.KING_PREFIX);

        this.addSubCommand(new KillTheKingStartCommand());
        this.addSubCommand(new KillTheKingStopCommand());

        PLAYER_COMMAND_USAGE = Collections.singletonList(Lazarus
            .getInstance().getKillTheKingManager().getKingLocationString());
    }

    @Override
    protected List<String> getUsageMessage(CommandSender sender) {
        return sender.hasPermission("lazarus.killtheking.admin")
            ? Language.KING_COMMAND_USAGE_ADMIN : PLAYER_COMMAND_USAGE;
    }
}
