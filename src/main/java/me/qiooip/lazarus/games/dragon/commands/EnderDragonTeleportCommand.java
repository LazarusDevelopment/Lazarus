package me.qiooip.lazarus.games.dragon.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class EnderDragonTeleportCommand extends SubCommand {

    EnderDragonTeleportCommand() {
        super("teleport", Collections.singletonList("tp"), "lazarus.enderdragon.tp", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!Lazarus.getInstance().getEnderDragonManager().isActive()) {
            sender.sendMessage(Language.ENDER_DRAGON_PREFIX + Language.ENDER_DRAGON_EXCEPTION_NOT_RUNNING);
            return;
        }

        Player player = (Player) sender;
        Lazarus.getInstance().getEnderDragonManager().teleportToDragon(player);
    }
}
