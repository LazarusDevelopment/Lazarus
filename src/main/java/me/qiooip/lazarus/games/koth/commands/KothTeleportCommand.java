package me.qiooip.lazarus.games.koth.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.koth.KothData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class KothTeleportCommand extends SubCommand {

    KothTeleportCommand() {
        super("teleport", Collections.singletonList("tp"), "lazarus.koth.teleport", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_TELEPORT_USAGE);
            return;
        }

        KothData koth = Lazarus.getInstance().getKothManager().getKoth(args[0]);

        if(koth == null) {
            sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_EXCEPTION_DOESNT_EXIST.replace("<koth>", args[0]));
            return;
        }

        Player player = (Player) sender;
        player.teleport(koth.getCuboid().getCenter());
    }
}
