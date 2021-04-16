package me.qiooip.lazarus.abilities.event;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.abilities.AbilityItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class ProjectileAbilityActivatedEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Location location;
    private final AbilityItem abilityItem;
    @Setter private boolean cancelled;

    public ProjectileAbilityActivatedEvent(Player player, Location location, AbilityItem abilityItem) {
        this.player = player;
        this.location = location;
        this.abilityItem = abilityItem;

        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
