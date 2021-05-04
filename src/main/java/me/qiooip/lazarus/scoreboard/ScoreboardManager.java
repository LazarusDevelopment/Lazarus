package me.qiooip.lazarus.scoreboard;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionPlayer;
import me.qiooip.lazarus.factions.event.FactionDisbandEvent;
import me.qiooip.lazarus.factions.event.FactionDtrChangeEvent;
import me.qiooip.lazarus.factions.event.FactionRelationChangeEvent;
import me.qiooip.lazarus.factions.event.FactionRenameEvent;
import me.qiooip.lazarus.factions.event.PlayerJoinFactionEvent;
import me.qiooip.lazarus.factions.event.PlayerLeaveFactionEvent;
import me.qiooip.lazarus.factions.event.PlayerLeaveFactionEvent.LeaveReason;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.scoreboard.task.ScoreboardUpdater;
import me.qiooip.lazarus.scoreboard.task.ScoreboardUpdaterImpl;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreboardManager implements Listener, ManagerEnabler {

    @Getter private final Map<UUID, PlayerScoreboard> scoreboards;

    private final Set<UUID> staffSb;
    private ScoreboardUpdater updater;

    public ScoreboardManager() {
        this.scoreboards = new ConcurrentHashMap<>();
        this.staffSb = new HashSet<>();

        Bukkit.getOnlinePlayers().forEach(this::loadScoreboard);
        this.updater = new ScoreboardUpdaterImpl(Lazarus.getInstance(), this);

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.updater.cancel();

        this.scoreboards.values().forEach(PlayerScoreboard::unregister);
        this.scoreboards.clear();
    }

    public void setUpdater(ScoreboardUpdater scoreboardUpdater) {
        this.updater.cancel();
        this.updater = scoreboardUpdater;
    }

    public void loadScoreboard(Player player) {
        PlayerScoreboard playerScoreboard = NmsUtils.getInstance().getNewPlayerScoreboard(player);
        playerScoreboard.updateTabRelations(Bukkit.getOnlinePlayers());

        for(PlayerScoreboard other : this.scoreboards.values()) {
            other.updateRelation(player);
        }

        this.scoreboards.put(player.getUniqueId(), playerScoreboard);
    }

    public void removeScoreboard(Player player) {
        PlayerScoreboard scoreboard = this.scoreboards.remove(player.getUniqueId());

        if(scoreboard != null) {
            scoreboard.unregister();
        }
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

    public void updateTabRelations(Player player, boolean lunarOnly) {
        Tasks.async(() -> {
            for(PlayerScoreboard scoreboard : this.scoreboards.values()) {
                scoreboard.updateRelation(player, lunarOnly);
            }
        });
    }

    public void updateTabRelations(Collection<? extends Player> players, boolean lunarOnly) {
        Tasks.async(() -> {
            for(PlayerScoreboard scoreboard : this.scoreboards.values()) {
                scoreboard.updateTabRelations(players, lunarOnly);
            }
        });
    }

    public void updateTabRelations(Collection<Player> updateFor, Collection<? extends Player> toUpdate, boolean lunarOnly) {
        Tasks.async(() -> {
            for(Player player : updateFor) {
                PlayerScoreboard scoreboard = this.getPlayerScoreboard(player);

                if(scoreboard != null) {
                    scoreboard.updateTabRelations(toUpdate, lunarOnly);
                }
            }
        });
    }

    private void fixInvisibilityForPlayer(PotionEffectEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        if(ServerUtils.getEffect(event).getType().getId() != 14) return;

        Player player = (Player) event.getEntity();
        this.updateTabRelations(player, false);
    }

    private void updateFactionPlayer(FactionPlayer fplayer, PlayerFaction faction) {
        Player player = fplayer.getPlayer();
        if(player == null) return;

        Tasks.async(() -> {
            Collection<Player> players = faction.getOnlinePlayers();

            this.getPlayerScoreboard(player).updateTabRelations(players);
            this.updateTabRelations(players, Collections.singletonList(player), false);
            this.updateTabRelations(player, true);
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
    public void onFactionRename(FactionRenameEvent event) {
        if(!(event.getFaction() instanceof PlayerFaction)) return;

        PlayerFaction faction = (PlayerFaction) event.getFaction();
        this.updateTabRelations(faction.getOnlinePlayers(), true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionAllyCreate(FactionRelationChangeEvent event) {
        PlayerFaction faction = event.getFaction();
        PlayerFaction targetFaction = event.getTargetFaction();

        List<Player> players = faction.getOnlinePlayers();
        players.addAll(targetFaction.getOnlinePlayers());

        this.updateTabRelations(players, players, false);
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionDtrChange(FactionDtrChangeEvent event) {
        this.updateTabRelations(event.getFaction().getOnlinePlayers(), true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionDisband(FactionDisbandEvent event) {
        if(!(event.getFaction() instanceof PlayerFaction)) return;

        PlayerFaction faction = (PlayerFaction) event.getFaction();
        List<Player> players = faction.getOnlinePlayers();

        for(PlayerFaction ally : faction.getAlliesAsFactions()) {
            players.addAll(ally.getOnlinePlayers());
        }

        this.updateTabRelations(players, players, false);
        this.updateTabRelations(players, true);
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
