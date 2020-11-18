package me.qiooip.lazarus.handlers.rank;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.chat.ChatHandler;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.userdata.Userdata;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ReclaimHandler extends Handler {

    private final List<ReclaimData> reclaims;

    public ReclaimHandler() {
        this.reclaims = new ArrayList<>();

        this.loadReclaimData();
    }

    @Override
    public void disable() {
        this.reclaims.clear();
    }

    private void loadReclaimData() {
        ConfigurationSection section = Lazarus.getInstance().getConfig()
            .getSection("RECLAIM_COMMAND");

        section.getKeys(false).forEach(key -> {
            ReclaimData reclaim = new ReclaimData();

            reclaim.setRankName(Color.translate(section.getString(key + ".RANK_NAME", "")));
            reclaim.setPermission(section.getString(key + ".PERMISSION"));
            reclaim.setCommands(section.getStringList(key + ".COMMANDS"));

            this.reclaims.add(reclaim);
        });
    }

    private boolean hasReclaimPermission(Player player) {
        return this.reclaims.stream().anyMatch(reclaim -> player.hasPermission(reclaim.getPermission()));
    }

    private ReclaimData getReclaim(Player player) {
        return this.reclaims.stream().filter(reclaim -> player.hasPermission(reclaim.getPermission()))
        .findFirst().orElse(null);
    }

    public void performCommand(Player player) {
        if(!this.hasReclaimPermission(player)) {
            player.sendMessage(Language.PREFIX + Language.RECLAIM_NO_PERMISSION);
            return;
        }

        Userdata data = Lazarus.getInstance().getUserdataManager().getUserdata(player);

        if(data.isReclaimUsed()) {
            player.sendMessage(Language.PREFIX + Language.RECLAIM_ALREADY_USED);
            return;
        }

        data.setReclaimUsed(true);

        ReclaimData reclaim = this.getReclaim(player);

        reclaim.getCommands().forEach(command -> Bukkit.dispatchCommand(Bukkit
        .getConsoleSender(), command.replace("<player>", player.getName())));

        String rankPrefix = Color.translate(ChatHandler.getInstance().getPrefix(player));

        Messages.sendMessage(Language.RECLAIM_BROADCAST_MESSAGE
            .replace("<rankName>", reclaim.getRankName())
            .replace("<rankPrefix>", rankPrefix)
            .replace("<player>", player.getName()));
    }

    @Getter
    @Setter
    private static class ReclaimData {

        private String rankName;
        private String permission;
        private List<String> commands;
    }
}
