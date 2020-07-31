package me.qiooip.lazarus.games.conquest.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.conquest.ConquestData;
import me.qiooip.lazarus.games.conquest.ZoneType;
import me.qiooip.lazarus.selection.Selection;
import me.qiooip.lazarus.selection.SelectionType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConquestSetCapzoneCommand extends SubCommand {

    ConquestSetCapzoneCommand() {
        super("setcapzone", "lazarus.conquest.setcapzone", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.CONQUEST_PREFIX + Language.CONQUEST_SET_CAPZONE_USAGE);
            return;
        }

        ZoneType type;

        try {
            type = ZoneType.valueOf(args[0].toUpperCase());
        } catch(IllegalArgumentException e) {
            sender.sendMessage(Language.CONQUEST_PREFIX + Language.CONQUEST_EXCEPTION_INVALID_ZONE);
            return;
        }

        Player player = (Player) sender;
        Selection selection = Lazarus.getInstance().getSelectionManager().getSelection(player);

        if(selection == null || selection.getType() != SelectionType.EVENT_CLAIM) {
            Lazarus.getInstance().getSelectionManager().toggleSelectionProcess(player, SelectionType.EVENT_CLAIM, null);
            player.sendMessage(Language.CONQUEST_PREFIX + Language.CONQUEST_SET_CAPZONE_MAKE_A_SELECTION);
            return;
        }

        if(!selection.areBothPositionsSet()) {
            player.sendMessage(Language.CONQUEST_PREFIX + Language.CONQUEST_SET_CAPZONE_SET_BOTH_POSITIONS);
            return;
        }

        ConquestData conquest = Lazarus.getInstance().getConquestManager().getConquest();
        conquest.setCuboid(type, selection.toCuboid());

        Lazarus.getInstance().getSelectionManager().removeSelectionProcess(player);

        player.sendMessage(Language.CONQUEST_PREFIX + Language.CONQUEST_SET_CAPZONE_CREATED
        .replace("<zone>", type.getName()));
    }
}
