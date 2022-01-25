package me.qiooip.lazarus.integration;

import com.broustudio.MizuAPI.MizuAPI;
import me.qiooip.lazarus.handlers.chat.ChatHandler;
import org.bukkit.entity.Player;

public class Chat_Mizu extends ChatHandler {

    @Override
    public String getRankName(Player player) {
        return MizuAPI.getAPI().getRank(player.getUniqueId());
    }

    @Override
    protected String getTag(Player player) {
        String tag = MizuAPI.getAPI().getTag(player.getUniqueId());
        String displayTag = MizuAPI.getAPI().getTagDisplay(tag);

        return displayTag != null ? displayTag : "";
    }

    @Override
    public String getPrefix(Player player) {
        String rankPrefix = MizuAPI.getAPI().getRankPrefix(this.getRankName(player));
        return rankPrefix != null ? rankPrefix : "";
    }

    @Override
    public String getNameColor(Player player) {
        String nameColor = MizuAPI.getAPI().getRankColor(this.getRankName(player));
        return nameColor != null ? nameColor : "";
    }

    @Override
    protected String getSuffix(Player player) {
        String rankSuffix = MizuAPI.getAPI().getRankSuffix(this.getRankName(player));
        return rankSuffix != null ? rankSuffix : "";
    }
}
