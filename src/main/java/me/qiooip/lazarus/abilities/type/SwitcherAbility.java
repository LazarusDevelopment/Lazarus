package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.config.ConfigFile;
import org.bukkit.entity.Player;

public class SwitcherAbility extends AbilityItem {

    public SwitcherAbility(ConfigFile config) {
        super("Switcher", "SWITCHER", config);
    }

    @Override
    protected void onItemClick(Player player) {

    }
}
