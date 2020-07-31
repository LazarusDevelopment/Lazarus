package me.qiooip.lazarus.classes.items;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BardHoldableItem extends ClickableItem {

    private int distance;
    private boolean canBardHimself;
}
