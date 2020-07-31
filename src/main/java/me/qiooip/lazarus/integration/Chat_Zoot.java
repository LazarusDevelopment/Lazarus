package me.qiooip.lazarus.integration;

import com.minexd.zoot.ZootAPI;
import me.qiooip.lazarus.handlers.chat.ChatHandler;
import org.bukkit.entity.Player;

public class Chat_Zoot extends ChatHandler {

    @Override
    public String getRankName(Player player) {
        return ZootAPI.getRankOfPlayer(player).getDisplayName();
    }

    @Override
    public String getPrefix(Player player) {
        return ZootAPI.getRankOfPlayer(player).getPrefix();
    }

    @Override
    public String getNameColor(Player player) {
        return ZootAPI.getColorOfPlayer(player).toString();
    }

    @Override
    protected String getSuffix(Player player) {
        return ZootAPI.getRankOfPlayer(player).getSuffix();
    }
}
