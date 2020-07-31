package me.qiooip.lazarus.games.schedule;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleData {

    private int id;
    private String name;

    private String timeString;
    private LocalDateTime time;
}
