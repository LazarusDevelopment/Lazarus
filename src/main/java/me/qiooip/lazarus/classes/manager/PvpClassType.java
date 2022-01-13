package me.qiooip.lazarus.classes.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PvpClassType {

    ARCHER("Archer"),
    BARD("Bard"),
    MINER("Miner"),
    ROGUE("Rogue"),
    MAGE("Mage"),
    BOMBER("Bomber");

    private final String name;
}
