package me.qiooip.lazarus.handlers.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.event.freeze.FreezePlayerEvent;
import me.qiooip.lazarus.handlers.event.freeze.FreezeType;
import me.qiooip.lazarus.handlers.event.freeze.UnfreezePlayerEvent;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.utils.Messages;
import me.qiooip.lazarus.utils.PlayerUtils;
import me.qiooip.lazarus.utils.nms.NmsUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FreezeHandler extends Handler implements Listener {

    private boolean freezeAll;
    private final Map<UUID, FrozenTask> frozen;

    public FreezeHandler() {
        this.frozen = new HashMap<>();
    }

    @Override
    public void disable() {
        this.frozen.clear();
    }

    public void toggleFreeze(CommandSender sender, Player target) {
        if(this.isFrozen(target)) {
            this.unfreezePlayer(sender, target);
            return;
        }

        this.freezePlayer(sender, target);
    }

    private void freezePlayer(CommandSender sender, Player target) {
        FreezePlayerEvent event = new FreezePlayerEvent(sender, target, FreezeType.PLAYER);
        if(event.isCancelled()) return;

        this.frozen.put(target.getUniqueId(), new FrozenTask(target));

        target.setWalkSpeed(0F);
        target.setFoodLevel(0);
        target.setSprinting(false);
        target.setAllowFlight(false);
        target.setFlying(false);
        target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128));

        Messages.sendMessage(Language.PREFIX + Language.FREEZE_FREEZED_STAFF_MESSAGE
            .replace("<target>", target.getName())
            .replace("<player>", sender.getName()), "lazarus.staff");
    }

    private void unfreezePlayer(CommandSender sender, Player target) {
        if(this.freezeAll) return;

        UnfreezePlayerEvent event = new UnfreezePlayerEvent(sender, target, FreezeType.PLAYER);
        if(event.isCancelled()) return;

        this.frozen.remove(target.getUniqueId()).cancel();

        target.setWalkSpeed(0.2F);
        target.setFoodLevel(20);
        target.removePotionEffect(PotionEffectType.JUMP);

        Messages.sendMessage(Language.PREFIX + Language.FREEZE_UNFREEZED_STAFF_MESSAGE
            .replace("<target>", target.getName())
            .replace("<player>", sender.getName()), "lazarus.staff");

        target.sendMessage(Language.PREFIX + Language.FREEZE_UNFREEZED_MESSAGE_TARGET
            .replace("<player>", sender.getName()));
    }

    public void toggleFreezeAll(CommandSender sender) {
        if(this.freezeAll) {
            this.unfreezeAll(sender);
        } else {
            this.freezeAll(sender);
        }
    }

    private void freezeAllPlayer(Player player) {
        if(player.hasPermission("lazarus.staff")) return;

        FreezePlayerEvent event = new FreezePlayerEvent(Bukkit.getConsoleSender(), player, FreezeType.SERVER);
        if(event.isCancelled()) return;

        this.frozen.put(player.getUniqueId(), null);

        player.setWalkSpeed(0F);
        player.setFoodLevel(0);
        player.setSprinting(false);
        player.setAllowFlight(false);
        player.setFlying(false);

        NmsUtils.getInstance().addPotionEffect(player, new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128));
    }

    private void freezeAll(CommandSender sender) {
        Bukkit.getOnlinePlayers().forEach(this::freezeAllPlayer);

        this.freezeAll = true;
        Messages.sendMessage(Language.FREEZE_FREEZED_ALL.replace("<player>", sender.getName()));
    }

    private void unfreezeAll(CommandSender sender) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if(!this.isFrozen(player)) return;

            UnfreezePlayerEvent event = new UnfreezePlayerEvent(Bukkit.getConsoleSender(), player, FreezeType.SERVER);
            if(event.isCancelled()) return;

            this.frozen.remove(player.getUniqueId());
            player.setWalkSpeed(0.2F);
            player.setFoodLevel(20);
            player.removePotionEffect(PotionEffectType.JUMP);
        });

        this.freezeAll = false;
        Messages.sendMessage(Language.FREEZE_UNFREEZED_ALL.replace("<player>", sender.getName()));
    }

    public boolean isFrozen(Player player) {
        return this.frozen.containsKey(player.getUniqueId());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        Player attacker = PlayerUtils.getAttacker(event);
        if(attacker == null) return;

        if(this.isFrozen(attacker)) {
            event.setCancelled(true);

            attacker.sendMessage(Language.PREFIX + Language.FREEZE_PVP_DENY_MESSAGE_DAMAGER);
            return;
        }

        if(this.isFrozen(player)) {
            event.setCancelled(true);

            attacker.sendMessage(Language.PREFIX + Language.FREEZE_PVP_DENY_MESSAGE_VICTIM
                .replace("<player>", player.getName()));
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(this.isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if(this.isFrozen(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if(!this.isFrozen(event.getPlayer())) return;

        if(Config.FREEZE_DISABLED_COMMANDS.stream().noneMatch(command -> event
        .getMessage().toLowerCase().startsWith(command.toLowerCase()))) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(Language.PREFIX + Language.FREEZE_DISABLED_COMMAND_MESSAGE);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if(this.isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if(this.isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if(this.isFrozen((Player) event.getWhoClicked())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemDrop(PlayerDropItemEvent event) {
        if(this.isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if(this.isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(this.freezeAll) {
            this.freezeAllPlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(!this.isFrozen(player)) return;

        player.setWalkSpeed(0.2F);
        player.setFoodLevel(20);
        player.removePotionEffect(PotionEffectType.JUMP);

        if(!this.freezeAll) {
            this.frozen.get(player.getUniqueId()).cancel();
        }

        this.frozen.remove(player.getUniqueId());

        Messages.sendMessage(Language.PREFIX + Language.FREEZE_QUIT_WHEN_FROZEN
            .replace("<player>", player.getName()), "lazarus.staff");
    }

    static class FrozenTask extends BukkitRunnable {

        private final UUID uuid;

        FrozenTask(Player player) {
            this.uuid = player.getUniqueId();
            this.runTaskTimerAsynchronously(Lazarus.getInstance(), 0L, Config.FREEZE_MESSAGE_INTERVAL * 20L);
        }

        @Override
        public void run() {
            Player player = Bukkit.getPlayer(this.uuid);

            if(player != null) {
                Language.FROZEN_MESSAGE.forEach(player::sendMessage);
            }
        }
    }
}
