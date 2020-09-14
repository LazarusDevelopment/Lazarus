package me.qiooip.lazarus.lunarclient.waypoint;

import com.moonsworth.client.api.object.LCWaypoint;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.awt.*;

@Getter @Setter
public class LunarClientWaypoint {

    private String name, color;

    public LCWaypoint createWaypoint(Location location, String replace) {
        return new LCWaypoint(this.name.replace("<name>", replace), location, Color.decode(this.color).getRGB(), true);
    }

    public LCWaypoint createWaypoint(Location location) {
        return new LCWaypoint(this.name, location, Color.decode(this.color).getRGB(), true);
    }
}
