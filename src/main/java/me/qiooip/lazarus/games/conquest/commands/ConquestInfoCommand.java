package me.qiooip.lazarus.games.conquest.commands;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.Placeholder;
import me.qiooip.lazarus.games.conquest.ConquestData;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class ConquestInfoCommand extends SubCommand {

    ConquestInfoCommand() {
        super("info");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ConquestData conquest = Lazarus.getInstance().getConquestManager().getConquest();

        if(conquest.getCuboids().values().stream().anyMatch(Objects::isNull)) {
            sender.sendMessage(Language.CONQUEST_PREFIX + Language.CONQUEST_INFO_NOT_SETUP);
            return;
        }

        Language.CONQUEST_INFO_MESSAGE.forEach(line -> sender.sendMessage(Placeholder
        .ConquestReplacer.parse(conquest, line)));
    }
}
