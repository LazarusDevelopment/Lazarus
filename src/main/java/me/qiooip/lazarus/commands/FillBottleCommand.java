package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FillBottleCommand extends BaseCommand {

    public FillBottleCommand() {
        super("fillbottle", "lazarus.fillbottle", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Lazarus.getInstance().getInventoryHandler().fillWaterBottles(player);
    }
}
