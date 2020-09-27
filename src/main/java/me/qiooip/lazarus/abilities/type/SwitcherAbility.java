package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.utils.PlayerUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SwitcherAbility extends AbilityItem implements Listener {

    private boolean switchWithTeammates;
    private final String metadataName;

    public SwitcherAbility(ConfigFile config) {
        super(AbilityType.SWITCHER, "SWITCHER", config);

        this.metadataName = "switcher";
        this.removeOneItem = false;

        this.overrideActivationMessage();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.switchWithTeammates = abilitySection.getBoolean("SWITCH_WITH_TEAMMATES");
    }

    @Override
    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        player.setMetadata(this.metadataName, PlayerUtils.TRUE_METADATA_VALUE);
        return true;
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if(!(event.getEntity().getShooter() instanceof Player)) return;

        Projectile projectile = event.getEntity();

        Player player = (Player) projectile.getShooter();
        if(!player.hasMetadata(this.metadataName)) return;

        projectile.setMetadata(this.metadataName, PlayerUtils.TRUE_METADATA_VALUE);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {

    }
}
