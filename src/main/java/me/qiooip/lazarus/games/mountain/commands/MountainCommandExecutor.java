package me.qiooip.lazarus.games.mountain.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommandExecutor;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class MountainCommandExecutor extends SubCommandExecutor {

    private final List<String> PLAYER_COMMAND_USAGE;

    public MountainCommandExecutor() {
        super("mountain", null);

        this.setPrefix(Language.MOUNTAIN_PREFIX);

        this.addSubCommand(new MountainCreateCommand());
        this.addSubCommand(new MountainDeleteCommand());
        this.addSubCommand(new MountainListCommand());
        this.addSubCommand(new MountainRespawnCommand());
        this.addSubCommand(new MountainTeleportCommand());
        this.addSubCommand(new MountainTimeCommand());
        this.addSubCommand(new MountainUpdateCommand());

        PLAYER_COMMAND_USAGE = Collections.singletonList(Lazarus
            .getInstance().getMountainManager().nextRespawnString());
    }

    @Override
    public List<String> getUsageMessage(CommandSender sender) {
        return sender.hasPermission("lazarus.mountain.admin")
            ? Language.MOUNTAIN_COMMAND_USAGE_ADMIN : PLAYER_COMMAND_USAGE;
    }
}
