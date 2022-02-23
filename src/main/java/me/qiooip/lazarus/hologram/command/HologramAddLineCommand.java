package me.qiooip.lazarus.hologram.command;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.command.CommandSender;

public class HologramAddLineCommand extends SubCommand {

    public HologramAddLineCommand() {
        super("addline", "lazarus.holograms");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(Language.HOLOGRAMS_PREFIX + Language.HOLOGRAMS_ADD_LINE_USAGE);
            return;
        }

        if(!this.checkNumber(sender, args[0])) return;

        int hologramId = Math.abs(Integer.parseInt(args[0]));
        String text = StringUtils.joinArray(args, " ", 2);

        Lazarus.getInstance().getHologramManager().addHologramLine(sender, hologramId, text);
    }
}
