package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.utils.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SwitcherAbility extends AbilityItem implements Listener {

    private boolean switchWithTeammates;
    private boolean switchWithAllies;

    private final String metadataName;

    public SwitcherAbility(ConfigFile config) {
        super(AbilityType.SWITCHER, "SWITCHER", config);

        this.metadataName = "switcher";
        this.removeOneItem = false;

        this.overrideActivationMessage();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.switchWithTeammates = abilitySection.getBoolean("SWITCH_WITH_TEAMMATES");
        this.switchWithAllies = abilitySection.getBoolean("SWITCH_WITH_ALLIES");
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

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Projectile)) return;

        Projectile projectile = (Projectile) event.getDamager();
        if(!projectile.hasMetadata(this.metadataName)) return;

        projectile.removeMetadata(this.metadataName, Lazarus.getInstance());

        Player shooter = (Player) projectile.getShooter();
        Player player = (Player) event.getEntity();

        PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFaction(player);
        PlayerFaction damagerFaction = FactionsManager.getInstance().getPlayerFaction(shooter);

        if(damagerFaction != null) {
            if(!this.switchWithTeammates && damagerFaction == playerFaction) {
                this.handleAbilityRefund(shooter);
                return;
            }

            if(!this.switchWithAllies && damagerFaction.isAlly(playerFaction)) {
                this.handleAbilityRefund(shooter);
                return;
            }
        }

        Location shooterLocation = shooter.getLocation();

        shooter.teleport(player.getLocation());
        player.teleport(shooterLocation);
    }

    private void handleAbilityRefund(Player player) {
        TimerManager timerManager = TimerManager.getInstance();
        timerManager.getGlobalAbilitiesTimer().cancel(player);
        timerManager.getAbilitiesTimer().cancel(player, this.type);

        player.getInventory().addItem(this.getItem());
    }
}
