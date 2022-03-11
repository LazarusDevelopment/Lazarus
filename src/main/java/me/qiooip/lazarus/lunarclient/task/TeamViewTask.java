package me.qiooip.lazarus.lunarclient.task;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.nethandler.LCPacket;
import com.lunarclient.bukkitapi.nethandler.client.LCPacketTeammates;
import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.lunarclient.LunarClientManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeamViewTask extends BukkitRunnable {

    public TeamViewTask() {
        this.runTaskTimerAsynchronously(Lazarus.getInstance(), 0L, 20L);
    }

    private Map<String, Double> positionMap(Location location) {
        Map<String, Double> position = new HashMap<>();

        position.put("x", location.getX());
        position.put("y", location.getY());
        position.put("z", location.getZ());

        return position;
    }

    private Map<Environment, LCPacket> createTeammatePackets(PlayerFaction faction) {
        Map<Environment, PositionMap> positions = new HashMap<>();

        faction.getOnlinePlayers().forEach(member -> {
            Environment env = member.getWorld().getEnvironment();
            UUID uuid = member.getUniqueId();

            positions.computeIfAbsent(env, t -> new PositionMap())
                .addPosition(uuid, this.positionMap(member.getLocation()));
        });

        Map<Environment, LCPacket> packets = new HashMap<>();

        positions.forEach((env, positionMap) -> packets.put(env,
            new LCPacketTeammates(null, 2000L, positionMap.getPositions())));

        return packets;
    }

    private void sendPerWorldPackets(PlayerFaction faction, Map<Environment, LCPacket> packets) {
        faction.getOnlinePlayers().forEach(member -> {
            LCPacket packet = packets.get(member.getWorld().getEnvironment());
            LunarClientAPI.getInstance().sendPacket(member, packet);
        });
    }

    private void sendTeamViewPackets() {
        LunarClientManager lcManager = Lazarus.getInstance().getLunarClientManager();
        FactionsManager factionsManager = FactionsManager.getInstance();

        Map<PlayerFaction, Map<Environment, LCPacket>> factions = new HashMap<>();

        for(Player player : Bukkit.getOnlinePlayers()) {
            if(!lcManager.isOnLunarClient(player)) continue;

            PlayerFaction faction = factionsManager.getPlayerFaction(player);

            if(faction != null && !factions.containsKey(faction)) {
                factions.put(faction, this.createTeammatePackets(faction));
            }
        }

        factions.forEach((faction, packets) -> this.sendPerWorldPackets(faction, packets));
    }

    @Override
    public void run() {
        try {
            this.sendTeamViewPackets();
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    @Getter
    private static class PositionMap {

        private final Map<UUID, Map<String, Double>> positions;

        public PositionMap() {
            this.positions = new HashMap<>();
        }

        public void addPosition(UUID uuid, Map<String, Double> position) {
            this.positions.put(uuid, position);
        }
    }
}
