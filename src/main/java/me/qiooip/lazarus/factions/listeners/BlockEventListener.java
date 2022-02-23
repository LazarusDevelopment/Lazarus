package me.qiooip.lazarus.factions.listeners;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.factions.type.SystemFaction;
import me.qiooip.lazarus.factions.type.WarzoneFaction;
import me.qiooip.lazarus.factions.type.WildernessFaction;
import me.qiooip.lazarus.utils.PlayerUtils;
import me.qiooip.lazarus.utils.Tasks;
import me.qiooip.lazarus.utils.item.ItemUtils;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;
import java.util.Set;

public class BlockEventListener implements Listener {

    private final Set<Material> safezoneClickables;

    public BlockEventListener() {
        this.safezoneClickables = EnumSet.of(Material.WORKBENCH, Material.ENDER_CHEST, Material.TRAPPED_CHEST,
            Material.CHEST, Material.ENCHANTMENT_TABLE, Material.ANVIL, Material.BEACON);
    }

    public boolean checkPlayerBuild(Player player, Location location, String message, boolean interactEvent) {
        return this.checkPlayerBuild(player, location, message, interactEvent, ClaimManager.getInstance().getFactionAt(location));
    }

    public boolean checkPlayerBuild(Player player, Location location, String message, boolean interactEvent, Faction factionAt) {
        if(player.hasPermission("lazarus.factions.bypass") && player.getGameMode() == GameMode.CREATIVE) return true;

        if(factionAt instanceof WildernessFaction || FactionsManager.getInstance().getPlayerFaction(player) == factionAt) return true;
        if(factionAt instanceof PlayerFaction && ((PlayerFaction) factionAt).isRaidable()) return true;

        if(factionAt instanceof WarzoneFaction) {
            if(this.playerCanBreakInWarzone(location)) return true;

            if(Config.KITMAP_MODE_ENABLED && interactEvent && NmsUtils.getInstance()
                .getKitmapClickables().contains(location.getBlock().getType())) return true;
        }

        if(message != null) {
            player.sendMessage(message.replace("<faction>", factionAt.getDisplayName(player)));
        }

        return false;
    }

    private boolean playerCanBreakInWarzone(Location location) {
        if(location.getWorld().getEnvironment() == Environment.THE_END) return false;

        int breakAfter = location.getWorld().getEnvironment() == Environment.NORMAL
            ? Config.FACTION_WARZONE_BREAK_AFTER_OVERWORLD
            : Config.FACTION_WARZONE_BREAK_AFTER_NETHER;

        return Math.max(Math.abs(location.getBlockX()), Math.abs(location.getBlockZ())) > breakAfter;
    }

    private boolean isItemClickable(Material blockType, ItemStack itemInHand, boolean purgeActive) {
        if(blockType == Material.GRASS && itemInHand != null && ItemUtils.isHoeItem(itemInHand.getType())) {
            return true;
        }

        return purgeActive ? NmsUtils.getInstance().getPurgeClickableItems().contains(blockType)
            : NmsUtils.getInstance().getClickableItems().contains(blockType);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if(this.checkPlayerBuild(event.getPlayer(), event.getBlock().getLocation(),
            Language.FACTIONS_PROTECTION_DENY_BUILD, false)) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if(this.checkPlayerBuild(event.getPlayer(), event.getBlock().getLocation(),
            Language.FACTIONS_PROTECTION_DENY_BUILD, false)) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(!event.hasBlock()) return;

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        Material blockType = block.getType();

        boolean purgeActive = Lazarus.getInstance().getPurgeHandler().isActive();
        boolean clickable = this.isItemClickable(blockType, event.getItem(), purgeActive);

        if(event.getAction() == Action.PHYSICAL) {
            if(clickable && purgeActive && ClaimManager.getInstance().getFactionAt(block) instanceof PlayerFaction) {
                return;
            }

            if(!this.checkPlayerBuild(player, block.getLocation(), null, true)) {
                event.setCancelled(true);
                return;
            }
        }

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Faction factionAt = ClaimManager.getInstance().getFactionAt(block);

            if(clickable && purgeActive && factionAt instanceof PlayerFaction) {
                return;
            }

            if(factionAt.isSafezone() && this.safezoneClickables.contains(blockType)) {
                return;
            }

            if(clickable && player.isSneaking() && event.hasItem() && !event.getItem().getType().isBlock()) {
                return;
            }

            if(clickable && !this.checkPlayerBuild(player, block.getLocation(), Language.FACTIONS_PROTECTION_DENY_INTERACT, true, factionAt)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBucketFill(PlayerBucketFillEvent event) {
        if(this.checkPlayerBuild(event.getPlayer(), event.getBlockClicked().getRelative(event.getBlockFace())
            .getLocation(), Language.FACTIONS_PROTECTION_DENY_INTERACT, false)) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if(this.checkPlayerBuild(event.getPlayer(), event.getBlockClicked().getRelative(event.getBlockFace())
            .getLocation(), Language.FACTIONS_PROTECTION_DENY_INTERACT, false)) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if(event.getCause() == IgniteCause.FLINT_AND_STEEL) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent event) {
        if(!(ClaimManager.getInstance().getFactionAt(event.getBlock()) instanceof SystemFaction)) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockFade(BlockFadeEvent event) {
        if(!(ClaimManager.getInstance().getFactionAt(event.getBlock()) instanceof SystemFaction)) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onLeavesDelay(LeavesDecayEvent event) {
        if(!(ClaimManager.getInstance().getFactionAt(event.getBlock()) instanceof SystemFaction)) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockForm(BlockFormEvent event) {
        if(!(ClaimManager.getInstance().getFactionAt(event.getBlock()) instanceof SystemFaction)) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if(!(event.getEntity() instanceof LivingEntity)) return;
        if(ClaimManager.getInstance().getFactionAt(event.getBlock().getLocation()) == null) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onHangingPlace(HangingPlaceEvent event) {
        if(this.checkPlayerBuild(event.getPlayer(), event.getEntity().getLocation(),
            Language.FACTIONS_PROTECTION_DENY_INTERACT, false)) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        if(!(event.getRemover() instanceof Player)) return;

        if(this.checkPlayerBuild((Player) event.getRemover(), event.getEntity()
            .getLocation(), Language.FACTIONS_PROTECTION_DENY_INTERACT, false)) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onHangingDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Hanging)) return;

        Player damager = PlayerUtils.getAttacker(event);
        if(damager == null) return;

        if(this.checkPlayerBuild(damager, event.getEntity().getLocation(),
            Language.FACTIONS_PROTECTION_DENY_INTERACT, false)) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onHangingInteractByPlayer(PlayerInteractEntityEvent event) {
        if(!(event.getRightClicked() instanceof Hanging)) return;

        if(this.checkPlayerBuild(event.getPlayer(), event.getRightClicked()
            .getLocation(), Language.FACTIONS_PROTECTION_DENY_INTERACT, false)) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onStickyPistonExtend(BlockPistonExtendEvent event) {
        Block extendBlock = event.getBlock().getRelative(event.getDirection(), event.getLength() + 1);

        Faction factionAt = ClaimManager.getInstance().getFactionAt(extendBlock);
        if(factionAt instanceof WildernessFaction) return;
        if(factionAt instanceof PlayerFaction && ((PlayerFaction) factionAt).isRaidable()) return;
        if(factionAt == ClaimManager.getInstance().getFactionAt(event.getBlock())) return;

        event.setCancelled(true);

        event.getBlock().breakNaturally();
        Tasks.sync(() -> extendBlock.setTypeIdAndData(extendBlock.getTypeId(), extendBlock.getData(), false));
    }

    @EventHandler(ignoreCancelled = true)
    public void onStickyPistonRetract(BlockPistonRetractEvent event) {
        if (!event.isSticky()) return;

        Block pullBlock = event.getRetractLocation().getBlock();
        if(pullBlock.isEmpty() || pullBlock.isLiquid()) return;

        Faction factionAt = ClaimManager.getInstance().getFactionAt(pullBlock);
        if(factionAt instanceof WildernessFaction) return;
        if(factionAt instanceof PlayerFaction && ((PlayerFaction) factionAt).isRaidable()) return;
        if(factionAt == ClaimManager.getInstance().getFactionAt(event.getBlock())) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockFromTo(BlockFromToEvent event) {
        if(Config.FACTION_CLAIM_LIQUID_FLOW_FROM_WILDERNESS_TO_CLAIMS || !event.getBlock().isLiquid()) return;

        Faction outerFactionAt = ClaimManager.getInstance().getFactionAt(event.getBlock());
        if(!(outerFactionAt instanceof WildernessFaction) && !(outerFactionAt instanceof WarzoneFaction)) return;

        Faction factionAt = ClaimManager.getInstance().getFactionAt(event.getToBlock());
        if(!(factionAt instanceof PlayerFaction) && !(factionAt instanceof SystemFaction)) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockDispense(BlockDispenseEvent event) {
        if(Config.FACTION_CLAIM_LIQUID_FLOW_FROM_WILDERNESS_TO_CLAIMS) return;
        if(event.getItem().getType() != Material.WATER_BUCKET && event.getItem().getType() != Material.LAVA_BUCKET) return;

        Faction outerFactionAt = ClaimManager.getInstance().getFactionAt(event.getBlock());
        if(!(outerFactionAt instanceof WildernessFaction) && !(outerFactionAt instanceof WarzoneFaction)) return;

        Location location = event.getVelocity().toLocation(event.getBlock().getWorld());

        Faction factionAt = ClaimManager.getInstance().getFactionAt(location);
        if(!(factionAt instanceof PlayerFaction) && !(factionAt instanceof SystemFaction)) return;

        event.setCancelled(true);
    }
}
