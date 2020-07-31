package me.qiooip.lazarus.factions.commands.admin;

import me.qiooip.lazarus.commands.manager.SubCommand;
import me.qiooip.lazarus.factions.FactionsManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class ChatSpyCommand extends SubCommand {

    public ChatSpyCommand() {
        super("chatspy", Collections.singletonList("socialspy"), "lazarus.factions.chatspy", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        FactionsManager.getInstance().toggleChatSpy(player);
    }
}
