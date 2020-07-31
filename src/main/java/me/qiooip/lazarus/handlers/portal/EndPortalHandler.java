package me.qiooip.lazarus.handlers.portal;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.selection.SelectionCheck;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.stream.IntStream;

public class EndPortalHandler extends Handler {

    public SelectionCheck createSelectionCheck() {
        return (selection, player, block) -> {

            if(block.getType() != Material.ENDER_PORTAL_FRAME) {
                player.sendMessage(Language.PREFIX + Language.ENDPORTAL_NOT_END_PORTAL_FRAME);
                return false;
            }

            if(!selection.areBothPositionsSet()) return true;

            if(selection.getPosOne().getBlockY() != selection.getPosTwo().getBlockY()) {
                player.sendMessage(Language.PREFIX + Language.ENDPORTAL_WRONG_ELEVATION);
                return false;
            }

            if((selection.sizeX() < 2 || selection.sizeX() > 4) || (selection.sizeZ() < 2 || selection.sizeZ() > 4)) {
                player.sendMessage(Language.PREFIX + Language.ENDPORTAL_SELECTION_TOO_BIG_OR_SMALL);
                return false;
            }

            Location pos1 = selection.getPosOne();
            Location pos2 = selection.getPosTwo();

            int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
            int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
            int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
            int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

            IntStream.rangeClosed(minX, maxX).forEach(x -> IntStream.rangeClosed(minZ, maxZ).forEach(z -> {
                Block newBlock = selection.getPosOne().getWorld().getBlockAt(x, selection.getPosOne().getBlockY(), z);
                if(newBlock.getType() != Material.ENDER_PORTAL_FRAME) newBlock.setType(Material.ENDER_PORTAL);
            }));

            Lazarus.getInstance().getSelectionManager().removeSelectionProcess(player);
            return true;
        };
    }
}
