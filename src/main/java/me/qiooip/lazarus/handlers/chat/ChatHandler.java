package me.qiooip.lazarus.handlers.chat;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.integration.Chat_AquaCore;
import me.qiooip.lazarus.integration.Chat_Atom;
import me.qiooip.lazarus.integration.Chat_NCore;
import me.qiooip.lazarus.integration.Chat_Basic;
import me.qiooip.lazarus.integration.Chat_Chronium;
import me.qiooip.lazarus.integration.Chat_Core;
import me.qiooip.lazarus.integration.Chat_GroupManager;
import me.qiooip.lazarus.integration.Chat_LuckPerms;
import me.qiooip.lazarus.integration.Chat_Mizu;
import me.qiooip.lazarus.integration.Chat_PermissionsEx;
import me.qiooip.lazarus.integration.Chat_PowerfulPerms;
import me.qiooip.lazarus.integration.Chat_Spark;
import me.qiooip.lazarus.integration.Chat_TeikoCore;
import me.qiooip.lazarus.integration.Chat_Zoot;
import me.qiooip.lazarus.integration.Chat_mCore;
import me.qiooip.lazarus.integration.Chat_zPermissions;
import me.qiooip.lazarus.userdata.Userdata;
import me.qiooip.lazarus.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class ChatHandler extends Handler implements Listener {

    @Getter public static ChatHandler instance;

    public static void setup() {
        PluginManager manager = Bukkit.getPluginManager();
        Plugin plugin;

        if((plugin = manager.getPlugin("AquaCore")) != null && plugin.isEnabled()) {
            instance = new Chat_AquaCore();
        } else if((plugin = manager.getPlugin("AtomAPI")) != null && plugin.isEnabled()) {
            instance = new Chat_Atom();
        } else if((plugin = manager.getPlugin("Chronium")) != null && plugin.isEnabled()) {
            instance = new Chat_Chronium();
        } else if((plugin = manager.getPlugin("CoreAPI")) != null && plugin.isEnabled()) {
            instance = new Chat_Basic();
        } else if((plugin = manager.getPlugin("Core")) != null && plugin.isEnabled()) {
            instance = new Chat_Core();
        } else if((plugin = manager.getPlugin("GroupManager")) != null && plugin.isEnabled()) {
            instance = new Chat_GroupManager();
        } else if((plugin = manager.getPlugin("LuckPerms")) != null && plugin.isEnabled()) {
            instance = new Chat_LuckPerms();
        } else if((plugin = manager.getPlugin("mCore")) != null && plugin.isEnabled()) {
            instance = new Chat_mCore();
        } else if((plugin = manager.getPlugin("MizuAPI")) != null && plugin.isEnabled()) {
            instance = new Chat_Mizu();
        } else if((plugin = manager.getPlugin("NCore")) != null && plugin.isEnabled()) {
            instance = new Chat_NCore();
        } else if((plugin = manager.getPlugin("PermissionsEx")) != null && plugin.isEnabled()) {
            instance = new Chat_PermissionsEx();
        } else if((plugin = manager.getPlugin("PowerfulPerms")) != null && plugin.isEnabled()) {
            instance = new Chat_PowerfulPerms();
        } else if((plugin = manager.getPlugin("Spark")) != null && plugin.isEnabled()) {
            instance = new Chat_Spark();
        } else if((plugin = manager.getPlugin("TeikoCore")) != null && plugin.isEnabled()) {
            instance = new Chat_TeikoCore();
        } else if((plugin = manager.getPlugin("Zoot")) != null && plugin.isEnabled()) {
            instance = new Chat_Zoot();
        } else if((plugin = manager.getPlugin("zPermissions")) != null && plugin.isEnabled()) {
            instance = new Chat_zPermissions();
        } else {
            instance = new ChatHandler();
        }
    }

    public String getRankName(Player player) {
        return "";
    }

    protected String getTag(Player player) {
        return "";
    }

    public String getPrefix(Player player) {
        return "";
    }

    public String getNameColor(Player player) {
        return "";
    }

    protected String getSuffix(Player player) {
        return "";
    }

    protected String getChatColor(Player player) {
        return "";
    }

    private String getChatMessage(Player player, PlayerFaction playerFaction, CommandSender recipient, String message) {
        String displayName = Color.translate(instance.getTag(player) + instance.getPrefix(player)
            + instance.getNameColor(player) +  player.getName() + instance.getSuffix(player));

        if(player.hasPermission("lazarus.chat.color")) {
            message = Color.translate(message);
        }

        String chatColor = Color.translate(instance.getChatColor(player));

        if(playerFaction == null) {
            return Config.CHAT_FORMAT.replace("<displayName>", displayName) + chatColor + message;
        }

        return Config.CHAT_FORMAT_WITH_FACTION.replace("<faction>", playerFaction
            .getName(recipient)).replace("<displayName>", displayName) + chatColor + message;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if(!Config.CHAT_FORMAT_ENABLED) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);

        Bukkit.getConsoleSender().sendMessage(this.getChatMessage(player,
            faction, Bukkit.getConsoleSender(), event.getMessage()));

        event.getRecipients().forEach(recipient -> {
            Userdata userdata = Lazarus.getInstance().getUserdataManager().getUserdata(recipient);

            if((player != recipient && !player.hasPermission("lazarus.staff") && !userdata
            .getSettings().isPublicChat()) || userdata.isIgnoring(player)) return;

            recipient.sendMessage(this.getChatMessage(player, faction, recipient, event.getMessage()));
        });
    }
}
