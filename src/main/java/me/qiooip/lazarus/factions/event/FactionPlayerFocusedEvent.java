package me.qiooip.lazarus.factions.event;

import lombok.Getter;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.Bukkit;

import java.util.UUID;

@Getter
public class FactionPlayerFocusedEvent extends FactionEvent {

    private final PlayerFaction faction;
    private final UUID target;

    public FactionPlayerFocusedEvent(PlayerFaction faction, UUID target) {
        this.faction = faction;
        this.target = target;

        Bukkit.getPluginManager().callEvent(this);
    }
}
