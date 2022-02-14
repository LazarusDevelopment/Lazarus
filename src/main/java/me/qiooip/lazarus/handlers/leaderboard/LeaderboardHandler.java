package me.qiooip.lazarus.handlers.leaderboard;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.leaderboard.entry.UuidCacheEntry;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.userdata.event.UserdataValueChangeEvent;
import me.qiooip.lazarus.userdata.event.UserdataValueType;
import me.qiooip.lazarus.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.NavigableSet;

@Getter
public class LeaderboardHandler extends Handler implements Listener {

    private final PlayerCacheHolder playerCacheHolder;

    public LeaderboardHandler() {
        this.playerCacheHolder = this.loadCache();
        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    @Override
    public void disable() {
        this.saveCache();
    }

    private File getPlayerLeaderboardFile() {
        return FileUtils.getOrCreateFile(Config.LEADERBOARDS_DIR, "players.json");
    }

    public PlayerCacheHolder loadCache() {
        String content = FileUtils.readWholeFile(this.getPlayerLeaderboardFile());

        return content == null ? new PlayerCacheHolder()
            : Lazarus.getInstance().getGson().fromJson(content, PlayerCacheHolder.class);
    }

    public void saveCache() {
        if(this.playerCacheHolder == null) return;

        FileUtils.writeString(this.getPlayerLeaderboardFile(), Lazarus.getInstance().getGson()
            .toJson(this.playerCacheHolder, PlayerCacheHolder.class));
    }

    public NavigableSet<UuidCacheEntry<Integer>> getTopKills() {
        return this.playerCacheHolder.getTopKills();
    }

    public NavigableSet<UuidCacheEntry<Integer>> getTopDeaths() {
        return this.playerCacheHolder.getTopDeaths();
    }

    public NavigableSet<UuidCacheEntry<Integer>> getTopBalance() {
        return this.playerCacheHolder.getTopBalance();
    }

    public NavigableSet<UuidCacheEntry<Integer>> getHighestKillstreaks() {
        return this.playerCacheHolder.getHighestKillstreaks();
    }

    public void onKill(Player killer, int newKills) {
        this.updateCacheValue(this.getTopKills(), killer, newKills);
    }

    public void onDeath(Player victim, int newDeaths) {
        this.updateCacheValue(this.getTopDeaths(), victim, newDeaths);
    }

    public void onBalanceChange(Player player, int newBalance) {
        this.updateCacheValue(this.getTopBalance(), player, newBalance);
    }

    public void onHighestKillstreakChange(Player player, int newHighestKillstreak) {
        this.updateCacheValue(this.getHighestKillstreaks(), player, newHighestKillstreak);
    }

    private void updateCacheValue(NavigableSet<UuidCacheEntry<Integer>> cache, Player player, int newValue) {
        cache.removeIf(entry -> entry.getKey().equals(player.getUniqueId()));
        cache.add(new UuidCacheEntry<>(player, newValue));

        if(cache.size() > 10) {
            cache.pollLast();
        }
    }

    private void updateLeaderboardsCache(Player player, UserdataValueType type, Number newValue) {
        switch(type) {
            case KILLS: this.onKill(player, newValue.intValue()); break;
            case DEATHS: this.onDeath(player, newValue.intValue()); break;
            case BALANCE: this.onBalanceChange(player, newValue.intValue()); break;
            case HIGHEST_KILLSTREAK: this.onHighestKillstreakChange(player, newValue.intValue()); break;
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

    @EventHandler
    public void onUserdataValueChange(UserdataValueChangeEvent event) {
        UserdataValueType type = event.getType();
        Player player = event.getUserdata().getPlayer();
        Number newValue = event.getNewValue();

        this.updateLeaderboardsCache(player, type, newValue);
    }
}
