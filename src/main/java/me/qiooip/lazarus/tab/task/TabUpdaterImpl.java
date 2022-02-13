package me.qiooip.lazarus.tab.task;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.tab.PlayerTab;
import me.qiooip.lazarus.tab.TabManager;
import me.qiooip.lazarus.tab.module.TabModule;
import me.qiooip.lazarus.tab.module.impl.FactionInfoModule;
import me.qiooip.lazarus.tab.module.impl.NextKothModule;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

public class TabUpdaterImpl implements TabUpdater {

    private static final String[] FACES = { "S", "SW", "W", "NW", "N", "NE", "E", "SE" };

    private final TabManager manager;
    private final Map<Integer, Function<Player, String>> updates;
    private final Map<Integer, String> initialSet;
    private final AtomicInteger counter;

    private ScheduledThreadPoolExecutor executor;
    private ScheduledFuture<?> updater;

    private final List<TabModule> tabModules;
    private int locationSlot;
    private Function<Player, String> locationFunction;

    public TabUpdaterImpl(TabManager manager) {
        this.manager = manager;

        this.updates = new HashMap<>();
        this.initialSet = new HashMap<>();
        this.counter = new AtomicInteger();
        this.tabModules = new ArrayList<>();

        this.loadUpdates();
        this.loadInitialSet();

        this.registerTabModule(new FactionInfoModule());
        this.registerTabModule(new NextKothModule());

        Tasks.syncLater(this::setupTasks, 10L);
    }

    private void setupTasks() {
        this.executor = new ScheduledThreadPoolExecutor(1, Tasks.newThreadFactory("Tab Thread - %d"));
        this.executor.setRemoveOnCancelPolicy(true);

        this.updater = this.executor.scheduleAtFixedRate(this, 0L, 200L, TimeUnit.MILLISECONDS);
    }

    public void cancel() {
        if(this.updater != null) this.updater.cancel(true);
        if(this.executor != null) this.executor.shutdownNow();

        this.updates.clear();
        this.initialSet.clear();
    }

    @Override
    public void registerTabModule(TabModule tabModule) {
        this.tabModules.add(tabModule);
    }

    @Override
    public void run() {
        try {
            for(Player player : Bukkit.getOnlinePlayers()) {
                PlayerTab tab = this.manager.getTab(player);
                if(tab == null) continue;

                if(this.counter.getAndIncrement() % 5 == 0) {
                    for(TabModule module : this.tabModules) {
                        if(module.isEnabled()) module.apply(tab);
                    }

                    for(Entry<Integer, Function<Player, String>> entry : this.updates.entrySet()) {
                        tab.set(entry.getKey(), entry.getValue().apply(player));
                    }
                }

                if(this.locationFunction != null) {
                    tab.set(this.locationSlot, this.locationFunction.apply(player));
                }
            }
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    public void initialSet(PlayerTab tab) {
        this.initialSet.forEach(tab::set);
    }

    private void loadInitialSet() {
        ConfigFile tabFile = Lazarus.getInstance().getTabFile();
        AtomicInteger count = new AtomicInteger(1);

        Stream.of("LEFT", "CENTER", "RIGHT", "FAR_RIGHT").forEach(sides -> tabFile.getStringList(sides).forEach(line -> {
            int slot = count.getAndIncrement();
            if(line.contains("<") && line.contains(">") || line.isEmpty()) return;

            this.initialSet.put(slot, line);
        }));
    }

    private void loadUpdates() {
        ConfigFile tabFile = Lazarus.getInstance().getTabFile();
        AtomicInteger count = new AtomicInteger(1);

        Stream.of("LEFT", "CENTER", "RIGHT", "FAR_RIGHT").forEach(column -> tabFile.getStringList(column).forEach(line -> {
            int slot = count.getAndIncrement();

            if(line.contains("<location>")) {
                String temp = line.replace("<location>", "");

                this.locationFunction = player -> temp + "(" + player.getLocation().getBlockX() + ", "
                    + player.getLocation().getBlockZ() + ") [" + this.getDirection(player) + "]";

                this.locationSlot = slot;
            } else {
                Function<Player, String> function = this.getFunction(line);
                if(function == null) return;

                this.updates.put(slot, function);
            }
        }));
    }

    private Function<Player, String> getFunction(String line) {
        if(line.contains("<kills>")) {
            String temp = line.replace("<kills>", "");
            return player -> temp + Lazarus.getInstance().getUserdataManager().getUserdata(player).getKills();
        } else if(line.contains("<deaths>")) {
            String temp = line.replace("<deaths>", "");
            return player -> temp + Lazarus.getInstance().getUserdataManager().getUserdata(player).getDeaths();
        } else if(line.contains("<lives>")) {
            String temp = line.replace("<lives>", "");
            return player -> temp + Lazarus.getInstance().getUserdataManager().getUserdata(player).getLives();
        } else if(line.contains("<balance>")) {
            String temp = line.replace("<balance>", "");
            return player -> temp + Lazarus.getInstance().getUserdataManager().getUserdata(player).getBalance();
        } else if(line.contains("<faction>")) {
            String temp = line.replace("<faction>", "");
            return player -> temp + ClaimManager.getInstance().getFactionAt(player).getDisplayName(player);
        } else if(line.contains("<online>")) {
            String temp = line.replace("<online>", "");
            return player -> temp + (Math.max(Bukkit.getOnlinePlayers().size() - Lazarus.getInstance()
                .getVanishManager().vanishedAmount(), 0)) + "/" + Bukkit.getMaxPlayers();
        } else {
            return null;
        }
    }

    private String getDirection(Player player) {
        return FACES[Math.round(player.getLocation().getYaw() / 45f) & 0x7];
    }
}
