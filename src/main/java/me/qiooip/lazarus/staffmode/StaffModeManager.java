package me.qiooip.lazarus.staffmode;

import com.google.common.collect.Iterables;
import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.*;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class StaffModeManager implements Listener, ManagerEnabler {

    private final Map<UUID, StaffPlayerData> staffMode;
    private final Map<ItemStack, StaffModeItem> staffModeItems;
    private final Map<String, StaffModeItem> cachedStaffModeItems;

    public StaffModeManager() {
        this.staffMode = new HashMap<>();
        this.staffModeItems = new HashMap<>();
        this.cachedStaffModeItems = new HashMap<>();

        this.loadStaffModeItems();

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.staffMode.keySet().stream().map(Bukkit::getPlayer).forEach(player -> this.disableStaffMode(player, true));
        this.staffMode.clear();
        this.staffModeItems.clear();
        this.cachedStaffModeItems.clear();
    }

    private void loadStaffModeItems() {
        ConfigurationSection section = Lazarus.getInstance().getConfig().getConfigurationSection("STAFF_MODE_ITEMS");

        section.getKeys(false).forEach(item -> {
            StaffModeItem staffItem = new StaffModeItem();

            ItemStack itemStack = ItemUtils.parseItem(section.getString(item + ".MATERIAL_ID"));
            if(itemStack == null) return;

            ItemBuilder builder = new ItemBuilder(itemStack);
            builder = builder.setName(section.getString(Color.translate(item + ".NAME")));
            builder = builder.setLore(Lazarus.getInstance().getConfig()
            .getStringList("STAFF_MODE_ITEMS." + item + ".LORE"));

            staffItem.setItem(builder.build());
            staffItem.setSlot(section.getInt(item + ".SLOT") - 1);
            staffItem.setCommand(section.getString(item + ".COMMAND"));
            staffItem.setReplacementItem(section.getString(item + ".REPLACEMENT_ITEM"));

            this.staffModeItems.put(staffItem.getItem(), staffItem);
            this.cachedStaffModeItems.put(item, staffItem);
        });
    }

    private void enableStaffMode(Player player) {
        StaffPlayerData staffData = new StaffPlayerData();
        staffData.setContents(player.getInventory().getContents());
        staffData.setArmor(player.getInventory().getArmorContents());
        staffData.setGameMode(player.getGameMode());

        this.staffMode.put(player.getUniqueId(), staffData);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        player.setGameMode(GameMode.CREATIVE);

        this.staffModeItems.values().forEach(staffItem -> player.getInventory()
            .setItem(staffItem.getSlot(), staffItem.getItem()));
    }

    private void disableStaffMode(Player player, boolean disable) {
        StaffPlayerData staffData = this.staffMode.get(player.getUniqueId());
        player.getInventory().setContents(staffData.getContents());
        player.getInventory().setArmorContents(staffData.getArmor());
        player.setGameMode(staffData.getGameMode());

        if(!disable) {
            this.staffMode.remove(player.getUniqueId());
        }
    }

    public void toggleStaffMode(Player player) {
        if(this.isInStaffMode(player)) {
            this.disableStaffMode(player, false);
            player.sendMessage(Language.PREFIX + Language.STAFF_MODE_DISABLED);
        } else {
            this.enableStaffMode(player);
            player.sendMessage(Language.PREFIX + Language.STAFF_MODE_ENABLED);
        }

        Lazarus.getInstance().getScoreboardManager().updateAllRelations(player);

        boolean inStaffMode = this.isInStaffMode(player);

        Lazarus.getInstance().getKothManager().togglePlayerCapzone(player, !inStaffMode);
        Lazarus.getInstance().getConquestManager().togglePlayerCapzone(player, !inStaffMode);
    }

    private boolean isInStaffMode(UUID uuid) {
        return this.staffMode.containsKey(uuid);
    }

    public boolean isInStaffMode(Player player) {
        return this.isInStaffMode(player.getUniqueId());
    }

    public boolean isInStaffModeOrVanished(UUID uuid) {
        return this.isInStaffMode(uuid) || Lazarus.getInstance().getVanishManager().isVanished(uuid);
    }

    public boolean isInStaffModeOrVanished(Player player) {
        return this.isInStaffModeOrVanished(player.getUniqueId());
    }

    public void randomTeleport(Player player) {
        if(Bukkit.getOnlinePlayers().size() == 1) {
            player.sendMessage(Language.PREFIX + Language.STAFF_MODE_RANDOM_TELEPORT_NO_PLAYER_MESSAGE);
            return;
        }

        Player target;
        do {
            target = Iterables.get(Bukkit.getOnlinePlayers(), ThreadLocalRandom
            .current().nextInt(Bukkit.getOnlinePlayers().size()));
        } while(target == player);

        player.teleport(target);
        player.sendMessage(Language.PREFIX + Language.STAFF_MODE_RANDOM_TELEPORT_MESSAGE
                .replace("<player>", target.getName()));
    }

    private Inventory previewInventory(Player target) {
        Inventory inventory = Bukkit.createInventory(null, 54, "Inventory preview");
        inventory.setContents(target.getInventory().getContents());

        ItemStack[] armor = target.getInventory().getArmorContents();
        IntStream.rangeClosed(45, 48).forEach(i -> inventory.setItem(i, armor[i-45]));

        ItemStack placeholder = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).setName(ChatColor.RED + "Inventory Preview").build();
        IntStream.rangeClosed(36, 44).forEach(i -> inventory.setItem(i, placeholder));
        inventory.setItem(49, placeholder);

        inventory.setItem(50, new ItemBuilder(Material.SPECKLED_MELON, (int) target.getHealth()).setName(ChatColor.RED + "Health").build());
        inventory.setItem(51, new ItemBuilder(Material.GRILLED_PORK, target.getFoodLevel()).setName(ChatColor.RED + "Hunger").build());

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.AQUA + "Playtime:");

        long playTime = target.getStatistic(Statistic.PLAY_ONE_TICK);
        lore.add(ChatColor.GRAY + DurationFormatUtils.formatDurationWords(playTime * 50, true, true));

        lore.add("");
        lore.add(ChatColor.AQUA + "PotionEffects:");

        target.getActivePotionEffects().forEach(effect -> lore.add(ChatColor.GRAY + StringUtils.getPotionEffectName(effect)
                + ", duration: " + (effect.getDuration() / 20) + ", level: " + (effect.getAmplifier() + 1)));

        inventory.setItem(52, new ItemBuilder(Material.SKULL_ITEM, 1, 3).setName(ChatColor.GREEN + target.getName()).setLore(lore).build());
        inventory.setItem(53, new ItemBuilder(Material.WOOL, 1, 14).setName("&cClose Preview").build());

        return inventory;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if(!this.isInStaffMode(event.getPlayer())) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(Language.PREFIX + Language.STAFF_MODE_BREAK_DENY);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if(!this.isInStaffMode(event.getPlayer())) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(Language.PREFIX + Language.STAFF_MODE_PLACE_DENY);

        Block block = event.getBlock();

        if(block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
            event.getPlayer().closeInventory();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if(this.isInStaffMode(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if(this.isInStaffMode(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(this.isInStaffMode(event.getEntity())) event.getDrops().clear();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player attacker = PlayerUtils.getAttacker(event);
        if(attacker == null || !this.isInStaffMode(attacker)) return;

        event.setCancelled(true);
        attacker.sendMessage(Language.PREFIX + Language.STAFF_MODE_DAMAGE_DENY);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(!this.isInStaffMode(player)) return;

        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();
        if(item == null || item.getType() == Material.AIR) return;

        if(!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;
        if(!item.getItemMeta().getDisplayName().equals(ChatColor.RED + "Close Preview")) return;

        Tasks.sync(player::closeInventory);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.useInteractedBlock() == Result.DENY && event.useItemInHand() == Result.DENY) return;

        Player player = event.getPlayer();
        if(!this.isInStaffMode(player) || !player.hasPermission("lazarus.staffmode")) return;

        ItemStack item = event.getItem();
        if(!event.hasItem() || !item.hasItemMeta()) return;
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if(!this.staffModeItems.containsKey(item)) return;
        StaffModeItem staffItem = this.staffModeItems.get(item);

        if(!staffItem.getCommand().isEmpty()) {
            player.chat(staffItem.getCommand(player));
        }

        if(!staffItem.getReplacementItem().isEmpty()) {
            StaffModeItem replacement = this.cachedStaffModeItems.get(staffItem.getReplacementItem());
            if(replacement == null) {
                player.sendMessage(Language.PREFIX + Language.STAFF_MODE_REPLACEMENT_ITEM_NOT_FOUND.replace("<item>", staffItem.getReplacementItem()));
                return;
            }

            player.getInventory().setItem(staffItem.getSlot(), staffItem.getItem());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if(!this.isInStaffMode(player) || !player.hasPermission("lazarus.staffmode")) return;

        ItemStack item = player.getItemInHand();
        if(item == null || item.getType() == Material.AIR) return;
        if(!item.hasItemMeta()) return;

        Entity entity = event.getRightClicked();
        if(!(entity instanceof Player)) return;

        Player rightClicked = (Player) entity;

        if(!this.staffModeItems.containsKey(item)) return;
        StaffModeItem staffItem = this.staffModeItems.get(item);

        if(!staffItem.getCommand().isEmpty()) {
            player.chat(staffItem.getCommand(rightClicked));
        }

        if(!staffItem.getReplacementItem().isEmpty()) {
            StaffModeItem replacement = this.cachedStaffModeItems.get(staffItem.getReplacementItem());
            if(replacement == null) {
                player.sendMessage(Language.PREFIX + Language.STAFF_MODE_REPLACEMENT_ITEM_NOT_FOUND.replace("<item>", staffItem.getReplacementItem()));
                return;
            }

            player.getInventory().setItem(staffItem.getSlot(), staffItem.getItem());
        }
    }

    public void inventoryInspect(Player player, Player target) {
        player.openInventory(this.previewInventory(target));
        player.sendMessage(Language.PREFIX + Language.STAFF_MODE_INVENTORY_INSPECT_MESSAGE
                .replace("<player>", target.getName()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(this.isInStaffMode(event.getPlayer())) {
            this.disableStaffMode(event.getPlayer(), false);
        }
    }

    @Getter
    @Setter
    private static class StaffModeItem {

        private ItemStack item;
        private String command;
        private String replacementItem;
        private int slot;

        public String getCommand(Player target) {
            return "/" + this.command.replace("<player>", target.getName());
        }
    }

    @Getter
    @Setter
    private static class StaffPlayerData {

        private ItemStack[] contents;
        private ItemStack[] armor;
        private GameMode gameMode;
    }
}
