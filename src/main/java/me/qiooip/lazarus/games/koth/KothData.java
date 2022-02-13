package me.qiooip.lazarus.games.koth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.SystemFaction;
import me.qiooip.lazarus.games.Cuboid;
import me.qiooip.lazarus.games.loot.LootData;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class KothData {

    private String name;
    private UUID factionId;
    private int captime;
    private int factionPoints;
    private Cuboid cuboid;

    private transient String color;
    private transient LootData loot;

    public KothData(String name) {
        this.name = name;
    }

    public Faction getFaction() {
        return FactionsManager.getInstance().getFactionByUuid(this.factionId);
    }

    public String getColoredName() {
        return this.color + this.name;
    }

    public void setupKothColor() {
        this.color =  ((SystemFaction) this.getFaction()).getColor();
    }
}
