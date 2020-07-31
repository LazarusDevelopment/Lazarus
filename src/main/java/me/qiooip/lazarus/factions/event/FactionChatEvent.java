package me.qiooip.lazarus.factions.event;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.factions.Faction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@Getter
public class FactionChatEvent extends FactionEvent implements Cancellable {

    private final Player sender;
    private final Faction faction;
    private final String message;
    @Setter private boolean cancelled;

    public FactionChatEvent(Player sender, Faction faction, String message) {
        this.sender = sender;
        this.faction = faction;
        this.message = message;

        Bukkit.getPluginManager().callEvent(this);
    }
}
