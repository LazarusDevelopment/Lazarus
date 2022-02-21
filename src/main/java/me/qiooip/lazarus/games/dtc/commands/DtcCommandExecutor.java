package me.qiooip.lazarus.games.dtc.commands;

import me.qiooip.lazarus.commands.manager.SubCommandExecutor;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class DtcCommandExecutor extends SubCommandExecutor {

    public DtcCommandExecutor() {
        super("dtc", Collections.singletonList("destroythecore"), null);

        this.setPrefix(Language.DTC_PREFIX);

        this.addSubCommand(new DtcAreaCommand());
        this.addSubCommand(new DtcInfoCommand());
        this.addSubCommand(new DtcLootCommand());
        this.addSubCommand(new DtcSetCommand());
        this.addSubCommand(new DtcSetHealthCommand());
        this.addSubCommand(new DtcStartCommand());
        this.addSubCommand(new DtcStopCommand());
        this.addSubCommand(new DtcTeleportCommand());
    }

    @Override
    protected List<String> getUsageMessage(CommandSender sender) {
        return sender.hasPermission("lazarus.dtc.admin")
            ? Language.DTC_COMMAND_USAGE_ADMIN
            : Language.DTC_COMMAND_USAGE_PLAYER;
    }
}
