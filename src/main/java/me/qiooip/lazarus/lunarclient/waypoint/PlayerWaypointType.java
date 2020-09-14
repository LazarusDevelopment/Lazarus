package me.qiooip.lazarus.lunarclient.waypoint;

public enum PlayerWaypointType {

    CONQUEST, DTC, FOCUSED_FACTION_HOME, HOME, KOTH, SPAWN;

    public static PlayerWaypointType getByName(String name, boolean throwOnNull) {
        for(PlayerWaypointType itemType : values()) {
            if(itemType.name().equalsIgnoreCase(name)) {
                return itemType;
            }
        }

        if(throwOnNull) {
            throw new IllegalArgumentException("LunarClientWaypointType with name = " + name + " doesn't exist!");
        } else {
            return null;
        }
    }
}
