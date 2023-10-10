package me.qiooip.lazarus.scoreboard.nms;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.BukkitApollo;
import com.lunarclient.apollo.common.Component;
import com.lunarclient.apollo.module.nametag.Nametag;
import com.lunarclient.apollo.module.nametag.NametagModule;
import lombok.Setter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.scoreboard.PlayerScoreboard;
import me.qiooip.lazarus.scoreboard.ScoreboardInput;
import me.qiooip.lazarus.scoreboard.base.ScoreboardBase_1_8;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.utils.ApolloUtils;
import me.qiooip.lazarus.utils.Color;
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
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerScoreboard_1_8 extends ScoreboardBase_1_8 implements PlayerScoreboard {

    private static final String SB_LINE = Config.SCOREBOARD_LINE_COLOR + ChatColor.STRIKETHROUGH + "------";
    private static final ScoreboardInput EMPTY_INPUT = new ScoreboardInput("", "", "");
    private static final NametagModule NAMETAG_MODULE = Apollo.getModuleManager().getModule(NametagModule.class);

    private final Deque<ScoreboardInput> entries;
    private Set<String> lastEntries;

    private final ScoreboardInput[] entryCache;

    private final AtomicBoolean update;
    private final AtomicBoolean lastLine;

    private Team members;
    private Team archers;
    private Team focused;
    private Team allies;
    private Team enemies;
    private Team sotw;
    private Team staff;
    private Team invis;

    @Setter private int maxSize;

    public PlayerScoreboard_1_8(Player player) {
        super(player, NmsUtils.getInstance().getPlayerScoreboard(player));

        this.setupObjective();

        this.entries = new ArrayDeque<>();
        this.lastEntries = new HashSet<>();

        this.entryCache = new ScoreboardInput[15];

        for(int i = 0; i < 15; i++) {
            this.entryCache[i] = EMPTY_INPUT;
        }

        this.setupTeams();

        this.update = new AtomicBoolean(false);
        this.lastLine = new AtomicBoolean(false);

        this.maxSize = Config.SCOREBOARD_FOOTER_ENABLED ? 11 : 13;

        player.setScoreboard(this.scoreboard);
    }

    @Override
    public void unregister() {
        synchronized(this.scoreboard) {
            for(Objective objective : this.scoreboard.getObjectives()) {
                objective.unregister();
            }

            for(Team team : this.scoreboard.getTeams()) {
                team.unregister();
            }
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

        for(int i = Math.min(15, this.entries.size()); i > 0; i--) {
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

        for(String entry : this.lastEntries) {
            if(!addedEntries.contains(entry)) {
                this.resetScore(entry);
            }
        }

        this.lastEntries = addedEntries;
        this.update.set(false);
    }

    @Override
    public void addLine(ChatColor color) {
        if(!Config.SCOREBOARD_LINE_AFTER_EVERY_SECTION || this.entries.isEmpty()
            || this.lastLine.get() || this.entries.size() >= this.maxSize) return;

        this.entries.addLast(this.getScoreboardLineInput(color));
        this.lastLine.set(true);
    }

    @Override
    public void addEmptyLine(ChatColor color) {
        this.entries.addLast(new ScoreboardInput("", color.toString() + Config.SCOREBOARD_LINE_COLOR + ChatColor.STRIKETHROUGH, ""));
        this.lastLine.set(true);
    }

    @Override
    public void add(String value, String time) {
        if(value.isEmpty() || this.entries.size() >= this.maxSize) return;

        if(time.length() > 16) {
            time = time.substring(0, 16);
        }

        if(value.length() <= 16) {
            this.entries.addLast(new ScoreboardInput("", value, time));
        } else if(value.length() <= 32) {
            this.entries.addLast(new ScoreboardInput(value.substring(0,
                value.length() - 16), value.substring(value.length() - 16), time));
        } else {
            value = value.substring(value.length() - 32);

            this.entries.addLast(new ScoreboardInput(value.substring(0, 16),
                value.substring(16, 32), time));
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
        this.entries.addFirst(this.getScoreboardLineInput(ChatColor.DARK_BLUE));

        if(Config.SCOREBOARD_FOOTER_ENABLED && this.lastLine.get()) {
            this.entries.pollLast();
            this.lastLine.set(false);
        }

        if(!this.lastLine.get()) {
            this.addFooter(Config.SCOREBOARD_FOOTER_PLACEHOLDER);

            if(!Config.SCOREBOARD_LINE_INVISIBLE) {
                this.entries.addLast(this.getScoreboardLineInput(ChatColor.DARK_GREEN));
            }
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

    private ScoreboardInput getScoreboardLineInput(ChatColor color) {
        if(Config.SCOREBOARD_LINE_INVISIBLE) {
            return new ScoreboardInput("", color.toString(), "");
        } else {
            return new ScoreboardInput(SB_LINE, color.toString() + SB_LINE, "---------");
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
        if(this.player == null || this.scoreboard == null || !Lazarus.getInstance().isFullyEnabled()) return;

        synchronized(this) {
            PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFaction(this.player);

            for(Player online : players) {
                List<Component> nametag = null;

                if(Config.LUNAR_CLIENT_API_ENABLED && Config.LUNAR_CLIENT_API_NAMETAGS_ENABLED) {
                    nametag = new ArrayList<>();
                    PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(online);

                    if(faction != null) {
                        nametag.add(ApolloUtils.textComponent(Config.LUNAR_CLIENT_API_NAMETAGS_FACTION
                            .replace("<faction>", faction.getName(this.player))
                            .replace("<dtr>", faction.getDtrString())));
                    }
                }

                if(Lazarus.getInstance().getStaffModeManager().isInStaffMode(online)) {
                    this.addAndUpdate(online, nametag, this.staff, lunarOnly);
                    continue;
                } else if(this.player == online) {
                    this.addAndUpdate(online, nametag, this.members, lunarOnly);
                    continue;
                } else if(playerFaction == null) {
                    Team updateTeam = this.enemies;

                    if(this.invis != null && online.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                        updateTeam = this.invis;
                    } else if(TimerManager.getInstance().getArcherTagTimer().isActive(online)) {
                        updateTeam = this.archers;
                    } else if(Lazarus.getInstance().getSotwHandler().isUnderSotwProtection(online)) {
                        updateTeam = this.sotw;
                    }

                    this.addAndUpdate(online, nametag, updateTeam, lunarOnly);
                    continue;
                }

                PlayerFaction targetFaction = FactionsManager.getInstance().getPlayerFaction(online);
                boolean isMemberOrAlly = playerFaction == targetFaction || playerFaction.isAlly(targetFaction);

                Team updateTeam = this.enemies;

                if(this.invis != null && online.hasPotionEffect(PotionEffectType.INVISIBILITY) && !isMemberOrAlly) {
                    updateTeam = this.invis;
                } else if(playerFaction.isFocusing(online) || playerFaction.isFocusing(targetFaction)) {
                    updateTeam = this.focused;
                } else if(playerFaction == targetFaction) {
                    updateTeam = this.members;
                } else if(playerFaction.isAlly(targetFaction)) {
                    updateTeam = this.allies;
                } else if(TimerManager.getInstance().getArcherTagTimer().isActive(online)) {
                    updateTeam = this.archers;
                } else if(Lazarus.getInstance().getSotwHandler().isUnderSotwProtection(online)) {
                    updateTeam = this.sotw;
                }

                this.addAndUpdate(online, nametag, updateTeam, lunarOnly);
            }
        }
    }

    private void addAndUpdate(Player online, List<Component> nametagLines, Team team, boolean lunarOnly) {
        if(!lunarOnly) {
            team.addEntry(online.getName());
        }

        if(nametagLines != null) {
            nametagLines.add(ApolloUtils.textComponent(team.getPrefix() + online.getName()));

            Nametag nametag = Nametag.builder().lines(nametagLines).build();
            BukkitApollo.runForPlayer(this.player, ap -> NAMETAG_MODULE.overrideNametag(ap, online.getUniqueId(), nametag));
        }
    }
}
