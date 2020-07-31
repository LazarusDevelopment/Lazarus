package me.qiooip.lazarus.factions.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;

@Getter
public class FactionCreateEvent extends FactionEvent implements Cancellable {

    private final String factionName;
    private final CommandSender sender;
    private final FactionType factionType;
    @Setter private boolean cancelled;

    public FactionCreateEvent(String factionName, CommandSender sender, FactionType factionType) {
        this.factionName = factionName;
        this.sender = sender;
        this.factionType = factionType;

        Bukkit.getPluginManager().callEvent(this);
    }

    public enum FactionType {
        PLAYER_FACTION, SYSTEM_FACTION
    }
}
