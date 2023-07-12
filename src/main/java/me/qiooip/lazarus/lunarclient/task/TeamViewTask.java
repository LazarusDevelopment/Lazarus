package me.qiooip.lazarus.lunarclient.task;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.team.TeamModule;
import com.lunarclient.apollo.player.ApolloPlayer;
import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.utils.ApolloUtils;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeamViewTask extends BukkitRunnable {

    private final TeamModule teamModule;

    public TeamViewTask() {
        this.teamModule = Apollo.getModuleManager().getModule(TeamModule.class);
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
            ApolloUtils.runForPlayer(member, ap -> this.teamModule.updateTeamMembers(ap, null));
        });
    }

    private void sendTeamViewPackets() {
        FactionsManager factionsManager = FactionsManager.getInstance();

        Map<PlayerFaction, Map<Environment, LCPacket>> factions = new HashMap<>();

        for(ApolloPlayer player : Apollo.getPlayerManager().getPlayers()) {
            PlayerFaction faction = factionsManager.getPlayerFaction(player.getUniqueId());

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
