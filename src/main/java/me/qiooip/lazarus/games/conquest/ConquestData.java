package me.qiooip.lazarus.games.conquest;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.games.Cuboid;
import me.qiooip.lazarus.utils.StringUtils;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

@Getter
@Setter
public class ConquestData {

    private Map<ZoneType, Cuboid> cuboids;

    ConquestData() {
        this.cuboids = new EnumMap<>(ZoneType.class);
        Stream.of(ZoneType.values()).forEach(type -> this.cuboids.put(type, null));
    }

    public Faction getFaction() {
        return FactionsManager.getInstance().getFactionByName("Conquest");
    }

    public Cuboid getCuboid(ZoneType type) {
        return this.cuboids.get(type);
    }

    public void setCuboid(ZoneType type, Cuboid capzone) {
        this.cuboids.put(type, capzone);
    }

    public String getLocationString(ZoneType type) {
        Cuboid cuboid = this.cuboids.get(type);
        return StringUtils.getLocationNameWithWorldWithoutY(cuboid.getCenter());
    }
}
