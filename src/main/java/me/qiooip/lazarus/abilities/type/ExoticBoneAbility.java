package me.qiooip.lazarus.abilities.type;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ExoticBoneAbility extends AbilityItem {

    private int duration;
    private int hits;

    private final Table<UUID, UUID, Integer> playerHits;

    public ExoticBoneAbility(ConfigFile config) {
        super(AbilityType.EXOTIC_BONE, "EXOTIC_BONE", config);

        this.playerHits = HashBasedTable.create();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection section) {
        this.duration = section.getInt("DURATION");
        this.hits = section.getInt("HITS");
    }

    @Override
    protected boolean onPlayerItemHit(Player damager, Player target) {
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

//    @EventHandler(ignoreCancelled = true)
//    public void onBlockBreak(BlockBreakEvent event) {
//        if(true) return; TODO: provjera jel na tom timeru
//
//        event.setCancelled(true);
//    }
//
//    @EventHandler(ignoreCancelled = true)
//    public void onBlockPlace(BlockPlaceEvent event) {
//        if(true) return; TODO: provjera jel na tom timeru
//
//        event.setCancelled(true);
//    }
//
//    @EventHandler
//    public void onPlayerInteract(PlayerInteractEvent event) {
//        if(true) return; TODO: provjera jel na tom timeru
//        if(event.useInteractedBlock() == Event.Result.DENY || !event.hasBlock()) return;
//        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
//        if(!NmsUtils.getInstance().getExoticBoneClickables().contains(event.getClickedBlock().getType())) return;
//
//        event.setCancelled(true);
//    }
}
