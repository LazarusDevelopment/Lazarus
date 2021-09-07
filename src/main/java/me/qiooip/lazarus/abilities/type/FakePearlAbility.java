package me.qiooip.lazarus.abilities.type;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.scoreboard.EnderPearlTimer;
import me.qiooip.lazarus.utils.PlayerUtils;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class FakePearlAbility extends AbilityItem implements Listener {

    private final String metadataName;
    @Getter private boolean activateEnderpearlTimer;

    public FakePearlAbility(ConfigFile config) {
        super(AbilityType.FAKE_PEARL, "FAKE_PEARL", config);

        this.metadataName = "fakePearl";
        this.removeOneItem = false;
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.activateEnderpearlTimer = abilitySection.getBoolean("ACTIVATE_ENDERPEARL_TIMER");
    }

    @Override
    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        if(player.getGameMode() == GameMode.CREATIVE) {
            return false;
        }

        if(TimerManager.getInstance().getEnderPearlTimer().isActive(player)) {
            return false;
        }

        if(Config.ENDER_PEARL_COOLDOWN_ENABLED && this.activateEnderpearlTimer) {
            EnderPearlTimer enderPearlTimer = TimerManager.getInstance().getEnderPearlTimer();
            enderPearlTimer.cancel(player);

            Tasks.sync(() -> enderPearlTimer.activate(player));
        }

        player.setMetadata(this.metadataName, PlayerUtils.TRUE_METADATA_VALUE);
        return true;
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if(!(event.getEntity().getShooter() instanceof Player)) return;

        Projectile projectile = event.getEntity();

        Player player = (Player) projectile.getShooter();
        if(!player.hasMetadata(this.metadataName)) return;

        projectile.setMetadata(this.metadataName, PlayerUtils.TRUE_METADATA_VALUE);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        if(player.hasMetadata(this.metadataName)) {
            player.removeMetadata(this.metadataName, Lazarus.getInstance());
            event.setCancelled(true);
        }
    }
}
