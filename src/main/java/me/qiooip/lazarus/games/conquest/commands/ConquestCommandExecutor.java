package me.qiooip.lazarus.games.conquest.commands;

import me.qiooip.lazarus.commands.manager.SubCommandExecutor;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ConquestCommandExecutor extends SubCommandExecutor {

    public ConquestCommandExecutor() {
        super("conquest", null);

        this.setPrefix(Language.CONQUEST_PREFIX);

        this.addSubCommand(new ConquestAreaCommand());
        this.addSubCommand(new ConquestInfoCommand());
        this.addSubCommand(new ConquestLootCommand());
        this.addSubCommand(new ConquestSetCapzoneCommand());
        this.addSubCommand(new ConquestSetPointsCommand());
        this.addSubCommand(new ConquestStartCommand());
        this.addSubCommand(new ConquestStopCommand());
        this.addSubCommand(new ConquestTeleportCommand());
    }

    @Override
    protected List<String> getUsageMessage(CommandSender sender) {
        return sender.hasPermission("lazarus.conquest.admin")
            ? Language.CONQUEST_COMMAND_USAGE_ADMIN
            : Language.CONQUEST_COMMAND_USAGE_PLAYER;
    }
}
