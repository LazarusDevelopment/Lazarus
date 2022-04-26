package me.qiooip.lazarus.handlers.timer;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.scoreboard.ScoreboardManager;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.PlayerUtils;
import me.qiooip.lazarus.utils.ServerUtils;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.StringUtils.FormatType;
import me.qiooip.lazarus.utils.Tasks;
import me.qiooip.lazarus.utils.item.ItemUtils;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class SotwHandler extends Handler implements Listener {

    private boolean hidePlayers;

    private final Set<UUID> sotwEnabledPlayers;
    private final Set<Material> autoInventoryMaterial;

    public SotwHandler() {
        this.hidePlayers = Config.SOTW_HIDE_PLAYERS;
        this.sotwEnabledPlayers = new HashSet<>();

        this.autoInventoryMaterial = EnumSet.of(Material.STONE, Material.COBBLESTONE, Material.DIRT,
            Material.GRASS, Material.SAND, Material.SOUL_SAND, Material.COAL_ORE, Material.DIAMOND_ORE,
            Material.EMERALD_ORE, Material.GLOWING_REDSTONE_ORE, Material.GOLD_ORE, Material.IRON_ORE,
            Material.LAPIS_ORE, Material.QUARTZ_ORE, Material.REDSTONE_ORE, Material.NETHERRACK);
    }

    @Override
    public void disable() {
        this.sotwEnabledPlayers.clear();
    }

    public void startSotwTimer(CommandSender sender, int time) {
        if(this.isActive()) {
            sender.sendMessage(Language.PREFIX + Language.SOTW_ALREADY_RUNNING);
            return;
        }

        if(this.hidePlayers) {
            Tasks.async(() -> this.toggleSotwInvisibility(null, true));
        }

        TimerManager.getInstance().getSotwTimer().activate(time);
        ScoreboardManager.getInstance().updateTabRelations(Bukkit.getOnlinePlayers(), false);

        Messages.sendMessage(Language.SOTW_STARTED.replace("<time>",
            StringUtils.formatTime(time, FormatType.SECONDS_TO_HOURS)));
    }

    public void stopSotwTimer(CommandSender sender) {
        if(!this.isActive()) {
            sender.sendMessage(Language.PREFIX + Language.SOTW_NOT_RUNNING);
            return;
        }

        this.sotwEnabledPlayers.clear();

        Tasks.async(() -> this.showSotwInvisiblePlayers());
        ScoreboardManager.getInstance().updateTabRelations(Bukkit.getOnlinePlayers(), false);

        TimerManager.getInstance().getSotwTimer().cancel();
        Messages.sendMessage(Language.SOTW_ENDED);
    }

    public boolean isActive() {
        return TimerManager.getInstance().getSotwTimer().isActive();
    }

    public boolean isPlayerSotwEnabled(Player player) {
        return this.sotwEnabledPlayers.contains(player.getUniqueId());
    }

    public boolean isUnderSotwProtection(Player player) {
        return this.isActive() && !this.isPlayerSotwEnabled(player);
    }

    public void enableSotwForPlayer(Player player) {
        if(!this.isActive()) {
            player.sendMessage(Language.PREFIX + Language.SOTW_NOT_RUNNING);
            return;
        }

        if(this.sotwEnabledPlayers.contains(player.getUniqueId())) {
            player.sendMessage(Language.PREFIX + Language.SOTW_ALREADY_ENABLED);
            return;
        }

        ScoreboardManager.getInstance().updateTabRelations(player, false);

        this.sotwEnabledPlayers.add(player.getUniqueId());
        player.sendMessage(Language.PREFIX + Language.SOTW_ENABLED);
    }

    public void showSotwInvisiblePlayers() {
        Bukkit.getOnlinePlayers().forEach(player -> Bukkit.getOnlinePlayers().forEach(online -> {
            if(Lazarus.getInstance().getVanishManager().isVanished(online)) return;

            player.showPlayer(online);
        }));
    }

    public void toggleSotwInvisibility(CommandSender sender, Boolean hide) {
        this.hidePlayers = hide != null ? hide : !this.hidePlayers;

        if(this.hidePlayers) {
            Bukkit.getOnlinePlayers().forEach(player -> Bukkit.getOnlinePlayers().forEach(online -> {
                if(Lazarus.getInstance().getVanishManager().isVanished(online)) return;
                if(!ClaimManager.getInstance().getFactionAt(online.getLocation()).isSafezone()) return;

                player.hidePlayer(online);
            }));
        } else {
            Bukkit.getOnlinePlayers().forEach(player -> Bukkit.getOnlinePlayers().forEach(online -> {
                if(Lazarus.getInstance().getVanishManager().isVanished(online)) return;

                player.showPlayer(online);
            }));
        }

        if(sender != null) {
            sender.sendMessage(Language.PREFIX + (this.hidePlayers ? Language
                .SOTW_HIDE_PLAYERS_ENABLED : Language.SOTW_HIDE_PLAYERS_DISABLED));
        }
    }

    private void toggleSotwInvisibility(Player player, Faction toFaction) {
        if(Lazarus.getInstance().getVanishManager().isVanished(player)) return;

        if(toFaction.isSafezone()) {
            Bukkit.getOnlinePlayers().forEach(online -> online.hidePlayer(player));
        } else {
            Bukkit.getOnlinePlayers().forEach(online -> online.showPlayer(player));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if(!this.isActive()) return;

        Location from = event.getFrom();
        Location to = event.getTo();

        if(from.getBlockX() == to.getBlockX()
            && from.getBlockY() == to.getBlockY()
            && from.getBlockZ() == to.getBlockZ()) return;

        Faction toFaction = ClaimManager.getInstance().getFactionAt(to);

        if(ClaimManager.getInstance().getFactionAt(from) != toFaction && this.hidePlayers) {
            this.toggleSotwInvisibility(event.getPlayer(), toFaction);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if(!this.isActive()) return;

        Faction toFaction = ClaimManager.getInstance().getFactionAt(event.getTo());

        if(ClaimManager.getInstance().getFactionAt(event.getFrom()) != toFaction && this.hidePlayers) {
            this.toggleSotwInvisibility(event.getPlayer(), toFaction);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(!this.isActive()) return;

        Player killer = event.getEntity().getKiller();
        if(killer == null) return;

        Location location = event.getEntity().getLocation();
        List<ItemStack> drops = event.getDrops();

        if(killer.getInventory().firstEmpty() == -1) {
            drops.forEach(drop -> location.getWorld().dropItemNaturally(location, drop));
        } else {
            drops.forEach(drop -> killer.getInventory().addItem(drop));
        }

        drops.clear();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if(!this.isActive() || event.getPlayer().getGameMode() == GameMode.CREATIVE) return;

        Block block = event.getBlock();
        if(!this.autoInventoryMaterial.contains(block.getType())) return;

        Player player = event.getPlayer();

        ItemStack item = player.getItemInHand();
        NmsUtils.getInstance().damageItemInHand(player);

        List<ItemStack> drops = NmsUtils.getInstance().getBlockDrops(item, block);

        if(player.getInventory().firstEmpty() == -1) {
            drops.forEach(drop -> block.getWorld().dropItemNaturally(block.getLocation(), drop));
        } else {
            drops.forEach(drop -> player.getInventory().addItem(drop));
        }

        if(!ItemUtils.hasEnchantment(item, Enchantment.SILK_TOUCH)) {
            player.giveExp(event.getExpToDrop());
        }

        if(!ItemUtils.isOre(block.getType()) || !ItemUtils.hasEnchantment(item, Enchantment.SILK_TOUCH)) {
            NmsUtils.getInstance().increaseStatistic(player, Statistic.MINE_BLOCK, block.getType());
        }

        block.setType(Material.AIR);
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onPlayerDamage(EntityDamageEvent event) {
        if(!this.isActive()) return;
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if(this.sotwEnabledPlayers.contains(player.getUniqueId())) return;

        if(event.getCause() == DamageCause.VOID) {
            Location endSpawn = Config.WORLD_SPAWNS.get(player.getWorld().getEnvironment());

            player.teleport(endSpawn == null ? player.getWorld().getSpawnLocation() : endSpawn);
            player.sendMessage(Language.PREFIX + Language.SOTW_VOID_FIX);
        }

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!this.isActive() || !(event.getEntity() instanceof Player)) return;

        Player damager = PlayerUtils.getAttacker(event);
        if(damager == null) return;

        Player victim = (Player) event.getEntity();
        if(this.isPlayerSotwEnabled(victim) && this.isPlayerSotwEnabled(damager)) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if(!this.isActive() || !Config.SOTW_SPAWN_MOBS_FROM_SPAWNERS_ONLY) return;

        SpawnReason reason = event.getSpawnReason();
        if(ServerUtils.ALLOWED_SPAWN_REASONS.contains(reason)) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(!this.isActive() || !this.hidePlayers) return;

        Bukkit.getOnlinePlayers().forEach(online -> {
            if(!ClaimManager.getInstance().getFactionAt(online.getLocation()).isSafezone()) return;
            event.getPlayer().hidePlayer(online);
        });

        this.toggleSotwInvisibility(event.getPlayer(), ClaimManager.getInstance().getFactionAt(event.getPlayer()));
    }
}
