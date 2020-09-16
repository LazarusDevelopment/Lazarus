package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class ExoticBoneAbility extends AbilityItem {

    private int duration;
    private int hits;

    public ExoticBoneAbility(ConfigFile config) {
        super(AbilityType.EXOTIC_BONE, "EXOTIC_BONE", config);
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection section) {
        this.duration = section.getInt("DURATION");
        this.hits = section.getInt("HITS");
    }

    @Override
    protected void onItemClick(Player player) { }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        if(!(event.getDamager() instanceof Player)) return;

        Player damager = (Player) event.getDamager();

        ItemStack item = damager.getItemInHand();
        if(item == null) return;


    }
}
