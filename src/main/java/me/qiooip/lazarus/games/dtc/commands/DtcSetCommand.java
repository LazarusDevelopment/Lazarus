package me.qiooip.lazarus.games.dtc.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.dtc.DtcManager;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class DtcSetCommand extends SubCommand {

    DtcSetCommand() {
        super("set", "lazarus.dtc.set", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Block targetBlock = player.getTargetBlock((HashSet<Byte>) null, 5);

        if(targetBlock == null || targetBlock.getType() != Material.OBSIDIAN) {
            player.sendMessage(Language.DTC_PREFIX + Language.DTC_SET_MUST_BE_OBSIDIAN);
            return;
        }

        DtcManager dtcManager = Lazarus.getInstance().getDtcManager();

        dtcManager.getDtcData().setLocation(targetBlock.getLocation());
        if(!dtcManager.isActive()) targetBlock.setType(Material.AIR);

        player.sendMessage(Language.DTC_PREFIX + Language.DTC_SET_CORE_SET.replace("<location>",
        StringUtils.getLocationNameWithWorld(targetBlock.getLocation())));
    }
}
