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
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Getter
public class AbilitiesManager implements Listener, ManagerEnabler {

    @Getter private static AbilitiesManager instance;

    private final Map<ItemStack, AbilityItem> abilityItems;

    public AbilitiesManager() {
        instance = this;
        this.abilityItems = new HashMap<>();

        this.setupAbilityItems();
        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void setupAbilityItems() {
        ConfigFile abilitiesFile = Lazarus.getInstance().getAbilitiesFile();

        new CocaineAbility(abilitiesFile);
        new PocketBardAbility(abilitiesFile);
        new SwitcherAbility(abilitiesFile);
    }

    public void disable() {
        this.abilityItems.clear();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.useInteractedBlock() == Event.Result.DENY && event.useItemInHand() == Event.Result.DENY) return;
    }
}
