package me.qiooip.lazarus.commands.base;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.commands.manager.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class SocialSpyCommand extends BaseCommand {

    public SocialSpyCommand() {
        super("socialspy", Collections.singletonList("chatspy"), "lazarus.socialspy", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Lazarus.getInstance().getMessagingHandler().toggleSocialSpy(player);
    }
}
