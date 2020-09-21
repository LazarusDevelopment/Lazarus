package me.qiooip.lazarus.abilities;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.item.ItemBuilder;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public abstract class AbilityItem implements Listener {

    private final AbilityType type;
    private final String configSection;

    private String displayName;
    private ItemStack item;
    private int cooldown;
    private boolean enabled;

    public AbilityItem(AbilityType type, String configSection, ConfigFile config) {
        this.type = type;
        this.configSection = configSection;

        this.loadAbilityData(config);

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void loadAbilityData(ConfigFile config) {
        ConfigurationSection section = config.getConfigurationSection(this.configSection);

        if(section == null) {
            Lazarus.getInstance().log("&7- &cCould not load configuration for '&4" + this.type.getName() + "&c'");
            return;
        }

        ConfigurationSection itemSection = section.getConfigurationSection("ITEM");
        ItemStack itemStack = ItemUtils.parseItem(itemSection.getString("MATERIAL_ID"));

        if(itemStack == null) {
            Lazarus.getInstance().log("&cCould not parse ability item for '&4" + this.type.getName() + "&c'");
            return;
        }

        List<String> itemLore = itemSection.getStringList("LORE")
            .stream().map(Color::translate).collect(Collectors.toList());

        ItemBuilder builder = new ItemBuilder(itemStack)
            .setName(itemSection.getString("NAME"))
            .setLore(itemLore);

        if(itemSection.getBoolean("ENCHANTED_GLOW")) {
            builder = builder.addFakeGlow();
        }

        this.item = builder.build();
        this.displayName = Color.translate(section.getString("DISPLAY_NAME"));
        this.cooldown = section.getInt("COOLDOWN");
        this.enabled = section.getBoolean("ENABLED");

        this.loadAdditionalData(section);
    }

    protected void loadAdditionalData(ConfigurationSection section) { }

    protected void onItemClick(Player player) { }

    protected boolean onPlayerItemHit(Player damager, Player target) { return false; }
}
