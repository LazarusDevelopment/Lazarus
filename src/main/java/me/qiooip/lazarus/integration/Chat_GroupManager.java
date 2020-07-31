package me.qiooip.lazarus.integration;

import me.qiooip.lazarus.handlers.chat.ChatHandler;
import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Chat_GroupManager extends ChatHandler {

    private final GroupManager groupManager;

    public Chat_GroupManager() {
        this.groupManager = (GroupManager) Bukkit.getServer().getPluginManager().getPlugin("GroupManager");
    }

    @Override
    public String getRankName(Player player) {
        return this.groupManager.getWorldsHolder().getWorldPermissions(player).getGroup(player.getName());
    }

    @Override
    public String getPrefix(Player player) {
        return this.groupManager.getWorldsHolder().getWorldPermissions(player).getUserPrefix(player.getName());
    }

    @Override
    protected String getSuffix(Player player) {
        return this.groupManager.getWorldsHolder().getWorldPermissions(player).getUserSuffix(player.getName());
    }
}
