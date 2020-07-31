package me.qiooip.lazarus.deathban;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Deathban {

    private long bannedUntil;
    private String reason;
    private String location;
}
