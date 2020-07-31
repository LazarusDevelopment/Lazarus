package me.qiooip.lazarus.factions.enums;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.StringUtils;

public enum ChatType {

    PUBLIC, ALLY, FACTION;

    public String getName() {
        return StringUtils.capitalize(this.name().toLowerCase());
    }

    public String getFormat() {
        if(this == ChatType.ALLY) {
            return Language.FACTION_CHAT_ALLY_FORMAT;
        }

        return Language.FACTION_CHAT_FACTION_FORMAT;
    }

    public static ChatType parseType(String arg) {
        switch(arg.toLowerCase()) {
            case "f":
            case "fc":
            case "faction": return ChatType.FACTION;
            case "a":
            case "ac":
            case "ally": return ChatType.ALLY;
            case "p":
            case "pc":
            case "public": return ChatType.PUBLIC;
            default: return null;
        }
    }

    public ChatType nextType() {
        switch(this) {
            case FACTION: {
                if(Config.FACTION_MAX_ALLIES <= 0) {
                    return PUBLIC;
                } else {
                    return ALLY;
                }
            }
            case PUBLIC: {
                return FACTION;
            }
            default: {
                return PUBLIC;
            }
        }
    }
}
