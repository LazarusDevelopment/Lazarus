package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.utils.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScramblerAbility extends AbilityItem implements Listener {

    private final ItemStack empty;
    private final String metadataName;

    public ScramblerAbility(ConfigFile config) {
        super(AbilityType.SCRAMBLER, "SCRAMBLER", config);

        this.empty = new ItemStack(Material.AIR);
        this.metadataName = "scrambler";
        this.removeOneItem = false;
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

        player.removeMetadata(this.metadataName, Lazarus.getInstance());
        projectile.setMetadata(this.metadataName, PlayerUtils.TRUE_METADATA_VALUE);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Projectile)) return;

        Projectile projectile = (Projectile) event.getDamager();
        if(!projectile.hasMetadata(this.metadataName)) return;

        projectile.removeMetadata(this.metadataName, Lazarus.getInstance());

        Player player = (Player) event.getEntity();

        List<ItemStack> hotbar = new ArrayList<>(9);
        PlayerInventory inventory = player.getInventory();

        for(int i = 0; i < 9; i++) {
            hotbar.add(inventory.getItem(i));
        }

        Collections.shuffle(hotbar);

        for(int i = 0; i < hotbar.size(); i++) {
            ItemStack item = hotbar.get(i);

            if(item == null) {
                inventory.setItem(i, this.empty);
                continue;
            }

            inventory.setItem(i, item);
        }

        player.updateInventory();
        hotbar.clear();
    }
}
