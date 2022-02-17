package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import me.qiooip.lazarus.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;

public class DayCommand extends BaseCommand {

    public DayCommand() {
        super("day", "lazarus.day");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Bukkit.getWorlds().stream()
            .filter(world -> world.getEnvironment() == Environment.NORMAL)
            .forEach(world -> world.setTime(0));

        sender.sendMessage(Language.PREFIX + Language.TIME_MESSAGE_DAY);
    }
}
