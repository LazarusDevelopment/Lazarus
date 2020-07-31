package me.qiooip.lazarus.factions.type;

import lombok.NoArgsConstructor;
import org.bukkit.ChatColor;

@NoArgsConstructor
public class MountainFaction extends SystemFaction {

    public MountainFaction(String name) {
        super(name);

        this.setColor(name.equals("Ore") ? ChatColor.AQUA : ChatColor.GOLD);
    }
}
