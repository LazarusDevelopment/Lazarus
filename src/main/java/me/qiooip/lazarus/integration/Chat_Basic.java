package me.qiooip.lazarus.integration;

import com.broustudio.CoreAPI.CoreAPI;
import me.qiooip.lazarus.handlers.chat.ChatHandler;
import org.bukkit.entity.Player;

public class Chat_Basic extends ChatHandler {

    @Override
    public String getRankName(Player player) {
        return CoreAPI.plugin.rankManager.getRank(player.getUniqueId());
    }

    @Override
    protected String getTag(Player player) {
        String tag = CoreAPI.plugin.tagManager.getTagDisplay(player.getUniqueId());
        return tag != null ? tag : "";
    }

    @Override
    public String getPrefix(Player player) {
        String prefix = CoreAPI.plugin.rankManager.getRankPrefix(player.getUniqueId());
        return prefix != null ? prefix : "";
    }

    @Override
    public String getNameColor(Player player) {
        String nameColor = CoreAPI.plugin.rankManager.getRankColor(player.getUniqueId());
        return nameColor != null ? nameColor : "";
    }
}
