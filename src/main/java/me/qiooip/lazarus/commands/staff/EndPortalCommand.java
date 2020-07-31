package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.selection.SelectionType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EndPortalCommand extends BaseCommand {

    public EndPortalCommand() {
        super("endportal", "lazarus.endportal", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        Lazarus.getInstance().getSelectionManager().toggleSelectionProcess(player, SelectionType.SELECTION,
        Lazarus.getInstance().getEndPortalHandler().createSelectionCheck());

        player.sendMessage(Language.PREFIX + Language.ENDPORTAL_SELECTOR_ADDED_TO_INVENTORY);
    }
}
