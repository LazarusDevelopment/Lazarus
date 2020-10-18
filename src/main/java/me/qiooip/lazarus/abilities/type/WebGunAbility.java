package me.qiooip.lazarus.abilities.type;

import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;

public class WebGunAbility extends AbilityItem {

    public WebGunAbility(ConfigFile config) {
        super(AbilityType.WEB_GUN, "WEB_GUN", config);
    }
}
