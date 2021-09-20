package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.abilities.event.ProjectileAbilityActivatedEvent;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.utils.PlayerUtils;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashSet;
import java.util.Set;

public class WebGunAbility extends AbilityItem implements Listener {

    private final String cooldownName;
    private final String metadataName;

    private int duration;
    private int radius;

    public WebGunAbility(ConfigFile config) {
        super(AbilityType.WEB_GUN, "WEB_GUN", config);

        this.cooldownName = "PrePearl";
        this.metadataName = "webGun";
        this.removeOneItem = false;
        this.projectileAbility = true;

        this.overrideActivationMessage();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.duration = abilitySection.getInt("DURATION");
        this.radius = abilitySection.getInt("RADIUS");
    }

    public void sendActivationMessage(Player player, int radius) {
        this.activationMessage.forEach(line -> player.sendMessage(line
                .replace("<abilityName>", this.displayName)
                .replace("<radius>", String.valueOf(radius))
                .replace("<cooldown>", StringUtils.formatDurationWords(this.cooldown * 1000L))));
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
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if(!(projectile.getShooter() instanceof Player) || !projectile.hasMetadata(this.metadataName)) return;

        Player player = (Player) projectile.getShooter();
        projectile.removeMetadata(this.metadataName, Lazarus.getInstance());

        ProjectileAbilityActivatedEvent abilityEvent = new ProjectileAbilityActivatedEvent(player, projectile.getLocation(), this);

        if(abilityEvent.isCancelled()) {
            return;
        }

        this.handleCobwebCreation(projectile);
    }

    private void handleCobwebCreation(Projectile projectile) {
        double radius = this.radius / 2.0D;

        int min = (int) Math.floor(radius);
        int max = (int) Math.ceil(radius);

        Set<Location> blocks = new HashSet<>();
        Location land = projectile.getLocation();

        for(int x = land.getBlockX() - min; x < land.getBlockX() + max; x++) {
            for(int z = land.getBlockZ() - min; z < land.getBlockZ() + max; z++) {
                Location location = new Location(land.getWorld(), x, land.getBlockY(), z);
                if(location.getBlock().getType() != Material.AIR) continue;

                location.getBlock().setType(Material.WEB);
                blocks.add(location);
            }
        }

        Player shooter = (Player) projectile.getShooter();
        String message = Language.ABILITIES_PREFIX + Language.ABILITIES_WEB_GUN_COBWEB_REMOVE;

        TimerManager.getInstance().getCooldownTimer().activate(shooter, this.cooldownName, this.duration,
            message, () -> Tasks.sync(() -> blocks.forEach(location -> location.getBlock().setType(Material.AIR))));

        this.sendActivationMessage(shooter, this.radius);
    }
}
