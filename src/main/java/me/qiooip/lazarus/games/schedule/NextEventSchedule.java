package me.qiooip.lazarus.games.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NextEventSchedule {

    private final String eventName;
    private final long startTime;
}
