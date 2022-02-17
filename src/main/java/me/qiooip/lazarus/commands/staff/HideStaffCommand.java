package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HideStaffCommand extends BaseCommand {

    public HideStaffCommand() {
        super("hidestaff", "lazarus.hidestaff", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Lazarus.getInstance().getVanishManager().toggleHideStaff(player);
    }
}
