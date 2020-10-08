package me.qiooip.lazarus.games.loot;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.ManagerEnabler;
import me.qiooip.lazarus.utils.FileUtils;
import me.qiooip.lazarus.utils.GsonUtils;
import me.qiooip.lazarus.utils.InventoryUtils;
import me.qiooip.lazarus.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class LootManager implements Listener, ManagerEnabler {

    private final File lootFile;

    private List<LootData> loots;
    private final List<UUID> editingLoot;

    public LootManager() {
        this.lootFile = FileUtils.getOrCreateFile(Config.GAMES_DIR, "loot.json");

        this.loots = new ArrayList<>();
        this.editingLoot = new ArrayList<>();

        this.loadLoot();
        this.setupDefaultLoot();

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.saveLoot();

        this.loots.clear();
        this.editingLoot.clear();
    }

    private void loadLoot() {
        String content = FileUtils.readWholeFile(this.lootFile);

        if(content == null) {
            this.loots = new ArrayList<>();
            return;
        }

        this.loots = Lazarus.getInstance().getGson().fromJson(content, GsonUtils.LOOT_TYPE);
    }

    private void saveLoot() {
        if(this.loots == null) return;

        FileUtils.writeString(this.lootFile, Lazarus.getInstance().getGson()
            .toJson(this.loots, GsonUtils.LOOT_TYPE));
    }

    private void setupDefaultLoot() {
        if(this.getLootByName("Conquest") == null) this.createLoot("Conquest");
        if(this.getLootByName("EnderDragon") == null) this.createLoot("EnderDragon");
        if(this.getLootByName("DTC") == null) this.createLoot("DTC");
    }

    public LootData createLoot(String name) {
        ItemBuilder builder = new ItemBuilder(Material.COOKIE).setName("&cDefault loot (Please change)");

        Inventory inv = Bukkit.createInventory(null, Config.LOOT_INVENTORY_SIZE,
        Config.LOOT_INVENTORY_NAME.replace("<event>", name));

        inv.setItem(Config.LOOT_INVENTORY_SIZE / 2, builder.build());

        LootData loot = new LootData();
        loot.setName(name);
        loot.setAmount(-1);
        loot.setItems(InventoryUtils.getItemsFromInventory(inv));
        loot.setInventory(inv);

        this.loots.add(loot);
        return loot;
    }

    public void removeLoot(String name) {
        this.loots.remove(this.getLootByName(name));
        this.saveLoot();
    }

    public LootData getLootByName(String name) {
        return this.loots.stream().filter(loot -> loot.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    private LootData getLootByInventoryName(String name) {
        return this.loots.stream().filter(loot -> loot.getInventory().getName().equals(name)).findFirst().orElse(null);
    }

    public void editLoot(Player player, LootData loot) {
        this.editingLoot.add(player.getUniqueId());
        player.openInventory(loot.getInventory());
    }

    public void listLoots(CommandSender sender) {
        sender.sendMessage(Language.LOOT_COMMAND_HEADER);
        sender.sendMessage(Language.LOOT_LIST_TITLE);

        this.loots.stream().sorted(Comparator.comparing(LootData::getName)).forEach(loot ->
            sender.sendMessage(Language.LOOT_LIST_FORMAT.replace("<loot>", loot.getName())
            .replace("<itemcount>", String.valueOf(loot.getItems().length))
            .replace("<amount>", loot.getAmount() == -1 ? String.valueOf(Config
            .LOOT_DEFAULT_REWARD_AMOUNT) : String.valueOf(loot.getAmount())))
        );

        sender.sendMessage(Language.LOOT_COMMAND_FOOTER);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        if(this.editingLoot.contains(player.getUniqueId())) return;

        LootData loot = this.getLootByInventoryName(event.getInventory().getName());
        if(loot == null) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(!(event.getPlayer() instanceof Player)) return;

        Player player = (Player) event.getPlayer();
        if(!this.editingLoot.contains(player.getUniqueId())) return;

        LootData loot = this.getLootByInventoryName(event.getInventory().getName());
        if(loot == null) return;

        this.editingLoot.remove(player.getUniqueId());
        loot.setItems(InventoryUtils.getItemsFromInventory(loot.getInventory()));

        player.sendMessage(Language.LOOT_PREFIX + Language.LOOT_EDIT_EDITED.replace("<loot>", loot.getName()));
    }
}
