package me.qiooip.lazarus.userdata.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.qiooip.lazarus.userdata.Userdata;

import java.util.function.Function;

@Getter
@AllArgsConstructor
public enum UserdataValueType {

    KILLS(Userdata::getKills),
    DEATHS(Userdata::getDeaths),
    BALANCE(Userdata::getBalance),
    HIGHEST_KILLSTREAK(Userdata::getHighestKillstreak);

    private final Function<Userdata, Number> consumer;

    public Number getNewValue(Userdata userdata) {
        return this.consumer.apply(userdata);
    }
}
