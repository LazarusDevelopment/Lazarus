package me.qiooip.lazarus.scoreboard;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionPlayer;
import me.qiooip.lazarus.factions.event.FactionDisbandEvent;
import me.qiooip.lazarus.factions.event.FactionRelationChangeEvent;
import me.qiooip.lazarus.factions.event.PlayerJoinFactionEvent;
import me.qiooip.lazarus.factions.event.PlayerLeaveFactionEvent;
import me.qiooip.lazarus.factions.event.PlayerLeaveFactionEvent.LeaveReason;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.scoreboard.task.ScoreboardUpdater;
import me.qiooip.lazarus.utils.ManagerEnabler;
import me.qiooip.lazarus.utils.ServerUtils;
import me.qiooip.lazarus.utils.Tasks;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionEffectAddEvent;
import org.bukkit.event.entity.PotionEffectEvent;
import org.bukkit.event.entity.PotionEffectExpireEvent;
import org.bukkit.event.entity.PotionEffectRemoveEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreboardManager implements Listener, ManagerEnabler {

    @Getter private final Map<UUID, PlayerScoreboard> scoreboards;
    private final Set<UUID> staffSb;

    private final ScoreboardUpdater updater;

    public ScoreboardManager() {
        this.scoreboards = new ConcurrentHashMap<>();
        this.staffSb = new HashSet<>();

        Bukkit.getOnlinePlayers().forEach(this::loadScoreboard);
        this.updater = new ScoreboardUpdater(Lazarus.getInstance(), this);

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.updater.cancel();

        this.scoreboards.values().forEach(PlayerScoreboard::unregister);
        this.scoreboards.clear();
    }

    public void loadScoreboard(Player player) {
        PlayerScoreboard playerScoreboard = NmsUtils.getInstance().getNewPlayerScoreboard(player);
        this.scoreboards.put(player.getUniqueId(), playerScoreboard);

        playerScoreboard.updateTabRelations(Bukkit.getOnlinePlayers());
        for(PlayerScoreboard other : this.scoreboards.values()) {
            other.updateRelation(player);
        }
    }

    public void removeScoreboard(Player player) {
        PlayerScoreboard scoreboard = this.scoreboards.remove(player.getUniqueId());
        if(scoreboard != null) scoreboard.unregister();
    }

    public void toggleStaffScoreboard(Player player) {
        if(this.staffSb.contains(player.getUniqueId())) {
            this.staffSb.remove(player.getUniqueId());
            player.sendMessage(Language.PREFIX + Language.STAFF_SCOREBOARD_ENABLED);
            return;
        }

        this.staffSb.add(player.getUniqueId());
        player.sendMessage(Language.PREFIX + Language.STAFF_SCOREBOARD_DISABLED);
    }

    public boolean isStaffSb(Player player) {
        return !this.staffSb.contains(player.getUniqueId());
    }

    public PlayerScoreboard getPlayerScoreboard(UUID uuid) {
        return this.scoreboards.get(uuid);
    }

    public PlayerScoreboard getPlayerScoreboard(Player player) {
        return this.getPlayerScoreboard(player.getUniqueId());
    }

    public void updateAllRelations(Player player) {
        for(PlayerScoreboard scoreboard : this.scoreboards.values()) {
            scoreboard.updateRelation(player);
        }
    }

    public void updateAllTabRelations() {
        for(PlayerScoreboard sb : this.scoreboards.values()) {
            sb.updateTabRelations(Bukkit.getOnlinePlayers());
        }
    }

    private void fixInvisibilityForPlayer(PotionEffectEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        if(ServerUtils.getEffect(event).getType().getId() != 14) return;

        Player player = (Player) event.getEntity();
        for(PlayerScoreboard scoreboard : this.scoreboards.values()) {
            scoreboard.updateRelation(player);
        }
    }

    private void updateFactionPlayer(FactionPlayer fplayer, PlayerFaction faction) {
        Player player = fplayer.getPlayer();
        if(player == null) return;

        Tasks.async(() -> {
            Collection<Player> players = faction.getOnlinePlayers();

            this.getPlayerScoreboard(player).updateTabRelations(players);

            for(Player online : players) {
                PlayerScoreboard playerScoreboard = this.getPlayerScoreboard(online);
                if(playerScoreboard != null) playerScoreboard.updateRelation(player);
            }
        });
    }

    @EventHandler
    public void onPotionEffectAdd(PotionEffectAddEvent event) {
        this.fixInvisibilityForPlayer(event);
    }

    @EventHandler
    public void onPotionEffectRemove(PotionEffectRemoveEvent event) {
        this.fixInvisibilityForPlayer(event);
    }

    @EventHandler
    public void onPotionEffectExpireInvisFix(PotionEffectExpireEvent event) {
        this.fixInvisibilityForPlayer(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoinFaction(PlayerJoinFactionEvent event) {
        this.updateFactionPlayer(event.getFactionPlayer(), event.getFaction());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLeaveFaction(PlayerLeaveFactionEvent event) {
        if(event.getReason() == LeaveReason.DISBAND) return;
        this.updateFactionPlayer(event.getFactionPlayer(), event.getFaction());
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionAllyCreate(FactionRelationChangeEvent event) {
        PlayerFaction faction = event.getFaction();
        PlayerFaction targetFaction = event.getTargetFaction();

        List<Player> players = faction.getOnlinePlayers();
        players.addAll(targetFaction.getOnlinePlayers());

        for(Player player : players) {
            this.getPlayerScoreboard(player).updateTabRelations(players);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionDisband(FactionDisbandEvent event) {
        if(!(event.getFaction() instanceof PlayerFaction)) return;

        PlayerFaction faction = (PlayerFaction) event.getFaction();

        List<Player> players = faction.getOnlinePlayers();
        for(PlayerFaction ally : faction.getAlliesAsFactions()) {
            players.addAll(ally.getOnlinePlayers());
        }

        for(Player player : players) {
            this.getPlayerScoreboard(player).updateTabRelations(players);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        NmsUtils.getInstance().getBukkitExecutor().execute(() -> this.loadScoreboard(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Tasks.async(() -> this.removeScoreboard(event.getPlayer()));
    }
}
