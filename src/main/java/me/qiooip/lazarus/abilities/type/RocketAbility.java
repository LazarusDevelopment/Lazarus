package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RocketAbility extends AbilityItem implements Listener {

    private double x, y, z;
    private float multiply;

    private final Set<UUID> boosted;

    public RocketAbility(ConfigFile config) {
        super(AbilityType.ROCKET, "ROCKET", config);

        this.boosted = new HashSet<>();
    }

    @Override
    protected void disable() {
        this.boosted.clear();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.x = abilitySection.getDouble("VECTOR.X");
        this.y = abilitySection.getDouble("VECTOR.Y");
        this.z = abilitySection.getDouble("VECTOR.Z");
        this.multiply = abilitySection.getFloat("VECTOR.MULTIPLY");
    }

    @Override
    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        Vector vector = player.getEyeLocation().getDirection();

        vector.multiply(this.multiply);
        vector.setX(this.x);
        vector.setY(this.y);
        vector.setZ(this.z);

        player.setVelocity(vector);
        this.boosted.add(player.getUniqueId());

        event.setCancelled(true);
        return true;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        if(event.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        Player player = (Player) event.getEntity();

        if(this.boosted.remove(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
