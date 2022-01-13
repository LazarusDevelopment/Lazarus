package me.qiooip.lazarus.scoreboard.task;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.classes.manager.PvpClass;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.games.conquest.RunningConquest;
import me.qiooip.lazarus.games.conquest.ZoneType;
import me.qiooip.lazarus.games.dragon.EnderDragonManager;
import me.qiooip.lazarus.games.dtc.DtcManager;
import me.qiooip.lazarus.games.king.KillTheKingManager;
import me.qiooip.lazarus.games.koth.RunningKoth;
import me.qiooip.lazarus.handlers.staff.RebootHandler;
import me.qiooip.lazarus.scoreboard.PlayerScoreboard;
import me.qiooip.lazarus.scoreboard.ScoreboardManager;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.abilities.GlobalAbilitiesTimer;
import me.qiooip.lazarus.timer.scoreboard.SotwTimer;
import me.qiooip.lazarus.timer.type.PlayerTimer;
import me.qiooip.lazarus.timer.type.ScoreboardTimer;
import me.qiooip.lazarus.timer.type.SystemTimer;
import me.qiooip.lazarus.userdata.Userdata;
import me.qiooip.lazarus.utils.ServerUtils;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class ScoreboardUpdaterImpl implements ScoreboardUpdater {

    private final Lazarus instance;
    private final ScoreboardManager manager;

    private ScheduledThreadPoolExecutor executor;
    private ScheduledFuture<?> updater;

    private final Function<String, String> conquestFactionFunction;
    private final Function<String, String> kothNameFunction;
    private final Function<String, String> factionFocusFunction;

    public ScoreboardUpdaterImpl(Lazarus instance, ScoreboardManager manager) {
        this.instance = instance;
        this.manager = manager;

        this.conquestFactionFunction = ServerUtils.parsePlaceholderFunction(
            Config.CONQUEST_FACTION_FORMAT, "<faction>");

        this.kothNameFunction = ServerUtils.parsePlaceholderFunction(
            Config.KOTH_PLACEHOLDER, "<kothName>");

        this.factionFocusFunction = ServerUtils.parsePlaceholderFunction(
            Config.FACTION_FOCUS_TITLE_PLACEHOLDER, "<focusedFaction>");

        Tasks.syncLater(this::setupTasks, 10L);
    }

    private void setupTasks() {
        this.executor = new ScheduledThreadPoolExecutor(2, Tasks.newThreadFactory("Scoreboard Thread - %d"));
        this.executor.setRemoveOnCancelPolicy(true);

        this.updater = this.executor.scheduleAtFixedRate(this, 0L, 100L, TimeUnit.MILLISECONDS);
    }

    public void cancel() {
        if(this.updater != null) this.updater.cancel(true);
        if(this.executor != null) this.executor.shutdownNow();
    }

    @Override
    public void run() {
        try {
            for(Player player : Bukkit.getOnlinePlayers()) {

                PlayerScoreboard scoreboard = this.manager.getPlayerScoreboard(player);
                if(scoreboard == null) continue;

                scoreboard.clear();

                Userdata userdata = this.instance.getUserdataManager().getUserdata(player);
                if(userdata == null) continue;

                if(!userdata.getSettings().isScoreboard()) {
                    scoreboard.update();
                    continue;
                }

                this.applyStaffPlaceholders(player, scoreboard);
                this.applyMiscellaneousPlaceholders(player, userdata, scoreboard);
                this.applyConquestPlaceholders(player, scoreboard);
                this.applyGamesPlaceholders(player, scoreboard);
                this.applyScoreboardTimerPlaceholders(player, scoreboard);
                this.applyFactionPlaceholders(player, scoreboard);
                this.applyPvpClassPlaceholders(player, scoreboard);
                this.applyAbilitiesPlaceholders(player, scoreboard);

                if(!scoreboard.isEmpty()) {
                    scoreboard.addLinesAndFooter();
                    scoreboard.setUpdate(true);
                }

                scoreboard.update();
            }
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    private void applyMiscellaneousPlaceholders(Player player, Userdata userdata, PlayerScoreboard scoreboard) {
        Faction factionAt = ClaimManager.getInstance().getFactionAt(player);

        if(Config.KITMAP_MODE_ENABLED) {
            scoreboard.add(Config.KITMAP_STATS_TITLE_PLACEHOLDER, "");
            scoreboard.add(Config.KITMAP_STATS_KILLS_PLACEHOLDER, userdata.getKills() + "");
            scoreboard.add(Config.KITMAP_STATS_DEATHS_PLACEHOLDER, userdata.getDeaths() + "");
            scoreboard.add(Config.KITMAP_STATS_BALANCE_PLACEHOLDER, userdata.getBalance() + "");

            if(userdata.getKillstreak() > 0) {
                scoreboard.add(Config.KITMAP_STATS_KILLSTREAK_PLACEHOLDER, userdata.getKillstreak() + "");
            }

            if(!Config.CLAIM_PLACEHOLDER.isEmpty() && factionAt != null) {
                scoreboard.addEmptyLine(ChatColor.DARK_RED);
                scoreboard.add(Config.CLAIM_PLACEHOLDER, factionAt.getDisplayName(player));
            }

            scoreboard.addLine(ChatColor.DARK_PURPLE);
        } else if(factionAt != null) {
            scoreboard.add(Config.CLAIM_PLACEHOLDER, factionAt.getDisplayName(player));
        }

        TimerManager.getInstance().getCustomTimer().handleScoreboardUpdate(scoreboard);

        RebootHandler reboot = Lazarus.getInstance().getRebootHandler();

        if(reboot.isRebooting()) {
            scoreboard.add(Config.REBOOT_PLACEHOLDER, reboot.getScoreboardEntry());
        }
    }

    private void applyConquestPlaceholders(Player player, PlayerScoreboard scoreboard) {
        RunningConquest conquest = Lazarus.getInstance().getConquestManager().getRunningConquest();

        if(conquest != null && !Config.CONQUEST_PLACEHOLDER.isEmpty()) {
            scoreboard.add(Config.CONQUEST_PLACEHOLDER, "");
            scoreboard.addConquest(" " + conquest.getTimeEntry(ZoneType.RED), "&a&b&1&r", "  " + conquest.getTimeEntry(ZoneType.BLUE));
            scoreboard.addConquest(" " + conquest.getTimeEntry(ZoneType.GREEN), "&a&b&2&r", "  " + conquest.getTimeEntry(ZoneType.YELLOW));

            int count = 1;

            if(this.conquestFactionFunction != null) {
                for(Entry<PlayerFaction, Integer> entry : conquest.getFactionPoints().entrySet()) {
                    scoreboard.add(ChatColor.GRAY.toString() + count + ". " +
                        this.conquestFactionFunction.apply(entry.getKey().getName()), entry.getValue() + "");

                    if(++count == 4) break;
                }
            }

            scoreboard.addLine(ChatColor.GOLD);
        }
    }

    private void applyGamesPlaceholders(Player player, PlayerScoreboard scoreboard) {
        KillTheKingManager killTheKing = Lazarus.getInstance().getKillTheKingManager();

        if(killTheKing.isActive()) {
            scoreboard.add(Config.KING_TITLE_PLACEHOLDER, "");
            scoreboard.add(Config.KING_KING_PLACEHOLDER, killTheKing.getKingName());
            scoreboard.add(Config.KING_TIME_LASTED_PLACEHOLDER, killTheKing.getTimeLasted());
            scoreboard.add(Config.KING_WORLD_PLACEHOLDER, killTheKing.getKingWorld());
            scoreboard.add(Config.KING_LOCATION_PLACEHOLDER, killTheKing.getKingLocation());

            scoreboard.addLine(ChatColor.GRAY);
        }

        DtcManager dtcManager = Lazarus.getInstance().getDtcManager();

        if(dtcManager.isActive()) {
            scoreboard.add(Config.DTC_PLACEHOLDER, dtcManager.getBreaksLeft() + "");
        }

        List<RunningKoth> koths = this.instance.getKothManager().getRunningKoths();

        if(!koths.isEmpty() && this.kothNameFunction != null) {
            for(RunningKoth koth : koths) {
                scoreboard.add(this.kothNameFunction.apply(koth.getKothData().getColoredName()), koth.getScoreboardEntry());
            }
        }

        EnderDragonManager dragon = Lazarus.getInstance().getEnderDragonManager();

        if(dragon.isActive()) {
            scoreboard.add(Config.ENDER_DRAGON_PLACEHOLDER, dragon.getScoreboardEntry());
        }
    }

    private void applyScoreboardTimerPlaceholders(Player player, PlayerScoreboard scoreboard) {
        for(ScoreboardTimer timer : TimerManager.getInstance().getScoreboardTimers()) {
            if(timer instanceof SystemTimer && ((SystemTimer) timer).isActive()) {
                if(timer instanceof SotwTimer) {
                    scoreboard.add(timer.getPlaceholder(player), timer.getScoreboardEntry(player));
                } else {
                    scoreboard.add(timer.getPlaceholder(), timer.getScoreboardEntry());
                }
            } else if(timer instanceof PlayerTimer && ((PlayerTimer) timer).isActive(player)) {
                scoreboard.add(timer.getPlaceholder(), timer.getScoreboardEntry(player));
            }
        }
    }

    private void applyStaffPlaceholders(Player player, PlayerScoreboard scoreboard) {
        if(this.instance.getStaffModeManager().isInStaffMode(player) && this.manager.isStaffSb(player)) {
            scoreboard.add(Config.STAFFMODE_PLACEHOLDER, "");

            scoreboard.add(Config.VISIBILITY_PLACEHOLDER, this.instance.getVanishManager().isVanished(player)
                ? Config.STAFF_SB_VANISHED : Config.STAFF_SB_VISIBLE);

            scoreboard.add(Config.CHATMODE_PLACEHOLDER, this.instance.getStaffChatHandler().isStaffChatEnabled(player)
                ? Config.STAFF_SB_STAFFCHAT : Config.STAFF_SB_GLOBAL);

            scoreboard.add(Config.GAMEMODE_PLACEHOLDER, player.getGameMode() == GameMode.CREATIVE
                ? Config.STAFF_SB_CREATIVE : Config.STAFF_SB_SURVIVAL);

            scoreboard.add(Config.ONLINE_PLACEHOLDER, Bukkit.getOnlinePlayers().size() + "");

            scoreboard.addLine(ChatColor.DARK_AQUA);
        } else if(this.instance.getVanishManager().isVanished(player) && this.manager.isStaffSb(player)) {
            scoreboard.add(Config.VANISH_PLACEHOLDER, "");

            scoreboard.add(Config.VISIBILITY_PLACEHOLDER, this.instance.getVanishManager().isVanished(player)
                ? Config.STAFF_SB_VANISHED : Config.STAFF_SB_VISIBLE);

            scoreboard.addLine(ChatColor.DARK_AQUA);
        }
    }

    private void applyFactionPlaceholders(Player player, PlayerScoreboard scoreboard) {
        PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFaction(player);
        if(playerFaction == null) return;

        if(playerFaction.getFocusedFaction() != null) {
            PlayerFaction focusedFaction = playerFaction.getFocusedAsFaction();

            if(focusedFaction != null && this.factionFocusFunction != null) {
                scoreboard.addLine(ChatColor.GREEN);
                scoreboard.add(this.factionFocusFunction.apply(focusedFaction.getName()), "");
                scoreboard.add(Config.FACTION_FOCUS_DTR_PLACEHOLDER, focusedFaction.getDtrString());
                scoreboard.add(Config.FACTION_FOCUS_HQ_PLACEHOLDER, focusedFaction.getHomeString());
                scoreboard.add(Config.FACTION_FOCUS_ONLINE_PLACEHOLDER, String.valueOf(focusedFaction.getOnlineMemberCount()));
            }
        }

        if(playerFaction.getRallyLocation() != null) {
            scoreboard.addLine(ChatColor.DARK_GRAY);
            scoreboard.add(Config.FACTION_RALLY_TITLE_PLACEHOLDER, "");

            scoreboard.add(Config.FACTION_RALLY_WORLD_PLACEHOLDER,
                StringUtils.getWorldName(playerFaction.getRallyLocation()));

            scoreboard.add(Config.FACTION_RALLY_LOCATION_PLACEHOLDER, Config.FACTION_RALLY_INCLUDE_Y_COORDINATE
                ? StringUtils.getLocationName(playerFaction.getRallyLocation())
                : StringUtils.getLocationNameWithoutY(playerFaction.getRallyLocation()));
        }
    }

    private void applyPvpClassPlaceholders(Player player, PlayerScoreboard scoreboard) {
        PvpClass pvpClass = this.instance.getPvpClassManager().getWarmupOrActivePvpClass(player);

        if(pvpClass != null) {
            pvpClass.applyScoreboardLines(player, scoreboard);
        }
    }

    private void applyAbilitiesPlaceholders(Player player, PlayerScoreboard scoreboard) {
        GlobalAbilitiesTimer globalAbilitiesTimer = TimerManager.getInstance().getGlobalAbilitiesTimer();
        boolean globalTimerActive = globalAbilitiesTimer.isActive(player);

        Map<String, String> activeAbilities = TimerManager.getInstance().getAbilitiesTimer().getActiveAbilities(player);

        if(globalTimerActive || activeAbilities != null) {
            scoreboard.addLine(ChatColor.BLUE);
            scoreboard.add(Config.ABILITIES_TITLE_PLACEHOLDER, "");

            if(globalTimerActive) {
                scoreboard.add(Config.ABILITIES_GLOBAL_COOLDOWN_PLACEHOLDER, globalAbilitiesTimer.getTimeLeft(player));
            }

            if(activeAbilities != null) {
                for(Entry<String, String> abilityPlaceholders : activeAbilities.entrySet()) {
                    scoreboard.add(abilityPlaceholders.getKey(), abilityPlaceholders.getValue());
                }
            }
        }
    }
}
