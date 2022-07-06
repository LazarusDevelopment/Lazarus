package me.qiooip.lazarus.hologram.command;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HologramCreateCommand extends SubCommand {

    public HologramCreateCommand() {
        super("create", "lazarus.holograms", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 1) {
            sender.sendMessage(Language.HOLOGRAMS_PREFIX + Language.HOLOGRAMS_CREATE_USAGE);
            return;
        }

        Player player = (Player) sender;
        Lazarus.getInstance().getHologramManager().createHologram(player, args[0]);
    }
}
