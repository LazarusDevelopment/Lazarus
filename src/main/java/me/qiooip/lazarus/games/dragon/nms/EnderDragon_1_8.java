package me.qiooip.lazarus.games.dragon.nms;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.games.dragon.EnderDragon;
import me.qiooip.lazarus.games.loot.LootData;
import me.qiooip.lazarus.utils.Messages;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class EnderDragon_1_8 extends EntityEnderDragon implements EnderDragon {

    private final LootData loot;
    private final Map<String, Float> damagers;

    public EnderDragon_1_8(Location loc, LootData loot) {
        super(((CraftWorld) loc.getWorld()).getHandle());

        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(Config.ENDER_DRAGON_HEALTH);
        this.setHealth(Config.ENDER_DRAGON_HEALTH);

        this.persistent = true;
        this.damagers = new HashMap<>();
        this.loot = loot;

        this.setCustomName(Config.ENDER_DRAGON_NAME);
        this.setCustomNameVisible(true);

        this.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

        int x = loc.getBlockX() >> 4;
        int z = loc.getBlockZ() >> 4;

        if(loc.getWorld().isChunkLoaded(x, z)) {
            ((CraftWorld) loc.getWorld()).getHandle().addEntity(this, SpawnReason.CUSTOM);
            return;
        }

        loc.getWorld().getChunkAtAsync(x, z, (chunk) -> ((CraftWorld) loc.getWorld()).getHandle().addEntity(this, SpawnReason.CUSTOM));
    }

    @Override
    public void disable() {
        this.damagers.clear();
    }

    @Override
    public Entity getDragonBukkitEntity() {
        return this.getBukkitEntity();
    }

    @Override
    public float getDragonHealth() {
        return this.getHealth();
    }

    @Override
    public void setDragonHealth(float value) {
        this.setHealth(value);
    }

    @Override
    public float getDragonMaxHealth() {
        return this.getMaxHealth();
    }

    @Override
    public void setDragonMaxHealth(float value) {
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(value);
    }

    @Override
    public boolean dealDamage(DamageSource damagesource, float amount) {
        if(damagesource.getEntity() instanceof EntityPlayer) {
            Player player = (Player) damagesource.getEntity().getBukkitEntity();
            this.damagers.put(player.getName(), this.damagers.getOrDefault(player.getName(), 0F) + amount);
        }

        return super.dealDamage(damagesource, amount);
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);

        AtomicReference<String> winner = new AtomicReference<>();
        Map<String, String> topDamagers = new HashMap<>();

        AtomicInteger counter = new AtomicInteger(1);
        Comparator<Map.Entry<String, Float>> comparator = Map.Entry.<String, Float>comparingByValue().reversed();

        this.damagers.entrySet().stream().sorted(comparator).limit(3).forEach(entry -> {
            if(winner.get() == null) {
                winner.set(entry.getKey());
            }

            String damagerFormat = Language.ENDER_DRAGON_DAMAGER_FORMAT
                .replace("<player>", entry.getKey())
                .replace("<damage>", String.valueOf(entry.getValue().intValue()));

            topDamagers.put("<damager" + counter.getAndIncrement() + ">", damagerFormat);
        });

        Language.ENDER_DRAGON_ENDED.forEach(line -> Messages.sendMessage(line
            .replace("<killer>", winner.get())
            .replace("<damager1>", topDamagers.getOrDefault("<damager1>", ""))
            .replace("<damager2>", topDamagers.getOrDefault("<damager2>", ""))
            .replace("<damager3>", topDamagers.getOrDefault("<damager3>", ""))));

        if(winner.get() != null) {
            this.loot.handleRewards(Bukkit.getPlayer(winner.get()));
        }

        Lazarus.getInstance().getEnderDragonManager().stopEnderDragon(false);
    }
}
