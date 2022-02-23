package me.qiooip.lazarus.hologram.command;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;

public class HologramRemoveLineCommand extends SubCommand {

    public HologramRemoveLineCommand() {
        super("removeline", "lazarus.holograms");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(Language.HOLOGRAMS_PREFIX + Language.HOLOGRAMS_REMOVE_LINE_USAGE);
            return;
        }

        if(!this.checkNumber(sender, args[0]) || !this.checkNumber(sender, args[1])) return;

        int hologramId = Math.abs(Integer.parseInt(args[0]));
        int lineNumber = Math.abs(Integer.parseInt(args[1]));

        Lazarus.getInstance().getHologramManager().removeHologramLine(sender, hologramId, lineNumber);
    }
}
