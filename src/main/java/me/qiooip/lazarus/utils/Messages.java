package me.qiooip.lazarus.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class Messages {

    public static void sendMessage(String message) {
        if(message.isEmpty()) return;

        Bukkit.getConsoleSender().sendMessage(message);
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
    }

    public static void sendMessage(List<String> messages) {
        if(messages.isEmpty()) return;

        messages.forEach(message -> Bukkit.getConsoleSender().sendMessage(message));
        Bukkit.getOnlinePlayers().forEach(player -> messages.forEach(message -> player.sendMessage(message)));
    }

    public static void sendMessageWithoutPlayer(Player player, String message) {
        if(message.isEmpty()) return;

        Bukkit.getConsoleSender().sendMessage(message);

        Bukkit.getOnlinePlayers().stream().filter(online -> online != player)
        .forEach(online -> online.sendMessage(message));
    }

    public static void sendMessage(String message, String permission) {
        if(message.isEmpty()) return;

        Bukkit.getConsoleSender().sendMessage(message);

        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission(permission))
        .forEach(player -> player.sendMessage(message));
    }

    public static void sendMessage(List<String> messages, String permission) {
        if(messages.isEmpty()) return;

        messages.forEach(message -> Bukkit.getConsoleSender().sendMessage(message));

        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission(permission))
        .forEach(player -> messages.forEach(message -> player.sendMessage(message)));
    }

    public static void sendMessageWithoutPlayer(Player player, String message, String permission) {
        if(message.isEmpty()) return;

        Bukkit.getConsoleSender().sendMessage(message);

        Bukkit.getOnlinePlayers().stream().filter(online -> online.hasPermission(permission) &&
        online != player).forEach(online -> online.sendMessage(message));
    }
}
