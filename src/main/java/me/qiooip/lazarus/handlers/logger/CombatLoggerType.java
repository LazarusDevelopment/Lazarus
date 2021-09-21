package me.qiooip.lazarus.handlers.logger;

public enum CombatLoggerType {

    VILLAGER, SKELETON;

    public static CombatLoggerType getByName(String name) {
        for(CombatLoggerType loggerType : values()) {
            if(loggerType.name().equalsIgnoreCase(name)) {
                return loggerType;
            }
        }

        return CombatLoggerType.SKELETON;
    }
}
