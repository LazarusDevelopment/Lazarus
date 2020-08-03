package me.qiooip.lazarus.games.koth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.SystemFaction;
import me.qiooip.lazarus.games.Cuboid;
import me.qiooip.lazarus.games.loot.LootData;
import org.bukkit.ChatColor;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class KothData {

    private static final String DEFAULT_COLOR = ChatColor.BLUE + ChatColor.BOLD.toString();

    private String name;
    private UUID factionId;
    private int captime;
    private Cuboid cuboid;

    private transient String color;
    private transient LootData loot;

    public Faction getFaction() {
        return FactionsManager.getInstance().getFactionByUuid(this.factionId);
    }

    public String getColoredName() {
        return this.color + this.name;
    }

    public void setupKothColor() {
        SystemFaction kothFaction = (SystemFaction) this.getFaction();
        this.color = kothFaction != null ? kothFaction.getColor() : DEFAULT_COLOR;
    }
}
