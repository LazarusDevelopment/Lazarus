package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.type.SystemTimer;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;

public class SaleCommand extends BaseCommand {

    public SaleCommand() {
        super("sale", "lazarus.sale");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            Language.SALE_USAGE.forEach(sender::sendMessage);
            return;
        }

        if(args.length == 2 && args[0].equalsIgnoreCase("stop")) {
            switch(args[1].toUpperCase()) {
                case "STORE": {
                    this.stopSaleTimer(sender, "Store");
                    return;
                }
                case "KEY": {
                    this.stopSaleTimer(sender, "Key");
                    return;
                }
                default: {
                    Language.SALE_USAGE.forEach(sender::sendMessage);
                    return;
                }
            }
        }

        if(args.length >= 3 && args[0].equalsIgnoreCase("start")) {
            switch(args[1].toUpperCase()) {
                case "STORE": {
                    this.startSaleTime(sender, "Store", args[2]);
                    return;
                }
                case "KEY": {
                    this.startSaleTime(sender, "Key", args[2]);
                    return;
                }
                default: Language.SALE_USAGE.forEach(sender::sendMessage);
            }
        }
    }

    private void startSaleTime(CommandSender sender, String name, String time) {
        SystemTimer timer = name.equalsIgnoreCase("Store") ? TimerManager.getInstance()
            .getSaleTimer() : TimerManager.getInstance().getKeySaleTimer();

        if(timer.isActive()) {
            sender.sendMessage(Language.PREFIX + Language.SALE_EXCEPTION_ALREADY_RUNNING.replace("<type>", name));
            return;
        }

        int duration = StringUtils.parseSeconds(time);

        if(duration == -1) {
            sender.sendMessage(Language.PREFIX + Language.COMMANDS_INVALID_DURATION);
            return;
        }

        timer.activate(duration);

        Messages.sendMessage(Language.SALE_STARTED
            .replace("<type>", name)
            .replace("<time>", timer.getDynamicTimeLeft()));
    }

    private void stopSaleTimer(CommandSender sender, String name) {
        SystemTimer timer = name.equalsIgnoreCase("STORE") ? TimerManager.getInstance()
            .getSaleTimer() : TimerManager.getInstance().getKeySaleTimer();

        if(!timer.isActive()) {
            sender.sendMessage(Language.PREFIX + Language.SALE_EXCEPTION_NOT_RUNNING.replace("<type>", name));
            return;
        }

        timer.cancel();

        Messages.sendMessage(Language.PREFIX + Language.SALE_STOPPED
            .replace("<type>", name)
            .replace("<player>", sender.getName()));
    }
}
