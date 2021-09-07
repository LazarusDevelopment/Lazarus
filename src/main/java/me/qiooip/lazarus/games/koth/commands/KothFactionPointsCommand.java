package me.qiooip.lazarus.games.koth.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.koth.KothData;
import me.qiooip.lazarus.utils.Messages;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class KothFactionPointsCommand extends SubCommand {

    KothFactionPointsCommand() {
        super("factionpoints", Arrays.asList("setfactionpoints", "setpoints"), "lazarus.koth.factionpoints");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_FACTION_POINTS_USAGE);
            return;
        }

        KothData koth = Lazarus.getInstance().getKothManager().getKoth(args[0]);

        if(koth == null) {
            sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_EXCEPTION_DOESNT_EXIST.replace("<koth>", args[0]));
            return;
        }

        if(!this.checkNumber(sender, args[1])) return;

        int factionPoints = Math.abs(Integer.parseInt(args[1]));
        koth.setFactionPoints(factionPoints);

        Messages.sendMessage(Language.KOTH_PREFIX + Language.KOTH_FACTION_POINTS_CHANGED
            .replace("<koth>", koth.getName())
            .replace("<points>", String.valueOf(factionPoints)));
    }
}
