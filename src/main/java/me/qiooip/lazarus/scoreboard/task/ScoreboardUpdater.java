package me.qiooip.lazarus.scoreboard.task;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.classes.Bard;
import me.qiooip.lazarus.classes.Miner;
import me.qiooip.lazarus.classes.manager.PvpClass;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.factions.Faction;
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
import me.qiooip.lazarus.timer.cooldown.CooldownTimer;
import me.qiooip.lazarus.timer.scoreboard.PvpClassWarmupTimer;
import me.qiooip.lazarus.timer.scoreboard.SotwTimer;
import me.qiooip.lazarus.timer.type.PlayerTimer;
import me.qiooip.lazarus.timer.type.ScoreboardTimer;
import me.qiooip.lazarus.timer.type.SystemTimer;
import me.qiooip.lazarus.userdata.Userdata;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScoreboardUpdater extends BukkitRunnable {

    private final Lazarus instance;
    private final ScoreboardManager manager;

    private ScheduledThreadPoolExecutor executor;
    private ScheduledFuture<?> updater;

    public ScoreboardUpdater(Lazarus instance, ScoreboardManager manager) {
        this.instance = instance;
        this.manager = manager;

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

                if(this.instance.getStaffModeManager().isInStaffMode(player) && this.manager.isStaffSb(player)) {
                    scoreboard.add(Config.STAFFMODE_PLACEHOLDER, "");

                    scoreboard.add(Config.VISIBILITY_PLACEHOLDER, this.instance.getVanishManager()
                    .isVanished(player) ? Config.STAFF_SB_VANISHED : Config.STAFF_SB_VISIBLE);

                    scoreboard.add(Config.CHATMODE_PLACEHOLDER, this.instance.getStaffChatHandler()
                    .isStaffChatEnabled(player) ? Config.STAFF_SB_STAFFCHAT : Config.STAFF_SB_GLOBAL);

                    scoreboard.add(Config.GAMEMODE_PLACEHOLDER, player.getGameMode() == GameMode
                    .CREATIVE ? Config.STAFF_SB_CREATIVE : Config.STAFF_SB_SURVIVAL);

                    scoreboard.add(Config.ONLINE_PLACEHOLDER, Bukkit.getOnlinePlayers().size() + "");

                    scoreboard.addLine(ChatColor.DARK_AQUA);
                } else if(this.instance.getVanishManager().isVanished(player) && this.manager.isStaffSb(player)) {
                    scoreboard.add(Config.VANISH_PLACEHOLDER, "");

                    scoreboard.add(Config.VISIBILITY_PLACEHOLDER, this.instance.getVanishManager()
                    .isVanished(player) ? Config.STAFF_SB_VANISHED : Config.STAFF_SB_VISIBLE);

                    scoreboard.addLine(ChatColor.DARK_AQUA);
                }

                Faction factionAt = ClaimManager.getInstance().getFactionAt(player);

                if(Config.KITMAP_MODE_ENABLED) {
                    if(scoreboard.add(Config.CLAIM_PLACEHOLDER, factionAt.getDisplayName(player))) {
                        scoreboard.addEmptyLine(ChatColor.DARK_RED);
                    }

                    scoreboard.add(Config.KITMAP_KILLS_PLACEHOLDER, userdata.getKills() + "");
                    scoreboard.add(Config.KITMAP_DEATHS_PLACEHOLDER, userdata.getDeaths() + "");
                    scoreboard.add(Config.KITMAP_BALANCE_PLACEHOLDER, userdata.getBalance() + "");

                    if(userdata.getKillstreak() > 0) {
                        scoreboard.add(Config.KITMAP_KILLSTREAK_PLACEHOLDER, userdata.getKillstreak() + "");
                    }

                    scoreboard.addLine(ChatColor.DARK_PURPLE);
                } else {
                    scoreboard.add(Config.CLAIM_PLACEHOLDER, factionAt.getDisplayName(player));
                }

                TimerManager.getInstance().getCustomTimer().handleScoreboardUpdate(scoreboard);

                RebootHandler reboot = Lazarus.getInstance().getRebootHandler();

                if(reboot.isRebooting()) {
                    scoreboard.add(Config.REBOOT_PLACEHOLDER, reboot.getScoreboardEntry());
                }

                RunningConquest conquest = Lazarus.getInstance().getConquestManager().getRunningConquest();

                if(conquest != null && !Config.CONQUEST_PLACEHOLDER.isEmpty()) {
                    scoreboard.add(Config.CONQUEST_PLACEHOLDER, "");
                    scoreboard.addConquest(" " + conquest.getTimeEntry(ZoneType.RED), "&a&b&1&r", "  " + conquest.getTimeEntry(ZoneType.BLUE));
                    scoreboard.addConquest(" " + conquest.getTimeEntry(ZoneType.GREEN), "&a&b&2&r", "  " + conquest.getTimeEntry(ZoneType.YELLOW));

                    int count = 1;

                    for(Entry<PlayerFaction, Integer> entry : conquest.getFactionPoints().entrySet()) {
                        scoreboard.add("&7" + count + ". " + Config.CONQUEST_FACTION_FORMAT
                        .replace("<faction>", entry.getKey().getName()),  entry.getValue() + "");

                        if(++count == 4) break;
                    }

                    scoreboard.addLine(ChatColor.GOLD);
                }

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

                if(!koths.isEmpty()) {
                    for(RunningKoth koth : koths) {
                        scoreboard.add(Config.KOTH_PLACEHOLDER.replace("<kothname>",
                                koth.getKothData().getColoredName()), koth.getScoreboardEntry());
                    }
                }

                EnderDragonManager dragon = Lazarus.getInstance().getEnderDragonManager();

                if(dragon.isActive()) {
                    scoreboard.add(Config.ENDER_DRAGON_PLACEHOLDER, dragon.getScoreboardEntry());
                }

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

                PvpClass pvpClass = this.instance.getPvpClassManager().getWarmupOrActivePvpClass(player);

                if(pvpClass != null) {
                    PvpClassWarmupTimer warmupTimer = TimerManager.getInstance().getPvpClassWarmupTimer();

                    if(warmupTimer.isActive(player, pvpClass.getName())) {

                        scoreboard.add(warmupTimer.getPlaceholder(),
                            warmupTimer.getScoreboardEntry(player, pvpClass.getName()));

                    } else if(pvpClass.isActive(player)) {
                        scoreboard.addLine(ChatColor.DARK_GRAY);
                        scoreboard.add(Config.PVPCLASS_ACTIVE_PLACEHOLDER, pvpClass.getDisplayName());

                        if(pvpClass instanceof Bard) {
                            Bard bard = (Bard) pvpClass;
                            scoreboard.add(Config.BARD_ENERGY_PLACEHOLDER, bard.getBardPower(player.getUniqueId()));

                            CooldownTimer timer = TimerManager.getInstance().getCooldownTimer();

                            if(timer.isActive(player, "BARDBUFF")) {
                                scoreboard.add(Config.COOLDOWN_PLACEHOLDER , timer.getTimeLeft(player, "BARDBUFF") + 's');
                            }
                        } else if(pvpClass instanceof Miner && !Config.KITMAP_MODE_ENABLED) {
                            scoreboard.add(Config.MINER_DIAMOND_COUNT_PLACEHOLDER,
                                player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE) + "");
                        }
                    }
                }

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
}
