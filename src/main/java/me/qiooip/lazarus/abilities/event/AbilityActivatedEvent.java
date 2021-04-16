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
public class AbilityActivatedEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Location location;
    private final AbilityItem abilityItem;
    private final boolean projectileAbility;
    @Setter private boolean cancelled;

    public AbilityActivatedEvent(Player player, Location location, AbilityItem abilityItem, boolean projectileAbility) {
        this.player = player;
        this.location = location;
        this.abilityItem = abilityItem;
        this.projectileAbility = projectileAbility;

        Bukkit.getPluginManager().callEvent(this);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}
