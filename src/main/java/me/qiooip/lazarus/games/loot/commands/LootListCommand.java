package me.qiooip.lazarus.games.loot.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import org.bukkit.command.CommandSender;

public class LootListCommand extends SubCommand {

    LootListCommand() {
        super("list");

        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Lazarus.getInstance().getLootManager().listLoots(sender);
    }
}
