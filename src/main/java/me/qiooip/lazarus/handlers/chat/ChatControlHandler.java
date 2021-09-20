package me.qiooip.lazarus.handlers.chat;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.cooldown.CooldownTimer;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@Getter
@Setter
public class ChatControlHandler extends Handler implements Listener {

    private boolean showFoundOre;
    private boolean muted;
    private int delay;

    public ChatControlHandler() {
        this.showFoundOre = true;
        this.delay = Config.DEFAULT_CHAT_DELAY;
    }

    public void toggleChat(CommandSender sender) {
        if(this.isMuted()) {
            this.muted = false;

            Messages.sendMessage(Language.PREFIX + Language.CHAT_UNMUTED
            .replace("<player>", sender.getName()));
            return;
        }

        this.muted = true;

        Messages.sendMessage(Language.PREFIX + Language.CHAT_MUTED
        .replace("<player>", sender.getName()));
    }

    public void setDelay(CommandSender sender, String time) {
        int duration = StringUtils.parseSeconds(time);

        if(duration == -1) {
            sender.sendMessage(Language.PREFIX + Language.COMMANDS_INVALID_DURATION);
            return;
        }

        this.delay = duration;

        Messages.sendMessage(Language.PREFIX + Language.CHAT_DELAY_BROADCAST
            .replace("<delay>", StringUtils.formatDurationWords(this.delay * 1000L)));
    }

    public void toggleFoundOreMessages(CommandSender sender) {
        if(this.isShowFoundOre()) {
            this.showFoundOre = false;
            sender.sendMessage(Language.PREFIX + Language.CHAT_FOUNDORE_DISABLED);
            return;
        }

        this.showFoundOre = true;
        sender.sendMessage(Language.PREFIX + Language.CHAT_FOUNDORE_ENABLED);
    }

    public void clearChat(CommandSender sender) {
        StringBuilder sb = new StringBuilder();

        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.hasPermission("lazarus.chatcontrol.clear.bypass")) continue;

            for(int i = 0; i < 100; i++) {
                player.sendMessage("\n");
            }
        }

        Messages.sendMessage(Language.PREFIX + Language.CHAT_CLEAR_BROADCAST
        .replace("<player>", sender.getName()));
    }

    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if(this.isMuted() && !player.hasPermission("lazarus.chatcontrol.mute.bypass")) {

            event.setCancelled(true);
            player.sendMessage(Language.PREFIX + Language.CHAT_EVENT_MUTED_MESSAGE);

        } else if(this.getDelay() > 0 && !player.hasPermission("lazarus.chatcontrol.delay.bypass")) {

            CooldownTimer timer = TimerManager.getInstance().getCooldownTimer();

            if(timer.isActive(player, "CHAT")) {
                event.setCancelled(true);
                player.sendMessage(Language.PREFIX + Language.CHAT_COOLDOWN_MESSAGE
                .replace("<seconds>", timer.getTimeLeft(player, "CHAT")));
                return;
            }

            timer.activate(player, "CHAT", this.delay, null);
        }
    }
}
