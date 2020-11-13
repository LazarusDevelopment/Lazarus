package me.qiooip.lazarus.lunarclient.waypoint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.qiooip.lazarus.games.conquest.ZoneType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum PlayerWaypointType {

    CONQUEST_RED(ZoneType.RED),
    CONQUEST_BLUE(ZoneType.BLUE),
    CONQUEST_GREEN(ZoneType.GREEN),
    CONQUEST_YELLOW(ZoneType.YELLOW),
    SPAWN,
    NETHER_SPAWN,
    END_SPAWN,
    KOTH,
    DTC,
    END_EXIT,
    FACTION_RALLY,
    FACTION_HOME,
    FOCUSED_FACTION_HOME;

    private ZoneType conquestZone;
}
