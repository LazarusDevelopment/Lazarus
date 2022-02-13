package me.qiooip.lazarus.tab.module;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.tab.PlayerTab;
import org.bukkit.configuration.ConfigurationSection;

@Getter
public abstract class TabModule {

    protected final boolean enabled;
    protected final int startSlot;

    public TabModule(String configSection) {
        ConfigFile tabConfig = Lazarus.getInstance().getTabFile();
        ConfigurationSection section = tabConfig.getSection(configSection);

        this.enabled = section.getBoolean("ENABLED");
        this.startSlot = section.getInt("START_SLOT");

        this.loadAdditionalData(section);
    }

    public abstract void apply(PlayerTab tab);

    public void loadAdditionalData(ConfigurationSection section) {

    }
}
