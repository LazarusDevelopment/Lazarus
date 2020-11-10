package me.qiooip.lazarus.integration;

import club.frozed.zoom.ZoomAPI;
import me.qiooip.lazarus.handlers.chat.ChatHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Chat_Zoom extends ChatHandler {

    @Override
    public String getRankName(Player player) {
        return ZoomAPI.getRankName(player);
    }

    @Override
    public String getPrefix(Player player) {
        return ZoomAPI.getRankPrefix(player);
    }

    @Override
    public String getNameColor(Player player) {
        ChatColor nameColor = ZoomAPI.getNameColor(player);
        return nameColor != null ? nameColor.toString() : "";
    }

    @Override
    protected String getSuffix(Player player) {
        return ZoomAPI.getRankSuffix(player);
    }

    @Override
    protected String getChatColor(Player player) {
        ChatColor chatColor = ZoomAPI.getChatColor(player);
        return chatColor != null ? chatColor.toString() : "";
    }
}
