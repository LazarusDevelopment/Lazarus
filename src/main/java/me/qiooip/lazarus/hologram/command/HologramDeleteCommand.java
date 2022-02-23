package me.qiooip.lazarus.hologram.command;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class HologramDeleteCommand extends SubCommand {

    public HologramDeleteCommand() {
        super("delete", Collections.singletonList("remove"), "lazarus.holograms");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(this.prefix + Language.HOLOGRAMS_DELETE_USAGE);
            return;
        }

        if(!this.checkNumber(sender, args[0])) return;

        int hologramId = Integer.parseInt(args[0]);
        Lazarus.getInstance().getHologramManager().deleteHologram(sender, hologramId);
    }
}
