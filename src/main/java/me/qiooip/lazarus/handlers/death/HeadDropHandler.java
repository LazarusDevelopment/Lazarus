package me.qiooip.lazarus.handlers.death;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.utils.item.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HeadDropHandler extends Handler implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(!Config.HEAD_DROP_ENABLED) return;

        Player player = event.getEntity();
        Player killer = player.getKiller();

        if(killer == null || player == killer) return;
        if(!killer.hasPermission("lazarus.headdrop")) return;

        Location location = player.getLocation();
        String skullName = Config.HEAD_DROP_NAME_FORMAT.replace("<player>", player.getName());

        ItemStack skullDrop = new ItemBuilder(Material.SKULL_ITEM, 1, 3)
            .setName(skullName)
            .setSkullOwner(player.getName()).build();

        location.getWorld().dropItemNaturally(location, skullDrop);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if(!Config.HEAD_DROP_ENABLED) return;

        Block block = event.getBlock();
        if(block.getType() != Material.SKULL) return;

        Skull skull = (Skull) block.getState();
        if(!skull.hasOwner()) return;

        event.setCancelled(true);

        String skullName = Config.HEAD_DROP_NAME_FORMAT.replace("<player>", skull.getOwner());

        ItemStack skullDrop = new ItemBuilder(Material.SKULL_ITEM, 1, 3).setName(skullName)
        .setSkullOwner(skull.getOwner()).build();

        block.getWorld().dropItemNaturally(block.getLocation(), skullDrop);
        block.setType(Material.AIR);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(!Config.HEAD_DROP_ENABLED || event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getClickedBlock().getType() != Material.SKULL) return;

        Skull skull = (Skull) event.getClickedBlock().getState();
        if(skull.getOwner() == null) return;

        Player player = event.getPlayer();
        player.sendMessage(Language.HEAD_DROP_CLICK_MESSAGE.replace("<player>", skull.getOwner()));
    }
}
