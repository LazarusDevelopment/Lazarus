package me.qiooip.lazarus.lunarclient.waypoint;

import com.lunarclient.bukkitapi.object.LCWaypoint;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.awt.*;

@Getter @Setter
public class LunarClientWaypoint {

    private String name;
    private int rgbColor;
    private boolean forced;

    public void setRgbColor(String colorString) {
        this.rgbColor = Color.decode(colorString).getRGB();
    }

    public LCWaypoint createWaypoint(Location location, String replace) {
        return new LCWaypoint(this.name.replace("<name>", replace), location, this.rgbColor, this.forced);
    }

    public LCWaypoint createWaypoint(Location location) {
        return new LCWaypoint(this.name, location, this.rgbColor, this.forced);
    }
}
