package me.qiooip.lazarus.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class LocationUtils {

    public static void teleportWithChunkLoad(Player player, Location location) {
        int x = location.getBlockX() >> 4;
        int z = location.getBlockZ() >> 4;

        if(location.getWorld().isChunkLoaded(x, z)) {
            player.teleport(location);
        } else {
            location.getWorld().getChunkAtAsync(location, (chunk) -> Tasks.sync(() -> player.teleport(location)));
        }
    }

    public static String locationToStringSimple(Location location) {
        if(location == null) return null;

        String worldName = location.getWorld().getName();
        String x = Double.toString(Math.round(location.getX() * 1000D) / 1000D);
        String y = Double.toString(Math.round(location.getY() * 1000D) / 1000D);
        String z = Double.toString(Math.round(location.getZ() * 1000D) / 1000D);

        return worldName + "|" + x + "|" + y + "|" + z;
    }

    public static Location stringToLocationSimple(String locString) {
        if(locString == null || locString.isEmpty()) return null;

        String[] stringArray = locString.split("\\|");

        World world = Bukkit.getWorld(stringArray[0]);
        String x = stringArray[1];
        String y = stringArray[2];
        String z = stringArray[3];

        return new Location(world, Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
    }

    public static String locationToString(Location location) {
        if(location == null) return null;

        String worldName = location.getWorld().getName();
        String x = Double.toString(Math.round(location.getX() * 1000D) / 1000D);
        String y = Double.toString(Math.round(location.getY() * 1000D) / 1000D);
        String z = Double.toString(Math.round(location.getZ() * 1000D) / 1000D);
        String yaw = Double.toString(Math.round(location.getYaw() * 100D) / 100D);
        String pitch = Double.toString(Math.round(location.getPitch() * 100D) / 100D);

        return worldName + "|" + x + "|" + y + "|" + z + "|" + yaw + "|" + pitch;
    }

    public static Location stringToLocation(String locString) {
        if(locString == null || locString.isEmpty()) return null;

        String[] stringArray = locString.split("\\|");

        World world = Bukkit.getWorld(stringArray[0]);
        if(world == null) return null;

        double x = Double.parseDouble(stringArray[1]);
        double y = Double.parseDouble(stringArray[2]);
        double z = Double.parseDouble(stringArray[3]);
        float yaw = Float.parseFloat(stringArray[4]);
        float pitch = Float.parseFloat(stringArray[5]);

        return new Location(world, x, y, z, yaw, pitch);
    }
}
