package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.cooldown.CooldownTimer;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GuardianAngelAbility extends AbilityItem implements Listener {

    private int healthThreshold;
    private int duration;

    private final String cooldownName;

    public GuardianAngelAbility(ConfigFile config) {
        super(AbilityType.GUARDIAN_ANGEL, "GUARDIAN_ANGEL", config);

        this.cooldownName = "GuardianAngel";
        this.overrideActivationMessage();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.healthThreshold = abilitySection.getInt("HEALTH");
        this.duration = abilitySection.getInt("DURATION");
    }

    public void sendActivationMessage(Player player, int health) {
        this.activationMessage.forEach(line -> player.sendMessage(line
            .replace("<abilityName>", this.displayName)
            .replace("<amount>", String.valueOf(health / 2.0))
            .replace("<cooldown>", StringUtils.formatDurationWords(this.cooldown * 1000L))));
    }

    @Override
    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        TimerManager.getInstance().getCooldownTimer().activate(player, this.cooldownName, this.duration,
            Language.ABILITIES_PREFIX + Language.ABILITIES_GUARDIAN_ANGEL_EXPIRED);

        this.sendActivationMessage(player, this.healthThreshold);
        return true;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        CooldownTimer cooldownTimer = TimerManager.getInstance().getCooldownTimer();

        if(!cooldownTimer.isActive(player, this.cooldownName)) return;
        if(player.getHealth() > this.healthThreshold) return;

        player.setHealth(player.getMaxHealth());
        cooldownTimer.cancel(player, this.cooldownName);

        player.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_GUARDIAN_ANGEL_HEALED);
    }
}
