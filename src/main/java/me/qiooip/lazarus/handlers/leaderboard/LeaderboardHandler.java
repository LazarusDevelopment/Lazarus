package me.qiooip.lazarus.handlers.leaderboard;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.event.FactionDataChangeEvent;
import me.qiooip.lazarus.factions.event.FactionDataType;
import me.qiooip.lazarus.factions.event.FactionDisbandEvent;
import me.qiooip.lazarus.factions.event.FactionRenameEvent;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.handlers.leaderboard.cache.FactionCacheHolder;
import me.qiooip.lazarus.handlers.leaderboard.cache.PlayerCacheHolder;
import me.qiooip.lazarus.handlers.leaderboard.entry.UuidCacheEntry;
import me.qiooip.lazarus.handlers.leaderboard.event.LeaderboardUpdateEvent;
import me.qiooip.lazarus.handlers.leaderboard.type.FactionLeaderboardType;
import me.qiooip.lazarus.handlers.leaderboard.type.LeaderboardType;
import me.qiooip.lazarus.handlers.leaderboard.type.PlayerLeaderboardType;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.userdata.Userdata;
import me.qiooip.lazarus.userdata.event.PlayerUsernameChangeEvent;
import me.qiooip.lazarus.userdata.event.UserdataValueChangeEvent;
import me.qiooip.lazarus.userdata.event.UserdataValueType;
import me.qiooip.lazarus.utils.FileUtils;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.HashSet;
import java.util.NavigableSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

@Getter
public class LeaderboardHandler extends Handler implements Listener {

    private final PlayerCacheHolder playerCacheHolder;
    private final FactionCacheHolder factionCacheHolder;

    public LeaderboardHandler() {
        this.playerCacheHolder = this.loadLeaderboardCache(
            this.getPlayersFile(), PlayerCacheHolder.class, new PlayerCacheHolder());

        this.factionCacheHolder = this.loadLeaderboardCache(
            this.getFactionsFile(), FactionCacheHolder.class, new FactionCacheHolder());

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    @Override
    public void disable() {
        this.saveCache();
    }

    private File getPlayersFile() {
        return FileUtils.getOrCreateFile(Config.LEADERBOARDS_DIR, "players.json");
    }

    private File getFactionsFile() {
        return FileUtils.getOrCreateFile(Config.LEADERBOARDS_DIR, "factions.json");
    }

    public <T> T loadLeaderboardCache(File file, Class<T> type, T defaultValue) {
        String content = FileUtils.readWholeFile(file);
        return content == null ? defaultValue : Lazarus.getInstance().getGson().fromJson(content, type);
    }

    public void saveCache() {
        Gson gson = Lazarus.getInstance().getGson();

        if(this.playerCacheHolder != null) {
            FileUtils.writeString(this.getPlayersFile(),
                gson.toJson(this.playerCacheHolder, PlayerCacheHolder.class));
        }

        if(this.factionCacheHolder != null) {
            FileUtils.writeString(this.getFactionsFile(),
                gson.toJson(this.factionCacheHolder, FactionCacheHolder.class));
        }
    }

    private void updateCacheValue(LeaderboardType type, UUID key, String name, int newValue) {
        NavigableSet<UuidCacheEntry<Integer>> leaderboard = type.getLeaderboard();

        boolean updated = leaderboard.removeIf(entry -> entry.getKey().equals(key));
        leaderboard.add(new UuidCacheEntry<>(key, name, newValue));

        if(leaderboard.size() > 10) {
            leaderboard.pollLast();
            updated = true;
        }

        if(updated) {
            new LeaderboardUpdateEvent(type);
        }
    }

    private boolean removeFromLeaderboard(LeaderboardType type, UUID key) {
        return type.getLeaderboard().removeIf(entry -> entry.getKey().equals(key));
    }

    private void handlePlayerUsernameChange(Userdata userdata, String newName) {
        for(PlayerLeaderboardType type : PlayerLeaderboardType.values()) {
            UserdataValueType valueType = PlayerLeaderboardType.getUserdataValueTypeFrom(type);
            int newValue = valueType.getNewValue(userdata).intValue();

            this.updateCacheValue(type, userdata.getUuid(), newName, newValue);
        }
    }

    private void handleFactionNameChange(PlayerFaction faction, String newName) {
        for(FactionLeaderboardType type : FactionLeaderboardType.values()) {
            FactionDataType valueType = FactionLeaderboardType.getFactionDataTypeFrom(type);
            int newValue = valueType.getNewValue(faction).intValue();

            this.updateCacheValue(type, faction.getId(), newName, newValue);
        }
    }

    public void removeFactionOnDisband(PlayerFaction disbanded) {
        Set<FactionLeaderboardType> containingLeaderboards = new HashSet<>();

        for(FactionLeaderboardType type : FactionLeaderboardType.values()) {
            boolean removed = this.removeFromLeaderboard(type, disbanded.getId());

            if(removed) {
                containingLeaderboards.add(type);
            }
        }

        if(!containingLeaderboards.isEmpty()) {
            this.reCacheFactionsOnDisband(containingLeaderboards);
        }
    }

    private void reCacheFactionsOnDisband(Set<FactionLeaderboardType> leaderboardTypes) {
        Table<LeaderboardType, PlayerFaction, Integer> scores = HashBasedTable.create();

        for(Faction faction : FactionsManager.getInstance().getFactions().values()) {
            if(!(faction instanceof PlayerFaction)) continue;

            PlayerFaction playerFaction = (PlayerFaction) faction;

            for(FactionLeaderboardType type : leaderboardTypes) {
                FactionDataType valueType = FactionLeaderboardType.getFactionDataTypeFrom(type);
                scores.put(type, playerFaction, valueType.getNewValue(playerFaction).intValue());
            }
        }

        for(FactionLeaderboardType type : leaderboardTypes) {
            Set<UuidCacheEntry<Integer>> newSet = new ConcurrentSkipListSet<>();

            scores.row(type).forEach((faction, value)
                -> newSet.add(new UuidCacheEntry<>(faction, value)));

            NavigableSet<UuidCacheEntry<Integer>> leaderboard = type.getLeaderboard();
            leaderboard.clear();

            int i = 0;

            for(UuidCacheEntry<Integer> entry : newSet) {
                leaderboard.add(entry.copy());

                if(++i >= 10) {
                    break;
                }
            }

            new LeaderboardUpdateEvent(type);
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

        this.updateCacheValue(type, userdata.getUuid(), userdata.getName(), newValue);
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionRename(FactionRenameEvent event) {
        if(!(event.getFaction() instanceof PlayerFaction)) return;

        PlayerFaction playerFaction = (PlayerFaction) event.getFaction();
        this.handleFactionNameChange(playerFaction, event.getNewName());
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionDisband(FactionDisbandEvent event) {
        Faction faction = event.getFaction();

        if(faction instanceof PlayerFaction) {
            Tasks.async(() -> this.removeFactionOnDisband((PlayerFaction) faction));
        }
    }

    @EventHandler
    public void onFactionDataChange(FactionDataChangeEvent event) {
        PlayerFaction faction = event.getFaction();
        int newValue = event.getNewValue().intValue();
        LeaderboardType type = FactionLeaderboardType.getFromFactionDataType(event.getType());

        this.updateCacheValue(type, faction.getId(), faction.getName(), newValue);
    }
}
