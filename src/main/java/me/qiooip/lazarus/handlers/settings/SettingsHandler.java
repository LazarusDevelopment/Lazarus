package me.qiooip.lazarus.handlers.settings;

import lombok.Getter;
import lombok.Setter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.userdata.Userdata;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.item.ItemBuilder;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SettingsHandler extends Handler implements Listener {

    private final List<PlayerSetting> playerSettings;
    private final Map<UUID, Inventory> settingsInventories;

    public SettingsHandler() {
        this.playerSettings = new ArrayList<>();
        this.settingsInventories = new HashMap<>();

        this.loadSettings();
        Bukkit.getOnlinePlayers().forEach(this::loadSettingsInventory);
    }

    @Override
    public void disable() {
        this.settingsInventories.clear();
        this.playerSettings.clear();
    }

    private void loadSettings() {
        ConfigurationSection section = Lazarus.getInstance().getConfig().getSection("PLAYER_SETTINGS.SETTINGS");

        section.getKeys(false).forEach(item -> {
            PlayerSetting setting = new PlayerSetting();

            ItemStack itemStack = ItemUtils.parseItem(section.getString(item + ".MATERIAL_ID"));
            if(itemStack == null) return;

            ItemBuilder builder = new ItemBuilder(itemStack);
            builder = builder.setName(section.getString(Color.translate(item + ".NAME")));

            setting.setItem(builder.build());
            setting.setSlot(section.getInt(item + ".SLOT") - 1);

            setting.setType(section.getString(item + ".SETTING"));
            setting.setCommand(section.getString(item + ".COMMAND"));

            setting.setToggledOnLore(section.getStringList(item + ".TOGGLED_ON_LORE")
                .stream().map(Color::translate).collect(Collectors.toList()));

            setting.setToggledOffLore(section.getStringList(item + ".TOGGLED_OFF_LORE")
                .stream().map(Color::translate).collect(Collectors.toList()));

            setting.setSettingLore((player) -> {
                Userdata userdata = Lazarus.getInstance().getUserdataManager().getUserdata(player);

                return this.getSettingStatus(userdata, setting.getType())
                    ? setting.getToggledOnLore() : setting.getToggledOffLore();
            });

            this.playerSettings.add(setting);
        });
    }

    private PlayerSetting getSetting(String itemName) {
        return this.playerSettings.stream().filter(setting -> setting.getItem()
        .getItemMeta().getDisplayName().equals(itemName)).findFirst().orElse(null);
    }

    private void loadSettingsInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(player, Config.PLAYER_SETTINGS_INVENTORY_SIZE, Config.PLAYER_SETTINGS_INVENTORY_NAME);

        this.playerSettings.forEach(setting -> {
            ItemBuilder builder = new ItemBuilder(setting.getItem());
            builder.setLore(setting.getSettingLore().apply(player));

            inventory.setItem(setting.getSlot(), builder.build());
        });

        this.settingsInventories.put(player.getUniqueId(), inventory);
    }

    public Inventory getSettingsInventory(Player player) {
        return this.settingsInventories.get(player.getUniqueId());
    }

    private boolean getSettingStatus(Userdata userdata, String type) {
        switch(type.toLowerCase()) {
            case "<messages>": return userdata.getSettings().isMessages();
            case "<sounds>": return userdata.getSettings().isSounds();
            case "<publicchat>": return userdata.getSettings().isPublicChat();
            case "<foundore>": return userdata.getSettings().isFoundOre();
            case "<deathmessages>": return userdata.getSettings().isDeathMessages();
            case "<cobble>": return userdata.getSettings().isCobble();
            case "<lightning>": return userdata.getSettings().isLightning();
            case "<scoreboard>": return userdata.getSettings().isScoreboard();
            default: return false;
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getSlotType() == SlotType.OUTSIDE || !(event.getWhoClicked() instanceof Player)) return;
        if(!event.getInventory().getName().equals(Config.PLAYER_SETTINGS_INVENTORY_NAME)) return;

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if(clicked == null || !clicked.hasItemMeta() || !clicked.getItemMeta().hasDisplayName()) return;

        PlayerSetting setting = this.getSetting(clicked.getItemMeta().getDisplayName());
        if(setting == null) return;

        Player player = (Player) event.getWhoClicked();
        player.chat(setting.getCommand());

        ItemBuilder builder = new ItemBuilder(setting.getItem());
        builder.setLore(setting.getSettingLore().apply(player));

        event.getInventory().setItem(setting.getSlot(), builder.build());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.loadSettingsInventory(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.settingsInventories.remove(event.getPlayer().getUniqueId());
    }

    @Getter
    @Setter
    private static class PlayerSetting {

        private ItemStack item;
        private int slot;

        private String type;
        private String command;

        private List<String> toggledOnLore;
        private List<String> toggledOffLore;

        private Function<Player, List<String>> settingLore;
    }
}
