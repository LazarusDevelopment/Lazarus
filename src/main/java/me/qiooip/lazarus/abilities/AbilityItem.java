package me.qiooip.lazarus.abilities;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.StringUtils;
import me.qiooip.lazarus.utils.item.ItemBuilder;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public abstract class AbilityItem {

    protected final AbilityType type;
    protected final String configSection;

    protected String displayName;
    protected int cooldown;
    protected boolean enabled;
    protected ItemStack item;
    protected boolean removeOneItem;
    protected boolean projectileAbility;

    private boolean overrideActivationMessage;
    protected List<String> activationMessage;

    private final Map<UUID, Long> messageDelays;

    public AbilityItem(AbilityType type, String configSection, ConfigFile config) {
        this.type = type;
        this.configSection = configSection;

        this.removeOneItem = true;
        this.messageDelays = new HashMap<>();

        this.loadAbilityData(config);
        this.loadActivationMessage();
    }

    public void loadAbilityData(ConfigFile config) {
        ConfigurationSection section = config.getSection(this.configSection);

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

    protected void disable() {

    }

    protected void sendDelayedMessage(Player player, String message) {
        if(this.messageDelays.containsKey(player.getUniqueId()) && (this.messageDelays
                .get(player.getUniqueId()) - System.currentTimeMillis() > 0)) return;

        player.sendMessage(message);
        this.messageDelays.put(player.getUniqueId(), System.currentTimeMillis() + 1000L);
    }

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
            .replace("<cooldown>", StringUtils.formatDurationWords(this.cooldown * 1000L))));
    }

    protected void addEffects(Player player, List<PotionEffect> effects) {
        for(PotionEffect effect : effects) {
            Lazarus.getInstance().getPvpClassManager().addPotionEffect(player, effect);
        }
    }

    public void handleAbilityRefund(Player player, String message) {
        TimerManager.getInstance().getGlobalAbilitiesTimer().cancel(player);
        TimerManager.getInstance().getAbilitiesTimer().cancel(player, this.type);

        player.getInventory().addItem(this.getItem());

        if(message != null) {
            player.sendMessage(message);
        }
    }

    protected void overrideActivationMessage() {
        this.overrideActivationMessage = true;
    }

    public void removeOneItem(Player player) {
        if(this.removeOneItem) {
            ItemUtils.removeOneItem(player);
        }
    }

    protected void loadAdditionalData(ConfigurationSection abilitySection) {

    }

    protected boolean onItemClick(Player player, PlayerInteractEvent event) {
        return false;
    }

    protected boolean onPlayerItemHit(Player damager, Player target, EntityDamageByEntityEvent event) {
        return false;
    }
}
