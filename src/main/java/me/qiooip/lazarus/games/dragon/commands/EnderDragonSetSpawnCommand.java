package me.qiooip.lazarus.games.dragon.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnderDragonSetSpawnCommand extends SubCommand {

    EnderDragonSetSpawnCommand() {
        super("setspawn", "lazarus.enderdragon.setspawn", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(player.getWorld().getEnvironment() != World.Environment.THE_END) {
            player.sendMessage(Language.ENDER_DRAGON_PREFIX + Language.ENDER_DRAGON_SET_SPAWN_NOT_IN_END);
            return;
        }

        Lazarus.getInstance().getEnderDragonManager().setSpawnLocation(player);

        player.sendMessage(Language.ENDER_DRAGON_PREFIX + Language.ENDER_DRAGON_SET_SPAWN_SET
        .replace("<location>", StringUtils.getLocationName(player.getLocation())));
    }
}
