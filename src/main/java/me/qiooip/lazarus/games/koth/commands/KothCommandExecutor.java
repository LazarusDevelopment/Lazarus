package me.qiooip.lazarus.games.koth.commands;

import me.qiooip.lazarus.commands.manager.SubCommandExecutor;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;

import java.util.List;

public class KothCommandExecutor extends SubCommandExecutor {

    public KothCommandExecutor() {
        super("koth", null);

        this.setPrefix(Language.KOTH_PREFIX);

        this.addSubCommand(new KothAreaCommand());
        this.addSubCommand(new KothCreateCommand());
        this.addSubCommand(new KothDeleteCommand());
        this.addSubCommand(new KothEndCommand());
        this.addSubCommand(new KothFactionPointsCommand());
        this.addSubCommand(new KothListCommand());
        this.addSubCommand(new KothLootCommand());
        this.addSubCommand(new KothSetTimeCommand());
        this.addSubCommand(new KothStartCommand());
        this.addSubCommand(new KothStartingTimeCommand());
        this.addSubCommand(new KothStopAllCommand());
        this.addSubCommand(new KothStopCommand());
        this.addSubCommand(new KothTeleportCommand());
    }

    @Override
    public List<String> getUsageMessage(CommandSender sender) {
        return sender.hasPermission("lazarus.koth.admin")
            ? Language.KOTH_COMMAND_USAGE_ADMIN
            : Language.KOTH_COMMAND_USAGE_PLAYER;
    }
}
