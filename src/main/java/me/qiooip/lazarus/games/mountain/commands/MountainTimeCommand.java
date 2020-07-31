package me.qiooip.lazarus.games.mountain.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import org.bukkit.command.CommandSender;

public class MountainTimeCommand extends SubCommand {

    MountainTimeCommand() {
        super("time", "lazarus.mountain.time");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(Lazarus.getInstance().getMountainManager().nextRespawnString());
    }
}
