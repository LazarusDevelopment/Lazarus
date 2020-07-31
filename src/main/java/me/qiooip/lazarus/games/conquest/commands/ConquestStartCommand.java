package me.qiooip.lazarus.games.conquest.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.command.CommandSender;

public class ConquestStartCommand extends SubCommand {

    ConquestStartCommand() {
        super("start", "lazarus.conquest.start");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(Lazarus.getInstance().getConquestManager().isActive()) {
            sender.sendMessage(Language.CONQUEST_PREFIX + Language.CONQUEST_START_ALREADY_RUNNING);
            return;
        }

        Lazarus.getInstance().getConquestManager().startConquest(sender);
    }
}
