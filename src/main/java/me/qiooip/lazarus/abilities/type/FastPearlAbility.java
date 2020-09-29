package me.qiooip.lazarus.abilities.type;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.utils.PlayerUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class FastPearlAbility extends AbilityItem implements Listener {

    @Getter private int reducedDuration;
    private final String metadataName;

    public FastPearlAbility(ConfigFile config) {
        super(AbilityType.FAST_PEARL, "FAST_PEARL", config);

        this.metadataName = "fastPearl";
        this.removeOneItem = false;

        this.overrideActivationMessage();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.reducedDuration = abilitySection.getInt("ENDERPEARL_COOLDOWN");
    }

    public void sendActivationMessage(Player player, int duration) {
        this.activationMessage.forEach(line -> player.sendMessage(line
            .replace("<abilityName>", this.displayName)
            .replace("<duration>", DurationFormatUtils.formatDurationWords(duration * 1000, true, true))
            .replace("<cooldown>", DurationFormatUtils.formatDurationWords(this.cooldown * 1000, true, true))));
    }

    @Override
    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        if(player.getGameMode() == GameMode.CREATIVE) {
            return false;
        }

        player.setMetadata(this.metadataName, PlayerUtils.TRUE_METADATA_VALUE);
        this.sendActivationMessage(player, this.reducedDuration);
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
}
