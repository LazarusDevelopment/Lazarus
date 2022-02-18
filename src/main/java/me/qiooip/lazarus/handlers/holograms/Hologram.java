package me.qiooip.lazarus.handlers.holograms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;

@Getter
@AllArgsConstructor
public class Hologram {

    private final int id;
    private final HologramType type;
    private final Location location;
}
