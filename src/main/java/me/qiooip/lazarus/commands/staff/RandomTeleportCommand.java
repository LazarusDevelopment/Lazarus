package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class RandomTeleportCommand extends BaseCommand {

    public RandomTeleportCommand() {
        super("randomteleport", Arrays.asList("randomtp", "rtp"), "lazarus.randomteleport", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Lazarus.getInstance().getStaffModeManager().randomTeleport(player);
    }
}

