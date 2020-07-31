package me.qiooip.lazarus.games.mountain.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.mountain.MountainData;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class MountainDeleteCommand extends SubCommand {

    MountainDeleteCommand() {
        super("delete", Collections.singletonList("remove"), "lazarus.mountain.delete");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.MOUNTAIN_PREFIX + Language.MOUNTAIN_REMOVE_USAGE);
            return;
        }

        if(!this.checkNumber(sender, args[0])) return;

        MountainData mountain = Lazarus.getInstance().getMountainManager().getMountain(Integer.parseInt(args[0]));

        if(mountain == null) {
            sender.sendMessage(Language.MOUNTAIN_PREFIX + Language.MOUNTAIN_EXCEPTION_DOESNT_EXISTS.replace("<id>", args[0]));
            return;
        }

        Lazarus.getInstance().getMountainManager().deleteMountain(mountain);

        sender.sendMessage(Language.MOUNTAIN_PREFIX + Language.MOUNTAIN_REMOVE_REMOVED
        .replace("<id>", String.valueOf(mountain.getId())));
    }
}
