package me.qiooip.lazarus.factions.type;

import org.bukkit.entity.Player;

public class SpawnFaction extends SystemFaction {

    public SpawnFaction() {
        super("Spawn");

        this.setColor("&a");
        this.setDeathban(false);
        this.setSafezone(true);
    }

    @Override
    public boolean shouldCancelPvpTimerEntrance(Player player) {
        return false;
    }
}
