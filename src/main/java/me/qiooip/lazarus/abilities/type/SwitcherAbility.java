package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class SwitcherAbility extends AbilityItem {

    public SwitcherAbility(ConfigFile config) {
        super(AbilityType.SWITCHER, "SWITCHER", config);

        this.overrideActivationMessage();
    }

    @Override
    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        return false;
    }
}
