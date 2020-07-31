package me.qiooip.lazarus.games.mountain.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.Placeholder;
import me.qiooip.lazarus.games.mountain.MountainData;
import me.qiooip.lazarus.utils.Messages;
import org.bukkit.command.CommandSender;

public class MountainRespawnCommand extends SubCommand {

    MountainRespawnCommand() {
        super("respawn", "lazarus.mountain.respawn");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.MOUNTAIN_PREFIX + Language.MOUNTAIN_RESPAWN_USAGE);
            return;
        }

        if(!this.checkNumber(sender, args[0])) return;

        MountainData mountain = Lazarus.getInstance().getMountainManager().getMountain(Integer.parseInt(args[0]));

        if(mountain == null) {
            sender.sendMessage(Language.MOUNTAIN_PREFIX + Language.MOUNTAIN_EXCEPTION_DOESNT_EXISTS.replace("<id>", args[0]));
            return;
        }

        mountain.respawn();
        Messages.sendMessage(Placeholder.MountainReplacer.parse(mountain, Language.MOUNTAIN_RESPAWN_RESPAWNED));
    }
}
