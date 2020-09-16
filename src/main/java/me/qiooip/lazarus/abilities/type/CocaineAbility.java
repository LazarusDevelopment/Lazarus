package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import org.bukkit.entity.Player;

public class CocaineAbility extends AbilityItem {

    public CocaineAbility(ConfigFile config) {
        super(AbilityType.COCAINE, "COCAINE", config);
    }

    @Override
    protected void onItemClick(Player player) {

    }
}
