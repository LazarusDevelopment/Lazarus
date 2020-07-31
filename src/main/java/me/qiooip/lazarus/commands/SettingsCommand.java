package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingsCommand extends BaseCommand {

    public SettingsCommand() {
        super("settings", "lazarus.settings", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        player.openInventory(Lazarus.getInstance().getSettingsHandler().getSettingsInventory(player));
    }
}
