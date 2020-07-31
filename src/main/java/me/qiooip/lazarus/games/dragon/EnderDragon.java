package me.qiooip.lazarus.games.dragon;

import org.bukkit.entity.Entity;

public interface EnderDragon {

    void disable();
    Entity getDragonBukkitEntity();

    float getDragonHealth();
    void setDragonHealth(float value);

    float getDragonMaxHealth();
    void setDragonMaxHealth(float value);
}
