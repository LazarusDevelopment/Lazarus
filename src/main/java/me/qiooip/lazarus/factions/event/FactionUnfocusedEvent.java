package me.qiooip.lazarus.factions.event;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;

import java.util.UUID;

@Getter
public class FactionUnfocusedEvent extends FactionEvent implements Cancellable {

    private final PlayerFaction faction;
    private final UUID focusedFactionUuid;
    @Setter private boolean cancelled;

    public FactionUnfocusedEvent(PlayerFaction faction, UUID focusedFactionUuid) {
        this.faction = faction;
        this.focusedFactionUuid = focusedFactionUuid;

        Bukkit.getPluginManager().callEvent(this);
    }
}
