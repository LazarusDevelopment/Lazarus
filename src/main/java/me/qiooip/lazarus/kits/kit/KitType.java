package me.qiooip.lazarus.kits.kit;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KitType {

    NORMAL, KITMAP, SPECIAL;

    public static KitType fromName(String name) {
        try {
            return KitType.valueOf(name);
        } catch(IllegalArgumentException e) {
            return null;
        }
    }
}
