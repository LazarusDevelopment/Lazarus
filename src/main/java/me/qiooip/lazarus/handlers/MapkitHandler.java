package me.qiooip.lazarus.handlers;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MapkitHandler extends Handler implements Listener {

    @Getter private Inventory mapkitInv;
    private final List<UUID> editingMapkit;

    public MapkitHandler() {
        this.editingMapkit = new ArrayList<>();
        this.loadInventory();
    }

    @Override
    public void disable() {
        this.saveInventory();
    }

    private void loadInventory() {
        this.mapkitInv = Bukkit.createInventory(null, Config.MAPKIT_INVENTORY_SIZE, Config.MAPKIT_INVENTORY_NAME);

        String mapkitItems = Lazarus.getInstance().getUtilitiesFile().getString("MAPKIT_ITEMS");
        if(mapkitItems == null || mapkitItems.isEmpty()) return;

        String base64Contents = Lazarus.getInstance().getUtilitiesFile().getString("MAPKIT_ITEMS");
        ItemStack[] contents = InventoryUtils.itemStackArrayFromBase64(base64Contents);

        if(contents.length > Config.MAPKIT_INVENTORY_SIZE) {
            ItemStack[] subContents = new ItemStack[Config.MAPKIT_INVENTORY_SIZE];
            System.arraycopy(contents, 0, subContents, 0, Config.MAPKIT_INVENTORY_SIZE);

            contents = subContents;
        }

        this.mapkitInv.setContents(contents);
    }

    private void saveInventory() {
        String contentsBase64 = InventoryUtils.itemStackArrayToBase64(this.mapkitInv.getContents());

        Lazarus.getInstance().getUtilitiesFile().set("MAPKIT_ITEMS", contentsBase64);
        Lazarus.getInstance().getUtilitiesFile().save();
    }

    public void setEditingMapkit(Player player) {
        this.editingMapkit.add(player.getUniqueId());
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();

        if(!inv.getName().equals(this.mapkitInv.getName())) return;
        if(player.hasPermission("lazarus.mapkit.edit") && this.editingMapkit.contains(player.getUniqueId())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inv = event.getInventory();

        if(!inv.getName().equals(this.mapkitInv.getName())) return;
        if(!this.editingMapkit.contains(player.getUniqueId())) return;

        this.editingMapkit.remove(player.getUniqueId());
        this.saveInventory();

        player.sendMessage(Language.PREFIX + Language.MAPKIT_EDIT_MESSAGE);
    }
}
