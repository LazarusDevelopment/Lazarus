package me.qiooip.lazarus.integration;

import me.TechsCode.UltraPermissions.UltraPermissions;
import me.TechsCode.UltraPermissions.storage.objects.User;
import me.qiooip.lazarus.handlers.chat.ChatHandler;
import org.bukkit.entity.Player;

import java.util.Optional;

public class Chat_UltraPermissions extends ChatHandler {

    @Override
    public String getPrefix(Player player) {
        Optional<User> userOptional = UltraPermissions.getAPI().getUsers().uuid(player.getUniqueId());
        return userOptional.map(user -> user.getPrefix().orElse("")).orElse("");
    }

    @Override
    protected String getSuffix(Player player) {
        Optional<User> userOptional = UltraPermissions.getAPI().getUsers().uuid(player.getUniqueId());
        return userOptional.map(user -> user.getSuffix().orElse("")).orElse("");
    }
}
