package me.qiooip.lazarus.integration;

import me.activated.core.api.player.PlayerData;
import me.activated.core.plugin.AquaCoreAPI;
import me.qiooip.lazarus.handlers.chat.ChatHandler;
import org.bukkit.entity.Player;

public class Chat_AquaCore extends ChatHandler {

    @Override
    public String getRankName(Player player) {
        return AquaCoreAPI.INSTANCE.getPlayerRank(player.getUniqueId()).getName();
    }

    @Override
    protected String getTag(Player player) {
        return AquaCoreAPI.INSTANCE.getTagFormat(player);
    }

    @Override
    public String getPrefix(Player player) {
        return AquaCoreAPI.INSTANCE.getPlayerRank(player.getUniqueId()).getPrefix();
    }

    @Override
    public String getNameColor(Player player) {
        return AquaCoreAPI.INSTANCE.getPlayerNameColor(player.getUniqueId()).toString();
    }

    @Override
    protected String getSuffix(Player player) {
        return AquaCoreAPI.INSTANCE.getPlayerRank(player.getUniqueId()).getSuffix();
    }

    @Override
    protected String getChatColor(Player player) {
        PlayerData data = AquaCoreAPI.INSTANCE.getPlayerData(player.getUniqueId());

        return data.getChatColor() != null ? data.getChatColor()
            .toString() : data.getHighestRank().getChatColor().toString();
    }
}
