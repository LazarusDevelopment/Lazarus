package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReclaimCommand extends BaseCommand {

    public ReclaimCommand() {
        super("reclaim", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Lazarus.getInstance().getReclaimHandler().performCommand(player);
    }
}
