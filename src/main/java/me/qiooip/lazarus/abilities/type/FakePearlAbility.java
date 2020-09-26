package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class FakePearlAbility extends AbilityItem implements Listener {

    public FakePearlAbility(ConfigFile config) {
        super(AbilityType.FAKE_PEARL, "FAKE_PEARL", config);
    }

    @Override
    protected boolean onProjectileClick(Player player, Projectile projectile) {
        projectile.setMetadata("fakePearl", new FixedMetadataValue(Lazarus.getInstance(), true));
        return true;
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        if(projectile.hasMetadata("fakePearl")) {
            projectile.removeMetadata("fakePearl", Lazarus.getInstance());
            projectile.remove();
        }
    }
}
