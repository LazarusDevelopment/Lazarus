package me.qiooip.lazarus.hologram;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter @Setter
@AllArgsConstructor
public class HologramEntry {

    private int entityId;
    private String message;
    private Location location;
}
