package me.qiooip.lazarus.games.dtc.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.Messages;
import org.bukkit.command.CommandSender;

public class DtcStopCommand extends SubCommand {

    DtcStopCommand() {
        super("stop", "lazarus.dtc.stop");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!Lazarus.getInstance().getDtcManager().isActive()) {
            sender.sendMessage(Language.DTC_PREFIX + Language.DTC_EXCEPTION_NOT_RUNNING);
            return;
        }

        Lazarus.getInstance().getDtcManager().stopDtc(null);

        Messages.sendMessage(Language.DTC_PREFIX + Language.DTC_STOP_STOPPED
            .replace("<player>", sender.getName()));
    }
}
