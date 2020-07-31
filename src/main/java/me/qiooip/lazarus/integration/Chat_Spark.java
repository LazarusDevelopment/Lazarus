package me.qiooip.lazarus.integration;

import com.voltanemc.spark.player.SparkPlayer;
import com.voltanemc.spark.player.prefix.Prefix;
import com.voltanemc.spark.player.rank.Rank;
import me.qiooip.lazarus.handlers.chat.ChatHandler;
import org.bukkit.entity.Player;

public class Chat_Spark extends ChatHandler {

    @Override
    public String getRankName(Player player) {
        return SparkPlayer.getByUUID(player.getUniqueId()).getActiveRank().getName();
    }

    @Override
    public String getPrefix(Player player) {
        Rank rank = SparkPlayer.getByUUID(player.getUniqueId()).getActiveRank();
        return rank.getPrefix() != null ? rank.getPrefix() : "";
    }

    @Override
    protected String getSuffix(Player player) {
        Prefix prefix = SparkPlayer.getByUUID(player.getUniqueId()).getActivePrefix();
        return prefix != null ? prefix.getDisplay() : "";
    }

    @Override
    protected String getChatColor(Player player) {
        return SparkPlayer.getByUUID(player.getUniqueId()).getChatColor();
    }
}
