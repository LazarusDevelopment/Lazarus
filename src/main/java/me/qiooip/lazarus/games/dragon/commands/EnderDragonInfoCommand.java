package me.qiooip.lazarus.games.dragon.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

public class EnderDragonInfoCommand extends SubCommand {

    EnderDragonInfoCommand() {
        super("info");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Location dragonSpawn = Lazarus.getInstance().getEnderDragonManager().getSpawnLocation();

        Language.ENDER_DRAGON_INFO_MESSAGE.forEach(line -> sender.sendMessage(line
        .replace("<health>", String.valueOf(Config.ENDER_DRAGON_HEALTH))
        .replace("<location>", dragonSpawn == null ? Language.ENDER_DRAGON_SPAWN_NOT_SET
        : StringUtils.getLocationNameWithWorld(dragonSpawn))));
    }
}
