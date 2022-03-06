package me.qiooip.lazarus.hologram.command;

import me.qiooip.lazarus.commands.manager.SubCommandExecutor;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class HologramCommandExecutor extends SubCommandExecutor {

    private final List<String> PLAYER_COMMAND_USAGE;

    public HologramCommandExecutor() {
        super("hologram", Collections.singletonList("holo"), Language.HOLOGRAMS_COMMAND_USAGE);

        this.setPrefix(Language.HOLOGRAMS_PREFIX);

        this.addSubCommand(new HologramAddLineCommand());
        this.addSubCommand(new HologramCreateCommand());
        this.addSubCommand(new HologramDeleteCommand());
        this.addSubCommand(new HologramInsertLineCommand());
        this.addSubCommand(new HologramListCommand());
        this.addSubCommand(new HologramRemoveLineCommand());
        this.addSubCommand(new HologramTeleportCommand());
        this.addSubCommand(new HologramTeleportHereCommand());
        this.addSubCommand(new HologramUpdateLineCommand());

        PLAYER_COMMAND_USAGE = Collections.singletonList(Language.COMMANDS_NO_PERMISSION);
    }

    @Override
    protected List<String> getUsageMessage(CommandSender sender) {
        return sender.hasPermission("lazarus.holograms")
            ? Language.HOLOGRAMS_COMMAND_USAGE : PLAYER_COMMAND_USAGE;
    }
}
