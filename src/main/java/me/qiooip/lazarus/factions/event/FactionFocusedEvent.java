package me.qiooip.lazarus.factions.event;

import lombok.Getter;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.Bukkit;

import java.util.UUID;

@Getter
public class FactionFocusedEvent extends FactionEvent {

    private final PlayerFaction faction;
    private final UUID focusedFactionUuid;

    public FactionFocusedEvent(PlayerFaction faction, UUID focusedFactionUuid) {
        this.faction = faction;
        this.focusedFactionUuid = focusedFactionUuid;

        Bukkit.getPluginManager().callEvent(this);
    }
}
