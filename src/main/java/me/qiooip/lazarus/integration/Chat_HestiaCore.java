package me.qiooip.lazarus.integration;

import me.qiooip.lazarus.handlers.chat.ChatHandler;
import me.quartz.hestia.HestiaAPI;
import org.bukkit.entity.Player;

public class Chat_HestiaCore extends ChatHandler {

    @Override
    public String getRankName(Player player) {
        return HestiaAPI.instance.getRank(player.getUniqueId());
    }

    @Override
    protected String getTag(Player player) {
        return HestiaAPI.instance.getTag(player.getUniqueId());
    }

    @Override
    public String getPrefix(Player player) {
        return HestiaAPI.instance.getRankPrefix(player.getUniqueId());
    }

    @Override
    public String getNameColor(Player player) {
        return HestiaAPI.instance.getRankColor(player.getUniqueId()).toString();
    }

    @Override
    protected String getSuffix(Player player) {
        return HestiaAPI.instance.getRankSuffix(player.getUniqueId());
    }
}
