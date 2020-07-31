package me.qiooip.lazarus.integration;

import com.broustudio.AtomAPI.AtomAPI;
import me.qiooip.lazarus.handlers.chat.ChatHandler;
import org.bukkit.entity.Player;

public class Chat_Atom extends ChatHandler {

    @Override
    public String getRankName(Player player) {
        return AtomAPI.getInstance().rankManager.getRank(player.getUniqueId());
    }

    @Override
    public String getPrefix(Player player) {
        return AtomAPI.getInstance().rankManager.getRankPrefix(player.getUniqueId());
    }

    @Override
    public String getNameColor(Player player) {
        return AtomAPI.getInstance().rankManager.getRankColor(player.getUniqueId());
    }

    @Override
    protected String getSuffix(Player player) {
        return AtomAPI.getInstance().rankManager.getRankSuffix(player.getUniqueId());
    }
}
