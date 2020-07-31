package me.qiooip.lazarus.handlers.logger;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface CombatLogger {

    void removeCombatLogger();

    float getCombatLoggerHealth();

    Location getCombatLoggerLocation();

    void handleEffectChanges(Player player);
}
