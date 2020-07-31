package me.qiooip.lazarus.games.dtc.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.dtc.DtcData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class DtcTeleportCommand extends SubCommand {

    DtcTeleportCommand() {
        super("teleport", Collections.singletonList("tp"), "lazarus.dtc.teleport", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        DtcData dtcData = Lazarus.getInstance().getDtcManager().getDtcData();

        if(dtcData.getLocation() == null) {
            sender.sendMessage(Language.DTC_PREFIX + Language.DTC_TELEPORT_CORE_NOT_SET);
            return;
        }

        Player player = (Player) sender;
        player.teleport(dtcData.getLocation().clone().add(0.5, 2, 0.5));
    }
}
