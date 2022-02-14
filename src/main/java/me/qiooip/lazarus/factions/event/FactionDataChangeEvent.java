package me.qiooip.lazarus.factions.event;

import lombok.Getter;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class FactionDataChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final PlayerFaction faction;
    private final FactionDataType type;
    private final Number newValue;

    public FactionDataChangeEvent(PlayerFaction faction, FactionDataType type) {
        this.faction = faction;
        this.type = type;
        this.newValue = type.getNewValue(faction);

        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
