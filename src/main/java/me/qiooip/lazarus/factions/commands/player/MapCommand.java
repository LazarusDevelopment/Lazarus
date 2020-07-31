package me.qiooip.lazarus.factions.commands.player;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MapCommand extends SubCommand {

    public MapCommand() {
        super("map", true);

        this.setExecuteAsync(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        ClaimManager.getInstance().getFactionMap().showFactionMap(player);
    }
}
