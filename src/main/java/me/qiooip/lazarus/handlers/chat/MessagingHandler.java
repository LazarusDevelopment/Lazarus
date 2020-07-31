package me.qiooip.lazarus.handlers.chat;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.userdata.Userdata;
import me.qiooip.lazarus.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MessagingHandler extends Handler implements Listener {

    private final Map<UUID, UUID> replies;
    private final Set<UUID> socialSpy;

    public MessagingHandler() {
        this.replies = new HashMap<>();
        this.socialSpy = new HashSet<>();
    }

    @Override
    public void disable() {
        this.replies.clear();
        this.socialSpy.clear();
    }

    public void sendMessage(Player sender, Player target, String message) {
        if(!this.isIgnoring(sender, target, message)) {
            this.sendMessageToBoth(sender, target, message);
        }
    }

    public void sendReplyMessage(Player sender, String message) {
        Player target = Bukkit.getPlayer(this.replies.get(sender.getUniqueId()));

        if(target == null) {
            sender.sendMessage(Language.PREFIX + Language.REPLY_NOBODY_TO_REPLY);
            this.replies.remove(sender.getUniqueId());
            return;
        }

        this.sendMessage(sender, target, message);
    }

    private void sendMessageToBoth(Player sender, Player target, String message) {
        this.replies.put(sender.getUniqueId(), target.getUniqueId());
        this.replies.put(target.getUniqueId(), sender.getUniqueId());

        sender.sendMessage(Language.MESSAGE_SEND_FORMAT
            .replace("<rankPrefix>", Color.translate(ChatHandler.getInstance().getPrefix(target)))
            .replace("<player>", target.getName()) + message);

        target.sendMessage(Language.MESSAGE_RECEIVE_FORMAT
            .replace("<rankPrefix>", Color.translate(ChatHandler.getInstance().getPrefix(sender)))
            .replace("<player>", sender.getName()) + message);

        this.socialSpy.stream().map(Bukkit::getPlayer).forEach(staff -> staff.sendMessage(Language.SOCIAL_SPY_MESSAGE_FORMAT
        .replace("<sender>", sender.getName()).replace("<target>", target.getName()).replace("<message>", message)));
    }

    private boolean isIgnoring(Player sender, Player target, String message) {
        Userdata data = Lazarus.getInstance().getUserdataManager().getUserdata(target);

        if(!data.getSettings().isMessages()) {
            sender.sendMessage(Language.PREFIX + Language.MESSAGE_MESSAGES_DISABLED.replace("<player>", target.getName()));
            return true;
        }

        if(data.isIgnoring(sender)) {
            sender.sendMessage(Language.MESSAGE_SEND_FORMAT
                .replace("<rankPrefix>", Color.translate(ChatHandler.getInstance().getPrefix(target)))
                .replace("<player>", target.getName()) + message);

            return true;
        }

        if(data.getSettings().isSounds()) {
            target.playSound(target.getLocation(), Sound.NOTE_PIANO, 1.0f, 1.0f);
        }

        return false;
    }

    public boolean isSocialSpying(Player player) {
        return this.socialSpy.contains(player.getUniqueId());
    }

    public void toggleSocialSpy(Player player) {
        if(this.isSocialSpying(player)) {
            this.socialSpy.remove(player.getUniqueId());
            player.sendMessage(Language.PREFIX + Language.SOCIAL_SPY_DISABLED);
        } else {
            this.socialSpy.add(player.getUniqueId());
            player.sendMessage(Language.PREFIX + Language.SOCIAL_SPY_ENABLED);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.replies.remove(event.getPlayer().getUniqueId());
        this.socialSpy.remove(event.getPlayer().getUniqueId());
    }
}
