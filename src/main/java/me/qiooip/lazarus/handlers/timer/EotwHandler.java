package me.qiooip.lazarus.handlers.timer;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.event.FactionCreateEvent;
import me.qiooip.lazarus.factions.event.FactionCreateEvent.FactionType;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.factions.type.SystemFaction;
import me.qiooip.lazarus.games.koth.KothData;
import me.qiooip.lazarus.games.koth.RunningKoth;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.scoreboard.PvpProtTimer;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EotwHandler extends Handler implements Listener {

    private boolean running;

    public boolean isActive() {
        return this.running;
    }

    private boolean isPreTaskOrActive() {
        return this.running || TimerManager.getInstance().getEotwTimer().isActive();
    }

    public void startPreEotwTask(CommandSender sender, int delay) {
        if(this.isPreTaskOrActive()) {
            sender.sendMessage(Language.PREFIX + Language.EOTW_ALREADY_RUNNING);
            return;
        }

        KothData koth = Lazarus.getInstance().getKothManager().getKoth("EOTW");

        if(koth == null) {
            sender.sendMessage(Language.PREFIX + Language.EOTW_NO_EOTW_KOTH);
            return;
        }

        TimerManager.getInstance().getEotwTimer().activate(sender, delay);
        Messages.sendMessage(Language.EOTW_TIMER_STARTED.replace("<seconds>", String.valueOf(delay)));
    }

    public void startEotw(int delay) {
        KothData koth = Lazarus.getInstance().getKothManager().getKoth("EOTW");

        if(koth == null) {
            Messages.sendMessage(Language.PREFIX + Language.EOTW_NO_EOTW_KOTH, "lazarus.staff");
            return;
        }

        this.running = true;

        Lazarus.getInstance().getKothManager().startKoth(koth);
        FactionsManager.getInstance().setAllRaidable(true);

        SystemFaction spawnFaction = (SystemFaction) FactionsManager.getInstance().getFactionByName("Spawn");
        spawnFaction.setSafezone(false);
        spawnFaction.setDeathban(true);

        PvpProtTimer timer = TimerManager.getInstance().getPvpProtTimer();

        Bukkit.getOnlinePlayers().forEach(player -> {
            if(!timer.isActive(player)) return;

            timer.cancel(player);
            player.sendMessage(Language.PREFIX + Language.EOTW_PVP_TIMER_DISABLED);
        });

        if(Config.EOTW_CLEAR_DEATHBANS_ON_START) {
            Lazarus.getInstance().getDeathbanManager().deleteAllDeathbans(false);
        }

        TimerManager.getInstance().getDtrRegenTimer().cancel();
        Messages.sendMessage(Language.EOTW_BROADCAST_START);
    }

    public void stopEotw(CommandSender sender) {
        if(!this.isPreTaskOrActive()) {
            sender.sendMessage(Language.PREFIX + Language.EOTW_NOT_RUNNING);
            return;
        }

        this.running = false;

        RunningKoth koth = Lazarus.getInstance().getKothManager().getRunningKoth("EOTW");

        if(koth != null) {
            Lazarus.getInstance().getKothManager().stopKoth(koth);
        }

        SystemFaction spawnFaction = (SystemFaction) FactionsManager.getInstance().getFactionByName("Spawn");
        spawnFaction.setSafezone(true);
        spawnFaction.setDeathban(false);

        FactionsManager.getInstance().setAllRaidable(false);

        TimerManager.getInstance().getEotwTimer().cancel();
        TimerManager.getInstance().getDtrRegenTimer().startRegenTask();

        Messages.sendMessage(Language.EOTW_BROADCAST_STOP);
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionCreate(FactionCreateEvent event) {
        if(!this.isActive() || event.getFactionType() != FactionType.PLAYER_FACTION) return;

        Tasks.sync(() -> {
            PlayerFaction faction = FactionsManager.getInstance().getPlayerFactionByName(event.getFactionName());
            if(faction == null) return;

            faction.setDtr(Config.FACTION_MIN_DTR);
        });
    }
}
