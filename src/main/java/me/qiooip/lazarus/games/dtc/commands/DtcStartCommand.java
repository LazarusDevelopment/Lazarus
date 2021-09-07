package me.qiooip.lazarus.games.dtc.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;

public class DtcStartCommand extends SubCommand {

    DtcStartCommand() {
        super("start", "lazarus.dtc.start");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(Lazarus.getInstance().getDtcManager().isActive()) {
            sender.sendMessage(Language.DTC_PREFIX + Language.DTC_EXCEPTION_ALREADY_RUNNING);
            return;
        }

        if(args.length == 0) {
            Lazarus.getInstance().getDtcManager().startDtc(sender, Config.DTC_CORE_BREAKS);
            return;
        }

        if(!this.checkNumber(sender, args[0])) return;

        Lazarus.getInstance().getDtcManager().startDtc(sender, Integer.parseInt(args[0]));
    }
}
