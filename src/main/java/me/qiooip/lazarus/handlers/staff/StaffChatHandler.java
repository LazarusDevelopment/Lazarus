package me.qiooip.lazarus.handlers.staff;

import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StaffChatHandler extends Handler implements Listener {

    private final List<UUID> staffChat;

    public StaffChatHandler() {
        this.staffChat = new ArrayList<>();
    }

    @Override
    public void disable() {
        this.staffChat.clear();
    }

    public void toggleStaffChat(Player player) {
        if(this.isStaffChatEnabled(player)) {
            this.staffChat.remove(player.getUniqueId());
            player.sendMessage(Language.PREFIX + Language.STAFF_CHAT_DISABLED);
        } else {
            this.staffChat.add(player.getUniqueId());
            player.sendMessage(Language.PREFIX + Language.STAFF_CHAT_ENABLED);
        }
    }

    public boolean isStaffChatEnabled(Player player) {
        return this.staffChat.contains(player.getUniqueId());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if(!this.isStaffChatEnabled(event.getPlayer())) return;

        if(!event.getPlayer().hasPermission("lazarus.staffchat")) {
            this.staffChat.remove(event.getPlayer().getUniqueId());
            return;
        }

        event.setCancelled(true);

        Messages.sendMessage(Language.STAFF_CHAT_FORMAT.replace("<player>", event.getPlayer().getName())
        .replace("<message>", event.getMessage()), "lazarus.staffchat");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.staffChat.remove(event.getPlayer().getUniqueId());
    }
}
