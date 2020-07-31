package me.qiooip.lazarus.integration;

import me.qiooip.lazarus.handlers.chat.ChatHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.tyrannyofheaven.bukkit.zPermissions.ZPermissionsService;

public class Chat_zPermissions extends ChatHandler {

    private final ZPermissionsService service;

    public Chat_zPermissions() {
        this.service = Bukkit.getServicesManager().load(ZPermissionsService.class);
    }

    @Override
    public String getRankName(Player player) {
        return this.service.getPlayerPrimaryGroup(player.getUniqueId());
    }

    @Override
    public String getPrefix(Player player) {
        return this.service.getPlayerPrefix(player.getUniqueId());
    }

    @Override
    protected String getSuffix(Player player) {
        return this.service.getPlayerSuffix(player.getUniqueId());
    }
}
