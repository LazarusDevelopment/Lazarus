package me.qiooip.lazarus.integration;

import land.potion.core.api.CoreAPI;
import land.potion.core.api.tag.Tag;
import me.qiooip.lazarus.handlers.chat.ChatHandler;
import org.bukkit.entity.Player;

public class Chat_Core extends ChatHandler {

    @Override
    public String getRankName(Player player) {
        return CoreAPI.getInstance().getRank(player).getName();
    }

    @Override
    protected String getTag(Player player) {
        Tag tag = CoreAPI.getInstance().getTag(player);
        return tag != null ? tag.getPrefix() : "";
    }

    @Override
    public String getPrefix(Player player) {
        return CoreAPI.getInstance().getRank(player).getPrefix();
    }
}
