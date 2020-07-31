package me.qiooip.lazarus.games.conquest.event;

import lombok.Getter;
import me.qiooip.lazarus.games.conquest.ConquestData;
import me.qiooip.lazarus.games.loot.LootData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class ConquestCappedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final ConquestData conquest;
    private final LootData loot;
    private final Player capper;

    public ConquestCappedEvent(ConquestData conquest, LootData loot, Player capper) {
        this.conquest = conquest;
        this.loot = loot;
        this.capper = capper;

        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
