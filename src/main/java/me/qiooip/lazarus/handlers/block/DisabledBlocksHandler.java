package me.qiooip.lazarus.handlers.block;

import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class DisabledBlocksHandler extends Handler implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if(!player.isOp() && Config.DISABLED_BLOCK_PLACEMENT.contains(block.getType())) {
            String blockName = ItemUtils.getMaterialName(block.getType());

            player.sendMessage(Language.PREFIX + Language.BLOCKS_PLACEMENT_DISABLED
               .replace("<block>", blockName));

            event.setCancelled(true);
        }
    }
}
