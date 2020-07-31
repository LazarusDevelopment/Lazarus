package me.qiooip.lazarus.games.dragon.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.Messages;
import org.bukkit.command.CommandSender;

public class EnderDragonSetHealthCommand extends SubCommand {

    EnderDragonSetHealthCommand() {
        super("sethealth", "lazarus.enderdragon.sethealth");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.ENDER_DRAGON_PREFIX + Language.ENDER_DRAGON_SET_HEALTH_USAGE);
            return;
        }

        if(!Lazarus.getInstance().getEnderDragonManager().isActive()) {
            sender.sendMessage(Language.ENDER_DRAGON_PREFIX + Language.ENDER_DRAGON_EXCEPTION_NOT_RUNNING);
            return;
        }

        if(!this.checkNumber(sender, args[0])) return;

        int amount = Math.abs(Integer.parseInt(args[0]));
        Lazarus.getInstance().getEnderDragonManager().changeDragonHealth(amount);

        Messages.sendMessage(Language.ENDER_DRAGON_PREFIX + Language.ENDER_DRAGON_SET_HEALTH_CHANGED
        .replace("<amount>", String.valueOf(amount)));
    }
}
