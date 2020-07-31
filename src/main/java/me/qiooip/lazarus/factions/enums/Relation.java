package me.qiooip.lazarus.factions.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.qiooip.lazarus.config.Config;

@AllArgsConstructor
public enum Relation {

    MEMBER(Config.TEAMMATE_COLOR),
    ALLY(Config.ALLY_COLOR),
    ENEMY(Config.ENEMY_COLOR);

    @Getter private final String color;
}
