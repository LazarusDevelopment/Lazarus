package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SalvageCommand extends BaseCommand {

    public SalvageCommand() {
        super("salvage", "lazarus.salvage", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(!ItemUtils.isSalvageable(player.getItemInHand().getType())) {
            player.sendMessage(Language.PREFIX + Language.SALVAGE_NOT_SALVAGEABLE);
            return;
        }

        Lazarus.getInstance().getSalvageHandler().handleSalvage(player);
    }
}
