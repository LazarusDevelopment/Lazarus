package me.qiooip.lazarus.abilities.type;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.utils.PlayerUtils;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
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

    public boolean hasFastPearlMetadata(Player player) {
        return player.hasMetadata(this.metadataName);
    }

    public void removeFastPearlMetadata(Player player) {
        player.removeMetadata(this.metadataName, Lazarus.getInstance());
    }

    public void sendActivationMessage(Player player, int duration) {
        this.activationMessage.forEach(line -> player.sendMessage(line
            .replace("<abilityName>", this.displayName)
            .replace("<duration>", StringUtils.formatDurationWords(duration * 1000L))
            .replace("<cooldown>", StringUtils.formatDurationWords(this.cooldown * 1000L))));
    }

    @Override
    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        if(player.getGameMode() == GameMode.CREATIVE) {
            return false;
        }

        if(TimerManager.getInstance().getEnderPearlTimer().isActive(player)) {
            return false;
        }

        player.setMetadata(this.metadataName, PlayerUtils.TRUE_METADATA_VALUE);
        this.sendActivationMessage(player, this.reducedDuration);
        return true;
    }
}
