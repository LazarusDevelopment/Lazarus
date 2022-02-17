package me.qiooip.lazarus.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.classes.Miner;
import me.qiooip.lazarus.classes.manager.PvpClass;
import me.qiooip.lazarus.classes.manager.PvpClassType;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class MinerEffectsCommand extends BaseCommand {

    public MinerEffectsCommand() {
        super("minereffects", Collections.singletonList("minereffect"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        PvpClass pvpClass = Lazarus.getInstance().getPvpClassManager()
            .getPvpClassByName(PvpClassType.MINER.getName());

        if(pvpClass instanceof Miner) {
            ((Miner) pvpClass).sendEffectInfo(sender);
        }
    }
}
