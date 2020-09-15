package me.qiooip.lazarus.abilities;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.type.CocaineAbility;
import me.qiooip.lazarus.abilities.type.PocketBardAbility;
import me.qiooip.lazarus.abilities.type.SwitcherAbility;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.utils.ManagerEnabler;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class AbilitiesManager implements Listener, ManagerEnabler {

    @Getter private static AbilitiesManager instance;

    private final Map<Integer, AbilityItem> abilityItems;

    public AbilitiesManager() {
        instance = this;
        this.abilityItems = new HashMap<>();

        this.setupAbilityItems();
        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        this.abilityItems.clear();
    }

    public void setupAbilityItems() {
        ConfigFile abilitiesFile = Lazarus.getInstance().getAbilitiesFile();

        this.loadAbilityItem(new CocaineAbility(abilitiesFile));
        this.loadAbilityItem(new PocketBardAbility(abilitiesFile));
        this.loadAbilityItem(new SwitcherAbility(abilitiesFile));
    }

    private void loadAbilityItem(AbilityItem abilityItem) {
        if(!abilityItem.isEnabled()) {
            return;
        }

        Integer itemHash = this.calculateItemHash(abilityItem.getItem().getItemMeta());
        this.abilityItems.put(itemHash, abilityItem);
    }

    private int calculateItemHash(ItemMeta itemMeta) {
        return Objects.hash(itemMeta.getDisplayName(), itemMeta.getLore());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.useInteractedBlock() == Event.Result.DENY && event.useItemInHand() == Event.Result.DENY) return;
        if(!event.hasItem() || !event.getItem().hasItemMeta()) return;

        ItemMeta itemMeta = event.getItem().getItemMeta();
        if(!itemMeta.hasDisplayName() || !itemMeta.hasLore()) return;

        int itemHashCode = this.calculateItemHash(itemMeta);

        AbilityItem abilityItem = this.abilityItems.get(itemHashCode);
        if(abilityItem == null) return;

        abilityItem.onItemClick(event.getPlayer());
    }
}
