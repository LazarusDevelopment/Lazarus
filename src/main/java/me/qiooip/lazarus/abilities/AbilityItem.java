package me.qiooip.lazarus.abilities;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.item.ItemBuilder;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public abstract class AbilityItem {

    protected final AbilityType type;
    protected final String configSection;

    protected String displayName;
    protected int cooldown;
    protected boolean enabled;
    protected ItemStack item;

    private boolean overrideActivationMessage;
    protected List<String> activationMessage;

    public AbilityItem(AbilityType type, String configSection, ConfigFile config) {
        this.type = type;
        this.configSection = configSection;

        this.loadAbilityData(config);
        this.loadActivationMessage();
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
        this.cooldown = section.getInt("COOLDOWN");
        this.enabled = section.getBoolean("ENABLED");

        this.displayName = Color.translate(section.getString("DISPLAY_NAME"));

        try {
            Field displayNameField = this.type.getClass().getDeclaredField("displayName");
            displayNameField.setAccessible(true);

            displayNameField.set(this.type, this.displayName);
        } catch(ReflectiveOperationException e) {
            e.printStackTrace();
        }

        this.loadAdditionalData(section);
    }

    protected void disable() { }

    private void loadActivationMessage() {
        ConfigFile language = Lazarus.getInstance().getLanguage();
        String messagePath = "ABILITIES." + this.configSection + "_ABILITY.ACTIVATED";

        this.activationMessage = language.getStringList(messagePath);
    }

    public void sendActivationMessage(Player player) {
        if(this.overrideActivationMessage) {
            return;
        }

        this.activationMessage.forEach(line -> player.sendMessage(line
            .replace("<abilityName>", this.displayName)
            .replace("<cooldown>", DurationFormatUtils.formatDurationWords(this.cooldown * 1000, true, true))));
    }

    protected void overrideActivationMessage() {
        this.overrideActivationMessage = true;
    }

    protected void loadAdditionalData(ConfigurationSection abilitySection) {

    }

    protected boolean onProjectileClick(Player player, Projectile projectile) {
        return false;
    }

    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        return false;
    }

    protected boolean onPlayerItemHit(Player damager, Player target, EntityDamageByEntityEvent event) {
        return false;
    }
}
