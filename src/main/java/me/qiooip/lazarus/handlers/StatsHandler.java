package me.qiooip.lazarus.handlers;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.userdata.Userdata;
import me.qiooip.lazarus.utils.item.ItemBuilder;
import me.qiooip.lazarus.utils.item.ItemUtils;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class StatsHandler extends Handler implements Listener {

    private final Map<UUID, Inventory> stats;
    private final List<StatsItem> statsItems;
    private final Set<UUID> viewingStats;

    public StatsHandler() {
        this.stats = new HashMap<>();
        this.statsItems = new ArrayList<>();
        this.viewingStats = new HashSet<>();

        this.loadStatsItems();
    }

    @Override
    public void disable() {
        this.stats.clear();
        this.statsItems.clear();
        this.viewingStats.clear();
    }

    private void loadStatsItems() {
        ConfigurationSection section = Lazarus.getInstance().getConfig()
            .getSection("STATS_COMMAND.INVENTORY_ITEMS");

        section.getKeys(false).forEach(item -> {
            StatsItem statsItem = new StatsItem();

            ItemStack itemStack = ItemUtils.parseItem(section.getString(item + ".MATERIAL_ID"));
            if(itemStack == null) return;

            statsItem.setItem(itemStack);
            statsItem.setName(section.getString(item + ".NAME"));
            statsItem.setReplace(section.getString(item + ".REPLACE"));
            statsItem.setSlot(section.getInt(item + ".SLOT") - 1);

            this.statsItems.add(statsItem);
        });
    }

    public void openStatsInventory(Player sender, Player player) {
        this.cacheInventory(player);

        Inventory inventory = this.stats.get(player.getUniqueId());
        this.updateItems(player, inventory);

        this.viewingStats.add(sender.getUniqueId());
        sender.openInventory(inventory);
    }

    private void cacheInventory(Player player) {
        if(this.stats.containsKey(player.getUniqueId())) return;

        String name = Config.STATS_INVENTORY_NAME.replace("<player>", player.getName());
        Inventory inv = Bukkit.createInventory(null, Config.STATS_INVENTORY_SIZE, name);

        this.updateItems(player, inv);
        this.stats.put(player.getUniqueId(), inv);
    }

    private void updateItems(Player player, Inventory inv) {
        Userdata data = Lazarus.getInstance().getUserdataManager().getUserdata(player);

        this.statsItems.forEach(item -> {
            ItemBuilder builder = new ItemBuilder(item.getItem());

            switch(item.getReplace()) {
                case "<kills>": builder.setName(item.getName() + data.getKills()); break;
                case "<deaths>": builder.setName(item.getName() + data.getDeaths()); break;
                case "<balance>": builder.setName(item.getName() + data.getBalance()); break;
                case "<diamonds>": builder.setName(item.getName() + player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE)); break;
                case "<emeralds>": builder.setName(item.getName() + player.getStatistic(Statistic.MINE_BLOCK, Material.EMERALD_ORE)); break;
                case "<gold>": builder.setName(item.getName() + player.getStatistic(Statistic.MINE_BLOCK, Material.GOLD_ORE)); break;
                case "<iron>": builder.setName(item.getName() + player.getStatistic(Statistic.MINE_BLOCK, Material.IRON_ORE)); break;
                case "<lapis>": builder.setName(item.getName() + player.getStatistic(Statistic.MINE_BLOCK, Material.LAPIS_ORE)); break;
                case "<coal>": builder.setName(item.getName() + player.getStatistic(Statistic.MINE_BLOCK, Material.COAL_ORE)); break;
                case "<redstone>": builder.setName(item.getName() + player.getStatistic(Statistic.MINE_BLOCK, Material.REDSTONE_ORE));
            }

            inv.setItem(item.getSlot(), builder.build());
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if(!this.viewingStats.contains(event.getWhoClicked().getUniqueId())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        this.viewingStats.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        if(!ItemUtils.isOre(event.getBlock().getType())) return;
        if(!event.getPlayer().getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH)) return;

        NmsUtils.getInstance().decreaseStatistic(event.getPlayer(), Statistic.MINE_BLOCK, event.getBlock().getType());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.stats.remove(event.getPlayer().getUniqueId());
    }

    @Getter
    @Setter
    private static class StatsItem {

        private String name;
        private String replace;
        private ItemStack item;
        private int slot;
    }
}
