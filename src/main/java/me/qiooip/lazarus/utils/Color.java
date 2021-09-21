package me.qiooip.lazarus.utils;

import org.bukkit.ChatColor;

public class Color {

    public static ChatColor[] CHAT_COLOR_CACHE = ChatColor.values();

    public static String translate(String line) {
        return ChatColor.translateAlternateColorCodes('&', line);
    }

    public static String strip(String line) {
        return ChatColor.stripColor(line);
    }
}
