package me.qiooip.lazarus.scoreboard.nms;

import com.lunarclient.bukkitapi.LunarClientAPI;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.scoreboard.PlayerScoreboard;
import me.qiooip.lazarus.scoreboard.ScoreboardInput;
import me.qiooip.lazarus.scoreboard.base.ScoreboardBase_1_8;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.Tasks;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public class PlayerScoreboard_1_8 extends ScoreboardBase_1_8 implements PlayerScoreboard {

    private static final String SB_LINE = Config.SCOREBOARD_LINE_COLOR + ChatColor.STRIKETHROUGH.toString() + "------";
    private static final ScoreboardInput EMPTY_INPUT = new ScoreboardInput("", "", "");

    private final Deque<ScoreboardInput> entries;
    private Set<String> lastEntries;

    private final ScoreboardInput[] entryCache;

    private final AtomicBoolean update;
    private final AtomicBoolean lastLine;

    private final int maxSize;

    private Team members;
    private Team archers;
    private Team focused;
    private Team allies;
    private Team enemies;
    private Team sotw;
    private Team staff;
    private Team invis;

    public PlayerScoreboard_1_8(Player player) {
        super(player, NmsUtils.getInstance().getPlayerScoreboard(player));

        this.setupObjective();

        this.entries = new ArrayDeque<>();
        this.lastEntries = new HashSet<>();

        this.entryCache = new ScoreboardInput[15];
        IntStream.range(0, 15).forEach(i -> this.entryCache[i] = EMPTY_INPUT);

        this.setupTeams();

        this.update = new AtomicBoolean(false);
        this.lastLine = new AtomicBoolean(false);

        this.maxSize = Config.SCOREBOARD_FOOTER_ENABLED ? 11 : 13;

        player.setScoreboard(scoreboard);
    }

    @Override
    public void unregister() {
        synchronized(this.scoreboard) {
            this.scoreboard.getObjectives().forEach(Objective::unregister);
            this.scoreboard.getTeams().forEach(Team::unregister);
        }

        for(Object entry : this.nmsScoreboard.getPlayers().toArray()) {
            this.resetScore((String) entry);
        }

        this.player = null;
    }

    private void setupObjective() {
        Objective objective = this.scoreboard.getObjective("lazarus");

        if(objective == null) {
            objective = this.scoreboard.registerNewObjective("lazarus", "dummy");
        }

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(Config.SCOREBOARD_TITLE);
    }

    private void setupTeams() {
        this.members = this.getTeam(Color.translate("&8&3members"));
        this.members.setPrefix(Config.TEAMMATE_COLOR);
        this.members.setCanSeeFriendlyInvisibles(true);

        this.allies = this.getTeam(Color.translate("&8&4allies"));
        this.allies.setPrefix(Config.ALLY_COLOR);

        this.archers = this.getTeam(Color.translate("&8&2archers"));
        this.archers.setPrefix(Config.ARCHER_TAGGED_COLOR);

        this.focused = this.getTeam(Color.translate("&8&1focused"));
        this.focused.setPrefix(Config.FOCUSED_COLOR);

        this.enemies = this.getTeam(Color.translate("&8&5enemies"));
        this.enemies.setPrefix(Config.ENEMY_COLOR);

        this.sotw = this.getTeam(Color.translate("&8&6sotw"));
        this.sotw.setPrefix(Config.SOTW_COLOR);

        this.staff = this.getTeam(Color.translate("&8&7staff"));
        this.staff.setPrefix(Config.STAFF_MODE_COLOR);

        this.invis = this.getTeam(Color.translate("&8&8invis"));
        this.invis.setNameTagVisibility(NameTagVisibility.NEVER);
    }

    @Override
    public void update() {
        if(!this.update.get() && this.lastEntries.isEmpty()) return;

        Set<String> addedEntries = new HashSet<>(this.entries.size());

        for(int i = this.entries.size(); i > 0; i--) {
            ScoreboardInput input = this.entries.pollFirst();
            if(input == null) return;

            addedEntries.add(input.getName());

            if(this.entryCache[i-1].equals(input)) {
                continue;
            }

            Team team = this.getTeam(input.getName());

            if(!team.hasEntry(input.getName())) {
                team.addEntry(input.getName());
            }

            this.updateTeam(team.getName(), input.getPrefix(), input.getSuffix());

            this.entryCache[i-1] = input;
            this.setScore(input.getName(), i);
        }

        if(addedEntries.size() < this.lastEntries.size()) {
            for(int i = addedEntries.size(); i < this.lastEntries.size(); i++) {
                this.entryCache[i] = EMPTY_INPUT;
            }
        }

        this.lastEntries.forEach(entry -> {
            if(!addedEntries.contains(entry)) {
                this.resetScore(entry);
            }
        });

        this.lastEntries = addedEntries;
        this.update.set(false);
    }

    @Override
    public void addLine(ChatColor color) {
        if(!Config.SCOREBOARD_LINE_AFTER_EVERY_SECTION || this.entries.size() >= this.maxSize) return;

        this.entries.addLast(new ScoreboardInput(SB_LINE, color.toString() + SB_LINE, "---------"));
        this.lastLine.set(true);
    }

    @Override
    public void add(String value, String time) {
        if(value.isEmpty() || this.entries.size() >= this.maxSize) return;

        value = Color.translate(value);
        if(!time.isEmpty()) time = Color.translate(time);

        if(value.length() <= 16) {
            this.entries.addLast(new ScoreboardInput("", value, time));
        } else {
            this.entries.addLast(new ScoreboardInput(value.substring(0, value
            .length() - 16), value.substring(value.length() - 16), time));
        }

        this.lastLine.set(false);
    }

    @Override
    public void addConquest(String prefix, String value, String suffix) {
        if(this.entries.size() >= this.maxSize) return;

        this.entries.addLast(new ScoreboardInput(prefix, Color.translate(value), suffix));
        this.lastLine.set(false);
    }

    @Override
    public void addLinesAndFooter() {
        this.entries.addFirst(new ScoreboardInput(SB_LINE, ChatColor.DARK_BLUE.toString() + SB_LINE, "---------"));

        if(Config.SCOREBOARD_FOOTER_ENABLED && this.lastLine.get()) {
            this.entries.pollLast();
            this.lastLine.set(false);
        }

        if(!this.lastLine.get()) {
            this.addFooter(Config.SCOREBOARD_FOOTER_PLACEHOLDER);
            this.entries.addLast(new ScoreboardInput(SB_LINE, ChatColor.DARK_GREEN.toString() + SB_LINE, "---------"));
        }
    }

    private void addFooter(String footer) {
        if(!Config.SCOREBOARD_FOOTER_ENABLED) return;

        this.entries.addLast(new ScoreboardInput("", ChatColor.AQUA + ChatColor.RESET.toString(), ""));

        if(footer.length() <= 16) {
            this.entries.addLast(new ScoreboardInput("", footer, ""));
        } else {
            this.entries.addLast(new ScoreboardInput(footer.substring(0, footer
            .length() - 16), footer.substring(footer.length() - 16), ""));
        }
    }

    private Team getTeam(String name) {
        synchronized(this.scoreboard) {
            Team team = this.scoreboard.getTeam(name);
            return team == null ? this.scoreboard.registerNewTeam(name) : team;
        }
    }

    @Override
    public void setUpdate(boolean value) {
        this.update.set(value);
    }

    @Override
    public boolean isEmpty() {
        return this.entries.isEmpty();
    }

    @Override
    public void clear() {
        this.entries.clear();
    }

    @Override
    public void updateTabRelations(Iterable<? extends Player> players, boolean lunarOnly) {
        if(Thread.currentThread() == NmsUtils.getInstance().getMainThread()) {
            Tasks.async(() -> this.updateAllTabRelations(players, lunarOnly));
        } else {
            this.updateAllTabRelations(players, lunarOnly);
        }
    }

    @Override
    public void updateRelation(Player player) {
        this.updateTabRelations(Collections.singletonList(player));
    }

    private void updateAllTabRelations(Iterable<? extends Player> players, boolean lunarOnly) {
        if(this.player == null) return;

        synchronized(this) {
            PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFaction(this.player);

            for(Player online : players) {
                List<String> nametag = null;

                if(Config.LUNAR_CLIENT_API_ENABLED && Config.LUNAR_CLIENT_API_NAMETAGS_ENABLED) {
                    nametag = new ArrayList<>();

                    PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(online);
                    if(faction != null) {
                        String tag = Config.LUNAR_CLIENT_API_NAMETAGS_FACTION
                                .replace("<faction>", faction.getName(this.player))
                                .replace("<dtr>", faction.getDtrString());
                        nametag.add(tag);
                    }
                }

                if(Lazarus.getInstance().getStaffModeManager().isInStaffMode(online)) {
                    this.addAndUpdate(online, nametag, this.staff, lunarOnly);
                    continue;
                } else if(this.player == online) {
                    this.addAndUpdate(online, nametag, this.members, lunarOnly);
                    continue;
                } else if(playerFaction == null) {
                    if(this.invis != null && online.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                        this.addAndUpdate(online, nametag, this.invis, lunarOnly);
                    } else if(TimerManager.getInstance().getArcherTagTimer().isActive(online)) {
                        this.addAndUpdate(online, nametag, this.archers, lunarOnly);
                    } else if(Lazarus.getInstance().getSotwHandler().isUnderSotwProtection(online)) {
                        this.addAndUpdate(online, nametag, this.sotw, lunarOnly);
                    } else {
                        this.addAndUpdate(online, nametag, this.enemies, lunarOnly);
                    }
                    continue;
                }

                PlayerFaction targetFaction = FactionsManager.getInstance().getPlayerFaction(online);
                boolean isMemberOrAlly = playerFaction == targetFaction || playerFaction.isAlly(targetFaction);

                if(this.invis != null && online.hasPotionEffect(PotionEffectType.INVISIBILITY) && !isMemberOrAlly) {
                    this.addAndUpdate(online, nametag, this.invis, lunarOnly);
                } else if(playerFaction.isFocusing(online.getUniqueId())) {
                    this.addAndUpdate(online, nametag, this.focused, lunarOnly);
                } else if(playerFaction == targetFaction) {
                    this.addAndUpdate(online, nametag, this.members, lunarOnly);
                } else if(playerFaction.isAlly(targetFaction)) {
                    this.addAndUpdate(online, nametag, this.allies, lunarOnly);
                } else if(TimerManager.getInstance().getArcherTagTimer().isActive(online)) {
                    this.addAndUpdate(online, nametag, this.archers, lunarOnly);
                } else if(Lazarus.getInstance().getSotwHandler().isUnderSotwProtection(online)) {
                    this.addAndUpdate(online, nametag, this.sotw, lunarOnly);
                } else {
                    this.addAndUpdate(online, nametag, this.enemies, lunarOnly);
                }
            }
        }
    }

    private void addAndUpdate(Player online, List<String> nametag, Team team, boolean lunarOnly) {
        if(!lunarOnly) {
            team.addEntry(online.getName());
        }

        if(nametag != null) {
            nametag.add(team.getPrefix() + online.getName());

            LunarClientAPI.getInstance().overrideNametag(online, nametag, this.player);
        }
    }
}
