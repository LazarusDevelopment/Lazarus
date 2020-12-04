package me.qiooip.lazarus.factions.event;

import lombok.Getter;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

@Getter
public class FactionSetHomeEvent extends FactionEvent {

    private final PlayerFaction faction;
    private final CommandSender sender;
    private final Location location;

    public FactionSetHomeEvent(PlayerFaction faction, CommandSender sender, Location location) {
        this.faction = faction;
        this.sender = sender;
        this.location = location;

        Bukkit.getPluginManager().callEvent(this);
    }
}
