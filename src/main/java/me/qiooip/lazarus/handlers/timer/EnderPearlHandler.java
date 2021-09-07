package me.qiooip.lazarus.handlers.timer;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.AbilitiesManager;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.abilities.type.FakePearlAbility;
import me.qiooip.lazarus.abilities.type.FastPearlAbility;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.scoreboard.EnderPearlTimer;
import me.qiooip.lazarus.utils.item.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnderPearlHandler extends Handler implements Listener {

    private final Map<UUID, Entity> killCheck;

    public EnderPearlHandler() {
        this.killCheck = new HashMap<>();
    }

    @Override
    public void disable() {
        this.killCheck.clear();
    }

    private ItemStack handleAbilityRefund(Player player, AbilityType abilityType) {
        TimerManager timerManager = TimerManager.getInstance();
        timerManager.getGlobalAbilitiesTimer().cancel(player);
        timerManager.getAbilitiesTimer().cancel(player, abilityType);

        return AbilitiesManager.getInstance().getAbilityItemByType(abilityType).getItem();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if(!Config.ENDER_PEARL_COOLDOWN_ENABLED || event.getEntity().getType() != EntityType.ENDER_PEARL) return;
        if(!(event.getEntity().getShooter() instanceof Player)) return;

        Player player = (Player) event.getEntity().getShooter();

        this.killCheck.put(player.getUniqueId(), event.getEntity());
        int cooldown = Config.ENDER_PEARL_COOLDOWN_TIME;

        ItemStack itemInHand = player.getItemInHand();
        AbilityItem abilityItem = AbilitiesManager.getInstance().getAbilityItem(itemInHand);

        if(abilityItem instanceof FastPearlAbility) {
            FastPearlAbility fastPearlAbility = (FastPearlAbility) abilityItem;

            if(fastPearlAbility.hasFastPearlMetadata(player)) {
                cooldown = ((FastPearlAbility) abilityItem).getReducedDuration();
            }

            fastPearlAbility.removeFastPearlMetadata(player);
        }

        if(!(abilityItem instanceof FakePearlAbility) || ((FakePearlAbility) abilityItem).isActivateEnderpearlTimer()) {
            TimerManager.getInstance().getEnderPearlTimer().activate(player, cooldown);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.useInteractedBlock() == Result.DENY && event.useItemInHand() == Result.DENY) return;
        if(!Config.ENDER_PEARL_COOLDOWN_ENABLED || !event.hasItem()) return;

        Player player = event.getPlayer();
        if(player.getGameMode() == GameMode.CREATIVE || event.getItem().getType() != Material.ENDER_PEARL) return;
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        EnderPearlTimer timer = TimerManager.getInstance().getEnderPearlTimer();

        if(timer.isActive(player)) {
            event.setUseItemInHand(Result.DENY);
            player.updateInventory();

            player.sendMessage(Language.PREFIX + Language.ENDERPEARL_DENY_MESSAGE.replace("<seconds>", timer.getTimeLeft(player)));
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        if(projectile.getType() != EntityType.ENDER_PEARL) return;
        if(!(projectile.getShooter() instanceof Player)) return;

        Player player = (Player) projectile.getShooter();
        this.killCheck.remove(player.getUniqueId());

        if(Config.ENDER_PEARL_REFUND_ENDER_PEARL_ON_CANCEL && player.hasMetadata("enderpearlRefund")) {
            ItemStack enderpearlItem;

            if(projectile.hasMetadata("fakePearl")) {
                enderpearlItem = this.handleAbilityRefund(player, AbilityType.FAKE_PEARL);
            } else if(projectile.hasMetadata("fastPearl")) {
                enderpearlItem = this.handleAbilityRefund(player, AbilityType.FAST_PEARL);
            } else {
                enderpearlItem = new ItemBuilder(Material.ENDER_PEARL).build();
            }

            player.getInventory().addItem(enderpearlItem);
            player.removeMetadata("enderpearlRefund", Lazarus.getInstance());
        }
    }

    @EventHandler
    public void onEnderpearlCancel(PlayerTeleportEvent event) {
        if(!Config.ENDER_PEARL_COOLDOWN_ENABLED) return;
        if(event.getCause() != TeleportCause.ENDER_PEARL || !event.isCancelled()) return;

        TimerManager.getInstance().getEnderPearlTimer().cancel(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        TimerManager.getInstance().getEnderPearlTimer().cancel(event.getEntity());

        Entity entity = this.killCheck.remove(event.getEntity().getUniqueId());
        if(entity != null) entity.remove();
    }
}
