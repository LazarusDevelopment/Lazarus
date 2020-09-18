package me.qiooip.lazarus.abilities.type;

import lombok.Getter;
import me.qiooip.lazarus.abilities.AbilityItem;
import me.qiooip.lazarus.abilities.AbilityType;
import me.qiooip.lazarus.config.ConfigFile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class FakePearlAbility extends AbilityItem {

    @Getter private boolean cooldown;

    public FakePearlAbility(ConfigFile config) {
        super(AbilityType.FAKE_PEARL, "FAKE_PEARL", config);
    }

    @Override
    protected void loadAdditionalData(ConfigurationSection section) {
        this.cooldown = section.getBoolean("ENDERPEARL_COOLDOWN");
    }

    @Override
    protected void onItemClick(Player player) { }
}
