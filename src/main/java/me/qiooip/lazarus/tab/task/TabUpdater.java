package me.qiooip.lazarus.tab.task;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.factions.FactionPlayer;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.tab.PlayerTab;
import me.qiooip.lazarus.tab.TabManager;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

public class TabUpdater implements Runnable {

    private static final String[] FACES = { "S", "SW", "W", "NW", "N", "NE", "E", "SE" };
    private static final Comparator<FactionPlayer> COMPARATOR = (m1, m2) -> m2.getRole().ordinal() - m1.getRole().ordinal();

    private final TabManager manager;

    private final Map<Integer, Function<Player, String>> updates;
    private final Map<Integer, String> initialSet;

    private ScheduledThreadPoolExecutor executor;
    private ScheduledFuture<?> updater;

    private int counter;

    private int locationSlot;
    private Function<Player, String> locationFunction;

    private boolean playerListEnabled;
    private int playerListStart, numberOfPlayers;
    private String factionColor, playerColor;

    private boolean factionInfoEnabled;
    private int factionInfoStart;
    private String[][] factionInfo;

    public TabUpdater(TabManager manager) {
        this.manager = manager;

        this.updates = new HashMap<>();
        this.initialSet = new HashMap<>();

        this.loadUpdates();
        this.loadInitialSet();
        this.loadPlayerListModule();
        this.loadFactionInfoModule();

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
    public void run() {
        try {
            for(Player player : Bukkit.getOnlinePlayers()) {
                PlayerTab tab = this.manager.getTab(player);
                if(tab == null) continue;

                if(this.counter++ % 5 == 0) {
                    this.updateFactionInfo(tab, FactionsManager.getInstance().getPlayerFaction(player));

                    for(Entry<Integer, Function<Player, String>> entry : this.updates.entrySet()) {
                        tab.set(entry.getKey(), entry.getValue().apply(player));
                    }
                }

                tab.set(this.locationSlot, this.locationFunction.apply(player));
            }
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    public void updateFactionPlayerList(PlayerTab tab, PlayerFaction faction) {
        if(!this.playerListEnabled) return;

        this.clearFactionPlayerList(tab);

        tab.set(this.playerListStart, this.factionColor + faction.getName());
        AtomicInteger count = new AtomicInteger(this.playerListStart + 1);

        faction.getMembers().values().stream()
            .filter(member -> member.getPlayer() != null)
            .sorted(COMPARATOR).limit(this.numberOfPlayers)
        .forEach(member ->
            tab.set(count.getAndIncrement(), this.playerColor + member.getRole().getPrefix() + member.getName())
        );
    }

    public void clearFactionPlayerList(PlayerTab tab) {
        if(!this.playerListEnabled || tab == null) return;

        for(int i = this.playerListStart; i <= this.playerListStart + this.numberOfPlayers + 1; i++) {
            tab.set(i, "");
        }
    }

    private void updateFactionInfo(PlayerTab tab, PlayerFaction faction) {
        if(!this.factionInfoEnabled) return;

        if(faction == null) {
            tab.set(this.factionInfoStart, this.factionInfo[0][0]);
            tab.set(this.factionInfoStart + 1, this.factionInfo[0][1]);
            return;
        }

        tab.set(this.factionInfoStart, this.factionInfo[1][0] + faction.getDtrString() + "&7/" + faction.getMaxDtrString());
        tab.set(this.factionInfoStart + 1, this.factionInfo[1][1] + (faction.getHome() == null ? "None" : faction.getHomeString()));
    }

    public void initialSet(Player player, PlayerTab tab) {
        this.initialSet.forEach((slot, value) -> tab.set(slot, value));
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

        Stream.of("LEFT", "CENTER", "RIGHT", "FAR_RIGHT").forEach(sides -> tabFile.getStringList(sides).forEach(line -> {
            int slot = count.getAndIncrement();

            if(line.toLowerCase().contains("<location>")) {
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
        String lowerCase = line.toLowerCase();

        if(lowerCase.contains("<kills>")) {
            String temp = line.replace("<kills>", "");
            return player -> temp + Lazarus.getInstance().getUserdataManager().getUserdata(player).getKills();
        } else if(lowerCase.contains("<deaths>")) {
            String temp = line.replace("<deaths>", "");
            return player -> temp + Lazarus.getInstance().getUserdataManager().getUserdata(player).getDeaths();
        } else if(lowerCase.contains("<lives>")) {
            String temp = line.replace("<lives>", "");
            return player -> temp + Lazarus.getInstance().getUserdataManager().getUserdata(player).getLives();
        } else if(lowerCase.contains("<balance>")) {
            String temp = line.replace("<balance>", "");
            return player -> temp + Lazarus.getInstance().getUserdataManager().getUserdata(player).getBalance();
        } else if(lowerCase.contains("<faction>")) {
            String temp = line.replace("<faction>", "");
            return player -> temp + ClaimManager.getInstance().getFactionAt(player).getDisplayName(player);
        } else if(lowerCase.contains("<online>")) {
            String temp = line.replace("<online>", "");
            return player -> temp + (Math.max(Bukkit.getOnlinePlayers().size() - Lazarus.getInstance()
                .getVanishManager().vanishedAmount(), 0)) + "/" + Bukkit.getMaxPlayers();
        } else {
            return null;
        }
    }

    private void loadPlayerListModule() {
        ConfigFile tabFile = Lazarus.getInstance().getTabFile();
        this.playerListEnabled = tabFile.getBoolean("PLAYER_LIST_MODULE.ENABLED");

        this.playerListStart = tabFile.getInt("PLAYER_LIST_MODULE.START_SLOT");
        this.numberOfPlayers = tabFile.getInt("PLAYER_LIST_MODULE.NUMBER_OF_PLAYERS");

        this.factionColor = tabFile.getString("PLAYER_LIST_MODULE.FACTION_NAME_COLOR");
        this.playerColor = tabFile.getString("PLAYER_LIST_MODULE.PLAYER_COLOR");
    }

    private void loadFactionInfoModule() {
        ConfigFile tabFile = Lazarus.getInstance().getTabFile();
        this.factionInfoEnabled = tabFile.getBoolean("FACTION_INFO_MODULE.ENABLED");

        this.factionInfoStart = tabFile.getInt("FACTION_INFO_MODULE.START_SLOT");
        this.factionInfo = new String[2][2];

        this.factionInfo[0] = tabFile.getStringList("FACTION_INFO_MODULE.NO_FACTION").toArray(new String[2]);
        this.factionInfo[1] = tabFile.getStringList("FACTION_INFO_MODULE.IN_FACTION").toArray(new String[2]);

        for(int i = 0; i < 2; i++) {
            this.factionInfo[1][i] = this.factionInfo[1][i].replace("<dtr>", "").replace("<home>", "");
        }
    }

    private String getDirection(Player player) {
        return FACES[Math.round(player.getLocation().getYaw() / 45f) & 0x7];
    }
}
