package me.qiooip.lazarus.lunarclient.task;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.audience.Audience;
import com.lunarclient.apollo.common.ApolloColors;
import com.lunarclient.apollo.common.Component;
import com.lunarclient.apollo.module.team.TeamMember;
import com.lunarclient.apollo.module.team.TeamModule;
import com.lunarclient.apollo.player.ApolloPlayer;
import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.utils.ApolloUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TeamViewTask extends BukkitRunnable {

    private final TeamModule teamModule;

    public TeamViewTask() {
        this.teamModule = Apollo.getModuleManager().getModule(TeamModule.class);
        this.runTaskTimerAsynchronously(Lazarus.getInstance(), 0L, 5L);
    }

    private TeamMember createTeamMember(Player member) {
        Location location = member.getLocation();

        return TeamMember.builder()
            .playerUuid(member.getUniqueId())
            .displayName(Component.builder()
                .content(member.getName())
                .color(ApolloColors.GREEN)
                .build())
            .markerColor(ApolloColors.WHITE)
            .location(ApolloUtils.toApolloLocation(location))
            .build();
    }

    public void resetPlayerTeamView(UUID playerId) {
        ApolloUtils.runForPlayer(playerId, this.teamModule::resetTeamMembers);
    }

    private List<TeamMember> createTeamViewMembers(PlayerFaction faction) {
        List<TeamMember> members = new ArrayList<>();
        faction.getOnlinePlayers().forEach(member -> members.add(this.createTeamMember(member)));
        return members;
    }

    private void sendTeamViewUpdate(PlayerFaction faction, List<TeamMember> members) {
        Audience factionPlayers = ApolloUtils.getAudienceFrom(faction.getOnlinePlayers());
        this.teamModule.updateTeamMembers(factionPlayers, members);
    }

    private void updateTeamViewMembers() {
        FactionsManager factionsManager = FactionsManager.getInstance();
        Map<PlayerFaction, List<TeamMember>> factions = new HashMap<>();

        for(ApolloPlayer player : Apollo.getPlayerManager().getPlayers()) {
            PlayerFaction faction = factionsManager.getPlayerFaction(player.getUniqueId());

            if(faction != null && !factions.containsKey(faction)) {
                factions.put(faction, this.createTeamViewMembers(faction));
            }
        }

        factions.forEach((faction, members) -> this.sendTeamViewUpdate(faction, members));
    }

    @Override
    public void run() {
        try {
            this.updateTeamViewMembers();
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
