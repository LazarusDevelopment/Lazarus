package me.qiooip.lazarus.factions.event;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;

@Getter
public class FactionDtrChangeEvent extends FactionEvent implements Cancellable {

    private final PlayerFaction faction;
    private final double oldDtr;
    private final double newDtr;
    @Setter private boolean cancelled;

    public FactionDtrChangeEvent(PlayerFaction faction, double oldDtr, double newDtr) {
        this.faction = faction;
        this.oldDtr = oldDtr;
        this.newDtr = newDtr;

        Bukkit.getPluginManager().callEvent(this);
    }
}
