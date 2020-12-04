package me.qiooip.lazarus.selection;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.event.PlayerLeaveFactionEvent;
import me.qiooip.lazarus.glass.GlassManager.GlassType;
import me.qiooip.lazarus.utils.item.ItemBuilder;
import me.qiooip.lazarus.utils.item.ItemUtils;
import me.qiooip.lazarus.utils.Tasks;
import me.qiooip.lazarus.utils.ManagerEnabler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SelectionManager implements Listener, ManagerEnabler {

    private final Map<UUID, Selection> selections;
    private final EnumMap<SelectionType, ItemStack> selectionWands;

    public SelectionManager() {
        this.selections = new HashMap<>();
        this.selectionWands = new EnumMap<>(SelectionType.class);

        this.loadSelectionWand("CLAIM");
        this.loadSelectionWand("SELECTION");
        this.selectionWands.put(SelectionType.SYSTEM_CLAIM, this.selectionWands.get(SelectionType.SELECTION));
        this.selectionWands.put(SelectionType.EVENT_CLAIM, this.selectionWands.get(SelectionType.SELECTION));

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.selections.forEach((uuid, selection) -> {
            Player player = Bukkit.getPlayer(uuid);
            if(player == null) return;

            player.getInventory().removeItem(selection.getWand());
        });

        this.selections.clear();
    }

    private void loadSelectionWand(String type) {
        ConfigFile config = Lazarus.getInstance().getConfig();
        ItemStack wandItem = ItemUtils.parseItem(config.getString(type + "_WAND.MATERIAL_ID"));

        if(wandItem == null) {
            wandItem = new ItemBuilder(Material.GOLD_HOE).build();
            Lazarus.getInstance().log("&cCould not parse wand item. Using default (Gold Hoe)");
        }

        ItemBuilder builder = new ItemBuilder(wandItem);
        builder.setName(config.getString(type + "_WAND.NAME"));
        builder.setLore(config.getStringList(type + "_WAND.LORE"));

        this.selectionWands.put(SelectionType.valueOf(type), builder.build());
    }

    public Selection getSelection(Player player) {
        return this.selections.get(player.getUniqueId());
    }

    private void startSelectionProcess(Player player, SelectionType selectionType, SelectionCheck check) {
        ItemStack wand = this.selectionWands.get(selectionType);
        player.getInventory().addItem(wand);

        this.selections.put(player.getUniqueId(), new Selection(player, selectionType, check, wand));
    }

    public void removeSelectionProcess(Player player) {
        Selection selection = this.selections.remove(player.getUniqueId());
        if(selection == null) return;

        this.removeClaimingWand(player, selection);
    }

    private void removeClaimingWand(Player player, Selection selection) {
        player.getInventory().remove(selection.getWand());

        if(selection.getType() == SelectionType.CLAIM) {
            Tasks.async(() -> Lazarus.getInstance().getGlassManager().clearGlassVisuals(player, GlassType.CLAIM_SELECTION));
        }
    }

    public void toggleSelectionProcess(Player player, SelectionType selectionType, SelectionCheck check) {
        this.removeSelectionProcess(player);
        this.startSelectionProcess(player, selectionType, check);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        if(player.getItemInHand() == null) return;

        Selection selection = this.getSelection(player);
        if(selection == null || selection.getType() == SelectionType.CLAIM) return;
        if(!player.getItemInHand().equals(selection.getWand())) return;

        event.setCancelled(true);
        Selection copy = selection.clone();

        boolean leftClick = false;

        if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
            selection.setPosOne(event.getClickedBlock().getLocation());
            leftClick = true;
        } else if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            selection.setPosTwo(event.getClickedBlock().getLocation());
        }

        if(selection.getCheck() != null && !selection.getCheck().check(selection, player, event.getClickedBlock())) {
            this.selections.put(player.getUniqueId(), copy);
            return;
        }

        player.sendMessage(Language.PREFIX + (leftClick ? Language.SELECTION_LOCATION_ONE_SET : Language.SELECTION_LOCATION_TWO_SET));
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Selection selection = this.getSelection(event.getPlayer());
        if(selection == null || !event.getPlayer().getItemInHand().equals(selection.getWand())) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getDamager();

        Selection selection = this.getSelection(player);
        if(selection == null || !player.getItemInHand().equals(selection.getWand())) return;

        event.setCancelled(true);

        this.removeClaimingWand(player, selection);
        this.selections.remove(player.getUniqueId());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Selection selection = this.getSelection(event.getPlayer());
        if(selection == null || !event.getItemDrop().getItemStack().equals(selection.getWand())) return;

        event.setCancelled(true);

        this.selections.remove(event.getPlayer().getUniqueId());
        Tasks.sync(() -> this.removeClaimingWand(event.getPlayer(), selection));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        this.removeSelectionProcess(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLeaveFaction(PlayerLeaveFactionEvent event) {
        Player player = event.getFactionPlayer().getPlayer();
        if(player == null) return;

        Selection selection = this.getSelection(player);
        if(selection == null || selection.getType() != SelectionType.CLAIM) return;

        this.removeSelectionProcess(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Selection selection = this.selections.remove(event.getEntity().getUniqueId());
        if(selection == null) return;

        event.getDrops().remove(selection.getWand());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.removeSelectionProcess(event.getPlayer());
    }
}
