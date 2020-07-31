package me.qiooip.lazarus.factions.type;

import lombok.NoArgsConstructor;
import org.bukkit.ChatColor;

@NoArgsConstructor
public class KothFaction extends SystemFaction {

    public KothFaction(String name) {
        super(name);

        this.setColor(ChatColor.DARK_PURPLE);
    }
}
