package me.qiooip.lazarus.factions.listeners;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.type.SystemFaction;
import me.qiooip.lazarus.factions.type.WarzoneFaction;
import org.bukkit.World.Environment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.event.entity.ProjectileHitEvent;

public class EntityEventListener implements Listener {

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if(!(event.getEntity() instanceof Arrow)) return;

        if(ClaimManager.getInstance().getFactionAt(event.getEntity().getLocation()).isSafezone()) {
            event.getEntity().remove();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if(!(event.getEntity() instanceof Monster) || Config.SPAWN_MOBS_IN_WARZONE) return;

        if(event.getLocation().getWorld().getEnvironment() == Environment.THE_END) return;
        if(event.getSpawnReason() == SpawnReason.SPAWNER || event.getSpawnReason() == SpawnReason.CUSTOM) return;

        Faction faction = ClaimManager.getInstance().getFactionAt(event.getLocation());
        if(!(faction instanceof WarzoneFaction) && !(faction instanceof SystemFaction)) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTarget(EntityTargetEvent event) {
        if(!(event.getEntity() instanceof LivingEntity)) return;
        if(event.getReason() != TargetReason.RANDOM_TARGET && event.getReason() != TargetReason.CLOSEST_PLAYER) return;
        if(!ClaimManager.getInstance().getFactionAt(event.getTarget().getLocation()).isSafezone()) return;

        event.setCancelled(true);
    }
}
