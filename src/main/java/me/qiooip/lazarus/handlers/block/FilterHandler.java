package me.qiooip.lazarus.handlers.block;

import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.utils.item.ItemUtils;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class FilterHandler extends Handler implements Listener {

    private final List<UUID> toggled;
    private final Map<UUID, List<Material>> filters;

    public FilterHandler() {
        this.toggled = new ArrayList<>();
        this.filters = new HashMap<>();
    }

    @Override
    public void disable() {
        this.toggled.clear();
        this.filters.clear();
    }

    private boolean isFiltered(UUID uuid, Material material) {
        return this.filters.containsKey(uuid) && this.filters.get(uuid).contains(material);
    }

    public void addItem(Player player, String value) {
        ItemStack material = ItemUtils.parseItem(value);

        if(material == null) {
            player.sendMessage(Language.PREFIX + Language.ITEMS_INVALID_NAME);
            return;
        }

        if(this.isFiltered(player.getUniqueId(), material.getType())) {
            player.sendMessage(Language.PREFIX + Language.FILTER_ALREADY_FILTERED);
            return;
        }

        this.filters.putIfAbsent(player.getUniqueId(), new ArrayList<>());
        this.filters.get(player.getUniqueId()).add(material.getType());

        player.sendMessage(Language.PREFIX + Language.FILTER_ADDED
        .replace("<item>", StringUtils.getMaterialName(material.getType())));
    }

    public void removeItem(Player player, String value) {
        ItemStack material = ItemUtils.parseItem(value);

        if(material == null) {
            player.sendMessage(Language.PREFIX + Language.ITEMS_INVALID_NAME);
            return;
        }

        if(!this.isFiltered(player.getUniqueId(), material.getType())) {
            player.sendMessage(Language.PREFIX + Language.FILTER_NOT_FILTERED);
            return;
        }

        this.filters.get(player.getUniqueId()).remove(material.getType());
        player.sendMessage(Language.PREFIX + Language.FILTER_REMOVED
        .replace("<item>", StringUtils.getMaterialName(material.getType())));

        if(this.filters.get(player.getUniqueId()).isEmpty()) this.filters.remove(player.getUniqueId());
    }

    public void toggleFilter(Player player) {
        if(this.toggled.contains(player.getUniqueId())) {

            this.toggled.remove(player.getUniqueId());
            player.sendMessage(Language.PREFIX + Language.FILTER_TOGGLED_ON);

        } else {

            this.toggled.add(player.getUniqueId());
            player.sendMessage(Language.PREFIX + Language.FILTER_TOGGLED_OFF);

        }
    }

    public void listFilter(Player player) {
        if(!this.filters.containsKey(player.getUniqueId())) {
            player.sendMessage(Language.PREFIX + Language.FILTER_EMPTY);
            return;
        }

        List<String> toSend = this.filters.get(player.getUniqueId()).stream().map(material -> StringUtils
        .capitalize(material.name().replace("_", " ").toLowerCase())).collect(Collectors.toList());

        String items = StringUtils.joinList(toSend, ", ", 1);

        Language.FILTER_LIST_MESSAGE.forEach(line -> player.sendMessage(line
        .replace("<itemcount>", String.valueOf(toSend.size())).replace("<items>", items)));
    }

    public void clearFilter(Player player) {
        this.filters.remove(player.getUniqueId());
        player.sendMessage(Language.PREFIX + Language.FILTER_CLEARED);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();

        if(this.toggled.contains(player.getUniqueId())) return;
        if(!this.filters.containsKey(player.getUniqueId())) return;

        ItemStack item = event.getItem().getItemStack();
        if(!this.isFiltered(player.getUniqueId(), item.getType())) return;

        event.setCancelled(true);
    }
}
