package me.qiooip.lazarus.factions.event;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;

import java.util.UUID;

@Getter
public class PlayerUnfocusedEvent extends FactionEvent implements Cancellable {

    private final PlayerFaction faction;
    private final UUID targetPlayer;
    @Setter private boolean cancelled;

    public PlayerUnfocusedEvent(PlayerFaction faction, UUID targetPlayer) {
        this.faction = faction;
        this.targetPlayer = targetPlayer;

        Bukkit.getPluginManager().callEvent(this);
    }
}
