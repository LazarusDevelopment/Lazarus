package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class CraftCommand extends BaseCommand {

    public CraftCommand() {
        super("craft", Collections.singletonList("workbench"), "lazarus.craft", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        player.openWorkbench(player.getLocation(), true);
    }
}
