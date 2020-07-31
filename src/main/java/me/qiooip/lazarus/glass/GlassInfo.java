package me.qiooip.lazarus.glass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.qiooip.lazarus.glass.GlassManager.GlassType;
import org.bukkit.Location;
import org.bukkit.Material;

@Getter
@AllArgsConstructor
public class GlassInfo {

    private final GlassType type;
    private final Location location;

    private final Material material;
    private final byte data;
}
