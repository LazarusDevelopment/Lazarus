package me.qiooip.lazarus.classes.items;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MageClickableItem extends ClickableItem {

    private int energyNeeded;
    private int distance;
    private int distanceSquared;
    private int cooldown;
    private boolean applyToHimself;
    private String chatColor;
}
