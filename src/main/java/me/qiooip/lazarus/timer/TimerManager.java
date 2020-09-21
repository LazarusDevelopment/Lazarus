package me.qiooip.lazarus.timer;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.timer.abilities.AbilitiesTimer;
import me.qiooip.lazarus.timer.abilities.GlobalAbilitiesTimer;
import me.qiooip.lazarus.timer.cooldown.CooldownTimer;
import me.qiooip.lazarus.timer.cooldown.RankReviveTimer;
import me.qiooip.lazarus.timer.custom.CustomTimer;
import me.qiooip.lazarus.timer.scoreboard.AppleTimer;
import me.qiooip.lazarus.timer.scoreboard.ArcherTagTimer;
import me.qiooip.lazarus.timer.scoreboard.CombatTagTimer;
import me.qiooip.lazarus.timer.scoreboard.EnderPearlTimer;
import me.qiooip.lazarus.timer.scoreboard.EotwTimer;
import me.qiooip.lazarus.timer.scoreboard.FactionFreezeTimer;
import me.qiooip.lazarus.timer.scoreboard.GAppleTimer;
import me.qiooip.lazarus.timer.scoreboard.HomeTimer;
import me.qiooip.lazarus.timer.scoreboard.KeySaleTimer;
import me.qiooip.lazarus.timer.scoreboard.LogoutTimer;
import me.qiooip.lazarus.timer.scoreboard.PvpClassWarmupTimer;
import me.qiooip.lazarus.timer.scoreboard.PvpProtTimer;
import me.qiooip.lazarus.timer.scoreboard.SaleTimer;
import me.qiooip.lazarus.timer.scoreboard.SotwTimer;
import me.qiooip.lazarus.timer.scoreboard.StuckTimer;
import me.qiooip.lazarus.timer.scoreboard.TeleportTimer;
import me.qiooip.lazarus.timer.type.ScoreboardTimer;
import me.qiooip.lazarus.utils.Tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Getter
public class TimerManager {

    @Getter private static TimerManager instance;

    private ConfigFile timersFile;

    private final ScheduledThreadPoolExecutor executor;
    private final List<ScoreboardTimer> scoreboardTimers;

    private final ArcherTagTimer archerTagTimer;
    private final CombatTagTimer combatTagTimer;
    private final EnderPearlTimer enderPearlTimer;
    private final EotwTimer eotwTimer;
    private final LogoutTimer logoutTimer;
    private final PvpProtTimer pvpProtTimer;
    private final SotwTimer sotwTimer;
    private final HomeTimer homeTimer;
    private final SaleTimer saleTimer;
    private final KeySaleTimer keySaleTimer;
    private final StuckTimer stuckTimer;
    private final TeleportTimer teleportTimer;
    private final AppleTimer appleTimer;
    private final GAppleTimer gAppleTimer;
    private final PvpClassWarmupTimer pvpClassWarmupTimer;

    private final GlobalAbilitiesTimer globalAbilitiesTimer;
    private final AbilitiesTimer abilitiesTimer;

    private final FactionFreezeTimer factionFreezeTimer;
    private final CooldownTimer cooldownTimer;
    private final RankReviveTimer rankReviveTimer;
    private final CustomTimer customTimer;

    public TimerManager() {
        instance = this;
        this.timersFile = new ConfigFile("timers.yml");

        this.executor = new ScheduledThreadPoolExecutor(1, Tasks.newThreadFactory("Timer Thread - %d"));
        this.executor.setRemoveOnCancelPolicy(true);

        this.scoreboardTimers = new ArrayList<>();

        this.scoreboardTimers.add(this.saleTimer = new SaleTimer(this.executor));
        this.scoreboardTimers.add(this.keySaleTimer = new KeySaleTimer(this.executor));
        this.scoreboardTimers.add(this.sotwTimer = new SotwTimer(this.executor));
        this.scoreboardTimers.add(this.eotwTimer = new EotwTimer(this.executor));
        this.scoreboardTimers.add(this.pvpProtTimer = new PvpProtTimer(this.executor));
        this.scoreboardTimers.add(this.combatTagTimer = new CombatTagTimer(this.executor));
        this.scoreboardTimers.add(this.enderPearlTimer = new EnderPearlTimer(this.executor));
        this.scoreboardTimers.add(this.archerTagTimer = new ArcherTagTimer(this.executor));
        this.scoreboardTimers.add(this.homeTimer = new HomeTimer(this.executor));
        this.scoreboardTimers.add(this.stuckTimer = new StuckTimer(this.executor));
        this.scoreboardTimers.add(this.teleportTimer = new TeleportTimer(this.executor));
        this.scoreboardTimers.add(this.logoutTimer = new LogoutTimer(this.executor));

        this.globalAbilitiesTimer = new GlobalAbilitiesTimer(this.executor);
        this.abilitiesTimer = new AbilitiesTimer(this.executor);

        this.pvpClassWarmupTimer = new PvpClassWarmupTimer(this.executor);
        this.appleTimer = new AppleTimer(this.executor);
        this.gAppleTimer = new GAppleTimer(this.executor);

        if(Config.ENCHANTED_GOLDEN_APPLE_ON_SCOREBOARD) this.scoreboardTimers.add(this.gAppleTimer);
        if(Config.NORMAL_GOLDEN_APPLE_ON_SCOREBOARD) this.scoreboardTimers.add(this.appleTimer);

        this.factionFreezeTimer = new FactionFreezeTimer(this.executor);
        this.cooldownTimer = new CooldownTimer(this.executor);
        this.rankReviveTimer = new RankReviveTimer(this.executor);
        this.customTimer = new CustomTimer(this.executor);
    }

    public void disable() {
        this.archerTagTimer.disable();
        this.combatTagTimer.disable();
        this.enderPearlTimer.disable();
        this.homeTimer.disable();
        this.logoutTimer.disable();
        this.pvpProtTimer.disable();
        this.saleTimer.disable();
        this.keySaleTimer.disable();
        this.sotwTimer.disable();
        this.eotwTimer.disable();
        this.stuckTimer.disable();
        this.appleTimer.disable();
        this.gAppleTimer.disable();
        this.pvpClassWarmupTimer.disable();

        this.factionFreezeTimer.disable();
        this.cooldownTimer.disable();
        this.rankReviveTimer.disable();
        this.customTimer.disable();

        this.scoreboardTimers.clear();
        this.timersFile.save();

        this.executor.shutdownNow();
    }

    public void deleteAllTimers() {
        this.scoreboardTimers.forEach(timer -> ((Timer) timer).disable());

        this.cooldownTimer.disable();
        this.rankReviveTimer.disable();
        this.customTimer.disable();

        this.timersFile.getFile().delete();
        this.timersFile = new ConfigFile("timers.yml");

        int deleted = this.scoreboardTimers.size() + 3;
        Lazarus.getInstance().log("- &cCleared &e" + deleted + " &ccooldown timers.");
    }
}
