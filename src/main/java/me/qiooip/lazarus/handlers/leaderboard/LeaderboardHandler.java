package me.qiooip.lazarus.handlers.leaderboard;

import com.google.gson.Gson;
import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.event.FactionDataChangeEvent;
import me.qiooip.lazarus.factions.event.FactionDataType;
import me.qiooip.lazarus.factions.event.FactionRenameEvent;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.handlers.leaderboard.cache.FactionCacheHolder;
import me.qiooip.lazarus.handlers.leaderboard.cache.PlayerCacheHolder;
import me.qiooip.lazarus.handlers.leaderboard.entry.UuidCacheEntry;
import me.qiooip.lazarus.handlers.leaderboard.type.FactionLeaderboardType;
import me.qiooip.lazarus.handlers.leaderboard.type.LeaderboardType;
import me.qiooip.lazarus.handlers.leaderboard.type.PlayerLeaderboardType;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.userdata.Userdata;
import me.qiooip.lazarus.userdata.event.PlayerUsernameChangeEvent;
import me.qiooip.lazarus.userdata.event.UserdataValueChangeEvent;
import me.qiooip.lazarus.userdata.event.UserdataValueType;
import me.qiooip.lazarus.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.NavigableSet;
import java.util.UUID;

@Getter
public class LeaderboardHandler extends Handler implements Listener {

    private final PlayerCacheHolder playerCacheHolder;
    private final FactionCacheHolder factionCacheHolder;

    public LeaderboardHandler() {
        this.playerCacheHolder = this.loadLeaderboardCache(
            this.getPlayerLeaderboardFile(), PlayerCacheHolder.class, new PlayerCacheHolder());

        this.factionCacheHolder = this.loadLeaderboardCache(
            this.getFactionLeaderboardFile(), FactionCacheHolder.class, new FactionCacheHolder());

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    @Override
    public void disable() {
        this.saveCache();
    }

    private File getPlayerLeaderboardFile() {
        return FileUtils.getOrCreateFile(Config.LEADERBOARDS_DIR, "players.json");
    }

    private File getFactionLeaderboardFile() {
        return FileUtils.getOrCreateFile(Config.LEADERBOARDS_DIR, "factions.json");
    }

    public <T> T loadLeaderboardCache(File file, Class<T> type, T defaultValue) {
        String content = FileUtils.readWholeFile(file);
        return content == null ? defaultValue : Lazarus.getInstance().getGson().fromJson(content, type);
    }

    public void saveCache() {
        Gson gson = Lazarus.getInstance().getGson();

        if(this.playerCacheHolder != null) {
            FileUtils.writeString(this.getPlayerLeaderboardFile(),
                gson.toJson(this.playerCacheHolder, PlayerCacheHolder.class));
        }

        if(this.factionCacheHolder != null) {
            FileUtils.writeString(this.getFactionLeaderboardFile(),
                gson.toJson(this.factionCacheHolder, FactionCacheHolder.class));
        }
    }

    private void updateCacheValue(NavigableSet<UuidCacheEntry<Integer>> cache, UUID key, String name, int newValue) {
        cache.removeIf(entry -> entry.getKey().equals(key));
        cache.add(new UuidCacheEntry<>(key, name, newValue));

        if(cache.size() > 10) {
            cache.pollLast();
        }
    }

    private void handlePlayerUsernameChange(Userdata userdata, String newName) {
        for(PlayerLeaderboardType type : PlayerLeaderboardType.values()) {
            UserdataValueType valueType = PlayerLeaderboardType.getUserdataValueTypeFrom(type);
            int newValue = valueType.getNewValue(userdata).intValue();

            this.updateCacheValue(type.getLeaderboard(), userdata.getUuid(), newName, newValue);
        }
    }

    private void handleFactionNameChange(PlayerFaction faction, String newName) {
        for(FactionLeaderboardType type : FactionLeaderboardType.values()) {
            FactionDataType valueType = FactionLeaderboardType.getFactionDataTypeFrom(type);
            int newValue = valueType.getNewValue(faction).intValue();

            this.updateCacheValue(type.getLeaderboard(), faction.getId(), newName, newValue);
        }
    }

    public void sendLeaderboardMessage(CommandSender sender, LeaderboardType type) {
        NavigableSet<UuidCacheEntry<Integer>> leaderboard = type.getLeaderboard();

        if(leaderboard.isEmpty()) {
            sender.sendMessage(Language.PREFIX + Language.LEADERBOARDS_NO_LEADERBOARDS);
            return;
        }

        String title = type.getTitle();
        String lineFormat = type.getLineFormat();

        int index = 1;

        sender.sendMessage(Language.LEADERBOARDS_COMMAND_HEADER);
        sender.sendMessage(title);

        for(UuidCacheEntry<Integer> entry : leaderboard) {
            sender.sendMessage(lineFormat
                .replace("<number>", String.valueOf(index))
                .replace("<player>", entry.getName())
                .replace("<value>", String.valueOf(entry.getValue())));

            index++;
        }

        sender.sendMessage(Language.LEADERBOARDS_COMMAND_FOOTER);
    }

    public void deleteAllLeaderboards() {
        this.playerCacheHolder.clear();
        this.factionCacheHolder.clear();

        Lazarus.getInstance().log("- &cDeleted all leaderboards.");
    }

    @EventHandler
    public void onPlayerUsernameChange(PlayerUsernameChangeEvent event) {
        this.handlePlayerUsernameChange(event.getUserdata(), event.getNewName());
    }

    @EventHandler
    public void onUserdataValueChange(UserdataValueChangeEvent event) {
        Userdata userdata = event.getUserdata();
        int newValue = event.getNewValue().intValue();
        LeaderboardType type = PlayerLeaderboardType.getFromUserdataValueType(event.getType());

        this.updateCacheValue(type.getLeaderboard(), userdata.getUuid(), userdata.getName(), newValue);
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionRename(FactionRenameEvent event) {
        if(!(event.getFaction() instanceof PlayerFaction)) return;

        PlayerFaction playerFaction = (PlayerFaction) event.getFaction();
        this.handleFactionNameChange(playerFaction, event.getNewName());
    }

    @EventHandler
    public void onFactionDataChange(FactionDataChangeEvent event) {
        PlayerFaction faction = event.getFaction();
        int newValue = event.getNewValue().intValue();
        LeaderboardType type = FactionLeaderboardType.getFromFactionDataType(event.getType());

        this.updateCacheValue(type.getLeaderboard(), faction.getId(), faction.getName(), newValue);
    }
}
