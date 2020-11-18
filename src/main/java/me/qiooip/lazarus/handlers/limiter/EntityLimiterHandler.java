package me.qiooip.lazarus.handlers.limiter;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.utils.ServerUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityLimiterHandler extends Handler implements Listener {

    private Set<EntityType> disabledEntities;

    public EntityLimiterHandler() {
        this.loadDisabledEntities();
    }

    @Override
    public void disable() {
        this.disabledEntities.clear();
    }

    private void loadDisabledEntities() {
        ConfigurationSection section = Lazarus.getInstance().getLimitersFile()
            .getSection("ENTITY_LIMITER");

        List<EntityType> entityTypes = section.getKeys(false).stream()
            .filter(typeName -> !section.getBoolean(typeName))
            .map(typeName -> EntityType.valueOf(typeName))
            .collect(Collectors.toList());

        this.disabledEntities = entityTypes.isEmpty()
            ? EnumSet.noneOf(EntityType.class)
            : EnumSet.copyOf(entityTypes);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntitySpawn(EntitySpawnEvent event) {
        if(this.disabledEntities.contains(event.getEntity().getType())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntitySpawn(VehicleCreateEvent event) {
        if(this.disabledEntities.contains(event.getVehicle().getType())) {
            event.getVehicle().remove();
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if(event.getSpawnReason() == SpawnReason.CUSTOM) return;

        if(Config.MOBS_SPAWN_ONLY_FROM_SPAWNERS && !ServerUtils.ALLOWED_SPAWN_REASONS.contains(event.getSpawnReason())) {
            event.setCancelled(true);
            return;
        }

        if(event.getEntity().getLocation().getChunk().getEntities().length > Config.MOB_LIMIT_PER_CHUNK) {
            event.setCancelled(true);
        }
    }
}
