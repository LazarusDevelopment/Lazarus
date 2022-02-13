package me.qiooip.lazarus.games.koth;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.SystemFaction;
import me.qiooip.lazarus.factions.type.SystemType;
import me.qiooip.lazarus.games.Cuboid;
import me.qiooip.lazarus.games.Placeholder;
import me.qiooip.lazarus.games.koth.event.KothStartEvent;
import me.qiooip.lazarus.games.koth.event.KothStopEvent;
import me.qiooip.lazarus.games.koth.listener.KothEventListeners;
import me.qiooip.lazarus.games.loot.LootData;
import me.qiooip.lazarus.utils.FileUtils;
import me.qiooip.lazarus.utils.GsonUtils;
import me.qiooip.lazarus.utils.ManagerEnabler;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

@Getter
public class KothManager implements Listener, ManagerEnabler {

    private final File kothsFile;

    private List<KothData> koths;
    private final List<RunningKoth> runningKoths;

    private BukkitTask tickTask;

    public KothManager() {
        this.kothsFile = FileUtils.getOrCreateFile(Config.GAMES_DIR, "koths.json");

        this.loadKoths();
        this.runningKoths = new ArrayList<>();

        new KothEventListeners();
    }

    public void disable() {
        this.saveKoths();

        this.koths.clear();
        this.runningKoths.clear();

        if(this.tickTask != null) {
            this.cancelTickTask();
        }
    }

    private void loadKoths() {
        String content = FileUtils.readWholeFile(this.kothsFile);

        if(content == null) {
            this.koths = new ArrayList<>();
            return;
        }

        this.koths = Lazarus.getInstance().getGson().fromJson(content, GsonUtils.KOTH_TYPE);

        Iterator<KothData> iterator = this.koths.iterator();

        while(iterator.hasNext()) {
            KothData koth = iterator.next();

            if(koth.getFaction() == null) {
                iterator.remove();
                continue;
            }

            koth.setupKothColor();

            if(koth.getFactionPoints() == 0) {
                koth.setFactionPoints(Config.FACTION_TOP_KOTH_CAP);
            }
        }

        FactionsManager.getInstance().removeKothFactionsWithoutKoths(this.koths);
    }

    private void saveKoths() {
        if(this.koths == null) return;

        FileUtils.writeString(this.kothsFile, Lazarus.getInstance().getGson()
            .toJson(this.koths, GsonUtils.KOTH_TYPE));
    }

    public KothData getKoth(String name) {
        return this.koths.stream().filter(koth ->
            koth.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public RunningKoth getRunningKoth(String name) {
        return this.runningKoths.stream().filter(koth ->
            koth.getKothData().getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public RunningKoth getFirstRunningKoth() {
        return this.runningKoths.isEmpty() ? null : this.runningKoths.get(0);
    }

    public void createKoth(CommandSender sender, String name, int captime, Cuboid capzone) {
        SystemFaction faction = FactionsManager.getInstance().createSystemFaction(name, SystemType.KOTH, sender);
        LootData loot = Lazarus.getInstance().getLootManager().createLoot(name);

        int factionPoints = Config.FACTION_TOP_KOTH_CAP;
        this.koths.add(new KothData(name, faction.getId(), captime, factionPoints, capzone, faction.getColor(), loot));
    }

    public void deleteKoth(CommandSender sender, KothData koth) {
        FactionsManager.getInstance().disbandFaction(koth.getFactionId(), sender);

        Lazarus.getInstance().getLootManager().removeLoot(koth.getName());
        Lazarus.getInstance().getScheduleManager().removeScheduleByName(koth.getName());

        this.koths.remove(koth);

        RunningKoth runningKoth = this.getRunningKoth(koth.getName());
        if(runningKoth == null) return;

        this.stopKoth(runningKoth);
    }

    public void startKoth(KothData koth) {
        this.startKoth(koth, koth.getCaptime());
    }

    public void startKoth(KothData koth, int time) {
        if(this.tickTask == null) {
            Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
            this.startTickTask();
        }

        RunningKoth runningKoth = new RunningKoth(koth, time);
        this.runningKoths.add(runningKoth);

        new KothStartEvent(runningKoth);

        Language.KOTH_START_STARTED.forEach(line -> Messages.sendMessage(Placeholder
            .RunningKothReplacer.parse(runningKoth, line)));
    }

    public void stopKoth(RunningKoth runningKoth) {
        this.runningKoths.remove(runningKoth);
        new KothStopEvent(runningKoth.getKothData());

        if(this.runningKoths.isEmpty()) {
            HandlerList.unregisterAll(this);
            this.cancelTickTask();
        }
    }

    public void stopAllKoths() {
        this.runningKoths.clear();

        HandlerList.unregisterAll(this);
        this.cancelTickTask();
    }

    private void startTickTask() {
        this.tickTask = Tasks.asyncTimer(() -> this.runningKoths.forEach(RunningKoth::tick), 0L, 20L);
    }

    public boolean isMaxRunningKothsReached() {
        return this.runningKoths.size() >= Config.KOTH_MAX_RUNNING_KOTHS_AT_THE_SAME_TIME;
    }

    private void cancelTickTask() {
        if(this.tickTask == null) return;

        this.tickTask.cancel();
        this.tickTask = null;
    }

    public void listKoths(CommandSender sender) {
        if(this.koths.isEmpty()) {
            sender.sendMessage(Language.KOTH_PREFIX + Language.KOTH_LIST_NO_KOTHS);
            return;
        }

        sender.sendMessage(Language.KOTH_COMMAND_HEADER);
        sender.sendMessage(Language.KOTH_LIST_COMMAND_TITLE);

        this.koths.stream().sorted(Comparator.comparing(KothData::getName)).forEach(koth -> sender
            .sendMessage(Placeholder.KothReplacer.parse(koth, Language.KOTH_LIST_COMMAND_FORMAT)));

        sender.sendMessage(Language.KOTH_COMMAND_FOOTER);
    }

    private void checkCapzone(Player player, Location from, Location to) {
        if(player.isDead()) return;

        this.runningKoths.forEach(runningKoth -> {
            boolean fromCapzone = runningKoth.getKothData().getCuboid().contains(from);
            boolean toCapzone = runningKoth.getKothData().getCuboid().contains(to);

            if(!fromCapzone && toCapzone) {
                runningKoth.onPlayerEnterCapzone(player);
            } else if(fromCapzone && !toCapzone) {
                runningKoth.onPlayerLeaveCapzone(player);
            }
        });
    }

    public void togglePlayerCapzone(Player player, boolean join) {
        if(this.runningKoths.isEmpty()) return;

        this.runningKoths.forEach(koth -> {
            if(!koth.getCapzone().getCuboid().contains(player.getLocation())) return;

            if(join && !player.isDead()) {
                koth.onPlayerEnterCapzone(player);
            } else {
                koth.onPlayerLeaveCapzone(player);
            }
        });
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();

        if(from.getBlockX() == to.getBlockX()
            && from.getBlockY() == to.getBlockY()
            && from.getBlockZ() == to.getBlockZ()) return;

        this.checkCapzone(event.getPlayer(), from, to);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        this.checkCapzone(event.getPlayer(), event.getFrom(), event.getTo());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        this.togglePlayerCapzone(event.getEntity(), false);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.togglePlayerCapzone(event.getPlayer(), true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.togglePlayerCapzone(event.getPlayer(), false);
    }
}
