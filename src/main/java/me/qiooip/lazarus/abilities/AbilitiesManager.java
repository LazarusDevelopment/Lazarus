package me.qiooip.lazarus.abilities;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.type.AntiRedstoneAbility;
import me.qiooip.lazarus.abilities.type.CocaineAbility;
import me.qiooip.lazarus.abilities.type.ExoticBoneAbility;
import me.qiooip.lazarus.abilities.type.FakePearlAbility;
import me.qiooip.lazarus.abilities.type.GuardianAngelAbility;
import me.qiooip.lazarus.abilities.type.InvisibilityAbility;
import me.qiooip.lazarus.abilities.type.LuckyIngotAbility;
import me.qiooip.lazarus.abilities.type.PocketBardAbility;
import me.qiooip.lazarus.abilities.type.PotionCounterAbility;
import me.qiooip.lazarus.abilities.type.SwitcherAbility;
import me.qiooip.lazarus.abilities.type.TankIngotAbility;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.abilities.AbilitiesTimer;
import me.qiooip.lazarus.timer.abilities.GlobalAbilitiesTimer;
import me.qiooip.lazarus.utils.ManagerEnabler;
import me.qiooip.lazarus.utils.item.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class AbilitiesManager implements Listener, ManagerEnabler {

    @Getter
    private static AbilitiesManager instance;

    private final Table<Integer, AbilityEventType, AbilityItem> abilityItems;
    private final Map<AbilityType, AbilityItem> enabledAbilities;

    public AbilitiesManager() {
        instance = this;
        this.abilityItems = HashBasedTable.create();
        this.enabledAbilities = new EnumMap<>(AbilityType.class);

        this.setupAbilityItems();

        Bukkit.getPluginManager().registerEvents(this, Lazarus.getInstance());
    }

    public void disable() {
        for(AbilityItem ability : this.enabledAbilities.values()) {
            ability.disable();
        }

        this.abilityItems.clear();
        this.enabledAbilities.clear();
    }

    public void setupAbilityItems() {
        ConfigFile config = Lazarus.getInstance().getAbilitiesFile();

        this.loadAbility(new AntiRedstoneAbility(config), AbilityEventType.ENTITY_DAMAGE);
        this.loadAbility(new CocaineAbility(config), AbilityEventType.PLAYER_INTERACT);
        this.loadAbility(new ExoticBoneAbility(config), AbilityEventType.ENTITY_DAMAGE);
        this.loadAbility(new FakePearlAbility(config), AbilityEventType.PROJECTILE_LAUNCH);
        this.loadAbility(new GuardianAngelAbility(config), AbilityEventType.PLAYER_INTERACT);
        this.loadAbility(new InvisibilityAbility(config), AbilityEventType.PLAYER_INTERACT);
        this.loadAbility(new LuckyIngotAbility(config), AbilityEventType.PLAYER_INTERACT);
        this.loadAbility(new PocketBardAbility(config), AbilityEventType.PLAYER_INTERACT);
        this.loadAbility(new PotionCounterAbility(config), AbilityEventType.ENTITY_DAMAGE);
        this.loadAbility(new SwitcherAbility(config), AbilityEventType.PROJECTILE_LAUNCH);
        this.loadAbility(new TankIngotAbility(config), AbilityEventType.PLAYER_INTERACT);

        this.enabledAbilities.values().stream()
            .filter(ability -> ability instanceof Listener).map(Listener.class::cast)
            .forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, Lazarus.getInstance()));
    }

    public AbilityItem getAbilityItemByType(AbilityType type) {
        return this.enabledAbilities.get(type);
    }

    private void loadAbility(AbilityItem abilityItem, AbilityEventType eventType) {
        if(!abilityItem.isEnabled()) {
            return;
        }

        Integer itemHash = this.calculateItemHash(abilityItem.getItem().getItemMeta());

        this.abilityItems.put(itemHash, eventType, abilityItem);
        this.enabledAbilities.put(abilityItem.getType(), abilityItem);
    }

    private int calculateItemHash(ItemMeta itemMeta) {
        return Objects.hash(itemMeta.getDisplayName(), itemMeta.getLore());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if(!(event.getEntity().getShooter() instanceof Player)) return;

        Player player = (Player) event.getEntity().getShooter();

        ItemStack itemInHand = player.getItemInHand();
        if(itemInHand == null || !itemInHand.hasItemMeta()) return;

        ItemMeta itemMeta = itemInHand.getItemMeta();
        if(!itemMeta.hasDisplayName() || !itemMeta.hasLore()) return;

        int hash = this.calculateItemHash(itemMeta);

        AbilityItem ability = this.abilityItems.get(hash, AbilityEventType.PROJECTILE_LAUNCH);
        if(ability == null) return;

        GlobalAbilitiesTimer globalTimer = TimerManager.getInstance().getGlobalAbilitiesTimer();

        if(globalTimer.isActive(player.getUniqueId())) {
            player.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_GLOBAL_COOLDOWN_ACTIVE
                .replace("<time>", globalTimer.getTimeLeft(player)));

            event.setCancelled(true);
            return;
        }

        AbilitiesTimer abilityTimer = TimerManager.getInstance().getAbilitiesTimer();

        if(abilityTimer.isActive(player, ability.getType())) {
            player.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_ABILITY_COOLDOWN_ACTIVE
                .replace("<ability>", ability.getDisplayName())
                .replace("<time>", abilityTimer.getDynamicTimeLeft(player, ability.getType())));

            event.setCancelled(true);
            return;
        }

        if(ability.onProjectileClick(player, event.getEntity())) {
            ability.sendActivationMessage(player);

            ItemUtils.removeOneItem(player);
            globalTimer.activate(player);

            abilityTimer.activate(player, ability.getType(), ability.getCooldown(), Language.ABILITIES_PREFIX
               + Language.ABILITIES_ABILITY_COOLDOWN_EXPIRED.replace("<ability>", ability.getDisplayName()));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.useInteractedBlock() == Event.Result.DENY && event.useItemInHand() == Event.Result.DENY) return;
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(!event.hasItem() || !event.getItem().hasItemMeta()) return;

        ItemMeta itemMeta = event.getItem().getItemMeta();
        if(!itemMeta.hasDisplayName() || !itemMeta.hasLore()) return;

        int hash = this.calculateItemHash(itemMeta);

        AbilityItem ability = this.abilityItems.get(hash, AbilityEventType.PLAYER_INTERACT);
        if(ability == null) return;

        Player player = event.getPlayer();
        GlobalAbilitiesTimer globalTimer = TimerManager.getInstance().getGlobalAbilitiesTimer();

        if(globalTimer.isActive(player.getUniqueId())) {
            player.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_GLOBAL_COOLDOWN_ACTIVE
                .replace("<time>", globalTimer.getTimeLeft(player)));
            return;
        }

        AbilitiesTimer abilityTimer = TimerManager.getInstance().getAbilitiesTimer();

        if(abilityTimer.isActive(player, ability.getType())) {
            player.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_ABILITY_COOLDOWN_ACTIVE
                .replace("<ability>", ability.getDisplayName())
                .replace("<time>", abilityTimer.getDynamicTimeLeft(player, ability.getType())));
            return;
        }

        if(ability.onItemClick(player, event)) {
            ability.sendActivationMessage(player);

            ItemUtils.removeOneItem(player);
            globalTimer.activate(player);

            abilityTimer.activate(player, ability.getType(), ability.getCooldown(), Language.ABILITIES_PREFIX
               + Language.ABILITIES_ABILITY_COOLDOWN_EXPIRED.replace("<ability>", ability.getDisplayName()));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        Player damager = (Player) event.getDamager();

        ItemStack item = damager.getItemInHand();
        if(item == null || !item.hasItemMeta()) return;

        ItemMeta itemMeta = item.getItemMeta();
        if(!itemMeta.hasDisplayName() || !itemMeta.hasLore()) return;

        int hash = this.calculateItemHash(itemMeta);

        AbilityItem ability = this.abilityItems.get(hash, AbilityEventType.ENTITY_DAMAGE);
        if(ability == null) return;

        GlobalAbilitiesTimer globalTimer = TimerManager.getInstance().getGlobalAbilitiesTimer();

        if(globalTimer.isActive(damager.getUniqueId())) {
            damager.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_GLOBAL_COOLDOWN_ACTIVE
                .replace("<time>", globalTimer.getTimeLeft(damager)));
            return;
        }

        AbilitiesTimer abilityTimer = TimerManager.getInstance().getAbilitiesTimer();

        if(abilityTimer.isActive(damager, ability.getType())) {
            damager.sendMessage(Language.ABILITIES_PREFIX + Language.ABILITIES_ABILITY_COOLDOWN_ACTIVE
                .replace("<ability>", ability.getDisplayName())
                .replace("<time>", abilityTimer.getDynamicTimeLeft(damager, ability.getType())));
            return;
        }

        if(ability.onPlayerItemHit(damager, (Player) event.getEntity(), event)) {
            ability.sendActivationMessage(damager);

            ItemUtils.removeOneItem(damager);
            globalTimer.activate(damager);

            abilityTimer.activate(damager, ability.getType(), ability.getCooldown(), Language.ABILITIES_PREFIX
               + Language.ABILITIES_ABILITY_COOLDOWN_EXPIRED.replace("<ability>", ability.getDisplayName()));
        }
    }

    private enum AbilityEventType {
        PROJECTILE_LAUNCH, PLAYER_INTERACT, ENTITY_DAMAGE
    }
}


