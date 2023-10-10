package me.qiooip.lazarus.lunarclient.task;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.BukkitApollo;
import com.lunarclient.apollo.common.ApolloColors;
import com.lunarclient.apollo.common.Component;
import com.lunarclient.apollo.module.team.TeamMember;
import com.lunarclient.apollo.module.team.TeamModule;
import com.lunarclient.apollo.player.ApolloPlayer;
import com.lunarclient.apollo.recipients.Recipients;
import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
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
            .location(BukkitApollo.toApolloLocation(location))
            .build();
    }

    public void resetPlayerTeamView(UUID playerId) {
        BukkitApollo.runForPlayer(playerId, this.teamModule::resetTeamMembers);
    }

    private List<TeamMember> createTeamViewMembers(PlayerFaction faction) {
        List<TeamMember> members = new ArrayList<>();
        faction.getOnlinePlayers().forEach(member -> members.add(this.createTeamMember(member)));
        return members;
    }

    private void sendTeamViewUpdate(PlayerFaction faction, List<TeamMember> members) {
        Recipients factionPlayers = BukkitApollo.getRecipientsFrom(faction.getOnlinePlayers());
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

        factions.forEach(this::sendTeamViewUpdate);
    }

    @Override
    public void run() {
        try {
            this.updateTeamViewMembers();
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }
}
