package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.abilities.AbilitiesManager;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.handlers.logger.CombatLoggerType;
import me.qiooip.lazarus.utils.PlayerUtils;
import me.qiooip.lazarus.utils.WorldUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;

public class LoggerBaitAbility extends AbilityItem implements Listener {

    private final Set<Integer> entityIds;
    private int duration;

    public LoggerBaitAbility(ConfigFile config) {
        super(AbilityType.LOGGER_BAIT, "LOGGER_BAIT", config);

        this.entityIds = new HashSet<>();
    }

    @Override
    protected void disable() {
        WorldUtils.forEachEntity(entity -> {
            if(this.entityIds.contains(entity.getEntityId())) {
                entity.remove();
            }
        });

        this.entityIds.clear();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.duration = abilitySection.getInt("DURATION") * 20;
    }

    @Override
    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        this.spawnLoggerEntity(player.getLocation(), player.getName());

        InvisibilityAbility ability = (InvisibilityAbility) AbilitiesManager
            .getInstance().getAbilityItemByType(AbilityType.INVISIBILITY);

        if(ability != null) {
            ability.hidePlayer(player, this.duration);
        }

        event.setCancelled(true);
        return true;
    }

    private void spawnLoggerEntity(Location location, String playerName) {
        LivingEntity entity;

        if(Config.COMBAT_LOGGER_TYPE == CombatLoggerType.SKELETON) {
            entity = (LivingEntity) location.getWorld().spawnEntity(location, EntityType.SKELETON);
        } else {
            Villager villager = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
            villager.setProfession(Profession.getProfession(Config.COMBAT_LOGGER_VILLAGER_PROFESSION));

            entity = villager;
        }

        entity.setCustomName(Config.COMBAT_LOGGER_NAME_FORMAT.replace("<player>", playerName));
        entity.setCustomNameVisible(true);
        entity.setMetadata("loggerBait", PlayerUtils.TRUE_METADATA_VALUE);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 128));

        this.entityIds.add(entity.getEntityId());
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        this.entityIds.remove(event.getEntity().getEntityId());
    }
}
