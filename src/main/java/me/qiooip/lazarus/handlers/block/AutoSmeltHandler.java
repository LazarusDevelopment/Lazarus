package me.qiooip.lazarus.handlers.block;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.utils.PlayerUtils;
import me.qiooip.lazarus.utils.item.ItemBuilder;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class AutoSmeltHandler extends Handler implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        this.handleAutoSmelt(event.getPlayer(), event.getBlock(), event);
    }

    public boolean handleAutoSmelt(Player player, Block block, Cancellable event) {
        if(!player.hasPermission("lazarus.autosmelt")) return false;

        ItemStack tool = player.getItemInHand();
        if(tool == null || tool.containsEnchantment(Enchantment.SILK_TOUCH)) return false;

        int xpToDrop = ThreadLocalRandom.current().nextInt(3, 6);

        if(block.getType() == Material.IRON_ORE) {
            block.setType(Material.AIR);

            ItemStack iron = new ItemBuilder(Material.IRON_INGOT).build();
            this.handleItemDrops(player, block, iron, xpToDrop);

            NmsUtils.getInstance().increaseStatistic(player, Statistic.MINE_BLOCK, Material.IRON_ORE);
            event.setCancelled(true);
            return true;
        } else if(block.getType() == Material.GOLD_ORE) {
            block.setType(Material.AIR);

            ItemStack gold = new ItemBuilder(Material.GOLD_INGOT).build();
            this.handleItemDrops(player, block, gold, xpToDrop);

            NmsUtils.getInstance().increaseStatistic(player, Statistic.MINE_BLOCK, Material.GOLD_ORE);
            event.setCancelled(true);
            return true;
        }

        return false;
    }

    private void handleItemDrops(Player player, Block block, ItemStack drop, int expToDrop) {
        if(Lazarus.getInstance().getSotwHandler().isActive()) {
            PlayerUtils.addToInventoryOrDropToFloor(player, drop);
        } else {
            block.getWorld().dropItemNaturally(block.getLocation(), drop);
        }

        player.giveExp(expToDrop);
    }
}
