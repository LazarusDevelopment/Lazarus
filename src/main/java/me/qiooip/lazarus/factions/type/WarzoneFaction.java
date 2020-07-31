package me.qiooip.lazarus.factions.type;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.factions.Faction;
import org.bukkit.entity.Player;

public class WarzoneFaction extends Faction {

    public WarzoneFaction() {
        super(Config.WARZONE_NAME);
    }

    @Override
    public boolean shouldCancelPvpTimerEntrance(Player player) {
        return false;
    }
}
