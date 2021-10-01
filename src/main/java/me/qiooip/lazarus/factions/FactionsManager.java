package me.qiooip.lazarus.factions;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.deathban.event.PlayerDeathbanEvent;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.enums.Relation;
import me.qiooip.lazarus.factions.enums.Role;
import me.qiooip.lazarus.factions.event.FactionChatEvent;
import me.qiooip.lazarus.factions.event.FactionClaimChangeEvent.ClaimChangeReason;
import me.qiooip.lazarus.factions.event.FactionCreateEvent;
import me.qiooip.lazarus.factions.event.FactionCreateEvent.FactionType;
import me.qiooip.lazarus.factions.event.FactionDisbandEvent;
import me.qiooip.lazarus.factions.event.FactionDtrChangeEvent;
import me.qiooip.lazarus.factions.event.FactionRallyEvent;
import me.qiooip.lazarus.factions.event.FactionRallyEvent.RallyEventType;
import me.qiooip.lazarus.factions.event.FactionRelationChangeEvent;
import me.qiooip.lazarus.factions.event.FactionRenameEvent;
import me.qiooip.lazarus.factions.event.PlayerJoinFactionEvent;
import me.qiooip.lazarus.factions.event.PlayerLeaveFactionEvent;
import me.qiooip.lazarus.factions.event.PlayerLeaveFactionEvent.LeaveReason;
import me.qiooip.lazarus.factions.listeners.BlockEventListener;
import me.qiooip.lazarus.factions.listeners.EntityEventListener;
import me.qiooip.lazarus.factions.listeners.PlayerEventListener;
import me.qiooip.lazarus.factions.type.ConquestFaction;
import me.qiooip.lazarus.factions.type.DtcFaction;
import me.qiooip.lazarus.factions.type.KothFaction;
import me.qiooip.lazarus.factions.type.MountainFaction;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.factions.type.RoadFaction;
import me.qiooip.lazarus.factions.type.SpawnFaction;
import me.qiooip.lazarus.factions.type.SystemFaction;
import me.qiooip.lazarus.factions.type.SystemType;
import me.qiooip.lazarus.games.koth.KothData;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.cooldown.CooldownTimer;
import me.qiooip.lazarus.timer.cooldown.DtrRegenTimer;
import me.qiooip.lazarus.timer.cooldown.FactionRallyTimer;
import me.qiooip.lazarus.timer.scoreboard.HomeTimer;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.FileUtils;
import me.qiooip.lazarus.utils.GsonUtils;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class FactionsManager implements Listener {

    @Getter private static FactionsManager instance;

    private final File factionsFile;
    private final File playersFile;

    protected Map<UUID, Faction> factions;
    protected final Map<String, UUID> factionNames;

    protected Map<UUID, FactionPlayer> players;

    private final Set<UUID> chatSpy;
    private final Map<UUID, Location> stuckInit;

    private BukkitTask saveTask;

    public FactionsManager() {
        instance = this;

        this.factionsFile = FileUtils.getOrCreateFile(Config.FACTIONS_DIR, "factions.json");
        this.playersFile = FileUtils.getOrCreateFile(Config.FACTIONS_DIR, "players.json");

        this.factionNames = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        this.loadFactions();
        this.loadPlayers();

        this.chatSpy = new HashSet<>();
        this.stuckInit = new HashMap<>();

        this.setupInitialFactions();
        this.startSaveTask();

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
        Bukkit.getPluginManager().registerEvents(new BlockEventListener(), Lazarus.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityEventListener(), Lazarus.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerEventListener(), Lazarus.getInstance());
    }

    public void disable() {
        this.saveFactions(true, true);
        this.savePlayers(true);

        this.factionNames.clear();
        this.factions.clear();
        this.players.clear();

        this.chatSpy.clear();
        this.stuckInit.clear();

        this.saveTask.cancel();
    }

    protected void loadFactions() {
        String content = FileUtils.readWholeFile(this.factionsFile);

        if(content == null) {
            this.factions = new HashMap<>();
            return;
        }

        this.factions = Lazarus.getInstance().getGson().fromJson(content, GsonUtils.FACTION_TYPE);

        for(Faction faction : this.factions.values()) {
            this.factionNames.put(faction.getName(), faction.getId());
            this.additionalFactionSetup(faction);
        }

        Lazarus.getInstance().log("- &7Loaded &a" + this.factions.size() + " &7factions.");
    }

    public void saveFactions(boolean log, boolean onDisable) {
        if(this.factions == null) return;

        if(onDisable) {
            this.factions.values().forEach(this::stripFactionColor);
        }

        FileUtils.writeString(this.factionsFile, Lazarus.getInstance().getGson()
            .toJson(this.factions, GsonUtils.FACTION_TYPE));

        if(log) {
            Lazarus.getInstance().log("- &7Saved &a" + this.factions.size() + " &7factions.");
        }
    }

    protected void loadPlayers() {
        String content = FileUtils.readWholeFile(this.playersFile);

        if(content == null) {
            this.players = new HashMap<>();
            return;
        }

        this.players = Lazarus.getInstance().getGson().fromJson(content, GsonUtils.PLAYER_TYPE);

        this.players.values().forEach(fplayer -> {
            PlayerFaction faction = fplayer.getFaction();

            if(faction != null) {
                faction.addMember(fplayer);
                if(fplayer.isOnline()) faction.incrementOnlineMembers();
            }
        });

        Lazarus.getInstance().log("- &7Loaded &a" + this.players.size() + " &7players.");
    }

    public void savePlayers(boolean log) {
        if(this.players == null) return;

        FileUtils.writeString(this.playersFile, Lazarus.getInstance().getGson()
            .toJson(this.players, GsonUtils.PLAYER_TYPE));

        if(log) {
            Lazarus.getInstance().log("- &7Saved &a" + this.players.size() + " &7players.");
        }
    }

    protected void additionalFactionSetup(Faction faction) {
        if(faction instanceof RoadFaction) {
            ((RoadFaction) faction).setupDisplayName();
        }

        if(faction instanceof SystemFaction) {
            SystemFaction systemFaction = (SystemFaction) faction;
            systemFaction.setColor(Color.translate(systemFaction.getColor()));
        }

        if(faction instanceof SpawnFaction) {
            SpawnFaction spawnFaction = (SpawnFaction) faction;

            spawnFaction.setSafezone(true);
            spawnFaction.setDeathban(false);
        }
    }

    protected void stripFactionColor(Faction faction) {
        if(faction instanceof SystemFaction) {
            SystemFaction systemFaction = (SystemFaction) faction;
            systemFaction.setColor(systemFaction.getColor().replace('§', '&'));
        }
    }

    private void setupInitialFactions() {
        this.createDefaultFaction(new SpawnFaction());

        this.createDefaultFaction(new RoadFaction("NorthRoad"));
        this.createDefaultFaction(new RoadFaction("EastRoad"));
        this.createDefaultFaction(new RoadFaction("SouthRoad"));
        this.createDefaultFaction(new RoadFaction("WestRoad"));

        this.createDefaultFaction(new ConquestFaction());
        this.createDefaultFaction(new DtcFaction());

        this.createDefaultFaction(new MountainFaction("Glowstone"));
        this.createDefaultFaction(new MountainFaction("Ore"));
    }

    public boolean isNameTaken(String name) {
        return this.factionNames.get(name) != null;
    }

    private FactionPlayer getPlayer(Player player) {
        return this.getPlayer(player.getUniqueId());
    }

    private FactionPlayer getPlayer(UUID uuid) {
        return this.players.get(uuid);
    }

    public Faction getAnyFaction(String argument) {
        Faction faction = this.getFactionByName(argument);
        return faction != null ? faction : this.searchForFaction(argument);
    }

    public PlayerFaction searchForFaction(String argument) {
        PlayerFaction faction = this.getPlayerFactionByName(argument);
        return faction != null ? faction : this.getPlayerFaction(argument);
    }

    public Faction getFactionByUuid(UUID uuid) {
        return this.factions.get(uuid);
    }

    public Faction getFactionByName(String name) {
        return this.getFactionByUuid(this.factionNames.get(name));
    }

    public PlayerFaction getPlayerFactionByUuid(UUID uuid) {
        Faction faction = this.getFactionByUuid(uuid);
        return faction instanceof PlayerFaction ? (PlayerFaction) faction : null;
    }

    public PlayerFaction getPlayerFactionByName(String name) {
        Faction faction = this.getFactionByName(name);
        return faction instanceof PlayerFaction ? (PlayerFaction) faction : null;
    }

    public PlayerFaction getPlayerFaction(UUID uuid) {
        FactionPlayer fplayer = this.getPlayer(uuid);
        return fplayer == null ? null : fplayer.getFaction();
    }

    public PlayerFaction getPlayerFaction(Player player) {
        return this.getPlayerFaction(player.getUniqueId());
    }

    public PlayerFaction getPlayerFaction(String playerName) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);

        return offlinePlayer.hasPlayedBefore() || offlinePlayer.isOnline()
            ? this.getPlayerFaction(offlinePlayer.getUniqueId()) : null;
    }

    public void setAllRaidable(boolean raidable) {
        double minDtr = Config.FACTION_MIN_DTR >= 0 ? -6.5 : Config.FACTION_MIN_DTR;

        this.factions.values().stream().filter(faction -> faction instanceof PlayerFaction)
            .map(faction -> (PlayerFaction) faction)
            .forEach(faction -> faction.setDtr(raidable ? minDtr : faction.getMaxDtr()));
    }

    public void checkHomeTeleports(PlayerFaction faction, String message) {
        HomeTimer timer = TimerManager.getInstance().getHomeTimer();

        faction.getMembers().values().stream().filter(member -> timer.isActive(member.getUuid())).forEach(member -> {
            timer.cancel(member.getUuid());
            member.sendMessage(Language.FACTION_PREFIX + message);
        });
    }

    public void toggleChatSpy(Player player) {
        if(this.chatSpy.contains(player.getUniqueId())) {
            this.chatSpy.remove(player.getUniqueId());
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CHATSPY_DISABLED);
        } else {
            this.chatSpy.add(player.getUniqueId());
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CHATSPY_ENABLED);
        }
    }

    public void removeStuckInitialLocation(Player player) {
        this.stuckInit.remove(player.getUniqueId());
    }

    public void setStuckInitialLocation(Player player, Location location) {
        this.stuckInit.put(player.getUniqueId(), location);
    }

    public boolean hasExceededStuckLimit(Player player) {
        return this.stuckInit.get(player.getUniqueId()).distance(player.getLocation()) > Config.FACTION_STUCK_ALLOWED_MOVEMENT_RADIUS;
    }

    public boolean joinFaction(Player player, PlayerFaction faction) {
        FactionPlayer fplayer = new FactionPlayer(player.getUniqueId(), faction);
        PlayerJoinFactionEvent event = new PlayerJoinFactionEvent(fplayer, faction);
        
        if(event.isCancelled()) {
            this.players.remove(player.getUniqueId());
            return false;
        }

        faction.addMember(fplayer);
        faction.incrementOnlineMembers();
        faction.getPlayerInvitations().remove(player.getName());

        this.players.put(fplayer.getUuid(), fplayer);
        return true;
    }

    public boolean leaveFaction(Player player, PlayerFaction faction) {
        FactionPlayer fplayer = this.getPlayer(player);

        PlayerLeaveFactionEvent event = new PlayerLeaveFactionEvent(fplayer, faction, LeaveReason.LEAVE);
        if(event.isCancelled()) return false;

        faction.removeMember(fplayer);
        faction.decrementOnlineMembers();

        this.players.remove(fplayer.getUuid());
        return true;
    }

    public boolean kickPlayer(OfflinePlayer player, PlayerFaction faction) {
        FactionPlayer fplayer = this.getPlayer(player.getUniqueId());

        PlayerLeaveFactionEvent event = new PlayerLeaveFactionEvent(fplayer, faction, LeaveReason.KICK);
        if(event.isCancelled()) return false;

        faction.removeMember(fplayer);
        if(player.isOnline()) faction.decrementOnlineMembers();

        this.players.remove(fplayer.getUuid());
        return true;
    }

    public boolean acceptAllyRequest(PlayerFaction faction, PlayerFaction targetFaction) {
        FactionRelationChangeEvent event = new FactionRelationChangeEvent(faction, targetFaction, Relation.ENEMY, Relation.ALLY);
        if(event.isCancelled()) return false;

        faction.getAllies().add(targetFaction.getId());
        faction.getAllyInvitations().remove(targetFaction.getId());

        targetFaction.getAllies().add(faction.getId());
        targetFaction.getAllyInvitations().remove(faction.getId());
        return true;
    }

    public boolean removeAllyRelation(PlayerFaction faction, PlayerFaction targetFaction) {
        FactionRelationChangeEvent event = new FactionRelationChangeEvent(faction, targetFaction, Relation.ALLY, Relation.ENEMY);
        if(event.isCancelled()) return false;

        faction.getAllies().remove(targetFaction.getId());
        targetFaction.getAllies().remove(faction.getId());
        return true;
    }

    public boolean createPlayerFaction(String name, Player player) {
        FactionCreateEvent event = new FactionCreateEvent(name, player, FactionType.PLAYER_FACTION);
        if(event.isCancelled()) return false;

        PlayerFaction faction = new PlayerFaction(name);

        FactionPlayer fplayer = new FactionPlayer(player.getUniqueId(), faction);
        fplayer.setRole(Role.LEADER);

        new PlayerJoinFactionEvent(fplayer, faction);

        faction.addMember(fplayer);
        faction.incrementOnlineMembers();

        this.factions.put(faction.getId(), faction);
        this.factionNames.put(faction.getName(), faction.getId());
        this.players.put(player.getUniqueId(), fplayer);
        return true;
    }

    public SystemFaction createSystemFaction(String name, SystemType type, CommandSender sender) {
        FactionCreateEvent event = new FactionCreateEvent(name, sender, FactionType.SYSTEM_FACTION);
        if(event.isCancelled()) return null;

        SystemFaction faction = type == SystemType.DEFAULT ? new SystemFaction(name) : new KothFaction(name);

        this.factions.put(faction.getId(), faction);
        this.factionNames.put(faction.getName(), faction.getId());
        return faction;
    }

    private void createDefaultFaction(Faction faction) {
        if(this.getFactionByName(faction.getName()) != null) return;

        this.factions.put(faction.getId(), faction);
        this.factionNames.put(faction.getName(), faction.getId());
    }

    public boolean disbandFaction(UUID uuid, CommandSender sender) {
        Faction toDisband = this.getFactionByUuid(uuid);
        if(toDisband == null) return true;

        return this.disbandFaction(toDisband, sender);
    }

    public boolean disbandFaction(Faction toDisband, CommandSender sender) {
        FactionDisbandEvent disbandEvent = new FactionDisbandEvent(toDisband, sender);
        if(disbandEvent.isCancelled()) return false;

        this.factions.remove(toDisband.getId());
        this.factionNames.remove(toDisband.getName());

        ClaimManager.getInstance().removeAllClaims(toDisband, ClaimChangeReason.DISBAND);

        if(!(toDisband instanceof PlayerFaction)) return true;

        PlayerFaction playerFaction = (PlayerFaction) toDisband;
        TimerManager.getInstance().getFactionFreezeTimer().cancel(playerFaction);

        playerFaction.getMembers().values().forEach(player -> {
            new PlayerLeaveFactionEvent(player, playerFaction, LeaveReason.DISBAND);
            this.players.remove(player.getUuid());
        });

        playerFaction.getFocusedAsFaction().getFocusing().remove(toDisband.getId());
        playerFaction.getAlliesAsFactions().forEach(ally -> ally.getAllies().remove(toDisband.getId()));

        return true;
    }

    private void startSaveTask() {
        int interval = Config.FACTIONS_AUTO_SAVE * 60 * 20;

        this.saveTask = Tasks.asyncTimer(() -> {
            Lazarus.getInstance().log("&3===&b=============================================&3===");

            this.saveFactions(true, false);
            this.savePlayers(true);
            ClaimManager.getInstance().saveClaims(true);

            Lazarus.getInstance().log("&3===&b=============================================&3===");
        }, interval, interval);
    }

    public void resetPlayerFactionPoints() {
        int counter = 0;

        for(Faction faction : this.factions.values()) {
            if(!(faction instanceof PlayerFaction)) continue;

            PlayerFaction playerFaction = (PlayerFaction) faction;
            playerFaction.setPoints(0);

            counter++;
        }

        Lazarus.getInstance().log("- &cReset &e" + counter + " &cplayer faction points.");
    }

    public void deleteAllPlayerFactions() {
        this.playersFile.delete();
        this.players.clear();

        int factionsSize = this.factions.size();
        Iterator<Faction> iterator = this.factions.values().iterator();

        while(iterator.hasNext()) {
            Faction faction = iterator.next();
            if(faction instanceof SystemFaction) continue;

            this.factionNames.remove(faction.getName());
            iterator.remove();
        }

        this.saveFactions(false, false);
        Lazarus.getInstance().log("- &cDeleted &e" + (factionsSize - this.factions.size()) + " &cplayer factions.");
    }

    public void removeKothFactionsWithoutKoths(List<KothData> koths) {
        List<Faction> kothFactions = this.factions.values().stream()
            .filter(faction -> faction instanceof KothFaction)
            .collect(Collectors.toList());

        Set<String> kothNames = koths.stream()
            .map(KothData::getName)
            .collect(Collectors.toSet());

        kothFactions.stream()
            .filter(kothFaction -> !kothNames.contains(kothFaction.getName()))
            .forEach(kothFaction -> this.disbandFaction(kothFaction, Bukkit.getConsoleSender()));
    }

    private void updateFocusNametagsOnDisband(PlayerFaction faction) {
        List<PlayerFaction> focusingFactions = faction.getFocusingAsFactions();
        List<Player> ownPlayers = faction.getOnlinePlayers();

        List<Player> enemies = new ArrayList<>();

        for(PlayerFaction focusing : focusingFactions) {
            focusing.setFocusedFaction(null);
            enemies.addAll(focusing.getOnlinePlayers());
        }

        Tasks.async(() -> Lazarus.getInstance().getScoreboardManager()
            .updateTabRelations(enemies, ownPlayers, false));
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionCreate(FactionCreateEvent event) {
        if(event.getFactionType() != FactionType.PLAYER_FACTION) return;

        Player player = (Player) event.getSender();
        CooldownTimer timer = TimerManager.getInstance().getCooldownTimer();

        if(timer.isActive(player, "FACTION_CREATE")) {
            player.sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_CREATE_COOLDOWN
                .replace("<time>", StringUtils.formatDurationWords(timer.getCooldown(player, "FACTION_CREATE"))));

            event.setCancelled(true);
            return;
        }

        timer.activate(player, "FACTION_CREATE", Config.FACTION_CREATE_COOLDOWN, null);
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionRename(FactionRenameEvent event) {
        if(event.getFaction() instanceof PlayerFaction && !event.isForceRename()) {
            PlayerFaction playerFaction = (PlayerFaction) event.getFaction();
            long diff = playerFaction.getRenameCooldown() - System.currentTimeMillis();

            if(diff >= 0) {
                event.getSender().sendMessage(Language.FACTION_PREFIX + Language.FACTIONS_RENAME_COOLDOWN
                    .replace("<time>", StringUtils.formatDurationWords(diff)));

                event.setCancelled(true);
                return;
            }

            playerFaction.setRenameCooldown(System.currentTimeMillis() + (Config.FACTION_RENAME_COOLDOWN * 1000L));
        }

        this.factionNames.remove(event.getFaction().getName());
        this.factionNames.put(event.getNewName(), event.getFaction().getId());
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionDisband(FactionDisbandEvent event) {
        TimerManager timerManager = TimerManager.getInstance();

        timerManager.getFactionFreezeTimer().cancel(event.getFaction().getId());
        timerManager.getFactionRallyTimer().cancel(event.getFaction().getId());

        if(event.getFaction() instanceof PlayerFaction) {
            PlayerFaction faction = (PlayerFaction) event.getFaction();
            timerManager.getDtrRegenTimer().removeFaction(faction);

            this.updateFocusNametagsOnDisband(faction);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoinFaction(PlayerJoinFactionEvent event) {
        Tasks.sync(() -> {
            PlayerFaction faction = event.getFaction();
            TimerManager.getInstance().getDtrRegenTimer().addFaction(faction, faction.getDtr());
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLeaveFaction(PlayerLeaveFactionEvent event) {
        if(event.getReason() == LeaveReason.DISBAND) return;

        Tasks.sync(() -> {
            PlayerFaction faction = event.getFaction();

            if(faction.getDtr() > faction.getMaxDtr()) {
                faction.setDtr(faction.getMaxDtr());
            }
        });
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onFactionDtrChange(FactionDtrChangeEvent event) {
        DtrRegenTimer dtrRegenTimer = TimerManager.getInstance().getDtrRegenTimer();

        if(event.getNewDtr() >= event.getFaction().getMaxDtr()) {
            dtrRegenTimer.removeFaction(event.getFaction());
        } else {
            dtrRegenTimer.addFaction(event.getFaction(), event.getNewDtr());
        }
    }

    @EventHandler
    public void onFactionRally(FactionRallyEvent event) {
        FactionRallyTimer timer = TimerManager.getInstance().getFactionRallyTimer();

        if(event.getType() == RallyEventType.ADD) {
            timer.activate(event.getFaction());
        } else {
            timer.cancel(event.getFaction());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionChat(FactionChatEvent event) {
        for(UUID uuid : this.chatSpy) {
            Player player = Bukkit.getPlayer(uuid);
            if(event.getSender() == player) continue;

            player.sendMessage(Language.FACTIONS_CHATSPY_FORMAT.replace("<format>", event.getMessage()));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeathban(PlayerDeathbanEvent event) {
        PlayerFaction faction = this.getPlayerFaction(event.getPlayer());
        if(faction == null || !faction.isAutoRevive() || faction.getLives() <= 0) return;

        event.setCancelled(true);
        faction.setLives(faction.getLives() - 1);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerFaction faction = this.getPlayerFaction(event.getPlayer());
        if(faction == null) return;

        faction.incrementOnlineMembers();
        faction.sendMessage(Language.FACTIONS_MEMBER_ONLINE.replace("<player>", event.getPlayer().getName()));

        if(Config.SHOW_FACTION_INFO_ON_JOIN) {
            Tasks.async(() -> faction.showInformation(event.getPlayer()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ClaimManager.getInstance().getFactionMap().removeFromMapUsers(event.getPlayer());

        this.chatSpy.remove(event.getPlayer().getUniqueId());
        this.stuckInit.remove(event.getPlayer().getUniqueId());

        PlayerFaction faction = this.getPlayerFaction(event.getPlayer());
        if(faction == null) return;

        faction.decrementOnlineMembers();
        faction.sendMessage(Language.FACTIONS_MEMBER_OFFLINE.replace("<player>", event.getPlayer().getName()));
    }
}
