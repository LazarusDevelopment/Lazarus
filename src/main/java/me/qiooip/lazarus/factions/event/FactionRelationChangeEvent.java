package me.qiooip.lazarus.factions.event;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.factions.enums.Relation;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;

@Getter
public class FactionRelationChangeEvent extends FactionEvent implements Cancellable {

    private final PlayerFaction faction;
    private final PlayerFaction targetFaction;
    private final Relation oldRelation;
    private final Relation newRelation;
    @Setter private boolean cancelled;

    public FactionRelationChangeEvent(PlayerFaction faction, PlayerFaction targetFaction, Relation oldRelation, Relation newRelation) {
        this.faction = faction;
        this.targetFaction = targetFaction;
        this.oldRelation = oldRelation;
        this.newRelation = newRelation;

        Bukkit.getPluginManager().callEvent(this);
    }
}
