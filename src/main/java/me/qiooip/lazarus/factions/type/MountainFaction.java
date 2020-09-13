package me.qiooip.lazarus.factions.type;

import lombok.NoArgsConstructor;
import me.qiooip.lazarus.utils.Color;

@NoArgsConstructor
public class MountainFaction extends SystemFaction {

    public MountainFaction(String name) {
        super(name);

        this.setColor(name.equals("Ore") ? Color.translate("&b") : Color.translate("&6"));
    }
}
