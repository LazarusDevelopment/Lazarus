package me.qiooip.lazarus.handlers;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.ChunkLoadEvent;

public class MobStackHandler extends Handler implements Listener {

    @Override
    public void disable() {
        Bukkit.getWorlds().forEach(world -> world.getLivingEntities().stream().filter(entity
            -> entity instanceof Monster && entity.isCustomNameVisible()).forEach(LivingEntity::remove));
    }

    private void unstackEntity(LivingEntity entity) {
        int stackSize = this.getAmount(entity);
        if(stackSize <= 1) return;

        String newName = ChatColor.valueOf(Config.MOB_STACK_COLOR) + "x" + (stackSize - 1);
        LivingEntity toSpawn = (LivingEntity) entity.getWorld().spawnEntity(entity.getLocation(), entity.getType());

        toSpawn.setCustomName(newName);
        toSpawn.setCustomNameVisible(true);

        if(toSpawn instanceof Ageable) {
            ((Ageable) toSpawn).setAdult();
        }
    }

    private void stackEntity(Entity stackOn, Entity toStack) {
		if(!(stackOn instanceof LivingEntity) || !(toStack instanceof LivingEntity)) return;
		if(toStack.getType() != stackOn.getType() || stackOn.isDead() || toStack.isDead()) return;

        if(!Config.MOB_STACK_ENTITIES.contains(stackOn.getType().name()) || !Config.MOB_STACK_ENTITIES.contains(toStack.getType().name())) return;
        if(NmsUtils.getInstance().isCombatLogger(stackOn) || NmsUtils.getInstance().isCombatLogger(toStack)) return;
        if(stackOn.hasMetadata("loggerBait") || toStack.hasMetadata("loggerBait")) return;

        LivingEntity livingStackOn = (LivingEntity) stackOn;
        LivingEntity livingToStack = (LivingEntity) toStack;

        int oldAmount = this.getAmount(livingStackOn);
        int newAmount = 1;

        if(this.isStacked(livingToStack)) newAmount = this.getAmount(livingToStack);

        int finalAmount = oldAmount == 0 ? newAmount + 1 : oldAmount + newAmount;
        if(finalAmount > Config.MOB_STACK_MAX_AMOUNT) return;

        toStack.remove();
        String name = ChatColor.valueOf(Config.MOB_STACK_COLOR) + "x" + finalAmount;

        livingStackOn.setCustomName(name);
        livingStackOn.setCustomNameVisible(true);
    }

    private int getAmount(LivingEntity entity) {
        if(entity.getCustomName() == null) return 0;

        String name = Color.strip(entity.getCustomName().replace("x", ""));
        if(!StringUtils.isInteger(name)) return 0;

        return Integer.parseInt(name);
    }

    private boolean isStacked(LivingEntity entity) {
        return this.getAmount(entity) != 0;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if(event.getSpawnReason() == SpawnReason.CUSTOM) return;

        LivingEntity entity = event.getEntity();
        int radius = Config.MOB_STACK_RADIUS;

        entity.getNearbyEntities(radius, radius, radius).forEach(nearby -> this.stackEntity(nearby, entity));
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();
        int radius = Config.MOB_STACK_RADIUS;

        for(Entity entity : chunk.getEntities()) {
            entity.getNearbyEntities(radius, radius, radius).forEach(nearby -> this.stackEntity(nearby, entity));
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(event.getEntity().getType() == EntityType.PLAYER) return;
        this.unstackEntity(event.getEntity());
    }
}
