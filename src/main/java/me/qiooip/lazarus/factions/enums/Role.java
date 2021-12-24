package me.qiooip.lazarus.factions.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.qiooip.lazarus.utils.StringUtils;

@AllArgsConstructor
public enum Role {

    MEMBER(""),
    CAPTAIN("*"),
    CO_LEADER("**"),
    LEADER("**");

    @Getter private final String prefix;

    public boolean isAtLeast(Role role) {
        return this.ordinal() >= role.ordinal();
    }

    public String getName() {
        if(this == Role.CO_LEADER) return "CoLeader";
        return StringUtils.capitalize(this.name().toLowerCase());
    }

    public Role getPromote() {
        switch(this) {
            case MEMBER: return CAPTAIN;
            case CAPTAIN: return CO_LEADER;
            default: return null;
        }
    }

    public Role getDemote() {
        switch(this) {
            case CO_LEADER: return CAPTAIN;
            case CAPTAIN: return MEMBER;
            default: return null;
        }
    }
}
