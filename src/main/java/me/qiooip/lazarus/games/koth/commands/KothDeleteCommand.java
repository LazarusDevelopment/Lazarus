package me.qiooip.lazarus.games.koth.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.koth.KothData;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class KothDeleteCommand extends SubCommand {

    KothDeleteCommand() {
        super("delete", Collections.singletonList("remove"), "lazarus.koth.delete");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_REMOVE_USAGE);
            return;
        }

        KothData koth = Lazarus.getInstance().getKothManager().getKoth(args[0]);

        if(koth == null) {
            sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_EXCEPTION_DOESNT_EXIST.replace("<koth>", args[0]));
            return;
        }

        Lazarus.getInstance().getKothManager().deleteKoth(sender, koth);
        sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_REMOVE_REMOVED.replace("<koth>", koth.getName()));
    }
}
