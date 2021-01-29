package me.qiooip.lazarus.factions.event;

import lombok.Getter;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.Bukkit;

@Getter
public class FactionDtrChangeEvent extends FactionEvent {

    private final PlayerFaction faction;
    private final double oldDtr;
    private final double newDtr;

    public FactionDtrChangeEvent(PlayerFaction faction, double oldDtr, double newDtr) {
        super(true);

        this.faction = faction;
        this.oldDtr = oldDtr;
        this.newDtr = newDtr;

        Bukkit.getPluginManager().callEvent(this);
    }
}
