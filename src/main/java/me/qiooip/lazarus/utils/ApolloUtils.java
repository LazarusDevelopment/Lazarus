package me.qiooip.lazarus.utils;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.common.Component;
import com.lunarclient.apollo.common.location.ApolloBlockLocation;
import com.lunarclient.apollo.common.location.ApolloLocation;
import com.lunarclient.apollo.player.ApolloPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Consumer;

public class ApolloUtils {

    public static void runForPlayer(Player player, Consumer<ApolloPlayer> playerConsumer) {
        runForPlayer(player.getUniqueId(), playerConsumer);
    }

    public static void runForPlayer(UUID playerId, Consumer<ApolloPlayer> playerConsumer) {
        Apollo.getPlayerManager().getPlayer(playerId).ifPresent(playerConsumer);
    }

    public static Component textComponent(String content) {
        return Component.builder().content(content).build();
    }

    public static ApolloLocation toApolloLocation(Location location) {
        return ApolloLocation.builder()
            .world(location.getWorld().getName())
            .x(location.getBlockX())
            .y(location.getBlockY())
            .z(location.getBlockZ())
            .build();
    }

    public static ApolloBlockLocation toApolloBlockLocation(Location location) {
        return ApolloBlockLocation.builder()
            .world(location.getWorld().getName())
            .x(location.getBlockX())
            .y(location.getBlockY())
            .z(location.getBlockZ())
            .build();
    }
}
