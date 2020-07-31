package me.qiooip.lazarus.integration;

import com.mizuledevelopment.chronium.api.ChroniumAPI;
import me.qiooip.lazarus.handlers.chat.ChatHandler;
import org.bukkit.entity.Player;

public class Chat_Chronium extends ChatHandler {

    @Override
    public String getRankName(Player player) {
        return ChroniumAPI.getApiInstance().getRankName(player.getUniqueId());
    }

    @Override
    public String getPrefix(Player player) {
        return ChroniumAPI.getApiInstance().getPrefix(player.getUniqueId());
    }

    @Override
    public String getNameColor(Player player) {
        return ChroniumAPI.getApiInstance().getRankColor(player.getUniqueId());
    }

    @Override
    protected String getSuffix(Player player) {
        return ChroniumAPI.getApiInstance().getSuffix(player.getUniqueId());
    }
}
