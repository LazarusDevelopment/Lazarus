package me.qiooip.lazarus.hologram;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter @Setter
@AllArgsConstructor
public class HologramEntry {

    private final int entityId;
    private final String message;
    private final Location location;
}
