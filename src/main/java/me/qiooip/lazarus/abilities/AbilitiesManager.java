package me.qiooip.lazarus.abilities;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.type.AntiRedstoneAbility;
import me.qiooip.lazarus.abilities.type.CocaineAbility;
import me.qiooip.lazarus.abilities.type.ExoticBoneAbility;
import me.qiooip.lazarus.abilities.type.FakePearlAbility;
import me.qiooip.lazarus.abilities.type.GuardianAngelAbility;
import me.qiooip.lazarus.abilities.type.InvisibilityAbility;
import me.qiooip.lazarus.abilities.type.PocketBardAbility;
import me.qiooip.lazarus.abilities.type.PotionCounterAbility;
import me.qiooip.lazarus.abilities.type.SwitcherAbility;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.utils.ManagerEnabler;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class AbilitiesManager implements Listener, ManagerEnabler {

    @Getter
    private static AbilitiesManager instance;

    private final Map<Integer, AbilityItem> abilityItems;
    private final Map<AbilityType, AbilityItem> enabledAbilities;

    public AbilitiesManager() {
        instance = this;
        this.abilityItems = new HashMap<>();
        this.enabledAbilities = new HashMap<>();

        this.setupAbilityItems();

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.abilityItems.clear();
        this.enabledAbilities.clear();
    }

    public void setupAbilityItems() {
        ConfigFile config = Lazarus.getInstance().getAbilitiesFile();

        this.loadAbility(new AntiRedstoneAbility(config));
        this.loadAbility(new CocaineAbility(config));
        this.loadAbility(new ExoticBoneAbility(config));
        this.loadAbility(new FakePearlAbility(config));
        this.loadAbility(new GuardianAngelAbility(config));
        this.loadAbility(new InvisibilityAbility(config));
        this.loadAbility(new PocketBardAbility(config));
        this.loadAbility(new PotionCounterAbility(config));
        this.loadAbility(new SwitcherAbility(config));
    }

    public AbilityItem getAbilityItemByType(AbilityType type) {
        return this.enabledAbilities.get(type);
    }

    private void loadAbility(AbilityItem abilityItem) {
        if (!abilityItem.isEnabled()) {
            return;
        }

        Integer itemHash = this.calculateItemHash(abilityItem.getItem().getItemMeta());

        this.abilityItems.put(itemHash, abilityItem);
        this.enabledAbilities.put(abilityItem.getType(), abilityItem);
    }

    private int calculateItemHash(ItemMeta itemMeta) {
        return Objects.hash(itemMeta.getDisplayName(), itemMeta.getLore());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.useInteractedBlock() == Event.Result.DENY && event.useItemInHand() == Event.Result.DENY) return;
        if (!event.hasItem() || !event.getItem().hasItemMeta()) return;

        ItemMeta itemMeta = event.getItem().getItemMeta();
        if (!itemMeta.hasDisplayName() || !itemMeta.hasLore()) return;

        int hash = this.calculateItemHash(itemMeta);

        AbilityItem ability = this.abilityItems.get(hash);
        if (ability == null) return;

        Player player = event.getPlayer();

        ability.onItemClick(player);
        ItemUtils.removeOneItem(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        Player damager = (Player) event.getDamager();

        ItemStack item = damager.getItemInHand();
        if (item == null || !item.hasItemMeta()) return;

        ItemMeta itemMeta = item.getItemMeta();
        if (!itemMeta.hasDisplayName() || !itemMeta.hasLore()) return;

        int hash = this.calculateItemHash(itemMeta);

        AbilityItem ability = this.abilityItems.get(hash);
        if (ability == null || !ability.onPlayerItemHit(damager, (Player) event.getEntity())) return;

        ItemUtils.removeOneItem(damager);
    }
}


