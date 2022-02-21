package me.qiooip.lazarus.hologram.command;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import org.bukkit.command.CommandSender;

public class HologramListCommand extends SubCommand {

    public HologramListCommand() {
        super("list", "lazarus.holograms");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Lazarus.getInstance().getHologramManager().listAllHolograms(sender);
    }
}
