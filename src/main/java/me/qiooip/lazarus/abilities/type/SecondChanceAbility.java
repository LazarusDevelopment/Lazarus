package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.scoreboard.EnderPearlTimer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class SecondChanceAbility extends AbilityItem {

    public SecondChanceAbility(ConfigFile config) {
        super(AbilityType.SECOND_CHANCE, "SECOND_CHANCE", config);
    }

    @Override
    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        EnderPearlTimer enderPearlTimer = TimerManager.getInstance().getEnderPearlTimer();

        if(!enderPearlTimer.isActive(player)) {
            player.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_SECOND_CHANCE_NOT_ON_COOLDOWN);
            return false;
        }

        enderPearlTimer.cancel(player);

        event.setCancelled(true);
        return true;
    }
}
