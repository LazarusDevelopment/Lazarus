package me.qiooip.lazarus.factions.type;

import lombok.NoArgsConstructor;
import me.qiooip.lazarus.utils.Color;

@NoArgsConstructor
public class KothFaction extends SystemFaction {

    public KothFaction(String name) {
        super(name);

        this.setColor(Color.translate("&9&l"));
    }
}
