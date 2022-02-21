package me.qiooip.lazarus.factions.type;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionPlayer;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.enums.Role;
import me.qiooip.lazarus.factions.event.FactionDataChangeEvent;
import me.qiooip.lazarus.factions.event.FactionDataType;
import me.qiooip.lazarus.factions.event.FactionDtrChangeEvent;
import me.qiooip.lazarus.factions.event.FactionFocusedEvent;
import me.qiooip.lazarus.factions.event.FactionUnfocusedEvent;
import me.qiooip.lazarus.factions.event.PlayerUnfocusedEvent;
import me.qiooip.lazarus.scoreboard.PlayerScoreboard;
import me.qiooip.lazarus.scoreboard.ScoreboardManager;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.userdata.Userdata;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@Setter
public class PlayerFaction extends Faction {

    private double dtr;
    private String announcement;

    private int balance;
    private int lives;
    private int points;
    private int kills;
    private int kothsCapped;

    private Location home;
    private boolean open;
    private boolean autoRevive;
    private boolean friendlyFire;

    private List<UUID> allies;

    private transient Map<UUID, FactionPlayer> members;
    private transient int onlineMemberCount;

    private transient Set<String> playerInvitations;
    private transient List<UUID> allyInvitations;

    private transient UUID focusedPlayer;
    private transient UUID focusedFaction;
    private transient Set<UUID> focusing;
    private transient Location rallyLocation;

    private transient long renameCooldown;
    private transient long openChangeCooldown;

    public PlayerFaction() {
        this.members = new HashMap<>();
        this.playerInvitations = new HashSet<>();
        this.allyInvitations = new ArrayList<>();
        this.focusing = new HashSet<>();
    }

    public PlayerFaction(String name) {
        super(name);

        this.members = new HashMap<>();
        this.playerInvitations = new HashSet<>();
        this.allyInvitations = new ArrayList<>();
        this.focusing = new HashSet<>();

        this.allies = new ArrayList<>();
        this.dtr = Config.FACTION_DTR_SOLO_FACTION_DTR;

        this.changeKills(0);
        this.changePoints(0);
        this.changeKothsCapped(0);
        this.changeBalance(Config.DEFAULT_BALANCE_FACTION);
    }

    public void sendMessage(String message) {
        this.members.values().forEach(player -> player.sendMessage(message));
    }

    public List<Player> getOnlinePlayers() {
        return this.members.values().stream().map(member -> Bukkit.getPlayer(member.getUuid()))
            .filter(Objects::nonNull).collect(Collectors.toList());
    }

    private int getOnlinePlayersCount(CommandSender sender) {
        Predicate<? super Player> condition = (player) -> Objects.nonNull(player) && (!(sender instanceof Player) ||
            (((Player) sender).canSee(player) || !Lazarus.getInstance().getVanishManager().isVanished(player)));

        return (int) this.members.values().stream().map(member -> Bukkit.getPlayer(member.getUuid())).filter(condition).count();
    }

    public void incrementOnlineMembers() {
        this.onlineMemberCount++;
    }

    public void decrementOnlineMembers() {
        this.onlineMemberCount--;
    }

    public FactionPlayer getLeader() {
        return this.members.values().stream().filter(member -> member.getRole() == Role.LEADER).findFirst().orElse(null);
    }

    public FactionPlayer getMember(OfflinePlayer player) {
        return player.hasPlayedBefore() || player.isOnline() ? this.getMember(player.getUniqueId()) : null;
    }

    public FactionPlayer getMember(String name) {
        return this.getMember(Bukkit.getOfflinePlayer(name));
    }

    public FactionPlayer getMember(Player player) {
        return this.getMember(player.getUniqueId());
    }

    public FactionPlayer getMember(UUID uuid) {
        return this.members.get(uuid);
    }

    public void addMember(FactionPlayer factionPlayer) {
        this.members.put(factionPlayer.getUuid(), factionPlayer);
    }

    public void removeMember(FactionPlayer factionPlayer) {
        this.members.remove(factionPlayer.getUuid());
    }

    public boolean isAlly(PlayerFaction faction) {
        if(faction == null) return false;
        return this.allies.contains(faction.getId());
    }

    public void addAlly(PlayerFaction faction) {
        this.allies.add(faction.getId());
    }

    public List<PlayerFaction> getAlliesAsFactions() {
        return this.allies.stream().map(FactionsManager.getInstance()::getPlayerFactionByUuid)
            .filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<PlayerFaction> getFocusingAsFactions() {
        return this.focusing.stream().map(FactionsManager.getInstance()::getPlayerFactionByUuid)
            .filter(Objects::nonNull).collect(Collectors.toList());
    }

    public PlayerFaction getFocusedAsFaction() {
        return FactionsManager.getInstance().getPlayerFactionByUuid(this.focusedFaction);
    }

    public String getHomeString() {
        return this.home == null ? Language.NONE_PLACEHOLDER : this.home.getBlockX() + ", " + this.home.getBlockZ();
    }

    public void setOpenStatus(boolean value) {
        this.open = value;
        this.openChangeCooldown = System.currentTimeMillis() + (Config.FACTION_OPEN_CHANGE_COOLDOWN * 1000L);
    }

    public boolean isRaidable() {
        return this.getDtr() <= 0;
    }

    public boolean isFrozen() {
        return TimerManager.getInstance().getFactionFreezeTimer().isActive(this.id);
    }

    private String getRegeneratingString() {
        return StringUtils.formatDurationWords(TimerManager.getInstance().getFactionFreezeTimer().getCooldown(this));
    }

    private String getRaidableString() {
        return Language.getYesOrNo(this.isRaidable());
    }

    public double getMaxDtr() {
        return this.members.size() == 1 || this.members.isEmpty()
            ? Config.FACTION_DTR_SOLO_FACTION_DTR
            : Math.min(Config.FACTION_MAX_DTR, this.members.size() * Config.FACTION_DTR_PER_PLAYER);
    }

    public void setDtrOnLoad(double amount) {
        this.dtr = amount;
    }

    public void setDtr(double amount) {
        double currentDtr = this.dtr;

        double newDtr = amount < 0 ? Math.max(Config.FACTION_MIN_DTR, amount) : Math.min(this.getMaxDtr(), amount);
        double newDtrRounded = Math.round(newDtr * 100) / 100.0;

        if(currentDtr != newDtrRounded) {
            this.callDtrUpdateEvent(currentDtr, newDtrRounded);
        }
    }

    private void callDtrUpdateEvent(double currentDtr, double newDtr) {
        FactionDtrChangeEvent event = new FactionDtrChangeEvent(this, currentDtr, newDtr);
        if(event.isCancelled()) return;

        this.dtr = event.getNewDtr();
    }

    public String getMaxDtrString() {
        return String.valueOf((double) Math.round(this.getMaxDtr() * 10d) / 10d);
    }

    public String getDtrString() {
        return this.getDtrColor().toString() + this.getDtr() + this.getDtrCharacter();
    }

    public ChatColor getDtrColor() {
        double dtr = this.getDtr();

        if(dtr <= 0) {
            return ChatColor.RED;
        } else if(dtr <= 1) {
            return ChatColor.YELLOW;
        } else {
            return ChatColor.GREEN;
        }
    }

    public String getDtrCharacter() {
        if(this.getDtr() >= this.getMaxDtr()) {
            return Config.FACTION_DTR_CHARACTERS_FULL_DTR;
        } else if(this.isFrozen()) {
            return Config.FACTION_DTR_CHARACTERS_FROZEN;
        } else {
            return Config.FACTION_DTR_CHARACTERS_REGENERATING;
        }
    }

    public void addKill() {
        this.changeKills(this.kills + 1);
    }

    public void changeKills(int value) {
        this.kills = value;
        new FactionDataChangeEvent(this, FactionDataType.KILLS);
    }

    public void addBalance(int amount) {
        this.changeBalance(this.balance + amount);
    }

    public void removeBalance(int amount) {
        this.changeBalance(this.balance - amount);
    }

    public void changeBalance(int amount) {
        this.balance = amount;
        new FactionDataChangeEvent(this, FactionDataType.BALANCE);
    }

    public void onDeath(Player player) {
        double dtrLoss = 0.0;

        if(!Config.KITMAP_MODE_ENABLED || !Config.KITMAP_MODE_DISABLE_DTR_LOSS) {
            dtrLoss = Config.FACTION_DTR_DEATH_LOSS.get(player.getWorld().getEnvironment());
            this.setDtr(this.getDtr() - dtrLoss);

            TimerManager.getInstance().getFactionFreezeTimer().activate(this);
        }

        this.changePoints(this.points + Config.FACTION_TOP_DEATH);

        this.sendMessage(Language.FACTIONS_MEMBER_DEATH
            .replace("<player>", player.getName()).replace("<dtrLoss>", "-" + dtrLoss)
            .replace("<dtr>", this.getDtrString()).replace("<maxDtr>", this.getMaxDtrString()));
    }

    public void incrementPoints(int value) {
        this.changePoints(this.points + Math.abs(value));
    }

    public void changePoints(int value) {
        this.points = Config.FACTION_TOP_ALLOW_NEGATIVE_POINTS ? value : Math.max(0, value);
        new FactionDataChangeEvent(this, FactionDataType.POINTS);
    }

    public void incrementKothsCapped() {
        this.changeKothsCapped(this.kothsCapped + 1);
    }

    public void changeKothsCapped(int value) {
        this.kothsCapped = value;
        new FactionDataChangeEvent(this, FactionDataType.KOTHS_CAPPED);
    }

    public boolean isFocusing(Player player) {
        return this.focusedPlayer != null && this.focusedPlayer.equals(player.getUniqueId());
    }

    public boolean isFocusing(PlayerFaction faction) {
        return this.focusedFaction != null && faction != null && this.focusedFaction.equals(faction.getId());
    }

    public void focusPlayer(Player player) {
        Player current = this.getOldFocusedPlayer();

        this.focusedPlayer = player.getUniqueId();
        new FactionFocusedEvent(this, player.getUniqueId());

        Tasks.async(() -> {
            for(FactionPlayer member : this.members.values()) {
                PlayerScoreboard scoreboard = ScoreboardManager.getInstance().getPlayerScoreboard(member.getUuid());
                if(scoreboard == null) continue;

                scoreboard.updateRelation(player);

                if(current != null) {
                    scoreboard.updateRelation(current);
                }
            }
        });
    }

    public void unfocusPlayer(OfflinePlayer offlinePlayer) {
        PlayerUnfocusedEvent event = new PlayerUnfocusedEvent(this, this.focusedPlayer);
        if(event.isCancelled()) return;

        this.focusedPlayer = null;
        if(!offlinePlayer.isOnline()) return;

        Tasks.async(() -> {
            for(FactionPlayer member : this.members.values()) {
                PlayerScoreboard scoreboard = ScoreboardManager.getInstance().getPlayerScoreboard(member.getUuid());

                if(scoreboard != null) {
                    scoreboard.updateRelation(offlinePlayer.getPlayer());
                }
            }
        });
    }

    public void focusFaction(PlayerFaction playerFaction) {
        List<Player> oldEnemies = this.getOldFocusedFactionPlayers();

        this.focusedFaction = playerFaction.getId();
        List<Player> newEnemies = playerFaction.getOnlinePlayers();

        playerFaction.getFocusing().add(this.id);
        new FactionFocusedEvent(this, playerFaction.getId());

        Tasks.async(() -> {
            for(FactionPlayer member : this.members.values()) {
                PlayerScoreboard scoreboard = ScoreboardManager.getInstance().getPlayerScoreboard(member.getUuid());
                if(scoreboard == null) continue;

                scoreboard.updateTabRelations(newEnemies);

                if(oldEnemies != null) {
                    scoreboard.updateTabRelations(oldEnemies);
                }
            }
        });
    }

    public void unfocusFaction(PlayerFaction playerFaction) {
        FactionUnfocusedEvent event = new FactionUnfocusedEvent(this, this.focusedFaction);
        if(event.isCancelled()) return;

        this.focusedFaction = null;
        playerFaction.getFocusing().remove(this.id);
        List<Player> enemies = playerFaction.getOnlinePlayers();

        Tasks.async(() -> {
            for(FactionPlayer member : this.members.values()) {
                PlayerScoreboard scoreboard = ScoreboardManager.getInstance().getPlayerScoreboard(member.getUuid());

                if(scoreboard != null) {
                    scoreboard.updateTabRelations(enemies);
                }
            }
        });
    }

    private Player getOldFocusedPlayer() {
        return this.focusedPlayer != null ? Bukkit.getPlayer(this.focusedPlayer) : null;
    }

    private List<Player> getOldFocusedFactionPlayers() {
        if(this.focusedFaction == null) return null;

        PlayerFaction currentFocused = this.getFocusedAsFaction();
        return currentFocused != null ? currentFocused.getOnlinePlayers() : null;
    }

    @Override
    public boolean shouldCancelPvpTimerEntrance(Player player) {
        return !Config.PVP_PROTECTION_CAN_ENTER_OWN_CLAIM || !this.members.containsKey(player.getUniqueId());
    }

    public void showInformation(CommandSender sender) {
        AtomicReference<String> leader = new AtomicReference<>();
        StringJoiner coLeaders = new StringJoiner(", ");
        StringJoiner captains = new StringJoiner(", ");
        StringJoiner members = new StringJoiner(", ");
        StringJoiner allies = new StringJoiner(", ");

        this.members.values().forEach(member -> {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(member.getUuid());
            Userdata userdata = Lazarus.getInstance().getUserdataManager().getUserdata(offlinePlayer);

            if(userdata != null) {
                switch(member.getRole()) {
                    case LEADER: leader.set(this.getPlayerNameFormatted(member, userdata)); break;
                    case CO_LEADER: coLeaders.add(this.getPlayerNameFormatted(member, userdata)); break;
                    case CAPTAIN: captains.add(this.getPlayerNameFormatted(member, userdata)); break;
                    case MEMBER: members.add(this.getPlayerNameFormatted(member, userdata)); break;
                }
            }
        });

        this.allies.forEach(uuid -> allies.add(FactionsManager.getInstance().getFactionByUuid(uuid).getName(sender)));

        List<String> showMessage = new ArrayList<>(Language.FACTIONS_PLAYER_FACTION_SHOW);

        boolean isSameFaction = sender instanceof Player && FactionsManager
            .getInstance().getPlayerFaction((Player) sender) == this;

        showMessage.removeIf(line -> line.contains("<co-leaders>") && coLeaders.length() == 0
            || line.contains("<announcement>") && (!isSameFaction || StringUtils.isNullOrEmpty(this.announcement))
            || line.contains("<autoRevive>") && !isSameFaction
            || line.contains("<lives>") && !isSameFaction
            || line.contains("<captains>") && captains.length() == 0
            || line.contains("<members>") && members.length() == 0
            || line.contains("<allies>") && allies.length() == 0
            || line.contains("<regen-time>") && !this.isFrozen());

        StringJoiner joiner = new StringJoiner("\n" + ChatColor.RESET);
        showMessage.forEach(joiner::add);

        sender.sendMessage(joiner.toString()
            .replace("<faction>", this.getName(sender))
            .replace("<online-count>", String.valueOf(this.getOnlinePlayersCount(sender)))
            .replace("<faction-size>", String.valueOf(this.members.size()))
            .replace("<home-location>", this.getHomeString())
            .replace("<announcement>", this.announcement != null ? this.announcement : "")
            .replace("<autoRevive>", Language.getEnabledOrDisabled(this.autoRevive))
            .replace("<lives>", String.valueOf(this.lives))
            .replace("<kills>", String.valueOf(this.kills))
            .replace("<leader>", leader.get())
            .replace("<co-leaders>", coLeaders.toString())
            .replace("<captains>", captains.toString())
            .replace("<members>", members.toString())
            .replace("<allies>", allies.toString())
            .replace("<balance>", String.valueOf(this.balance))
            .replace("<points>", String.valueOf(this.points))
            .replace("<koth-captures>", String.valueOf(this.kothsCapped))
            .replace("<regen-time>", this.getRegeneratingString())
            .replace("<dtr>", this.getDtrString())
            .replace("<dtrMax>", this.getMaxDtrString())
            .replace("<raidable>", this.getDtrColor() + this.getRaidableString()));
    }

    private String getPlayerNameFormatted(FactionPlayer fplayer, Userdata userdata) {
        ChatColor color;

        if(Lazarus.getInstance().getDeathbanManager().isDeathBanned(fplayer.getUuid())) {
            color = ChatColor.RED;
        } else if(fplayer.getPlayer() != null && !Lazarus.getInstance().getStaffModeManager().isInStaffModeOrVanished(fplayer.getUuid())) {
            color = ChatColor.GREEN;
        } else {
            color = ChatColor.GRAY;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(fplayer.getUuid());

        return Language.FACTIONS_SHOW_NAME_FORMAT
            .replace("<player>", color + offlinePlayer
            .getName()).replace("<kills>", String.valueOf(userdata.getKills()));
    }
}
