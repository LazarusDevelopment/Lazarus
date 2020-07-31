package me.qiooip.lazarus.games.koth.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import org.bukkit.command.CommandSender;

public class KothListCommand extends SubCommand {

    KothListCommand() {
        super("list");

        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Lazarus.getInstance().getKothManager().listKoths(sender);
    }
}
