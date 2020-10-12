package me.qiooip.lazarus.factions.event;

import lombok.Getter;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

@Getter
public class FactionRallyEvent extends FactionEvent {

    private final UUID uuid;
    private final Location location;
    private final PlayerFaction faction;
    private final RallyEventType type;

    public FactionRallyEvent(UUID uuid, Location location, PlayerFaction faction, RallyEventType type) {
        this.uuid = uuid;
        this.location = location;
        this.faction = faction;
        this.type = type;

        Bukkit.getPluginManager().callEvent(this);
    }

    public enum RallyEventType {
        ADD, REMOVE
    }
}
