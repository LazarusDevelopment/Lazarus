package me.qiooip.lazarus.integration;

import me.abhi.core.CoreAPI;
import me.abhi.core.rank.Rank;
import me.qiooip.lazarus.handlers.chat.ChatHandler;
import org.bukkit.entity.Player;

public class Chat_mCore extends ChatHandler {

    private Rank getRank(Player player) {
        return CoreAPI.getRank(player);
    }

    @Override
    public String getRankName(Player player) {
        Rank rank = this.getRank(player);
        return rank == null ? "" : rank.getName();
    }

    @Override
    public String getPrefix(Player player) {
        Rank rank = this.getRank(player);
        return rank == null ? "" : rank.getPrefix() == null ? "" : rank.getPrefix();
    }

    @Override
    public String getNameColor(Player player) {
        Rank rank = this.getRank(player);
        return rank == null ? "" : rank.getLitePrefix() == null ? "" : rank.getLitePrefix();
    }

    @Override
    protected String getSuffix(Player player) {
        Rank rank = this.getRank(player);
        return rank == null ? "" : rank.getSuffix() == null ? "" : rank.getSuffix();
    }
}
