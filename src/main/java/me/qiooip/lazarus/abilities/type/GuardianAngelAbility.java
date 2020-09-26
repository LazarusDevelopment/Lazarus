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

public class GuardianAngelAbility extends AbilityItem implements Listener {

    private int health;
    private int duration;

    public GuardianAngelAbility(ConfigFile config) {
        super(AbilityType.GUARDIAN_ANGLE, "GUARDIAN_ANGEL", config);
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.health = abilitySection.getInt("HEALTH");
        this.duration = abilitySection.getInt("DURATION");
    }

    @Override
    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        // TODO: aktivirat timer i message
        return true;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if(true) return; // TODO: provjera jel na tom timeru

        if(player.getHealth() > this.health) return;
        player.setHealth(player.getMaxHealth());
        // TODO: message
    }
}
