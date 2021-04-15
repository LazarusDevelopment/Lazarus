package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.abilities.event.ProjectileAbilityActivatedEvent;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.utils.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScramblerAbility extends AbilityItem implements Listener {

    private final ItemStack empty;
    private final String metadataName;

    public ScramblerAbility(ConfigFile config) {
        super(AbilityType.SCRAMBLER, "SCRAMBLER", config);

        this.empty = new ItemStack(Material.AIR);
        this.metadataName = "scrambler";
        this.removeOneItem = false;
        this.projectileAbility = true;
    }

    @Override
    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        player.setMetadata(this.metadataName, PlayerUtils.TRUE_METADATA_VALUE);
        return true;
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if(!(event.getEntity().getShooter() instanceof Player)) return;

        Projectile projectile = event.getEntity();

        Player player = (Player) projectile.getShooter();
        if(!player.hasMetadata(this.metadataName)) return;

        player.removeMetadata(this.metadataName, Lazarus.getInstance());
        projectile.setMetadata(this.metadataName, PlayerUtils.TRUE_METADATA_VALUE);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Projectile)) return;

        Projectile projectile = (Projectile) event.getDamager();
        if(!(projectile.getShooter() instanceof Player) || !projectile.hasMetadata(this.metadataName)) return;

        Player player = (Player) projectile.getShooter();
        projectile.removeMetadata(this.metadataName, Lazarus.getInstance());

        event.setCancelled(true);

        ProjectileAbilityActivatedEvent abilityEvent = new ProjectileAbilityActivatedEvent(player, projectile.getLocation(), this);

        if(abilityEvent.isCancelled()) {
            return;
        }

        Player target = (Player) event.getEntity();
        Player shooter = (Player) projectile.getShooter();

        if(this.isScrambleAllowed(shooter, target)) {
            this.shuffleInventory(target);
            target.damage(0, shooter);
        }
    }

    private void shuffleInventory(Player player) {
        List<ItemStack> playerHotbar = new ArrayList<>(9);
        PlayerInventory inventory = player.getInventory();

        for(int i = 0; i < 9; i++) {
            playerHotbar.add(inventory.getItem(i));
        }

        Collections.shuffle(playerHotbar);

        for(int i = 0; i < playerHotbar.size(); i++) {
            ItemStack item = playerHotbar.get(i);
            inventory.setItem(i, item == null ? this.empty : item);
        }

        player.updateInventory();
        playerHotbar.clear();
    }

    private boolean isScrambleAllowed(Player shooter, Player target) {
        Faction factionAtShooter = ClaimManager.getInstance().getFactionAt(shooter);

        if(factionAtShooter.isSafezone()) {
            this.handleAbilityRefund(shooter, Language.ABILITIES_PREFIX + Language.ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_SAFEZONE);
            return false;
        }

        Faction factionAtPlayer = ClaimManager.getInstance().getFactionAt(target);

        if(factionAtPlayer.isSafezone()) {
            this.handleAbilityRefund(shooter, Language.ABILITIES_PREFIX + Language.ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_SAFEZONE_TARGET);
            return false;
        }

        if(TimerManager.getInstance().getPvpProtTimer().isActive(shooter)) {
            this.handleAbilityRefund(shooter, Language.ABILITIES_PREFIX + Language.ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_PVP_TIMER);
            return false;
        }

        if(TimerManager.getInstance().getPvpProtTimer().isActive(target)) {
            this.handleAbilityRefund(shooter, Language.ABILITIES_PREFIX + Language.ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_PVP_TIMER_TARGET);
            return false;
        }

        if(Lazarus.getInstance().getSotwHandler().isUnderSotwProtection(shooter)) {
            this.handleAbilityRefund(shooter, Language.ABILITIES_PREFIX + Language.ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_SOTW);
            return false;
        }

        if(Lazarus.getInstance().getSotwHandler().isUnderSotwProtection(target)) {
            this.handleAbilityRefund(shooter, Language.ABILITIES_PREFIX + Language.ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_SOTW_TARGET);
            return false;
        }

        PlayerFaction damagerFaction = FactionsManager.getInstance().getPlayerFaction(shooter);

        if(damagerFaction != null) {
            PlayerFaction targetFaction = FactionsManager.getInstance().getPlayerFaction(target);

            if(damagerFaction == targetFaction) {
                shooter.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_TEAMMATES);
                return false;
            }

            if(damagerFaction.isAlly(targetFaction)) {
                shooter.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_SCRAMBLER_SCRAMBLE_DENIED_ALLIES);
                return false;
            }
        }

        return true;
    }
}
