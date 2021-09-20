package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.Config;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.Tasks;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PrePearlAbility extends AbilityItem implements Listener {

    private final String cooldownName;

    private int teleportAfter;
    private boolean activateEnderpearlTimer;

    public PrePearlAbility(ConfigFile config) {
        super(AbilityType.PRE_PEARL, "PRE_PEARL", config);

        this.cooldownName = "PrePearl";

        this.overrideActivationMessage();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.teleportAfter = abilitySection.getInt("TELEPORT_AFTER");
        this.activateEnderpearlTimer = abilitySection.getBoolean("ACTIVATE_ENDERPEARL_TIMER");
    }

    public void sendActivationMessage(Player player, int teleportAfter) {
        this.activationMessage.forEach(line -> player.sendMessage(line
            .replace("<abilityName>", this.displayName)
            .replace("<time>", String.valueOf(teleportAfter))
            .replace("<cooldown>", StringUtils.formatDurationWords(this.cooldown * 1000L))));
    }

    @Override
    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_HUGE, 1, 32);

        String message = Language.ABILITIES_PREFIX + Language.ABILITIES_PRE_PEARL_TELEPORTED;
        Location prePearlLocation = player.getLocation().clone();

        TimerManager.getInstance().getCooldownTimer().activate(player, this.cooldownName,
            this.teleportAfter, message, () -> Tasks.sync(() -> player.teleport(prePearlLocation)));

        if(Config.ENDER_PEARL_COOLDOWN_ENABLED && this.activateEnderpearlTimer) {
            TimerManager.getInstance().getEnderPearlTimer().reactivate(player);
        }

        this.sendActivationMessage(player, this.teleportAfter);

        event.setCancelled(true);
        return true;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        TimerManager.getInstance().getCooldownTimer().cancel(event.getPlayer(), this.cooldownName);
    }
}
