package me.qiooip.lazarus.factions.event;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.factions.Faction;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;

@Getter
public class FactionDisbandEvent extends FactionEvent implements Cancellable {

    private final Faction faction;
    private final CommandSender sender;
    @Setter private boolean cancelled;

    public FactionDisbandEvent(Faction faction, CommandSender sender) {
        this.faction = faction;
        this.sender = sender;

        Bukkit.getPluginManager().callEvent(this);
    }
}
