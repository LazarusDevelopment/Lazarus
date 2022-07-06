package me.qiooip.lazarus.classes.items;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BardClickableItem extends ClickableItem {

    private int energyNeeded;
    private int distance;
    private int distanceSquared;
    private int cooldown;
    private boolean canBardHimself;
    private boolean applyToEnemy;
    private String chatColor;
}
