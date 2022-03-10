package me.qiooip.lazarus.lunarclient.task;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.nethandler.client.LCPacketTeammates;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.factions.FactionPlayer;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.lunarclient.LunarClientManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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

    private LCPacketTeammates createTeammatePacket(PlayerFaction faction) {
        Map<UUID, Map<String, Double>> positions = new HashMap<>();

        faction.getOnlinePlayers().forEach(member -> {
            UUID uuid = member.getUniqueId();
            positions.put(uuid, this.positionMap(member.getLocation()));
        });

        FactionPlayer leader = faction.getLeader();
        UUID leaderUuid = leader.getPlayer() != null ? leader.getUuid() : null;

        return new LCPacketTeammates(leaderUuid, 2000L, positions);
    }

    private void sendTeamViewPackets() {
        LunarClientManager lcManager = Lazarus.getInstance().getLunarClientManager();
        FactionsManager factionsManager = FactionsManager.getInstance();

        Map<PlayerFaction, LCPacketTeammates> packets = new HashMap<>();

        for(Player player : Bukkit.getOnlinePlayers()) {
            if(!lcManager.isOnLunarClient(player)) continue;

            PlayerFaction faction = factionsManager.getPlayerFaction(player);

            if(faction != null && !packets.containsKey(faction)) {
                packets.put(faction, this.createTeammatePacket(faction));
            }
        }

        packets.forEach((faction, packet) -> faction.getOnlinePlayers().forEach(member
            -> LunarClientAPI.getInstance().sendPacket(member, packet)));
    }

    @Override
    public void run() {
        try {
            this.sendTeamViewPackets();
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }
}
