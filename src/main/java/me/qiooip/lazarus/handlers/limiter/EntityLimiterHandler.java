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

import java.util.HashSet;
import java.util.Set;

public class EntityLimiterHandler extends Handler implements Listener {

    private final Set<String> disabledEntities;

    public EntityLimiterHandler() {
        this.disabledEntities = new HashSet<>();

        this.loadDisabledEntities();
    }

    @Override
    public void disable() {
        this.disabledEntities.clear();
    }

    private void loadDisabledEntities() {
        this.disabledEntities.clear();

        ConfigurationSection section = Lazarus.getInstance().getLimitersFile()
            .getConfigurationSection("ENTITY_LIMITER");

        section.getKeys(false).forEach(type -> {
            if(section.getBoolean(type)) return;

            this.disabledEntities.add(EntityType.valueOf(type).name());
        });
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if(event.getSpawnReason() == SpawnReason.CUSTOM) return;

        if(this.disabledEntities.contains(event.getEntity().getType().name())) {
            event.setCancelled(true);
            return;
        }

        if(Config.MOBS_SPAWN_ONLY_FROM_SPAWNERS && !ServerUtils.ALLOWED_SPAWN_REASONS.contains(event.getSpawnReason())) {
            event.setCancelled(true);
            return;
        }

        if(event.getEntity().getLocation().getChunk().getEntities().length > Config.MOB_LIMIT_PER_CHUNK) {
            event.setCancelled(true);
        }
    }
}
