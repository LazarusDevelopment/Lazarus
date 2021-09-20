package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class AntiAbilityBallAbility extends AbilityItem {

    private int radius;
    private int duration;

    public AntiAbilityBallAbility(ConfigFile config) {
        super(AbilityType.ANTI_ABILITY_BALL, "ANTI_ABILITY_BALL", config);

        this.overrideActivationMessage();
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection abilitySection) {
        this.radius = abilitySection.getInt("RADIUS");
        this.duration = abilitySection.getInt("DURATION");
    }

    public void sendActivationMessage(Player player, int radius, int duration) {
        this.activationMessage.forEach(line -> player.sendMessage(line
            .replace("<abilityName>", this.displayName)
            .replace("<radius>", String.valueOf(radius))
            .replace("<duration>", StringUtils.formatDurationWords(duration * 1000L))
            .replace("<cooldown>", StringUtils.formatDurationWords(this.cooldown * 1000L))));
    }

    @Override
    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        player.getNearbyEntities(this.radius, this.radius, this.radius).forEach(entity -> {
            if(!(entity instanceof Player) || entity == player) return;

            this.activateAbilityOnTarget(player, (Player) entity);
        });

        this.sendActivationMessage(player, this.radius, this.duration);

        event.setCancelled(true);
        return true;
    }

    private void activateAbilityOnTarget(Player damager, Player target) {
        TimerManager.getInstance().getGlobalAbilitiesTimer().activate(target, this.duration);

        target.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_ANTI_ABILITY_BALL_TARGET_ACTIVATED
            .replace("<player>", damager.getName())
            .replace("<abilityName>", this.displayName)
            .replace("<duration>", StringUtils.formatDurationWords(this.duration * 1000L)));
    }
}
