package me.qiooip.lazarus.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.qiooip.lazarus.Lazarus;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;

public class PlayerUtils {

    public static FixedMetadataValue TRUE_METADATA_VALUE;

    public static void setupMetadataValue() {
        TRUE_METADATA_VALUE = new FixedMetadataValue(Lazarus.getInstance(), true);
    }

    public static Player getAttacker(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player) {
            return (Player) event.getDamager();
        }

        if(event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if(!(projectile.getShooter() instanceof Player)) return null;

            return (Player) projectile.getShooter();
        }

        return null;
    }

    public static boolean removeSplashFromInventory(PlayerInventory inventory) {
        boolean removed = false;

        for(int i = 0; i < 36; i++) {
            ItemStack itemStack = inventory.getItem(i);
            if(itemStack.getType() != Material.POTION || itemStack.getDurability() != 16421) continue;

            inventory.clear(i);
            removed = true;
            break;
        }

        return removed;
    }

    public static void addToInventoryOrDropToFloor(Player player, ItemStack itemStack) {
        if(player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
        } else {
            player.getInventory().addItem(itemStack);
        }
    }

    public static void sendToServer(Player player, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);

        player.sendPluginMessage(Lazarus.getInstance(), "BungeeCord", out.toByteArray());
    }
}
