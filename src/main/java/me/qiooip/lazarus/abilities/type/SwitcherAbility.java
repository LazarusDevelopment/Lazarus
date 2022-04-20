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
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SwitcherAbility extends AbilityItem implements Listener {

    private int maxDistance;
    private int maxDistanceSquared;
    private boolean switchWithTeammates;
    private boolean switchWithAllies;

    private final String metadataName;

    public SwitcherAbility(ConfigFile config) {
        super(AbilityType.SWITCHER, "SWITCHER", config);

        this.metadataName = "switcher";
        this.removeOneItem = false;
        this.projectileAbility = true;

        this.overrideActivationMessage();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.maxDistance = abilitySection.getInt("MAX_DISTANCE");
        this.maxDistanceSquared = this.maxDistance * this.maxDistance;
        this.switchWithTeammates = abilitySection.getBoolean("SWITCH_WITH_TEAMMATES");
        this.switchWithAllies = abilitySection.getBoolean("SWITCH_WITH_ALLIES");
    }

    public void sendActivationMessage(Player player, int distance) {
        this.activationMessage.forEach(line -> player.sendMessage(line
            .replace("<abilityName>", this.displayName)
            .replace("<maxDistance>", String.valueOf(distance))
            .replace("<cooldown>", StringUtils.formatDurationWords(this.cooldown * 1000L))));
    }

    @Override
    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        player.setMetadata(this.metadataName, PlayerUtils.TRUE_METADATA_VALUE);
        this.sendActivationMessage(player, this.maxDistance);
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

        if(this.isSwitchAllowed(shooter, target)) {
            this.switchPositions(shooter, target);
            target.damage(0, shooter);
        }
    }

    private void switchPositions(Player shooter, Player target) {
        Location shooterLocation = shooter.getLocation();

        shooter.teleport(target.getLocation());
        target.teleport(shooterLocation);
    }

    private boolean isSwitchAllowed(Player shooter, Player target) {
        if(shooter.getLocation().distanceSquared(target.getLocation()) > this.maxDistanceSquared) {
            shooter.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_SWITCHER_SWITCH_DENIED_DISTANCE_TOO_FAR);
            return false;
        }

        Faction factionAtShooter = ClaimManager.getInstance().getFactionAt(shooter);

        if(factionAtShooter.isSafezone()) {
            this.handleAbilityRefund(shooter, Language.ABILITIES_PREFIX + Language.ABILITIES_SWITCHER_SWITCH_DENIED_SAFEZONE);
            return false;
        }

        Faction factionAtPlayer = ClaimManager.getInstance().getFactionAt(target);

        if(factionAtPlayer.isSafezone()) {
            this.handleAbilityRefund(shooter, Language.ABILITIES_PREFIX + Language.ABILITIES_SWITCHER_SWITCH_DENIED_SAFEZONE_TARGET);
            return false;
        }

        if(TimerManager.getInstance().getPvpProtTimer().isActive(shooter)) {
            this.handleAbilityRefund(shooter, Language.ABILITIES_PREFIX + Language.ABILITIES_SWITCHER_SWITCH_DENIED_PVP_TIMER);
            return false;
        }

        if(TimerManager.getInstance().getPvpProtTimer().isActive(target)) {
            this.handleAbilityRefund(shooter, Language.ABILITIES_PREFIX + Language.ABILITIES_SWITCHER_SWITCH_DENIED_PVP_TIMER_TARGET);
            return false;
        }

        if(Lazarus.getInstance().getSotwHandler().isUnderSotwProtection(shooter)) {
            this.handleAbilityRefund(shooter, Language.ABILITIES_PREFIX + Language.ABILITIES_SWITCHER_SWITCH_DENIED_SOTW);
            return false;
        }

        if(Lazarus.getInstance().getSotwHandler().isUnderSotwProtection(target)) {
            this.handleAbilityRefund(shooter, Language.ABILITIES_PREFIX + Language.ABILITIES_SWITCHER_SWITCH_DENIED_SOTW_TARGET);
            return false;
        }

        PlayerFaction damagerFaction = FactionsManager.getInstance().getPlayerFaction(shooter);

        if(damagerFaction != null) {
            PlayerFaction targetFaction = FactionsManager.getInstance().getPlayerFaction(target);

            if(!this.switchWithTeammates && damagerFaction == targetFaction) {
                shooter.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_SWITCHER_SWITCH_DENIED_TEAMMATES);
                return false;
            }

            if(!this.switchWithAllies && damagerFaction.isAlly(targetFaction)) {
                shooter.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_SWITCHER_SWITCH_DENIED_ALLIES);
                return false;
            }
        }

        return true;
    }
}
