package me.qiooip.lazarus.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerUtils {

    public static void refundEnderpearl(Player player) {
        if(Config.ENDER_PEARL_REFUND_ENDER_PEARL_ON_CANCEL) {
            player.getInventory().addItem(new ItemBuilder(Material.ENDER_PEARL).build());
        }
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

    public static void sendToServer(Player player, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);

        player.sendPluginMessage(Lazarus.getInstance(), "BungeeCord", out.toByteArray());
    }
}
