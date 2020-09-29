package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.utils.PlayerUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
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
        this.maxDistance = abilitySection.getInt("MAX_DISTANCE");
        this.switchWithTeammates = abilitySection.getBoolean("SWITCH_WITH_TEAMMATES");
        this.switchWithAllies = abilitySection.getBoolean("SWITCH_WITH_ALLIES");
    }

    public void sendActivationMessage(Player player, int distance) {
        this.activationMessage.forEach(line -> player.sendMessage(line
            .replace("<abilityName>", this.displayName)
            .replace("<maxDistance>", String.valueOf(distance))
            .replace("<cooldown>", DurationFormatUtils.formatDurationWords(this.cooldown * 1000, true, true))));
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
        if(!projectile.hasMetadata(this.metadataName)) return;

        projectile.removeMetadata(this.metadataName, Lazarus.getInstance());

        event.setCancelled(true);

        Player player = (Player) event.getEntity();
        Player shooter = (Player) projectile.getShooter();

        if(!this.isSwitchAllowed(shooter, player)) return;

        PlayerFaction damagerFaction = FactionsManager.getInstance().getPlayerFaction(shooter);
        PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFaction(player);

        if(damagerFaction != null) {
            if(!this.switchWithTeammates && damagerFaction == playerFaction) {
                shooter.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_SWITCHER_SWITCH_DENIED_TEAMMATES);
                return;
            }

            if(!this.switchWithAllies && damagerFaction.isAlly(playerFaction)) {
                shooter.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_SWITCHER_SWITCH_DENIED_ALLIES);
                return;
            }
        }

        Location shooterLocation = shooter.getLocation();

        shooter.teleport(player.getLocation());
        player.teleport(shooterLocation);
    }

    private boolean isSwitchAllowed(Player shooter, Player target) {
        if(shooter.getLocation().distance(target.getLocation()) > this.maxDistance) {
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

        return true;
    }

    private void handleAbilityRefund(Player player, String message) {
        TimerManager.getInstance().getGlobalAbilitiesTimer().cancel(player);
        TimerManager.getInstance().getAbilitiesTimer().cancel(player, this.type);

        player.getInventory().addItem(this.getItem());
        player.sendMessage(message);
    }
}
