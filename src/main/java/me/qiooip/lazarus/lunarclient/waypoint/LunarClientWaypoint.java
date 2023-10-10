package me.qiooip.lazarus.lunarclient.waypoint;

import com.lunarclient.apollo.BukkitApollo;
import com.lunarclient.apollo.module.waypoint.Waypoint;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.awt.*;

@Getter @Setter
public class LunarClientWaypoint {

    private String name;
    private Color rgbColor;
    private boolean forced;

    public void setRgbColor(String colorString) {
        this.rgbColor = Color.decode(colorString);
    }

    private Waypoint createNewWaypoint(Location location, String name) {
        return Waypoint.builder()
            .name(name)
            .color(this.rgbColor)
            .location(BukkitApollo.toApolloBlockLocation(location))
            .build();
    }

    public Waypoint createWaypoint(Location location) {
        return this.createNewWaypoint(location, this.name);
    }

    public Waypoint createWaypoint(Location location, String replacement) {
        return this.createNewWaypoint(location, this.name.replace("<name>", replacement));
    }
}
