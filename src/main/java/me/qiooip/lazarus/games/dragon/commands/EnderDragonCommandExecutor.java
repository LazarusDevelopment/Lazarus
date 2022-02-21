package me.qiooip.lazarus.games.dragon.commands;

import me.qiooip.lazarus.commands.manager.SubCommandExecutor;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class EnderDragonCommandExecutor extends SubCommandExecutor {

    public EnderDragonCommandExecutor() {
        super("enderdragon", Collections.singletonList("dragon"), null);

        this.setPrefix(Language.ENDER_DRAGON_PREFIX);

        this.addSubCommand(new EnderDragonInfoCommand());
        this.addSubCommand(new EnderDragonLootCommand());
        this.addSubCommand(new EnderDragonSetHealthCommand());
        this.addSubCommand(new EnderDragonSetSpawnCommand());
        this.addSubCommand(new EnderDragonStartCommand());
        this.addSubCommand(new EnderDragonStopCommand());
        this.addSubCommand(new EnderDragonTeleportCommand());
    }

    @Override
    public List<String> getUsageMessage(CommandSender sender) {
        return sender.hasPermission("lazarus.enderdragon.admin")
            ? Language.ENDER_DRAGON_COMMAND_USAGE_ADMIN
            : Language.ENDER_DRAGON_COMMAND_USAGE_PLAYER;
    }
}
