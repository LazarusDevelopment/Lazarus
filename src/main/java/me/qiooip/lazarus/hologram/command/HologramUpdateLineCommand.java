package me.qiooip.lazarus.hologram.command;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;

public class HologramUpdateLineCommand extends SubCommand {

    public HologramUpdateLineCommand() {
        super("updateline", "lazarus.holograms");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 3) {
            sender.sendMessage(Language.HOLOGRAMS_PREFIX + Language.HOLOGRAMS_UPDATE_LINE_USAGE);
            return;
        }

        if(!this.checkNumber(sender, args[0]) || !this.checkNumber(sender, args[1])) return;

        int hologramId = Math.abs(Integer.parseInt(args[0]));
        int lineNumber = Math.abs(Integer.parseInt(args[1]));
        String text = StringUtils.joinArray(args, " ", 3);

        Lazarus.getInstance().getHologramManager().updateHologramLine(sender, hologramId, lineNumber, text);
    }
}
