package me.qiooip.lazarus.handlers.portal;

import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class NetherPortalTrapHandler extends Handler implements Listener {

    private boolean isInPortal(Player player) {
        return player.getLocation().getBlock().getType() == Material.PORTAL
        && player.getEyeLocation().getBlock().getType() == Material.PORTAL;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(!this.isInPortal(event.getPlayer())) return;
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if(block.getType() != Material.PORTAL) return;

        boolean sendMessage = false;

        for(int x = -1; x < 2; x++) {
            for(int y = -1; y < 2; y++) {
                for(int z = -1; z < 2; z++) {
                    Block newBlock = block.getRelative(x, y, z);
                    if(newBlock.getType() != Material.PORTAL) continue;

                    sendMessage = true;
                    Tasks.sync(() -> event.getPlayer().sendBlockChange(newBlock.getLocation(), Material.AIR, (byte) 0));
                }
            }
        }

        if(sendMessage) event.getPlayer().sendMessage(Language.PREFIX + Language.NETHER_PORTAL_TRAP_FIX);
    }
}
