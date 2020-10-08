package me.qiooip.lazarus.games.mountain;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.games.Cuboid;
import me.qiooip.lazarus.utils.item.ItemUtils;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class MountainData implements Listener {

    @Setter private int id;
    private final MountainType type;
    private final UUID factionId;
    private final Cuboid cuboid;

    private Map<Location, Material> materials;

    MountainData(int id, MountainType type, UUID factionId, Cuboid cuboid) {
        this.id = id;
        this.type = type;
        this.factionId = factionId;
        this.cuboid = cuboid;

        this.cacheMaterials();
    }

    public Faction getFaction() {
        return FactionsManager.getInstance().getFactionByUuid(this.factionId);
    }

    public void respawn() {
        this.materials.forEach((location, material) -> location.getBlock().setType(material));
    }

    public void cacheMaterials() {
        this.materials = new HashMap<>();

        for(int x = this.cuboid.getMinX(); x <= this.cuboid.getMaxX(); x++) {
            for(int y = this.cuboid.getMinY(); y <= this.cuboid.getMaxY(); y++) {
                for(int z = this.cuboid.getMinZ(); z <= this.cuboid.getMaxZ(); z++) {

                    Block block = this.cuboid.getWorld().getBlockAt(x, y, z);
                    if(!this.type.getMaterials().test(block.getType())) continue;

                    this.materials.put(block.getLocation(), block.getType());
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        if(!this.materials.containsKey(event.getBlock().getLocation())) return;

        this.handleItemDrops(event.getPlayer(), event.getBlock(), event);
    }

    private void handleItemDrops(Player player, Block block, BlockBreakEvent event) {
        if(Lazarus.getInstance().getAutoSmeltHandler().handleAutoSmelt(player, block, event)) {
            return;
        }

        ItemStack item = player.getItemInHand();
        NmsUtils.getInstance().damageItemInHand(player);

        List<ItemStack> drops = NmsUtils.getInstance().getBlockDrops(item, block);

        if(Lazarus.getInstance().getSotwHandler().isActive() && player.getInventory().firstEmpty() != -1) {
            drops.forEach(drop -> player.getInventory().addItem(drop));
        } else {
            drops.forEach(drop -> block.getWorld().dropItemNaturally(block.getLocation(), drop));
        }

        if(!ItemUtils.hasEnchantment(item, Enchantment.SILK_TOUCH)) {
            player.giveExp(event.getExpToDrop());
        }

        if(!ItemUtils.isOre(block.getType()) || !ItemUtils.hasEnchantment(item, Enchantment.SILK_TOUCH)) {
            NmsUtils.getInstance().increaseStatistic(player, Statistic.MINE_BLOCK, block.getType());
        }

        block.setType(Material.AIR);
        event.setCancelled(true);
    }
}
