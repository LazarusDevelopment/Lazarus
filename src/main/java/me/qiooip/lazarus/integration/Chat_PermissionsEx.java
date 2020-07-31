package me.qiooip.lazarus.integration;

import me.qiooip.lazarus.handlers.chat.ChatHandler;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.List;

public class Chat_PermissionsEx extends ChatHandler {

    @Override
    public String getRankName(Player player) {
        List<String> groups = PermissionsEx.getUser(player).getParentIdentifiers();
        return !groups.isEmpty() ? groups.get(0) : "";
    }

    @Override
    public String getPrefix(Player player) {
        return PermissionsEx.getUser(player).getPrefix();
    }

    @Override
    protected String getSuffix(Player player) {
        return PermissionsEx.getUser(player).getSuffix();
    }
}
