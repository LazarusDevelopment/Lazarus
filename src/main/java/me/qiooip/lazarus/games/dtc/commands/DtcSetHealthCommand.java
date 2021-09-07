package me.qiooip.lazarus.games.dtc.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.dtc.DtcData;
import me.qiooip.lazarus.utils.Messages;
import org.bukkit.command.CommandSender;

public class DtcSetHealthCommand extends SubCommand {

    public DtcSetHealthCommand() {
        super("sethealth", "lazarus.dtc.sethealth");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Language.DTC_PREFIX + Language.DTC_SET_HEALTH_USAGE);
            return;
        }

        if(!Lazarus.getInstance().getDtcManager().isActive()) {
            sender.sendMessage(Language.DTC_PREFIX + Language.DTC_EXCEPTION_NOT_RUNNING);
            return;
        }

        if(!this.checkNumber(sender, args[0])) return;

        int newHealth = Math.max(1, Math.abs(Integer.parseInt(args[0])));

        DtcData dtcData = Lazarus.getInstance().getDtcManager().getDtcData();
        dtcData.setBreaksLeft(newHealth);

        Messages.sendMessage(Language.DTC_PREFIX + Language.DTC_SET_HEALTH_CHANGED
            .replace("<health>", String.valueOf(newHealth)));
    }
}
