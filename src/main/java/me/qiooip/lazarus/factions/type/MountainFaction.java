package me.qiooip.lazarus.factions.type;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MountainFaction extends SystemFaction {

    public MountainFaction(String name) {
        super(name);

        this.setColor(name.equals("Ore") ? "&b" : "&6");
    }
}
