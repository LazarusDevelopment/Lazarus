package me.qiooip.lazarus.abilities;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.abilities.event.AbilityActivatedEvent;
import me.qiooip.lazarus.abilities.type.AggressivePearlAbility;
import me.qiooip.lazarus.abilities.type.AntiAbilityBallAbility;
import me.qiooip.lazarus.abilities.type.AntiRedstoneAbility;
import me.qiooip.lazarus.abilities.type.AntiTrapStarAbility;
import me.qiooip.lazarus.abilities.type.CocaineAbility;
import me.qiooip.lazarus.abilities.type.ComboAbility;
import me.qiooip.lazarus.abilities.type.DecoyAbility;
import me.qiooip.lazarus.abilities.type.ExoticBoneAbility;
import me.qiooip.lazarus.abilities.type.FakePearlAbility;
import me.qiooip.lazarus.abilities.type.FastPearlAbility;
import me.qiooip.lazarus.abilities.type.GuardianAngelAbility;
import me.qiooip.lazarus.abilities.type.InvisibilityAbility;
import me.qiooip.lazarus.abilities.type.LoggerBaitAbility;
import me.qiooip.lazarus.abilities.type.LuckyIngotAbility;
import me.qiooip.lazarus.abilities.type.PocketBardAbility;
import me.qiooip.lazarus.abilities.type.PotionCounterAbility;
import me.qiooip.lazarus.abilities.type.PrePearlAbility;
import me.qiooip.lazarus.abilities.type.RageAbility;
import me.qiooip.lazarus.abilities.type.RageBrickAbility;
import me.qiooip.lazarus.abilities.type.RocketAbility;
import me.qiooip.lazarus.abilities.type.ScramblerAbility;
import me.qiooip.lazarus.abilities.type.SecondChanceAbility;
import me.qiooip.lazarus.abilities.type.StarvationFleshAbility;
import me.qiooip.lazarus.abilities.type.SwitchStickAbility;
import me.qiooip.lazarus.abilities.type.SwitcherAbility;
import me.qiooip.lazarus.abilities.type.TankIngotAbility;
import me.qiooip.lazarus.abilities.type.WebGunAbility;
import me.qiooip.lazarus.config.ConfigFile;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.timer.abilities.AbilitiesTimer;
import me.qiooip.lazarus.timer.abilities.GlobalAbilitiesTimer;
import me.qiooip.lazarus.utils.ManagerEnabler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.EnumMap;
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

        this.loadAbility(new AggressivePearlAbility(config));
        this.loadAbility(new AntiAbilityBallAbility(config));
        this.loadAbility(new AntiRedstoneAbility(config));
        this.loadAbility(new AntiTrapStarAbility(config));
        this.loadAbility(new CocaineAbility(config));
        this.loadAbility(new ComboAbility(config));
        this.loadAbility(new DecoyAbility(config));
        this.loadAbility(new ExoticBoneAbility(config));
        this.loadAbility(new FakePearlAbility(config));
        this.loadAbility(new FastPearlAbility(config));
        this.loadAbility(new GuardianAngelAbility(config));
        this.loadAbility(new InvisibilityAbility(config));
        this.loadAbility(new LoggerBaitAbility(config));
        this.loadAbility(new LuckyIngotAbility(config));
        this.loadAbility(new PocketBardAbility(config));
        this.loadAbility(new PotionCounterAbility(config));
        this.loadAbility(new PrePearlAbility(config));
        this.loadAbility(new RageAbility(config));
        this.loadAbility(new RageBrickAbility(config));
        this.loadAbility(new RocketAbility(config));
        this.loadAbility(new ScramblerAbility(config));
        this.loadAbility(new SecondChanceAbility(config));
        this.loadAbility(new StarvationFleshAbility(config));
        this.loadAbility(new SwitcherAbility(config));
        this.loadAbility(new SwitchStickAbility(config));
        this.loadAbility(new TankIngotAbility(config));
        this.loadAbility(new WebGunAbility(config));

        this.enabledAbilities.values().stream()
            .filter(ability -> ability instanceof Listener).map(Listener.class::cast)
            .forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, Lazarus.getInstance()));
    }

    public AbilityItem getAbilityItemByType(AbilityType type) {
        return this.enabledAbilities.get(type);
    }

    public void loadAbility(AbilityItem abilityItem) {
        if(!abilityItem.isEnabled()) {
            return;
        }

        Integer itemHash = this.calculateItemHash(abilityItem.getItem().getItemMeta());

        this.abilityItems.put(itemHash, abilityItem);
        this.enabledAbilities.put(abilityItem.getType(), abilityItem);
    }

    private int calculateItemHash(ItemMeta itemMeta) {
        return Objects.hash(itemMeta.getDisplayName(), itemMeta.getLore());
    }

    public AbilityItem getAbilityItem(ItemStack itemStack) {
        if(!itemStack.hasItemMeta()) return null;

        ItemMeta itemMeta = itemStack.getItemMeta();
        if(!itemMeta.hasDisplayName() || !itemMeta.hasLore()) return null;

        return this.abilityItems.get(this.calculateItemHash(itemMeta));
    }

    public boolean isAbilityItem(ItemStack itemStack) {
        return this.abilityItems.containsKey(this.calculateItemHash(itemStack.getItemMeta()));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.useInteractedBlock() == Result.DENY && event.useItemInHand() == Result.DENY) return;
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(!event.hasItem() || !event.getItem().hasItemMeta()) return;

        Player player = event.getPlayer();
        if(event.getItem().getType() == Material.ENDER_PEARL && player.getGameMode() == GameMode.CREATIVE) return;

        ItemMeta itemMeta = event.getItem().getItemMeta();
        if(!itemMeta.hasDisplayName() || !itemMeta.hasLore()) return;

        int hash = this.calculateItemHash(itemMeta);

        AbilityItem ability = this.abilityItems.get(hash);
        if(ability == null) return;

        AbilityActivatedEvent abilityEvent = new AbilityActivatedEvent(player, player.getLocation(), ability, ability.isProjectileAbility());

        if(abilityEvent.isCancelled()) {
            event.setUseItemInHand(Result.DENY);
            return;
        }

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

        if(ability.onItemClick(player, event)) {
            ability.sendActivationMessage(player);

            ability.removeOneItem(player);
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

        AbilityItem ability = this.abilityItems.get(hash);
        if(ability == null) return;

        AbilityActivatedEvent abilityEvent = new AbilityActivatedEvent(damager, damager.getLocation(), ability, ability.isProjectileAbility());

        if(abilityEvent.isCancelled()) {
            return;
        }

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

            ability.removeOneItem(damager);
            globalTimer.activate(damager);

            abilityTimer.activate(damager, ability.getType(), ability.getCooldown(), Language.ABILITIES_PREFIX
               + Language.ABILITIES_ABILITY_COOLDOWN_EXPIRED.replace("<ability>", ability.getDisplayName()));
        }
    }
}


