package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.staffmode.StaffModeManager;
import me.qiooip.lazarus.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class StaffModeCommand extends BaseCommand {

    public StaffModeCommand() {
        super("staffmode", Arrays.asList("staff", "mod"), "lazarus.staffmode");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        StaffModeManager manager = Lazarus.getInstance().getStaffModeManager();

        if(args.length == 0) {
            if(!this.checkConsoleSender(sender)) return;

            Player player = (Player) sender;
            manager.toggleStaffMode(player);
            return;
        }

        if(!this.checkPermission(sender, "lazarus.staffmode.others")) return;

        Player target = Bukkit.getPlayer(args[0]);
        if(!this.checkPlayer(sender, target, args[0])) return;

        manager.toggleStaffMode(target);

        this.sendChangedMessage(sender, target, manager.isInStaffMode(target)
            ? Language.STAFF_MODE_ENABLED_OTHERS : Language.STAFF_MODE_DISABLED_OTHERS);
    }

    private void sendChangedMessage(CommandSender sender, Player target, String message) {
        Messages.sendMessage(Language.PREFIX + message
            .replace("<player>", sender.getName())
            .replace("<target>", target.getName()), "lazarus.staff");
    }
}
