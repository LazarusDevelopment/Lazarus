package me.qiooip.lazarus.abilities.commands;

import me.qiooip.lazarus.commands.manager.BaseCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class AbilityTimerCommand extends BaseCommand {

    public AbilityTimerCommand() {
        super("abilitytimer", Collections.singletonList("abilitiestimer"), "lazarus.abilitytimer");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

    }
}
