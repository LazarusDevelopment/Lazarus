package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import org.bukkit.entity.Player;

public class PocketBardAbility extends AbilityItem {

    public PocketBardAbility(ConfigFile config) {
        super(AbilityType.POCKET_BARD, "POCKET_BARD", config);
    }

    @Override
    protected void onItemClick(Player player) {

    }
}
