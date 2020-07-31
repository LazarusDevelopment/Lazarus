package me.qiooip.lazarus.games.koth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
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
    private Cuboid cuboid;
    private transient LootData loot;

    public Faction getFaction() {
        return FactionsManager.getInstance().getFactionByUuid(this.factionId);
    }
}
