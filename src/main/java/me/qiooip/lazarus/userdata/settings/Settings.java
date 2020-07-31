package me.qiooip.lazarus.userdata.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Settings {

    private boolean foundOre;
    private boolean sounds;
    private boolean messages;
    private boolean scoreboard;
    private boolean deathMessages;
    private boolean publicChat;
    private boolean cobble;
    private boolean lightning;

    public Settings() {
        this.foundOre = true;
        this.sounds = true;
        this.messages = true;
        this.scoreboard = true;
        this.deathMessages = true;
        this.publicChat = true;
        this.cobble = true;
        this.lightning = true;
    }
}
