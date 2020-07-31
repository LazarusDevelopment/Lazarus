package me.qiooip.lazarus.scoreboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScoreboardInput {

    private final String prefix;
    private final String name;
    private final String suffix;
}
