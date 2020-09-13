package me.qiooip.lazarus.handlers.restore;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.FileUtils;
import me.qiooip.lazarus.utils.ManagerEnabler;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.Tasks;
import me.qiooip.lazarus.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class InventoryRestoreManager implements Listener, ManagerEnabler {

    private final Map<UUID, InventoryData> inventories;

    public InventoryRestoreManager() {
        this.inventories = new HashMap<>();
        this.loadInventories();

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.saveInventories();
        this.inventories.clear();
    }

    private void loadInventories() {
        File[] files = Config.RESTORE_DIR.listFiles();
        if(files == null) return;

        Stream.of(files).forEach(file -> {
            Instant creationDate = FileUtils.creationDate(file);
            if(creationDate == null) return;

            if(ChronoUnit.HOURS.between(creationDate, Instant.now()) > Config.INVENTORY_RESTORE_FILE_CACHE) {
                file.delete();
                return;
            }

            String content = FileUtils.readWholeFile(file);
            if(content == null) return;

            UUID uuid = UUID.fromString(file.getName().replace(".json", ""));
            InventoryData data = Lazarus.getInstance().getGson().fromJson(content, InventoryData.class);

            this.inventories.put(uuid, data);
        });
    }

    private void saveInventories() {
        this.inventories.forEach((uuid, inventory) -> {
            String content = Lazarus.getInstance().getGson().toJson(inventory, InventoryData.class);

            File file = FileUtils.getOrCreateFile(Config.RESTORE_DIR, uuid.toString() + ".json");
            FileUtils.writeString(file, content);
        });
    }

    public void saveInventoryUponDeath(Player player) {
        if(!Config.INVENTORY_RESTORE_ENABLED) return;

        this.inventories.put(player.getUniqueId(), new InventoryData(player.getInventory()
        .getContents(), player.getInventory().getArmorContents()));
    }

    private void restorePlayerInventory(Player player, Player target) {
        InventoryData data = this.inventories.remove(target.getUniqueId());

        target.getInventory().setContents(data.getContents());
        target.getInventory().setArmorContents(data.getArmor());

        Messages.sendMessage(Language.PREFIX + Language.INVENTORY_RESTORE_RESTORED_SENDER
            .replace("<player>", player.getName())
            .replace("<target>", target.getName()), "lazarus.staff");

        target.sendMessage(Language.PREFIX + Language.INVENTORY_RESTORE_RESTORED_PLAYER
            .replace("<player>", player.getName()));

        new File(Config.RESTORE_DIR, target.getUniqueId().toString() + ".json").delete();
    }

    public void openRestoreInventory(Player player, Player target) {
        InventoryData data = this.inventories.get(target.getUniqueId());

        if(data == null) {
            player.sendMessage(Language.PREFIX + Language.INVENTORY_RESTORE_NOTHING_TO_RESTORE.replace("<target>", target.getName()));
            return;
        }

        Inventory inventory = Bukkit.createInventory(null, 54, target.getName() + "'s Inventory");

        inventory.setContents(data.getContents());
        IntStream.rangeClosed(45, 48).forEach(i -> inventory.setItem(i, data.getArmor()[i-45]));

        ItemStack placeholder = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).setName(ChatColor.RED + "Inventory Preview").build();
        IntStream.rangeClosed(36, 44).forEach(i -> inventory.setItem(i, placeholder));
        IntStream.rangeClosed(49, 51).forEach(i -> inventory.setItem(i, placeholder));

        inventory.setItem(52, new ItemBuilder(Material.STAINED_CLAY, 1, 14).setName(ChatColor.RED + "Close Preview").build());
        inventory.setItem(53, new ItemBuilder(Material.STAINED_CLAY, 1, 5).setName(ChatColor.GREEN + "Rollback Inventory (" + target.getName() + ")").build());

        player.openInventory(inventory);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if(!event.getInventory().getName().endsWith("'s Inventory")) return;
        if(event.getSlotType() == InventoryType.SlotType.OUTSIDE) return;

        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();
        if(item == null || !item.hasItemMeta()) return;
        if(!item.getItemMeta().hasDisplayName()) return;

        Player player = (Player) event.getWhoClicked();

        if(item.getItemMeta().getDisplayName().contains("Close Preview")) {
            Tasks.sync(() -> player.closeInventory());
            return;
        }

        if(item.getItemMeta().getDisplayName().contains("Rollback Inventory")) {
            String playerName = event.getInventory().getName().split("'")[0];
            Player target = Bukkit.getPlayer(playerName);

            if(target != null) {
                this.restorePlayerInventory(player, target);
            } else {
                player.sendMessage(Language.PREFIX + Language.COMMANDS_PLAYER_NOT_ONLINE.replace("<player>", playerName));
            }

            Tasks.sync(() -> player.closeInventory());
        }
    }
}
