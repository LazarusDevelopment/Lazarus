package me.qiooip.lazarus.integration;

import me.qiooip.lazarus.handlers.chat.ChatHandler;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

public class Chat_LuckPerms extends ChatHandler {

    private final LuckPerms luckPerms;

    public Chat_LuckPerms() {
        this.luckPerms = Bukkit.getServicesManager().load(LuckPerms.class);
    }

    private CachedMetaData getMetaData(Player player) {
        User user = this.luckPerms.getUserManager().getUser(player.getUniqueId());

        if(user == null) {
            throw new IllegalArgumentException("LuckPerms user for " + player.getName() + " could not be found");
        }

        Optional<QueryOptions> queryOptions = this.luckPerms.getContextManager().getQueryOptions(user);

        if(!queryOptions.isPresent()) {
            throw new IllegalArgumentException("LuckPerms context of " + player.getName() + " could not be loaded");
        }

        return user.getCachedData().getMetaData(queryOptions.get());
    }

    @Override
    public String getRankName(Player player) {
        User user = this.luckPerms.getUserManager().getUser(player.getUniqueId());

        if(user == null) {
            throw new IllegalArgumentException("LuckPerms user for " + player.getName() + " could not be found");
        }

        return user.getPrimaryGroup();
    }

    @Override
    public String getPrefix(Player player) {
        String prefix = this.getMetaData(player).getPrefix();
        return prefix == null ? "" : prefix;
    }

    @Override
    protected String getSuffix(Player player) {
        String prefix = this.getMetaData(player).getSuffix();
        return prefix == null ? "" : prefix;
    }
}
