package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.abilities.AbilitiesManager;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.utils.PlayerUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
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
        Skeleton skeleton = (Skeleton) player.getWorld().spawnEntity(player.getLocation(), EntityType.SKELETON);

        skeleton.setCustomName(Config.COMBAT_LOGGER_NAME_FORMAT.replace("<player>", player.getName()));
        skeleton.setCustomNameVisible(true);
        skeleton.setMetadata("loggerBait", PlayerUtils.TRUE_METADATA_VALUE);

        InvisibilityAbility ability = (InvisibilityAbility) AbilitiesManager
            .getInstance().getAbilityItemByType(AbilityType.INVISIBILITY);

        if(ability != null) {
            ability.hidePlayer(player, this.duration);
        }

        event.setCancelled(true);
        return true;
    }
}
