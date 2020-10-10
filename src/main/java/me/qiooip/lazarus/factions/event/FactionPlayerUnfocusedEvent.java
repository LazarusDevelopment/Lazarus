package me.qiooip.lazarus.factions.event;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;

import java.util.UUID;

@Getter
public class FactionPlayerUnfocusedEvent extends FactionEvent implements Cancellable {

    private final PlayerFaction faction;
    private final UUID target;
    @Setter private boolean cancelled;

    public FactionPlayerUnfocusedEvent(PlayerFaction faction, UUID target) {
        this.faction = faction;
        this.target = target;

        Bukkit.getPluginManager().callEvent(this);
    }
}
