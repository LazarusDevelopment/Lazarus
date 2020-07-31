package me.qiooip.lazarus.handlers.chat;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class BlockedCommandsHandler extends Handler implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("lazarus.blockedcommands.bypass")) return;

        String[] args = event.getMessage().split(" ", 2);

        if(args[0].contains(":") && Config.BLOCKED_COMMANDS_DISABLE_COLON) {
            event.setCancelled(true);
            player.sendMessage(Language.PREFIX + Language.DISABLED_COMMANDS_COLON_DISABLED);
            return;
        }

        if(Config.BLOCKED_COMMANDS.stream().noneMatch(command -> args[0].equalsIgnoreCase(command))) return;

        event.setCancelled(true);
        player.sendMessage(Language.PREFIX + Language.DISABLED_COMMANDS_MESSAGE);
    }
}
