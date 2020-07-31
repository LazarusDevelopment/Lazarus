package me.qiooip.lazarus.staffmode;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.utils.ManagerEnabler;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.PlayerUtils;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class VanishManager implements Listener, ManagerEnabler {

    private final List<UUID> vanished;

    private final List<UUID> hideStaff;
    private final List<UUID> buildEnabled;

    private final Set<Material> interactables;

    public VanishManager() {
        this.vanished = new ArrayList<>();

        this.hideStaff = new ArrayList<>();
        this.buildEnabled = new ArrayList<>();

        this.interactables = EnumSet.of(Material.ENDER_PEARL, Material.SNOW_BALL,
        Material.FISHING_ROD, Material.POTION, Material.EGG);

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.vanished.forEach(uuid -> this.disableVanish(Bukkit.getPlayer(uuid), true));
        this.vanished.clear();

        this.hideStaff.clear();
        this.buildEnabled.clear();

        this.interactables.clear();
    }

    private void enableVanish(Player player, boolean onJoin) {
        this.vanished.add(player.getUniqueId());

        Bukkit.getOnlinePlayers().forEach(online -> {
            if(!online.hasPermission("lazarus.vanish")) online.hidePlayer(player);
        });

        player.spigot().setCollidesWithEntities(false);

        if(!onJoin) {
            Messages.sendMessageWithoutPlayer(player, Language.PREFIX + Language.VANISH_ENABLED_STAFF_BROADCAST
            .replace("<player>", player.getName()), "lazarus.vanish");
        }
    }

    private void disableVanish(Player player, boolean disable) {
        if(!disable) {
            this.vanished.remove(player.getUniqueId());
        }

        Bukkit.getOnlinePlayers().forEach(online -> {
            if(!online.canSee(player)) online.showPlayer(player);
        });

        player.spigot().setCollidesWithEntities(true);
    }

    public void toggleVanish(Player player) {
        if(this.isVanished(player)) {
            this.disableVanish(player, false);

            Messages.sendMessageWithoutPlayer(player, Language.PREFIX + Language.VANISH_DISABLED_STAFF_BROADCAST
            .replace("<player>", player.getName()), "lazarus.vanish");

            player.sendMessage(Language.PREFIX + Language.VANISH_DISABLED);
        } else {
            this.enableVanish(player, false);
            player.sendMessage(Language.PREFIX + Language.VANISH_ENABLED);
        }

        boolean vanished = this.isVanished(player);

        Lazarus.getInstance().getKothManager().togglePlayerCapzone(player, !vanished);
        Lazarus.getInstance().getConquestManager().togglePlayerCapzone(player, !vanished);
    }

    boolean isVanished(UUID uuid) {
        return this.vanished.contains(uuid);
    }

    public boolean isVanished(Player player) {
        return this.isVanished(player.getUniqueId());
    }

    public List<UUID> getAllVanished() {
        return this.vanished;
    }

    public void toggleHideStaff(Player player) {
        if(this.hideStaff.contains(player.getUniqueId())) {
            this.showStaff(player);
            player.sendMessage(Language.PREFIX + Language.HIDE_STAFF_SHOWN);
        } else {
            this.hideStaff(player);
            player.sendMessage(Language.PREFIX + Language.HIDE_STAFF_HIDDEN);
        }
    }

    private void hideStaff(Player player) {
        this.hideStaff.add(player.getUniqueId());
        this.vanished.forEach(uuid -> player.hidePlayer(Bukkit.getPlayer(uuid)));
    }

    private void showStaff(Player player) {
        this.hideStaff.remove(player.getUniqueId());
        this.vanished.forEach(uuid -> player.showPlayer(Bukkit.getPlayer(uuid)));
    }

    private boolean isBuildEnabled(Player player) {
        return this.buildEnabled.contains(player.getUniqueId());
    }

    public void toggleVanishBuild(Player player) {
        if(this.isBuildEnabled(player)) {
            this.buildEnabled.remove(player.getUniqueId());
            player.sendMessage(Language.PREFIX+ Language.VANISH_BUILD_DISABLED);
        } else {
            this.buildEnabled.add(player.getUniqueId());
            player.sendMessage(Language.PREFIX + Language.VANISH_BUILD_ENABLED);
        }
    }

    public int vanishedAmount() {
        return this.vanished.size();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if(!this.isVanished(event.getPlayer()) || this.isBuildEnabled(event.getPlayer())) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(Language.PREFIX + Language.VANISH_BREAK_DENY);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if(!this.isVanished(event.getPlayer()) || this.isBuildEnabled(event.getPlayer())) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(Language.PREFIX + Language.VANISH_PLACE_DENY);

        Block block = event.getBlock();

        if(block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
            event.getPlayer().closeInventory();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(PlayerBucketEmptyEvent event) {
        if(!this.isVanished(event.getPlayer())) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(Language.PREFIX + Language.VANISH_PLACE_DENY);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(PlayerBucketFillEvent event) {
        if(!this.isVanished(event.getPlayer())) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(Language.PREFIX + Language.VANISH_BREAK_DENY);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if(this.isVanished(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if(this.isVanished(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getInventory().getName().equals("Silent View")) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(!this.isVanished(event.getPlayer())) return;

        Action action = event.getAction();

        if(action == Action.PHYSICAL) {
            event.setCancelled(true);
            return;
        }

        if(action != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if((block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) && !player.isSneaking()) {
            event.setCancelled(true);
            Chest chest = (Chest) block.getState();

            Inventory inventory = Bukkit.createInventory(null, chest.getInventory().getSize(), "Silent View");
            inventory.setContents(chest.getInventory().getContents());

            player.openInventory(inventory);
            player.sendMessage(Language.VANISH_CHEST_MESSAGE);
            return;
        }

        if(NmsUtils.getInstance().getClickableItems().contains(block.getType())) {
            event.setCancelled(true);
            player.sendMessage(Language.PREFIX + Language.VANISH_INTERACT_DENY);
        }
    }

    @EventHandler
    public void onProjectileCancel(PlayerInteractEvent event) {
        if(event.useInteractedBlock() == Result.DENY && event.useItemInHand() == Result.DENY) return;

        if(!event.hasItem() || !this.isVanished(event.getPlayer())) return;
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        Material material = player.getItemInHand().getType();

        if(this.interactables.contains(material) || (material == Material.BOW && player.getInventory().contains(Material.ARROW))) {
            event.setCancelled(true);

            player.updateInventory();
            player.sendMessage(Language.PREFIX + Language.VANISH_INTERACT_DENY);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        if(!this.isVanished((Player) event.getEntity())) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player attacker = PlayerUtils.getAttacker(event);
        if(attacker == null || !this.isVanished(attacker)) return;

        event.setCancelled(true);
        attacker.sendMessage(Language.PREFIX + Language.VANISH_DAMAGE_DENY);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if(!player.hasPermission("lazarus.vanish")) {
            this.vanished.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach(player::hidePlayer);
            return;
        }

        if(Config.VANISH_ON_JOIN_ENABLED && player.hasPermission("lazarus.vanish.onjoin")) {
            this.enableVanish(player, true);

            Messages.sendMessage(Language.STAFF_JOIN_MESSAGE_VANISHED
                .replace("<player>", player.getName()), "lazarus.vanish");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(this.isVanished(event.getPlayer())) {
            this.disableVanish(event.getPlayer(), false);
        }
    }
}
