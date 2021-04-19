package me.qiooip.lazarus.factions.event;

import lombok.Getter;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.Bukkit;

import java.util.UUID;

@Getter
public class PlayerFocusedEvent extends FactionEvent {

    private final PlayerFaction faction;
    private final UUID targetPlayer;

    public PlayerFocusedEvent(PlayerFaction faction, UUID targetPlayer) {
        this.faction = faction;
        this.targetPlayer = targetPlayer;

        Bukkit.getPluginManager().callEvent(this);
    }
}
