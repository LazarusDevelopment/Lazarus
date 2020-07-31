package me.qiooip.lazarus.commands.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class FreezeAllCommand extends BaseCommand {

    public FreezeAllCommand() {
        super("freezeall", Collections.singletonList("ssall"), "lazarus.freezeall");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Lazarus.getInstance().getFreezeHandler().toggleFreezeAll(sender);
    }
}
