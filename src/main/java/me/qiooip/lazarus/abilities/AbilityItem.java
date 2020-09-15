package me.qiooip.lazarus.abilities;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.utils.item.ItemBuilder;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public abstract class AbilityItem {

    private final String name;
    private final String configSection;

    private ItemStack item;
    private int cooldown;
    private boolean enabled;

    public AbilityItem(String name, String configSection, ConfigFile config) {
        this.name = name;
        this.configSection = configSection;

        this.loadAbilityData(config);
    }

    public void loadAbilityData(ConfigFile config) {
        ConfigurationSection section = config.getConfigurationSection(this.configSection);
        ConfigurationSection itemSection = section.getConfigurationSection("ITEM");

        ItemStack itemStack = ItemUtils.parseItem(itemSection.getString("MATERIAL_ID"));

        if(itemStack == null) {
            Lazarus.getInstance().log("&cCould not parse ability item for '&4" + this.name + "&c'");
            return;
        }

        ItemBuilder builder = new ItemBuilder(itemStack)
            .setName(itemSection.getString("NAME"))
            .setLore(itemSection.getStringList("LORE"));

        if(itemSection.getBoolean("ENCHANTED_GLOW")) {
            builder.addFakeGlow();
        }

        this.item = builder.build();
        this.cooldown = section.getInt("COOLDOWN");
        this.enabled = section.getBoolean("ENABLED");
    }

    protected abstract void onItemClick(Player player);
}
