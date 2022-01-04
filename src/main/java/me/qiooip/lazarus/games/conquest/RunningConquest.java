package me.qiooip.lazarus.games.conquest;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.games.Capzone;
import me.qiooip.lazarus.games.conquest.event.ConquestCappedEvent;
import me.qiooip.lazarus.games.loot.LootData;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class RunningConquest extends BukkitRunnable {

    private final ConquestData conquest;
    private final LootData loot;

    private final long startTime;

    private final Map<ZoneType, Capzone> capzones;
    private final Map<PlayerFaction, Integer> factionPoints;

    private final AtomicBoolean running;

    RunningConquest(ConquestData conquest, LootData loot, int time) {
        this.conquest = conquest;
        this.loot = loot;

        this.startTime = System.currentTimeMillis();
        this.capzones = new EnumMap<>(ZoneType.class);

        this.conquest.getCuboids().forEach((type, cuboid) -> {
            this.capzones.put(type, new Capzone(cuboid, time));
            cuboid.getPlayers().forEach(player -> this.onPlayerEnterCapzone(player, type));
        });

        this.factionPoints = Collections.synchronizedMap(new LinkedHashMap<>());
        this.running = new AtomicBoolean(true);

        this.runTaskTimerAsynchronously(Lazarus.getInstance(), 0L, 20L);
    }

    public String getTimeEntry(ZoneType type) {
        return type.getColor() + this.capzones.get(type).getTimeLeft();
    }

    private int getPoints(PlayerFaction faction) {
        return this.factionPoints.getOrDefault(faction, 0);
    }

    public int setPoints(PlayerFaction faction, int value) {
        value = Config.CONQUEST_ALLOW_NEGATIVE_POINTS ? value : Math.max(0, value);
        this.factionPoints.put(faction, value);

        List<Entry<PlayerFaction, Integer>> copy = new ArrayList<>(this.factionPoints.entrySet());
        this.factionPoints.clear();

        copy.stream().sorted(Entry.<PlayerFaction, Integer>comparingByValue().reversed())
            .forEach(entry -> this.factionPoints.put(entry.getKey(), entry.getValue()));

        return value;
    }

    private int addPoints(PlayerFaction faction, int amount) {
        return this.setPoints(faction, this.getPoints(faction) + amount);
    }

    void removePoints(PlayerFaction faction, int amount) {
        this.setPoints(faction, this.getPoints(faction) - amount);
    }

    private void handleWin(Player capper, PlayerFaction faction) {
        this.running.set(false);

        Language.CONQUEST_CAPPED.forEach(line -> Messages.sendMessage(line
            .replace("<faction>", faction.getName())
            .replace("<uptime>", StringUtils.formatMillis(System.currentTimeMillis() - this.startTime))));

        Tasks.sync(() -> {
            new ConquestCappedEvent(this.conquest, this.loot, capper);
            Lazarus.getInstance().getConquestManager().stopConquest(false);
        });
    }

    private void sendStartedCappingMessage(PlayerFaction faction, ZoneType type) {
        faction.sendMessage(Language.CONQUEST_PREFIX + Language
        .CONQUEST_STARTED_CAPPING.replace("<zone>", type.getName()));
    }

    void onPlayerEnterCapzone(Player player, ZoneType type) {
        if(Lazarus.getInstance().getStaffModeManager().isInStaffModeOrVanished(player)) return;

        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);
        if(faction == null) return;

        Capzone capzone = this.capzones.get(type);

        if(capzone.hasNoPlayers()) {
            this.sendStartedCappingMessage(faction, type);
        }

        capzone.addPlayer(player);
    }

    void onPlayerLeaveCapzone(Player player, ZoneType type) {
        if(FactionsManager.getInstance().getPlayerFaction(player) == null) return;

        Capzone capzone = this.capzones.get(type);
        if(!capzone.getPlayers().contains(player.getName())) return;

        if(capzone.isCapper(player)) {
            capzone.setTime(Config.CONQUEST_CAP_TIME);

            player.sendMessage(Language.CONQUEST_PREFIX + Language.CONQUEST_STOPPED_CAPPING
            .replace("<zone>", type.getName()));

            if(capzone.getPlayers().size() > 1) {
                this.sendStartedCappingMessage(FactionsManager.getInstance()
                .getPlayerFaction(capzone.getNextCapper()), type);
            }
        }

        capzone.removePlayer(player);
    }

    @Override
    public void run() {
        this.capzones.values().forEach(capzone -> {
            if(capzone.hasNoPlayers()) return;
            if(capzone.decreaseTime() > 0) return;

            capzone.setTime(Config.CONQUEST_CAP_TIME);

            Player capper = capzone.getCapper();
            PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(capper);

            int current = this.addPoints(faction, Config.CONQUEST_POINTS_PER_CAP);

            if(current >= Config.CONQUEST_POINTS_TO_WIN && this.running.get()) {
                this.handleWin(capper, faction);
            }
        });
    }
}
