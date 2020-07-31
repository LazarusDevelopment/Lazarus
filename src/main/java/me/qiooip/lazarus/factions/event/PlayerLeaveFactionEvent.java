package me.qiooip.lazarus.factions.event;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.factions.FactionPlayer;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;

@Getter
public class PlayerLeaveFactionEvent extends FactionEvent implements Cancellable {

    private final FactionPlayer factionPlayer;
    private final PlayerFaction faction;
    private final LeaveReason reason;
    @Setter private boolean cancelled;

    public PlayerLeaveFactionEvent(FactionPlayer factionPlayer, PlayerFaction faction, LeaveReason reason) {
        this.factionPlayer = factionPlayer;
        this.faction = faction;
        this.reason = reason;

        Bukkit.getPluginManager().callEvent(this);
    }

    public enum LeaveReason {
        KICK, LEAVE, DISBAND
    }
}
