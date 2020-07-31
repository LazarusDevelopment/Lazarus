package me.qiooip.lazarus.factions.type;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.factions.Faction;
import org.bukkit.entity.Player;

public class WildernessFaction extends Faction {

    public WildernessFaction() {
        super(Config.WILDERNESS_NAME);
    }

    @Override
    public boolean shouldCancelPvpTimerEntrance(Player player) {
        return false;
    }
}
