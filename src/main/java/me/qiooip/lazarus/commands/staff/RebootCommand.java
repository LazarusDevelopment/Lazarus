package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;

public class RebootCommand extends BaseCommand {

    public RebootCommand() {
        super("reboot", "lazarus.reboot");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            Language.REBOOT_USAGE.forEach(sender::sendMessage);
            return;
        }

        switch(args[0].toLowerCase()) {
            case "stop":
            case "cancel": {
                if(!Lazarus.getInstance().getRebootHandler().isRebooting()) {
                    sender.sendMessage(Language.PREFIX + Language.REBOOT_NOT_RUNNING);
                    return;
                }

                Lazarus.getInstance().getRebootHandler().cancelReboot();
                return;
            } case "start": {
                if(args.length < 2) {
                    Language.REBOOT_USAGE.forEach(sender::sendMessage);
                    return;
                }

                if(Lazarus.getInstance().getRebootHandler().isRebooting()) {
                    sender.sendMessage(Language.PREFIX + Language.REBOOT_ALREADY_RUNNING);
                    return;
                }

                int duration = StringUtils.parseSeconds(args[1]);

                if(duration == -1) {
                    sender.sendMessage(Language.PREFIX + Language.COMMANDS_INVALID_DURATION);
                    return;
                }

                Lazarus.getInstance().getRebootHandler().startReboot(duration);
                return;
            } default: {
                Language.REBOOT_USAGE.forEach(sender::sendMessage);
            }
        }
    }
}
