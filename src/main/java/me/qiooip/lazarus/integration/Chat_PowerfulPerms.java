package me.qiooip.lazarus.integration;

import com.github.gustav9797.PowerfulPerms.PowerfulPerms;
import me.qiooip.lazarus.handlers.chat.ChatHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Chat_PowerfulPerms extends ChatHandler {

    private final PowerfulPerms powerfulPerms;

    public Chat_PowerfulPerms() {
        this.powerfulPerms = (PowerfulPerms) Bukkit.getServer().getPluginManager().getPlugin("PowerfulPerms");
    }

    @Override
    public String getRankName(Player player) {
        return this.powerfulPerms.getPermissionManager().getPermissionPlayer(player.getUniqueId()).getPrimaryGroup().getName();
    }

    @Override
    public String getPrefix(Player player) {
        return this.powerfulPerms.getPermissionManager().getPermissionPlayer(player.getUniqueId()).getPrefix();
    }

    @Override
    protected String getSuffix(Player player) {
        return this.powerfulPerms.getPermissionManager().getPermissionPlayer(player.getUniqueId()).getSuffix();
    }
}
