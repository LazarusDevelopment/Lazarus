package me.qiooip.lazarus.games.dragon.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;

public class EnderDragonStartCommand extends SubCommand {

    EnderDragonStartCommand() {
        super("start", "lazarus.enderdragon.start");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(Lazarus.getInstance().getEnderDragonManager().isActive()) {
            sender.sendMessage(Language.ENDER_DRAGON_PREFIX + Language.ENDER_DRAGON_START_ALREADY_RUNNING);
            return;
        }

        if(Lazarus.getInstance().getEnderDragonManager() == null) {

            return;
        }

        Lazarus.getInstance().getEnderDragonManager().startEnderDragon(sender);
    }
}
