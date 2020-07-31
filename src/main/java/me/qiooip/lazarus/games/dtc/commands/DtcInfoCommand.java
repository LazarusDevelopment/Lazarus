package me.qiooip.lazarus.games.dtc.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.dtc.DtcData;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;

public class DtcInfoCommand extends SubCommand {

    DtcInfoCommand() {
        super("info");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        DtcData dtcData = Lazarus.getInstance().getDtcManager().getDtcData();

        if(dtcData.getLocation() == null) {
            sender.sendMessage(Language.DTC_PREFIX + Language.DTC_INFO_NOT_SETUP);
            return;
        }

        Language.DTC_INFO_MESSAGE.forEach(line -> sender.sendMessage(line
        .replace("<location>", StringUtils.getLocationNameWithWorld(dtcData.getLocation()))
        .replace("<amount>", String.valueOf(Config.DTC_CORE_BREAKS))));
    }
}
