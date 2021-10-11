package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.abilities.AbilitiesManager;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.handlers.logger.CombatLoggerType;
import me.qiooip.lazarus.utils.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class LoggerBaitAbility extends AbilityItem implements Listener {

    private int duration;

    public LoggerBaitAbility(ConfigFile config) {
        super(AbilityType.LOGGER_BAIT, "LOGGER_BAIT", config);
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
        Entity entity;

        if(Config.COMBAT_LOGGER_TYPE == CombatLoggerType.SKELETON) {
            entity = location.getWorld().spawnEntity(location, EntityType.SKELETON);
        } else {
            entity = location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        }

        entity.setCustomName(Config.COMBAT_LOGGER_NAME_FORMAT.replace("<player>", playerName));
        entity.setCustomNameVisible(true);
        entity.setMetadata("loggerBait", PlayerUtils.TRUE_METADATA_VALUE);
    }
}
