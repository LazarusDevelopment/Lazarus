package me.qiooip.lazarus.lunarclient.task;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.nethandler.client.LCPacketTeammates;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.factions.FactionPlayer;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeamViewTask extends BukkitRunnable {

    public TeamViewTask() {
        this.runTaskTimerAsynchronously(Lazarus.getInstance(), 0L, 30L);
    }

    private Map<String, Double> positionMap(Location location) {
        Map<String, Double> position = new HashMap<>();

        position.put("x", location.getX());
        position.put("y", location.getY());
        position.put("z", location.getZ());

        return position;
    }

    private void sendTeamViewPackets() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(!Lazarus.getInstance().getLunarClientManager().isOnLunarClient(player)) continue;

            PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player);
            if(faction == null) continue;

            Map<UUID, Map<String, Double>> positions = new HashMap<>();

            faction.getOnlinePlayers().forEach(member -> {
                UUID uuid = member.getUniqueId();
                positions.put(uuid, this.positionMap(member.getLocation()));
            });

            FactionPlayer leader = faction.getLeader();
            UUID leaderUuid = leader.getPlayer() != null ? leader.getUuid() : null;

            LunarClientAPI.getInstance().sendPacket(player, new LCPacketTeammates(leaderUuid, 2000L, positions));
        }
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
