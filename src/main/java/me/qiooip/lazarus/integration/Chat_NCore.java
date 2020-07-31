package me.qiooip.lazarus.integration;

import me.absurd.ncore.NCore;
import me.qiooip.lazarus.handlers.chat.ChatHandler;
import org.bukkit.entity.Player;

public class Chat_NCore extends ChatHandler {

    @Override
    public String getRankName(Player player) {
        String rankName = NCore.api.useApi.getRank(player.getUniqueId());
        return rankName != null ? rankName : "";
    }

    @Override
    protected String getTag(Player player) {
        String tag =  NCore.api.useApi.getTag(player.getUniqueId());
        return tag != null ? tag : "";
    }

    @Override
    public String getPrefix(Player player) {
        String prefix = NCore.api.useApi.getPrefix(player.getUniqueId());
        return prefix != null ? prefix : "";
    }

    @Override
    public String getNameColor(Player player) {
        String nameColor = NCore.api.useApi.getNameColor(player.getUniqueId());
        return nameColor != null ? nameColor : "";
    }

    @Override
    protected String getSuffix(Player player) {
        String suffix = NCore.api.useApi.getSuffix(player.getUniqueId());
        return suffix != null ? suffix : "";
    }
}
