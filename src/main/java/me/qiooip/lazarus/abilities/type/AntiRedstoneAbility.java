package me.qiooip.lazarus.abilities.type;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

public class AntiRedstoneAbility extends AbilityItem implements Listener {

    private int duration;
    private int hits;

    private final Set<Material> clickables;
    private final Set<Material> physical;

    private final Table<UUID, UUID, Integer> playerHits;

    public AntiRedstoneAbility(ConfigFile config) {
        super(AbilityType.ANTI_REDSTONE, "ANTI_REDSTONE", config);

        this.clickables = EnumSet.of(Material.LEVER, Material.STONE_BUTTON, Material.WOOD_BUTTON);
        this.physical = EnumSet.of(Material.GOLD_PLATE, Material.IRON_PLATE, Material.STONE_PLATE, Material.WOOD_PLATE);

        this.playerHits = HashBasedTable.create();
    }

    @Override
    protected void disable() {
        this.clickables.clear();
        this.physical.clear();

        this.playerHits.clear();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.duration = abilitySection.getInt("DURATION");
        this.hits = abilitySection.getInt("HITS");
    }

    @Override
    protected boolean onPlayerItemHit(Player damager, Player target, EntityDamageByEntityEvent event) {
        if(this.playerHits.contains(damager.getUniqueId(), target.getUniqueId())) {
            int hitsNeeded = this.playerHits.get(damager.getUniqueId(), target.getUniqueId()) - 1;
            if(hitsNeeded == 0) {
                // TODO: timer i message
                this.playerHits.remove(damager.getUniqueId(), target.getUniqueId());
                return true;
            }

            this.playerHits.put(damager.getUniqueId(), target.getUniqueId(), hitsNeeded);
            return false;
        }

        this.playerHits.put(damager.getUniqueId(), target.getUniqueId(), --this.hits);
        return false;
    }

//    @EventHandler
//    public void onPlayerInteract(PlayerInteractEvent event) {
//        if(true) return; TODO: provjera jel na tom timeru i message
//        if(event.useInteractedBlock() == Event.Result.DENY || !event.hasBlock()) return;
//
//        Block block = event.getClickedBlock();
//
//        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && this.clickables.contains(block.getType())) {
//            event.setCancelled(true);
//            return;
//        }
//
//        if(event.getAction() == Action.PHYSICAL && this.physical.contains(block.getType())) {
//            event.setCancelled(true);
//        }
//    }
}
