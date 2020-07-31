package me.qiooip.lazarus.games.koth.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.koth.KothData;
import me.qiooip.lazarus.selection.Selection;
import me.qiooip.lazarus.selection.SelectionType;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class KothCreateCommand extends SubCommand {

    KothCreateCommand() {
        super("create", Collections.singletonList("new"), "lazarus.koth.create", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_CREATE_USAGE);
            return;
        }

        KothData koth = Lazarus.getInstance().getKothManager().getKoth(args[0]);

        if(koth != null) {
            sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_CREATE_ALREADY_EXISTS.replace("<koth>", koth.getName()));
            return;
        }

        Player player = (Player) sender;
        Selection selection = Lazarus.getInstance().getSelectionManager().getSelection(player);

        if(selection == null || selection.getType() != SelectionType.EVENT_CLAIM) {
            Lazarus.getInstance().getSelectionManager().toggleSelectionProcess(player, SelectionType.EVENT_CLAIM, null);
            player.sendMessage(Language.KOTH_PREFIX + Language.KOTH_CREATE_MAKE_A_SELECTION);
            return;
        }

        if(!selection.areBothPositionsSet()) {
            player.sendMessage(Language.KOTH_PREFIX + Language.KOTH_CREATE_SET_BOTH_POSITIONS);
            return;
        }

        int captime = args.length == 1 ? Config.KOTH_DEFAULT_CAP_TIME : StringUtils.parseSeconds(args[1]);

        if(captime == -1) {
            sender.sendMessage(Language.KIT_PREFIX + Language.COMMANDS_INVALID_DURATION);
            return;
        }

        Lazarus.getInstance().getKothManager().createKoth(sender, args[0], captime, selection.toCuboid());
        Lazarus.getInstance().getSelectionManager().removeSelectionProcess(player);

        player.sendMessage(Language.KOTH_PREFIX + Language.KOTH_CREATE_CREATED.replace("<koth>", args[0]));
    }
}
