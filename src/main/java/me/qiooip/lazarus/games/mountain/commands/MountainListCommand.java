package me.qiooip.lazarus.games.mountain.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import org.bukkit.command.CommandSender;

public class MountainListCommand extends SubCommand {

    MountainListCommand() {
        super("list");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Lazarus.getInstance().getMountainManager().listMountains(sender);
    }
}
